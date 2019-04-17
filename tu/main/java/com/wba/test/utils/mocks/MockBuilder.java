/*
 * Copyright 2018 Walgreen Co.
 */

package com.wba.test.utils.mocks;

import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.RegExp;
import org.apache.commons.lang.StringUtils;
import org.apache.http.annotation.Obsolete;

import java.util.HashMap;

public class MockBuilder {

    private String name = "";
    private String url = "";
    @Obsolete
    private String operationId = "";
    private String input = null;
    private String output = "{}";
    private String excludeList = null;
    private String method = "GET";
    private String httpStatusCode = "200";
    private String resource;
    private HashMap<String, String> availableParams = new HashMap<>();
    private HashMap<String, String> headerParams = new HashMap<>();
    private MockServer mockServer;
    private boolean overwriteWhenExists = false;

    public MockBuilder() {
    }

    public MockBuilder(String url, String operationId, String input, String output, String excludeList, String method, String httpStatusCode) {
        this.url = url;
        this.operationId = operationId;
        this.input = input;
        this.output = output;
        this.excludeList = excludeList;
        this.method = method;
        this.httpStatusCode = httpStatusCode;
    }

    private static String getJson() {
        return "{\n" +
                "\t\"id\": \"\",\n" +
                "\t\"resource\": \"\",\n" +
                "\t\"url\": \"\",\n" +

                "\t\"input\": null,\n" +
                "\t\"output\": \"\",\n" +
                "\t\"excludeList\": null,\n" +
                "\t\"method\": \"GET\",\n" +
                "\t\"httpStatusCode\": \"200\",\n" +
                "\t\"availableParams\": []\n" +
                "}";
    }

    public String getName() {
        return name;
    }

    public MockBuilder name(String name) {
        this.name = name;
        return this;
    }

    public MockBuilder httpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        return this;
    }

    public MockBuilder url(String url) {
        this.url = (url.startsWith("/") ? "" : "/") + url;
        return this;
    }

    public MockBuilder method(String method) {
        this.method = method;
        return this;
    }

    /**
     * OperationId is not required for mock creation anymore. But it was used for define a mock name, so use .name() instead.
     *
     * @param operationId
     * @return
     */
    @Deprecated
    public MockBuilder operationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public MockBuilder output(String json) {
        this.output = json;
        return this;
    }

    public MockBuilder input(String json) {
        this.input = json;
        return this;
    }

    public MockBuilder addHeader(String key, String value) {
        headerParams.put(key, value);
        return this;
    }

    public MockBuilder addParam(String key, String value) {
        availableParams.put(key, value);
        return this;
    }

    public MockBuilder resource(String resource) {
        this.resource = resource;
        return this;
    }

    public MockBuilder excludeList(String excludeList) {
        this.excludeList = excludeList;
        return this;
    }

    public MockBuilder mockServer(MockServer url) {
        this.mockServer = url;
        return this;
    }

    public MockBuilder overwriteWhenExists(boolean value) {
        this.overwriteWhenExists = value;
        return this;
    }

    public void build() {

        String json = getJson();
        json = JsonUtils.setJsonAttribute(json, "resource",
                resource == null ? RegExp.getMatches(url, "^/([^/?]+)(?:/|\\?|$)").get(0)[0] : resource);
        json = JsonUtils.setJsonAttribute(json, "url", url);
        json = JsonUtils.setJsonAttribute(json, "input", input);
        json = JsonUtils.setJsonAttribute(json, "output", output);
        json = JsonUtils.setJsonAttribute(json, "excludeList", excludeList);
        json = JsonUtils.setJsonAttribute(json, "method", method);
        json = JsonUtils.setJsonAttribute(json, "httpStatusCode", httpStatusCode);

        int i = 0;
        for (String key : availableParams.keySet()) {
            json = JsonUtils.setJsonAttribute(json, "availableParams[" + i + "].key", key);
            json = JsonUtils.setJsonAttribute(json, "availableParams[" + i++ + "].value", availableParams.get(key));
        }

        int j = 0;
        for (String key : headerParams.keySet()) {
            json = JsonUtils.setJsonAttribute(json, "headerParams[" + j + "].key", key);
            json = JsonUtils.setJsonAttribute(json, "headerParams[" + j++ + "].value", headerParams.get(key));
        }

        new MockUtils().createMock(new DataMock(
                (mockServer == null ? null : mockServer.getUrl()),
                json,
                StringUtils.defaultIfEmpty(name, operationId),
                overwriteWhenExists));
    }
}
