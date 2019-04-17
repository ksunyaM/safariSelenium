/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.swagger;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SwaggerUtilsTest {

    private final String yaml = "swagger: '2.0'\r\n" +
            "info:\r\n" +
            "  description: 'unitTest'\r\n" +
            "  version: 1.0.0\r\n" +
            "  title: unitTest\r\n" +
            "basePath: /v1\r\n" +
            "schemes:\r\n" +
            "  - http\r\n" +
            "produces:\r\n" +
            "  - application/json\r\n" +
            "paths:\r\n" +
            "  /test:\r\n" +
            "    post:\r\n" +
            "      operationId: 'testOperation'\r\n" +
            "      produces:\r\n" +
            "        - application/json\r\n" +
            "      parameters:\r\n" +
            "        - in: body\r\n" +
            "          name: testRequest\r\n" +
            "          required: true\r\n" +
            "          schema:\r\n" +
            "            $ref: '#/definitions/Request'\r\n" +
            "      responses:\r\n" +
            "        '200':\r\n" +
            "          description: OK\r\n" +
            "          schema:\r\n" +
            "            $ref: '#/definitions/Response'\r\n" +
            "        '400':\r\n" +
            "          description: Business Exception\r\n" +
            "          schema:\r\n" +
            "            $ref: '#/definitions/Response'\r\n" +
            "      security:\r\n" +
            "        - basicAuth: []\r\n" +
            "securityDefinitions:\r\n" +
            "  basicAuth:\r\n" +
            "    description: HTTP Basic Authentication\r\n" +
            "    type: basic\r\n" +
            "definitions:\r\n" +
            "  Request:\r\n" +
            "    type: object\r\n" +
            "    properties:\r\n" +
            "      id1:\r\n" +
            "        type: integer\r\n" +
            "        format: int32\r\n" +
            "      name1:\r\n" +
            "        type: string\r\n" +
            "  Response:\r\n" +
            "    type: object\r\n" +
            "    properties:\r\n" +
            "      id2:\r\n" +
            "        type: integer\r\n" +
            "        format: int32\r\n" +
            "      name2:\r\n" +
            "        type: string\r\n";

    private final String correctRequest = "{\r\n" +
            "  \"id1\": 100,\r\n" +
            "  \"name1\": \"testString\"\r\n" +
            "}\r\n";

    private final String wrongRequest = "{\r\n" +
            "  \"id\": 0,\r\n" +
            "  \"name1\": 100\r\n" +
            "}\r\n";

    private final String correctResponse = "{\r\n" +
            "  \"id2\": 100,\r\n" +
            "  \"name2\": \"testString\"\r\n" +
            "}\r\n";

    private final String wrongResponse = "{\r\n" +
            "  \"id\": 0,\r\n" +
            "  \"name2\": 100\r\n" +
            "}\r\n";

    @Test
    public void correctRequest() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = SwaggerUtils.validateRequest(yaml, correctRequest, "v1/test", "POST", messages);
        Assert.assertTrue(validationResult);
        Assert.assertEquals(messages.toString(), "[]");
    }

    @Test
    public void wrongRequest() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = SwaggerUtils.validateRequest(yaml, wrongRequest, "v1/test", "POST", messages);
        Assert.assertFalse(validationResult);
        Assert.assertTrue(messages.toString().contains("ERROR"));
    }

    @Test
    public void correctResponse() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = SwaggerUtils.validateResponse(yaml, correctResponse, "v1/test", "POST", messages);
        Assert.assertTrue(validationResult);
        Assert.assertEquals(messages.toString(), "[]");
    }

    @Test
    public void wrongResponse() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = SwaggerUtils.validateResponse(yaml, wrongResponse, "v1/test", "POST", messages);
        Assert.assertFalse(validationResult);
        Assert.assertTrue(messages.toString().contains("ERROR"));
    }

    @Test
    public void correctResponseWithoutMsg() {
        boolean validationResult;
        validationResult = SwaggerUtils.validateResponse(yaml, correctResponse, "v1/test", "POST");
        Assert.assertTrue(validationResult);
    }

    @Test
    public void wrongResponseWithoutMsg() {
        boolean validationResult;
        validationResult = SwaggerUtils.validateResponse(yaml, wrongResponse, "v1/test", "POST");
        Assert.assertFalse(validationResult);
    }

    @Test
    public void correctRequestWithResponseCode() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = SwaggerUtils.validateResponse(yaml, correctResponse, "v1/test", "POST", 400, messages);
        Assert.assertTrue(validationResult);
        Assert.assertEquals(messages.toString(), "[]");
    }

    @Test
    public void wrongRequestWithResponceCode() {
        StringBuilder messages = new StringBuilder();
        boolean validationResult;
        validationResult = SwaggerUtils.validateResponse(yaml, wrongResponse, "v1/test", "POST", 400, messages);
        Assert.assertFalse(validationResult);
        Assert.assertTrue(messages.toString().contains("ERROR"));
    }

}

