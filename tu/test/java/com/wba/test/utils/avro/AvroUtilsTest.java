/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.avro;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AvroUtilsTest {
    String avro = "{"+
            "  \"type\": \"record\","+
            "  \"name\": \"Test\","+
            "  \"doc\": \"Test Avro\","+
            "  \"namespace\": \"com.wba.test\","+
            "  \"version\": \"1.0.0\","+
            "  \"fields\": ["+
            "    {"+
            "      \"name\": \"id\","+
            "      \"type\": {"+
            "        \"type\": \"long\""+
            "      }"+
            "    },"+
            "    {"+
            "      \"name\": \"name\","+
            "      \"type\": \"string\""+
            "    }"+
            "  ]"+
            "}";

    private final String correctJson = "{\r\n" +
            "  \"id\": 100,\r\n" +
            "  \"name\": \"testString\"\r\n" +
            "}\r\n";

    private final String wrongJson = "{\r\n" +
            "  \"id1\": 0,\r\n" +
            "  \"name\": 100\r\n" +
            "}\r\n";

    @Test
    public void correctJson() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = AvroUtils.validate(avro, correctJson, messages);
        Assert.assertTrue(validationResult);
        Assert.assertEquals(messages.toString(), "");
    }

    @Test
    public void wrongJson() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = AvroUtils.validate(avro, wrongJson, messages);
        Assert.assertFalse(validationResult);
        Assert.assertTrue(messages.toString().length() > 0);
    }

    @Test
    public void correctJsonWithoutMsg() {
        boolean validationResult;
        validationResult = AvroUtils.validate(avro, correctJson);
        Assert.assertTrue(validationResult);
    }

    @Test
    public void wrongJsonWithoutMsg() {
        boolean validationResult;
        validationResult = AvroUtils.validate(avro, wrongJson);
        Assert.assertFalse(validationResult);
    }
}
