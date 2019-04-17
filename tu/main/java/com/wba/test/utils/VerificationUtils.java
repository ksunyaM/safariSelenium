/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.fasterxml.jackson.databind.node.*;
import com.wba.test.utils.kafka.LogUtils;
import cucumber.runtime.CucumberException;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.oneleo.test.automation.core.LogUtils.log;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;

public class VerificationUtils {

    private static final Logger LOGGER = log(LogUtils.class);

    public static void compare(Object value1, String compareSign, Object value2) {
        if (value1 == null || value2 == null) {
            VerificationUtils.verifyNulls(value1, compareSign, value2);
        } else if (value1 instanceof String) {
            VerificationUtils.verifyEquals(value1, compareSign, value2);
        } else if (value1 instanceof Boolean) {
            VerificationUtils.verifyEquals(value1, compareSign, Boolean.parseBoolean(value2.toString()));
        } else {
            VerificationUtils.verifyAsBigDecimal(
                    VerificationUtils.toBigDecimal(value1),
                    compareSign,
                    VerificationUtils.toBigDecimal(value2));
        }
    }

    /**
     * The method verifies that given example is correct.
     * this is correct: 1 + 1.2 = 2.2
     * Example string transformation:
     * - define value for the whole string
     * - define value for each operand
     *
     * @param example - String with example: 1 + 1.2 = 2.2
     * @param message - message for assert
     */
    public static void verify_example(String example, String... message) {
        LOGGER.info(message.length > 0 ? message[0] : "Going to verify: " + example);
        String[] p = new BaseStep().defineValue(example).toString().split("\\s+", 5);
        BigDecimal opResult =
                VerificationUtils.doMath(
                        new BaseStep().defineValue(p[0]),
                        p[1],
                        new BaseStep().defineValue(p[2]));
        VerificationUtils.compare(opResult, p[3],
                new BaseStep().defineValue(p[4]));
    }


    public static BigDecimal doMath(Object one, String action, Object two) {
        return doMath(Op.getOp(action), one, two);
    }

    public static BigDecimal doMath(Op action, Object one, Object two) {
        if (null == one || null == two) {
            throw new ArithmeticException(
                    String.format("Arithmetic parameters can not be null : %s %s %s", one, action.sign, two));
        }
        switch (action) {
            case PLUS:
                return toBigDecimal(one).add(toBigDecimal(two));
            case MINUS:
                return toBigDecimal(one).subtract(toBigDecimal(two));
            case MULTIPLY:
                return toBigDecimal(one).multiply(toBigDecimal(two));
            case DIVIDE:
                return toBigDecimal(one).divide(toBigDecimal(two), 5, BigDecimal.ROUND_HALF_UP);
            default:
                throw new RuntimeException("Wrong operation:" + action);
        }
    }

    public static BigDecimal toBigDecimal(Object value) {
        int scale = 5;
        return value == null ? null : BigDecimal
                .valueOf(Double.parseDouble(value.toString()))
                .setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static Object convert(Object value) {
        if (value instanceof TextNode) {
            return ((TextNode) value).asText();
        } else if (value instanceof IntNode) {
            return ((IntNode) value).asDouble();
        } else if (value instanceof DoubleNode) {
            return ((DoubleNode) value).asDouble();
        } else if (value instanceof LongNode) {
            return ((LongNode) value).asDouble();
        } else if (value instanceof BooleanNode) {
            return ((BooleanNode) value).asBoolean();
        } else if (value instanceof NullNode) {
            return null;
        } else {
            return value;
        }
    }

    private static void verifyNulls(Object value, String compareSign, Object otherValue) {
        String message = String.format("Value %s is not equal(%s) to value %s", value, compareSign, otherValue);
        boolean res = value == null && otherValue == null;
        switch (compareSign) {
            case "=":
            case "==":
            case "equal":
            case "equals":
                assertTrue(message, res);
                break;
            default:
                assertTrue(message, !res);
        }
    }

    private static void verifyEquals(Object value, String compareSign, Object otherValue) {
        switch (compareSign) {
            case "=":
            case "==":
            case "equal":
            case "equals":
                assertEquals(value, otherValue);
                break;
            case "!=":
            case "not_equal":
            case "not_equals":
                assertNotEquals(value, otherValue);
                break;
            default:
                throw new RuntimeException("Unknown comparison sign: " + compareSign);
        }
    }

    private static void verifyAsBigDecimal(BigDecimal value, String compareSign, BigDecimal otherValue) {
        switch (compareSign) {
            case "<":
                assertThat(value, lessThan(otherValue));
                break;
            case "<=":
                assertThat(value, lessThanOrEqualTo(otherValue));
                break;
            case ">":
                assertThat(value, greaterThan(otherValue));
                break;
            case ">=":
                assertThat(value, greaterThanOrEqualTo(otherValue));
                break;
            case "=":
            case "==":
                assertEquals(value, otherValue);
                break;
            case "!=":
                assertNotEquals(value, otherValue);
                break;
            default:
                throw new RuntimeException("Unknown numeric comparison sign: " + compareSign);
        }
    }

    public enum Op {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/");

        private String sign;

        Op(String sign) {
            this.sign = sign;
        }

        public static Op getOp(String sign) {
            return Arrays.stream(Op.values())
                    .filter(op -> op.sign.equals(sign))
                    .findFirst()
                    .orElseThrow(() -> new CucumberException("Can not recognize sign: " + sign));
        }

    }
}
