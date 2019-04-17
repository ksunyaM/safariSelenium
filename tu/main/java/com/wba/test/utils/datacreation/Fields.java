/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.datacreation;

import com.wba.test.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fields {
    Logger LOGGER = LoggerFactory.getLogger(Fields.class);
    private Map<String, List<String>> fields = new HashMap<>();
    private final String fieldsYamlPath = "schemas/fields.yaml";

    public Fields get() {
        try {
            return ResourceUtils.readYamlResource(fieldsYamlPath, Fields.class);
        } catch (Exception e) {
            LOGGER.error("Properties YAML  has not been found in the path: {}", fieldsYamlPath);
            return new Fields();
        }
    }

    public Map<String, List<String>> getFields() {
        return fields;
    }
}
