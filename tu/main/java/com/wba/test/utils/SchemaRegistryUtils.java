/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.oneleo.test.automation.core.kafka.KafkaConstants;
import com.oneleo.test.automation.core.kafka.KafkaPropertiesReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.oneleo.test.automation.core.LogUtils.log;
import static io.restassured.RestAssured.given;

public class SchemaRegistryUtils {
    private static final Logger LOGGER = log(SchemaRegistryUtils.class);
    private String schemaRegistryUrl;

    public SchemaRegistryUtils() {
        schemaRegistryUrl = KafkaPropertiesReader.read()
                .get(KafkaConstants.DEFAULT_API_PROPERTIES_BUNDLE_KEY)
                .getProperty(KafkaConstants.SCHEMA_REGISTRY_URL);
    }

    public String getSchemaValue(String schemaName) {
        return new JSONObject(given().get(schemaRegistryUrl + "/subjects/" + schemaName + "/versions/latest").asString())
                .get("schema")
                .toString();
    }

    public List<String> getListOfSchemas() {
        return new JSONArray(given().get(schemaRegistryUrl + "/subjects/").asString()).toList().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public boolean isSchemaExist(String schemaName) {
        try {
            getSchemaValue(schemaName);
            LOGGER.info("Schema with name " + schemaName + " is exist in SchemaRegistry");
            return true;
        } catch (JSONException e) {
            LOGGER.warn("Schema with name " + schemaName + " is NOT exist in SchemaRegistry");
            return false;
        }
    }
}
