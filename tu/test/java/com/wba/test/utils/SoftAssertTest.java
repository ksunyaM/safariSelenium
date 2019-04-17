/*
 * Copyright 2018 Walgreen Co.
 */

/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.rmi.UnexpectedException;

/* Copyright 2018 Walgreen Co. */
public class SoftAssertTest extends BaseStep {

    private SoftAssert2 sa;
    private String json = "{\n" +
            "  \"v10\": {\n" +
            "    \"string\": \"123\"\n" +
            "  },\n" +
            "  \"v12\": {\n" +
            "    \"string\": \"123\"\n" +
            "  },\n" +
            "  \"v13\": {\n" +
            "    \"string\": null\n" +
            "  },\n" +
            "  \"v14\": {\n" +
            "    \"int\": 123\n" +
            "  },\n" +
            "  \"v21\": 123,\n" +
            "  \"v22\": 123,\n" +
            "  \"v31\": null\n" +
            "}";

    @BeforeMethod
    public void data() {
        dataStorage.reset();
        sa = new SoftAssert2();
        dataStorage.add("key1", "same_value");
        dataStorage.add("key2", "same_value");
        dataStorage.add("dateTime", "2018-05-18 15:36:56.358Z");
        dataStorage.add("miliseconds", "1526657816358");
    }

    @Test
    public void OnPassedTest() {
        sa.on();
        sa.isTrue(true);
        sa.isNull(null);
        sa.notNull("not null");
        sa.equals(true, true);
        sa.verify("key1 = key2");
        sa.verify("key1", "=", "key2");
        sa.calculation("1 + 1.2 = 2.2");
        sa.dateTime("dateTime", "=", "miliseconds");
        sa.failIfErrors();
    }

    @Test
    public void OffPassedTest() {
        sa.off();
        sa.isTrue(true);
        sa.isNull(null);
        sa.notNull("not null");
        sa.equals(true, true);
        sa.verify("key1 = key2");
        sa.verify("key1", "=", "key2");
        sa.calculation("1 + 1.2 = 2.2");
        sa.dateTime("dateTime", "=", "miliseconds");
        sa.failIfErrors();
    }


    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = "BUG\\(S\\) FOUND: 2.*")
    public void OnFailedTest() {
        sa.on();
        sa.verify("key1 != key1", "Will fail");
        sa.fail("QWE");
        sa.verify("key1 = key1", "Should pass");
        Assert.assertTrue(asStr(SoftAssert2.BUG + "1").contains("QWE"));

        sa.failIfErrors();
    }

    @Test(expectedExceptions = AssertionError.class, expectedExceptionsMessageRegExp = ".*verification message.*")
    public void OffFailedTest() throws UnexpectedException {
        sa.off();
        sa.verify("key1 != key1", "verification message");
        throw new UnexpectedException("You should get error before this line");
    }
}
