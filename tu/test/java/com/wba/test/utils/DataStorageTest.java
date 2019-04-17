/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataStorageTest {

    private DataStorage dataStorage = DataStorage.getInstance();
    private Map<String, Object> map = new HashMap<>();

    @BeforeMethod
    public void createData() {
        map.put("key00", "value00");
        map.put("key01", "value01");
        map.put("key02", false);
        map.put("key03", 100500);
        map.put("key04", null);
        dataStorage.add(map);
    }

    @AfterMethod
    public void clear() {
        dataStorage.reset();
    }


    @Test
    public void getNextNumberTest() {
        Assert.assertEquals(dataStorage.getNextNumber("key0"), dataStorage.map().size());
        Assert.assertEquals(dataStorage.getNextNumber("key"), 0);
    }

    @Test
    public void addWithNumberTest() {
        Assert.assertEquals(dataStorage.addWithNumber("qwe", 1), "qwe0");
        Assert.assertEquals(dataStorage.unmask("qwe0"), 1);
    }

    @Test
    public void testAdd01() {
        int size = dataStorage.map().size();
        dataStorage.add("key100", null);
        Assert.assertEquals(dataStorage.map().size(), size + 1);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testAdd02() {
        dataStorage.add("key01", "value1");
    }

    @Test
    public void testUnmask01() {
        map.forEach((k, v) -> Assert.assertEquals(dataStorage.unmask(k), v));
    }

    @Test
    public void testUnmask02() {
        String key = UUID.randomUUID().toString();
        Assert.assertEquals(dataStorage.unmask(key), key);
    }

    @Test
    public void testHas() {
        Assert.assertTrue(dataStorage.has("key02"));
    }

    @Test
    public void testReset() {
        Assert.assertNotEquals(dataStorage.map().size(), 0);
        dataStorage.reset();
        Assert.assertEquals(dataStorage.map().size(), 0);
    }
}
