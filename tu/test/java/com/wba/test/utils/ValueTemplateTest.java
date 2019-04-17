/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/* Copyright 2018 Walgreen Co. */
public class ValueTemplateTest {

    @DataProvider(name = "test1")
    public Object[][] data() {
        return new Object[][]{
                {"key", "default::", "^(\\{\"key\":\"qwe\"})$"},
                {"key", "def::", "^(\\{\"key\":\"qwe\"})$"},
                {"key", "corr::4", "^(\\{\"key\":\"corr\\d{4}\"})$"},
                {"key", "long::106", "^(\\{\"key\":106})$"},
                {"key", "b::true", "^(\\{\"key\":true})$"},
                {"key", "bool::true", "^(\\{\"key\":true})$"},
                {"key", "bool::false", "^(\\{\"key\":false})$"},
                {"key", "boolean::true", "^(\\{\"key\":true})$"},
                {"key", "boolean::false", "^(\\{\"key\":false})$"},
                {"key", "boolean::blah", "^(\\{\"key\":false})$"},
                {"key", "double::10.6", "^(\\{\"key\":10\\.6})$"},
                {"key", "double::", "^(\\{\"key\":0.0})$"},
                {"key", "i::10", "^(\\{\"key\":10})$"},
                {"key", "i::", "^(\\{\"key\":0})$"},
                {"key", "random::", "^(\\{\"key\":\"\"\\})$"},
                {"key", "random::5", "^(\\{\"key\":\"\\d{5}\"\\})$"},
                {"key", "timestamp::", "^(\\{\"key\":\\d{13}\\})$"},
                {"key", "timestamp::+12m", "^(\\{\"key\":\\d{13}\\})$"},
                {"key", "timestamp::-1d", "^(\\{\"key\":\\d{13}\\})$"},
                {"key", "UUID::", "^(\\{\"key\":\"[a-z,0-9]{8}\\-)"},
                {"key", "null::", "^(\\{\"key\":null\\})$"},
                {"key", "delete::", "^(\\{\\})$"},
        };
    }

    @Test(dataProvider = "test1")
    public void templateTest(String key, String value, String expectedJsonRegExp) {
        Object result = ValueTemplateUtils.createValue(value);
        String initialJson = "{\"key\": \"qwe\"}";
        String newJson = JsonUtils.setJsonAttribute(initialJson, key, result);
        List<String[]> matches = RegExp.getMatches(newJson, expectedJsonRegExp);
        assertEquals("Json is wrong: " + newJson, 1, matches.size());
    }

    @DataProvider(name = "test3")
    public Object[][] data3() {
        return new Object[][]{
                {"timestamp::+1"},
                {"timestamp::+1a"},
                {"dt::+1"},
                {"dt::12a"},
                {"dt::h12"},
        };
    }

    @Test(dataProvider = "test3", expectedExceptions = IllegalArgumentException.class)
    public void templateFailTest(String key) {
        final Object value = ValueTemplateUtils.createValue(key);
        System.out.println(value);
    }

    @Test
    public void dateTimeTest() {
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dt::-6h")).toString().matches("^20\\d\\d-.*Z$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dt::+1s")).toString().matches("^20\\d\\d-.*Z$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dt::1d")).toString().matches("^20\\d\\d-.*Z$"));
    }

    @Test
    public void dateTimeTest01() {
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dtt::-6h")).toString().matches("^20\\d\\d-.*Z$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dtt::-6h")).toString().matches("^.+T.+$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dtt::+1s")).toString().matches("^20\\d\\d-.*Z$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dtt::+1s")).toString().matches("^.+T.+$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dtt::1d")).toString().matches("^20\\d\\d-.*Z$"));
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("dtt::1d")).toString().matches("^.+T.+$"));
    }

    @Test
    public void dateTest01() {
        assertTrue(Objects.requireNonNull(ValueTemplateUtils.createValue("date::-6d")).toString().matches("^20\\d\\d-\\d+-\\d+$"));
    }

    @Test
    public void uuidVersionTest() {
        assertEquals(4, UUID.fromString(Objects.requireNonNull(ValueTemplateUtils.createValue("uuid::")).toString()).version());
        assertEquals(1, UUID.fromString(Objects.requireNonNull(ValueTemplateUtils.createValue("timeuuid::")).toString()).version());
    }

    @Test
    public void daysSinceEpochTest() {
        long expected = System.currentTimeMillis() / 1000 / 3600 / 24;
        assertEquals(expected, ValueTemplateUtils.createValue("days::"));
        expected = (System.currentTimeMillis() / 1000 / 3600 / 24) - 2;
        assertEquals(expected, ValueTemplateUtils.createValue("days::-2d"));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void dateTimeTest02() {
        assertTrue(518399900 <
                ((Long) ValueTemplateUtils.createValue("timestamp::")) -
                        ((Long) ValueTemplateUtils.createValue("timestamp::-6d"))
        );
    }
}
