/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.kafka;

import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.ResourceUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.oneleo.test.automation.core.LogUtils.log;
import static com.wba.test.utils.EnvironmentNameTransformUtils.clearName;
import static com.wba.test.utils.EnvironmentNameTransformUtils.transformName;
import static com.wba.test.utils.ResourceUtils.getResourcePath;

public class EventBuilder {

    private static final String PATH_TOPIC_METADATA = "topics";
    private static final Logger LOGGER = log(EventBuilder.class);

    private String name;
    private String topicName;
    private String schemaName;
    private int partition;
    private Map<String, String> headers;
    private String body;
    private String schemaNameKey = null;
    private String bodyKey = null;
    private String serviceName;

    public EventBuilder() {
    }

    public EventBuilder applyDefaultsForTopic(String topicName) {
        TopicMetadata topicMetadata;
        try {
            topicMetadata = ResourceUtils.readYamlResource(getResourcePath(PATH_TOPIC_METADATA, clearName(topicName) + ".yaml"), TopicMetadata.class);
            this.topicName = topicMetadata.getHeaders().get("requestType") != null ? topicName : transformName(topicName);
            this.headers(topicMetadata.getHeaders());
            this.schemaName(topicMetadata.getSchemaName());
            this.body(ResourceUtils.getResourceAsString(topicMetadata.getDefaultBodyPath()));
            this.serviceName(topicMetadata.getServiceName());
            this.schemaNameKey(topicMetadata.getSchemaNameKey());
            this.bodyKey(this.schemaNameKey == null ? null : ResourceUtils.getResourceAsString(topicMetadata.getDefaultBodyPathKey()));
            this.partition(Optional.ofNullable(topicMetadata.getPartition()).orElse(0));
        } catch (IOException e) {
            LOGGER.warn("Unable to read topic metadata: {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return this;
    }


    public EventBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EventBuilder topicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public EventBuilder schemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    public EventBuilder body(String body) {
        this.body = JsonUtils.prettify(body);
        return this;
    }

    public EventBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public EventBuilder schemaNameKey(String schemaNameKey) {
        this.schemaNameKey = schemaNameKey;
        return this;
    }

    public EventBuilder bodyKey(String bodyKey) {
        this.bodyKey = JsonUtils.prettify(bodyKey);
        return this;
    }

    public EventBuilder partition(Integer partition) {
        this.partition = partition;
        return this;
    }


    public EventBuilder serviceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public EventBuilder buildServiceName(Object... params) {
        serviceName = String.format(serviceName, params);
        return this;
    }

    public Event build() {
        return new Event(name, topicName, schemaName, body, headers, serviceName, schemaNameKey, bodyKey, partition);
    }
}
