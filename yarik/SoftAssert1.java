package com.wba.rxdata.test;

import com.fasterxml.jackson.databind.node.ContainerNode;
import com.wba.test.utils.BaseStep;
import com.wba.test.utils.RegExp;
import com.wba.test.utils.Utils;
import com.wba.test.utils.VerificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SoftAssert1 extends BaseStep {

    public static final String SOFT_ASSERT = "~~soft_assert";
    public static final String BUG = "bug";
    private static final String TEST_MESSAGE = "~~test_message";

    public SoftAssert1 on() {
        dataStorage.map().put(SOFT_ASSERT, true);
        return this;
    }

    public SoftAssert1 off() {
        dataStorage.map().put(SOFT_ASSERT, false);
        return this;
    }

    private boolean isOn() {
        return asStr(SOFT_ASSERT).equalsIgnoreCase("true");
    }

    public String getMessage() {
        return dataStorage.unmask(TEST_MESSAGE).toString();
    }

    public SoftAssert1 setMessage(String message) {
        dataStorage.map().put(TEST_MESSAGE, message);
        return this;
    }

    private void onError(AssertionError e, String message) {
        final String t = StringUtils.defaultIfEmpty(message, dataStorage.map().getOrDefault(TEST_MESSAGE, "").toString())
                + " " + e;
        if (isOn()) {
            LOGGER.error("~~BUG~~: " + t);
            dataStorage.add(BUG + dataStorage.getNextDataStorageNumber(BUG), Arrays.asList(t, e));
        } else {
            throw new AssertionError(t);
        }
    }

    public void failIfErrors() {
        if (dataStorage.map().containsKey(BUG + "0")) {
            int i = 0;
            while (dataStorage.map().containsKey(BUG + i)) {
                final List list = (List) defineValue(BUG + i);
                LOGGER.error(list.get(0).toString() +
                        "\n" +
                        Arrays.stream(((AssertionError) list.get(1)).getStackTrace())
                                .map(StackTraceElement::toString)
                                .filter(s -> s.contains("com.wba"))
                                .collect(Collectors.joining("\n")) +
                        "\n=====================================\n");
                i++;

            }
            Assert.fail("BUGS FOUND!");
        }
    }

    /*
     ****************** Verifications
     */

    public SoftAssert1 isTrue(boolean condition, String... message) {
        try {
            Assert.assertTrue(Utils.defaultOrFirst("", message), condition);
        } catch (AssertionError e) {
            onError(e, "True:" + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 fail(String... message) {
        try {
            Assert.fail(Utils.defaultOrFirst("", message));
        } catch (AssertionError e) {
            onError(e, "Fail: " + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 isNull(Object expected, String... message) {
        try {
            Assert.assertNull(Utils.defaultOrFirst("", message),
                    expected instanceof String ? asStr(expected.toString()) : expected);
        } catch (AssertionError e) {
            onError(e, String.format("Is null: %s ", expected) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 notNull(Object expected, String... message) {
        try {
            assertNotNull(Utils.defaultOrFirst("", message),
                    expected instanceof String ? asStr(expected.toString()) : expected);
        } catch (AssertionError e) {
            onError(e, String.format("Not null: %s ", expected) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 equals(Object expected, Object actual, String... message) {
        try {
            if (expected instanceof String)
                assertEquals(Utils.defaultOrFirst("", message), asStr(expected.toString()), asStr(actual.toString()));
        } catch (AssertionError e) {
            onError(e, String.format("Equals: %s | %s ", expected, actual) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 matches(String regExp, String actual, String... message) {
        try {
            assertTrue(asStr(actual).matches(regExp));
        } catch (AssertionError e) {
            onError(e, String.format("Matches: %s | %s / %s ", regExp, asStr(actual), actual) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 verify(String expected, String sign, String actual, String... message) {
        try {
            VerificationUtils.compare(defineValue(expected), sign, defineValue(actual));
        } catch (AssertionError e) {
            onError(e, String.format("%s %s %s ", expected, sign, actual) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    // Required for correct method call
    public SoftAssert1 verify(String expression) {
        verify(expression, null);
        return this;
    }

    public SoftAssert1 verify(String expression, String message) {
        try {
            final String[] m = RegExp.getMatches(expression, "([^ ]+)\\s+([^ ]+)\\s+(.+)").get(0);
            VerificationUtils.compare(defineValue(m[0]), m[1], defineValue(m[2]));
        } catch (AssertionError e) {
            onError(e, String.format("%s ", expression) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 calculation(String example, String... message) {
        try {
            VerificationUtils.verify_example(example);
        } catch (AssertionError e) {
            onError(e, example + " " + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 dateTime(String expected, String sign, String actual, String... message) {
        try {
            VerificationUtils.compare(asMillSec(expected), sign, asMillSec(actual));
        } catch (AssertionError e) {
            onError(e, String.format("%s %s %s ", expected, sign, actual) + Utils.defaultOrFirst("", message));
        }
        return this;
    }

    public SoftAssert1 jsonCurrency(String node, Object amount, String... message) {
        ContainerNode _node = (ContainerNode) defineValue(node);
        final String _amount = asCurr(amount);
//        equals(_node.findValue("symbol").asText(), "$", message); // FIXME BUG
        equals(asCurr(_node.findValue("amount").asText()), _amount, message);
        equals(_node.findValue("isoCode").asText(), "USD", message);
        equals(asCurr(_node.findValue("decimalValue").asText()), _amount, message);
        equals(_node.findValue("formattedAmount").asText(), "$" + _amount, message);
        return this;
    }
}