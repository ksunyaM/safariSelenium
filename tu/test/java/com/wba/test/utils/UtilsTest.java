/*
 * Copyright 2018 Walgreen Co.
 */

/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils;

import cucumber.runtime.CucumberException;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;


public class UtilsTest extends BaseStep {

    @Test
    public void getResourceAsStringTest() {
        final String s = ResourceUtils.getResourceAsString("com/wba/test/data/empty.json");
        Assert.assertTrue(s.length() > 0);
    }

    @Test(expectedExceptions = CucumberException.class)
    public void getResourceAsStringFailedTest() {
        ResourceUtils.getResourceAsString("com/wba/test/data/" + RandomStringUtils.randomNumeric(10) + ".json");
    }

    @Test
    public void timeFormatTest01() {
        HashMap<Long, String> res = new HashMap<>();

        res.put(asMillSec("Thu Jun 07 15:30:18 UTC 2018"), "");
        res.put(asMillSec("2018-06-07 15:30:18.000Z"), "");
        res.put(asMillSec("2018-06-07T15:30:18.000Z"), "");
        res.put(asMillSec("2018-06-07T15:30:18"), ""); // This is incorrect time format that should be parsed as FormatTo.CAS_DT_T

        Assert.assertEquals(res.size(), 1);
    }

    @Test
    public void isTagExistTest() {
        dataStorage.map().put("~SCENARIO_TAGS", "a1 a2 a22 a21 a31");
        Assert.assertTrue(new Utils().isTagExist("a1", "a31"));
        Assert.assertFalse(new Utils().isTagExist("a3"));
        Assert.assertFalse(new Utils().isTagExist("31"));
    }

    @Test
    public void getTagValue() {
        dataStorage.map().put("~SCENARIO_TAGS", "a1 a2 b22 c21 i b23 d31");
        Assert.assertEquals(new Utils().getTagValues("i").get(0), "");
        Assert.assertEquals(new Utils().getTagValues("f").size(), 0);
        Assert.assertEquals(new Utils().getTagValues("a").get(0), "1");
        Assert.assertEquals(new Utils().getTagValues("A").get(1), "2");
        Assert.assertEquals(new Utils().getTagValues("d").get(0), "31");
        Assert.assertEquals(new Utils().getTagValues("b").get(0), "22");
        Assert.assertEquals(new Utils().getTagValues("B").size(), 2);
        Assert.assertEquals(new Utils().getTagValue("B"), "22");
        Assert.assertEquals(new Utils().getTagValue("i"), "");
        Assert.assertNull(new Utils().getTagValue("ii"));

    }
}
