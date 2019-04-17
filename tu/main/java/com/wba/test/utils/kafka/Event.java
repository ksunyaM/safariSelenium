/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.kafka;

import com.fasterxml.jackson.databind.node.NullNode;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.ValueTemplateUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.UUID;

public class Event {

    private String body;
    private String name;
    private String topicName;
    private String schemaName;
    private String serviceName;
    private Map<String, String> headers;
    private String bodyKey;
    private String schemaNameKey;

    private int partition;

    Event(String name, String topicName, String schemaName, String body, Map<String, String> headers, String serviceName, String schemaNameKey, String bodyKey, int partition) {
        this.name = StringUtils.isEmpty(name) ? UUID.randomUUID().toString() : name;
        this.topicName = topicName;
        this.schemaName = schemaName;
        this.body = body;
        this.headers = headers;
        this.serviceName = serviceName;
        this.schemaNameKey = schemaNameKey;
        this.bodyKey = bodyKey;
        this.partition = partition;
    }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public void replaceBodyAttribute(String key, Object value) {
        setBody(JsonUtils.setJsonAttribute(
                getBody(),
                key,
                ValueTemplateUtils.createValue(value.toString())));
    }

    public <T> T getBodyAttribute(String key) {
        T value = JsonUtils.getJsonValue(getBody(), key);
        return value instanceof NullNode ? null : value;
    }

    public <T> T getBodyAttribute(String key, Class<T> aClass) {
        return JsonUtils.getJsonValue(getBody(), key, aClass);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSchemaNameKey() {
        return schemaNameKey;
    }

    public void setSchemaNameKey(String schemaNameKey) {
        this.schemaNameKey = schemaNameKey;
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    @Override
    public String toString() {
        return String.format("%n Event name=%s" +
                        "%n topicName=%s" +
                        "%n schemaName=%s" +
                        "%n serviceName=%s" +
//                        "%n headers=%s" + // may contains binary value that corrupt the log
                        "%n schemaNameKey=%s" +
                        "%n bodyKey=%n%s" +
                        "%n body=%n%s" +
                        "%n partition=%n%s",
                name,
                topicName,
                schemaName,
                serviceName,
//                headers,
                schemaNameKey,
                bodyKey,
                body,
                partition
        );
    }

    public enum Action {
        REPLACE_VARIABLES,
        QUOTE_SPECIAL_CHARACTERS,
        PRETTIFY,
        ENCRYPT_FIELDS,
        DECRYPT_FIELDS,
    }
}
