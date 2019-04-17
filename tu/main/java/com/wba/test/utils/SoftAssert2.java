/*
 * Copyright 2018 Walgreen Co.
 */

package com.wba.test.utils;

import com.fasterxml.jackson.databind.node.ContainerNode;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
/* Copyright 2018 Walgreen Co. */
public class SoftAssert2 extends BaseStep {

    public static final String SOFT_ASSERT = "~~soft_assert";
    public static final String BUG = "bug";
    public static final String TP = "TEST PASS";
    public static final String TF = "TEST FAIL";
    private String baseMessage = "";

    public SoftAssert2 on() {
        dataStorage.map().put(SOFT_ASSERT, true);
        return this;
    }

    public SoftAssert2 off() {
        dataStorage.map().put(SOFT_ASSERT, false);
        return this;
    }

    private boolean isOn() {
        return asStr(SOFT_ASSERT).equalsIgnoreCase("true");
    }

//    public SoftAssert2 optional() {
//        isOptional = true;
//        return this;
//    }

    public String getBaseMessage() {
        return baseMessage;
    }

    public SoftAssert2 setBaseMessage(String baseMessage) {
        this.baseMessage = baseMessage;
        return this;
    }

    private String buildMessage(String... messages) {
        return Stream.of(messages)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(" | "));
    }

    private void onSuccess(String testMessage, String... userMessage) {
//        isOptional = false;
        LOGGER.info(buildMessage(TP, getBaseMessage(), Utils.defaultOrFirst("", userMessage), testMessage));
    }

    private void onError(AssertionError e, String testMessage, String... userMessage) {
//        isOptional = false;
        final String t = buildMessage(TF, getBaseMessage(), Utils.defaultOrFirst("", userMessage), testMessage);
        if (isOn()) {
            printError(dataStorage.addWithNumber(BUG, Arrays.asList(t, e)));
        } else {
            throw new AssertionError(t, e);
        }
    }

    private void printError(String key) {
        final String line = "\n== %s %s ===================================\n";
        final List list = (List) defineValue(key);
        final String errorMessage = list.get(0).toString() +
                String.format(line, key, "start") +
                Arrays.stream(((AssertionError) list.get(1)).getStackTrace())
                        .map(StackTraceElement::toString)
                        .filter(s -> s.contains("com.wba"))
                        .collect(Collectors.joining("\n")) +
                String.format(line, key, "end");
        LOGGER.error(errorMessage);
    }

    public void printAllErrors() {
        final int count = dataStorage.getNextNumber(BUG);
        for (int i = 0; i < count; i++) {
            printError(BUG + i);
        }
        if (count == 0) {
            LOGGER.info("\n NO BUGS found so far.\n");
        } else {
            LOGGER.error("\n BUG(S) FOUND: " + count + "\n");
        }
    }

    public void failIfErrors() {
        final int count = dataStorage.getNextNumber(BUG);
        if (count != 0) {
            printAllErrors();
            Assert.assertEquals("BUG(S) FOUND: " + count, 0, count);
        }
    }

    /*
     ****************** Verifications
     */

    /**
     * Verify that defined (json)path is not present in EventStorage or DataStorage
     *
     * @param field   - path to element
     * @param message optional test message
     * @return instance od soft assert class
     */
    //FIXME Bug if field contains search
    public SoftAssert2 notExist(String field, String... message) {
        String _m = String.format("Field %s is not exist. ", field);
        try {
            Assert.assertEquals(defineValue(field), field);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 isTrue(boolean condition, String... message) {
        String _m = "True";
        try {
            Assert.assertTrue(condition);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 fail(String... message) {
        String _m = "Fail";
        try {
            Assert.fail();
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 isNull(Object v1, String... message) {
        Object _e = v1 instanceof String ? asStr(v1.toString()) : v1;
        String _m = String.format("Is null: %s (%s)", v1, _e);
        try {
            Assert.assertNull(_e);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 notNull(Object v1, String... message) {
        Object _e = v1 instanceof String ? asStr(v1.toString()) : v1;
        String _m = String.format("Is not null: %s (%s)", v1, _e);
        try {
            assertNotNull(_e);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }


    public boolean isEventOptionalNull(String v1) {
        final String[] v1List = RegExp.getMatches(v1, "(.+?)(?:\\.(string|int|long))?$").get(0);
        return v1List[1] != null && asStr(v1List[0]) == null;
    }

    public SoftAssert2 equals(Object v1, Object v2, String... message) {
        String _m = "";
        try {
            if (v1 instanceof String) {
                if (isEventOptionalNull(v1.toString())) {
                    _m = String.format("Optional null. %s <--> %s", v1, v2);
                    assertTrue(!isValueExists(v2.toString()) || asStr(v2.toString()) == null || isEventOptionalNull(v2.toString()));
                } else {
                    final String _e = asStr(v1.toString());
                    final String _a = asStr(v2.toString());
                    _m = String.format(" %s _equals_ %s. Values: >%s<->%s<", v1, v2, _e, _a);
                    assertEquals(Utils.defaultOrFirst("", message), _e, _a);
                }
            } else {
                _m = String.format("%s _equals_ %s.", v1, v2);
                assertEquals(v1, v2);
            }
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 matches(String regExp, String v2, String... message) {
        final String _a = asStr(v2);
        String _m = String.format("%s >%s< _matches_ with regexp %s", v2, _a, regExp);
        try {
            assertTrue(_a.matches(regExp));
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 verify(String v1, String sign, String v2, String... message) {
        final Object _e = defineValue(v1);
        final Object _a = defineValue(v2);
        String _m = String.format("Verify: %s _%s_ %s. Values: %s _%s_ %s", v1, sign, v2, _e, sign, _a);
        try {
            VerificationUtils.compare(_e, sign, _a);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    // Required for correct method call
    public SoftAssert2 verify(String expression) {
        verify(expression, null);
        return this;
    }

    public SoftAssert2 verify(String expression, String message) {
        final String[] m = RegExp.getMatches(expression, "([^ ]+)\\s+([^ ]+)\\s+(.+)").get(0);
        verify(m[0], m[1], m[2], message);
        return this;
    }

    public SoftAssert2 calculation(String example, String... message) {
        String _m = "Calculation: " + example;
        try {
            VerificationUtils.verify_example(example);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    public SoftAssert2 dateTime(String v1, String sign, String v2, String... message) {
        final Long _e = asMillSec(v1);
        final Long _a = asMillSec(v2);
        String _m = String.format("Date comparison: %s _%s_ %s. Values: %s _%s_ %s", v1, sign, v2, _e, sign, _a);
        try {
            VerificationUtils.compare(_e, sign, _a);
            onSuccess(_m, message);
        } catch (AssertionError e) {
            onError(e, _m, message);
        }
        return this;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public SoftAssert2 jsonCurrency(String node, Object amount, String... message) {
        String _m = "Currency: " + Utils.defaultOrFirst("", message);
        ContainerNode _node = (ContainerNode) defineValue(node);
        final String _amount = asCurr(amount);

//        equals(_node.findValue("symbol").asText(), "$", message); // FIXME BUG
        equals(asCurr(_node.findValue("amount").asText()), _amount, _m)
                .equals(_node.findValue("isoCode").asText(), "USD", _m)
                .equals(asCurr(_node.findValue("decimalValue").asText()), _amount, _m)
                .equals(_node.findValue("formattedAmount").asText(), "$" + _amount, _m)
        ;
        return this;
    }
}
