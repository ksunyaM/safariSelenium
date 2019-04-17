/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

/* Copyright 2018 Walgreen Co. */
public class VerificationUtilsTest extends BaseStep {

    private final String initialJson = "{\"text\": \"0\"," +
            "\"zero\": 0," +
            "\"int\": 123," +
            "\"double\": 2234523.2343434," +
            "\"double1\": 123.00," +
            "\"double2\": 123," +
            "\"double3\": 0.01," +
            "\"boolean\": false, " +
            "\"null\": null}";

    @DataProvider(name = "test1")
    public Object[][] data() {
        return new Object[][]{
                {"text", "equal", "0"},
                {"zero", "==", "0.0"},
                {"zero", "=", "-0"},
                {"int", "==", "123"},
                {"int", "==", "123.00"},
                {"double", "==", "2234523.2343434"},
                {"double1", "==", "123"},
                {"double3", "==", "0.01"},
                {"double3", "==", Double.valueOf("0.02") - Double.valueOf("0.01")},
                {"double3", "==", BigDecimal.valueOf(Double.valueOf("0.01"))},
                {"double1", "<=", "123"},
                {"double2", ">=", "123.00"},
                {"boolean", "==", false},
                {"boolean", "==", "false"},
                {"null", "==", null},

                {"double1", "<", "123.01"},
                {"double2", ">", "122.00"},

                {"text", "not_equal", "00"},
                {"int", "!=", "123.1"},
                {"double", "!=", "2234523.23435"},
                {"boolean", "!=", true},
                {"null", "!=", 1},
        };
    }

    @Test(dataProvider = "test1")
    public void verificationUtilsTest(String jsonPath, String comparisonSign, Object expected) {
        Object jsonValue = JsonUtils.getJsonValue(initialJson, jsonPath);
        Object value = VerificationUtils.convert(jsonValue);
        VerificationUtils.compare(value, comparisonSign, expected);
    }

    @DataProvider(name = "testFalse")
    public Object[][] data01() {
        return new Object[][]{
                {null, "not_equal", null},
                {null, "==", 0},
                {1, "==", null},
        };
    }

    @Test(dataProvider = "testFalse", expectedExceptions = AssertionError.class)
    public void verificationUtilsTestFalse(Object value1, String comparisonSign, Object value2) {
        VerificationUtils.compare(value1, comparisonSign, value2);
    }

    @DataProvider(name = "testExample")
    public Object[][] data02() {
        return new Object[][]{
                {"1 + 1 = 2"},
                {"1 - 1 = 0.00"},
                {"-1 - 1 = -2"},
                {"-1 - -1 = 0"},
                {"1 / 3 = 0.33333"},
        };
    }

    @Test(dataProvider = "testExample")
    public void verifyExampleTest(String example) {
        VerificationUtils.verify_example(example);
    }

    @DataProvider(name = "testExampleFail")
    public Object[][] data03() {
        return new Object[][]{
                {"1 + 1 = 1"},
                {"1 - 1 = 1"},
                {"1 / 3 = 0"},
        };
    }

    @Test(dataProvider = "testExampleFail", expectedExceptions = AssertionError.class)
    public void verifyExampleFailTest(String example) {
        VerificationUtils.verify_example(example);
    }

    @Test
    public void verifyExampleTest1() {
        dataStorage.add("one", 10);
        dataStorage.add("two", 10);
        dataStorage.add("res", 100);

        VerificationUtils.verify_example("one * two == res");
    }
}
