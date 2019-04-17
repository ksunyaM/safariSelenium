/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.fasterxml.jackson.databind.node.ContainerNode;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// TODO delete after 11/18
@Deprecated
public class SoftAssert extends BaseStep {

    public static final String SOFT_ASSERT = "~~soft_assert";
    public static final String BUG = "bug";

    public SoftAssert on() {
        dataStorage.map().put(SOFT_ASSERT, true);
        return this;
    }

    public SoftAssert off() {
        dataStorage.map().put(SOFT_ASSERT, false);
        return this;
    }

    private boolean isOn() {
        return asStr(SOFT_ASSERT).equalsIgnoreCase("true");
    }

    private void onError(AssertionError e, String message) {
        final String t = message + " " + e;
        if (isOn()) {
            LOGGER.error("~~BUG~~: " + t);
            dataStorage.add(BUG + dataStorage.getNextNumber(BUG), Arrays.asList(t, e));
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
                                .filter(s -> s.toString().contains("com.wba"))
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

    public void isTrue(boolean condition, String... message) {
        try {
            Assert.assertTrue(Utils.defaultOrFirst("", message), condition);
        } catch (AssertionError e) {
            onError(e, "True:" + Utils.defaultOrFirst("", message));
        }
    }

    public void fail(String... message) {
        try {
            Assert.fail(Utils.defaultOrFirst("", message));
        } catch (AssertionError e) {
            onError(e, "Fail: " + Utils.defaultOrFirst("", message));
        }
    }

    public void isNull(Object expected, String... message) {
        try {
            Assert.assertNull(Utils.defaultOrFirst("", message), expected);
        } catch (AssertionError e) {
            onError(e, String.format("Is null: %s ", expected) + Utils.defaultOrFirst("", message));
        }
    }

    public void notNull(Object expected, String... message) {
        try {
            assertNotNull(Utils.defaultOrFirst("", message), expected);
        } catch (AssertionError e) {
            onError(e, String.format("Not null: %s ", expected) + Utils.defaultOrFirst("", message));
        }
    }

    public void equals(Object expected, Object actual, String... message) {
        try {
            assertEquals(Utils.defaultOrFirst("", message), expected, actual);
        } catch (AssertionError e) {
            onError(e, String.format("Equals: %s %s ", expected, actual) + Utils.defaultOrFirst("", message));
        }
    }

    public void verify(String expected, String sign, String actual, String... message) {
        try {
            VerificationUtils.compare(defineValue(expected), sign, defineValue(actual));
        } catch (AssertionError e) {
            onError(e, String.format("%s %s %s ", expected, sign, actual) + Utils.defaultOrFirst("", message));
        }
    }

    // Required for correct method call
    public void verify(String expression) {
        verify(expression, null);
    }

    public void verify(String expression, String message) {
        try {
            final String[] m = RegExp.getMatches(expression, "([^ ]+)\\s+([^ ]+)\\s+(.+)").get(0);
            VerificationUtils.compare(defineValue(m[0]), m[1], defineValue(m[2]));
        } catch (AssertionError e) {
            onError(e, String.format("%s ", expression) + Utils.defaultOrFirst("", message));
        }
    }

    public void calculation(String example, String... message) {
        try {
            VerificationUtils.verify_example(example);
        } catch (AssertionError e) {
            onError(e, example + " " + Utils.defaultOrFirst("", message));
        }
    }

    public void dateTime(String expected, String sign, String actual, String... message) {
        try {
            VerificationUtils.compare(asMillSec(expected), sign, asMillSec(actual));
        } catch (AssertionError e) {
            onError(e, String.format("%s %s %s ", expected, sign, actual) + Utils.defaultOrFirst("", message));
        }
    }

    public void jsonCurrency(String node, Object amount, String... message) {
        ContainerNode _node = (ContainerNode) defineValue(node);
        final String _amount = asCurr(amount);
//        equals(_node.findValue("symbol").asText(), "$", message); // FIXME BUG
        equals(asCurr(_node.findValue("amount").asText()), _amount, message);
        equals(_node.findValue("isoCode").asText(), "USD", message);
        equals(asCurr(_node.findValue("decimalValue").asText()), _amount, message);
        equals(_node.findValue("formattedAmount").asText(), "$" + _amount, message);
    }
}

