/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.fge.jsonpatch.diff.JsonDiff;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidModificationException;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import cucumber.runtime.CucumberException;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Arrays;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.oneleo.test.automation.core.LogUtils.log;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonUtils {

    private static final Logger LOGGER = log(JsonUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static DocumentContext parseJson(String json) {
        Configuration CONFIGURATION = Configuration.builder()
                .jsonProvider(new JacksonJsonNodeJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
//                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
        return com.jayway.jsonpath.JsonPath.using(CONFIGURATION).parse(json);
    }

    public static <T> T readJson(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new CucumberException("Json read error:" + e);
        }
    }

    public static <T> T getJsonValue(String json, String jsonPath, Class<T> clazz, boolean... dieIfElementIsNotExist) {
        if (dieIfElementIsNotExist.length > 0 && dieIfElementIsNotExist[0]) {
            assertThat(json, hasJsonPath(jsonPath));
        }
        return parseJson(json).read(jsonPath, clazz);
    }

    public static <T> T getJsonValue(String json, String jsonPath, boolean... dieIfElementIsNotExist) {
        if (dieIfElementIsNotExist.length > 0 && dieIfElementIsNotExist[0]) {
            assertThat(json, hasJsonPath(jsonPath));
        }
        return parseJson(json).read(jsonPath);
    }

    public static boolean isJsonPathExists(String json, String jsonPath) {
        try {
            DocumentContext dc = com.jayway.jsonpath.JsonPath.using(Configuration.defaultConfiguration()).parse(json);
            dc.read(jsonPath);
        } catch (PathNotFoundException e) {
            LOGGER.trace("Json path '{}' is not exist", jsonPath);
            return false;
        }
        LOGGER.trace("Json path '{}' is exist", jsonPath);
        return true;
    }

    public static String setJsonAttribute(String json, String jsonPath, Object value) {
        DocumentContext dc = parseJson(json);
        if (value == ValueTemplateUtils.TemplateAction.DELETE) {
            LOGGER.debug("Going to delete: " + jsonPath);
            dc.delete(jsonPath);
        } else if (value == ValueTemplateUtils.TemplateAction.DO_NOT_CHANGE) {
            LOGGER.debug("Going to leave value as is: " + jsonPath);
        } else if (isJsonPathExists(json, jsonPath)) {
            LOGGER.debug("Going to update: " + jsonPath + " value: " + (value == null ? "null" : value.toString()));
            dc.set(jsonPath, value);
        } else {
            final String[] elements = jsonPath.split("\\.");
            int i = 0;
            boolean delByMe = false;
            while (i < elements.length) {
                String path = String.join(".", Arrays.copyOfRange(elements, 0, i + 1));
                String pathPrev = i > 0 ? String.join(".", Arrays.copyOfRange(elements, 0, i)) : "$";
                final String[] e = RegExp.getMatches(elements[i], "(.*?)(?:\\[(\\d+)\\])?$").get(0);
                if (!isJsonPathExists(dc.jsonString(), path) || delByMe) {
                    LOGGER.debug("Going to create: " + path);
                    if (e[1] == null) {
                        try {
                            if (getJsonValue(dc.jsonString(), pathPrev) instanceof NullNode) {
                                throw new InvalidModificationException("found null node");
                            }
                            dc.put(pathPrev, elements[i], OBJECT_MAPPER.createObjectNode());
                        } catch (InvalidModificationException e1) {
                            dc.delete(pathPrev);
                            delByMe = true;
                            --i;
                            continue;
                        }
                    } else {
                        if (!isJsonPathExists(dc.jsonString(), pathPrev + "." + e[0]) ||
                                !(getJsonValue(dc.jsonString(), pathPrev + "." + e[0]) instanceof ArrayNode)
                        ) {
                            dc.put(pathPrev, e[0], OBJECT_MAPPER.createArrayNode());
                            continue;
                        }
                        dc.add(pathPrev + "." + e[0], OBJECT_MAPPER.createObjectNode());
                    }
                }
                ++i;
            }
            dc.set(jsonPath, OBJECT_MAPPER.valueToTree(value));
        }
        return dc.jsonString();
    }

    public static String compareJSONs(String jsonBefore, String jsonAfter) {
        try {
            String diff = JsonUtils.prettify(JsonDiff.asJson(OBJECT_MAPPER.readTree(jsonBefore), OBJECT_MAPPER.readTree(jsonAfter)).toString());
            diff = RegExp.replaceAll(diff, "\\[\n    \n\\]", "[]");
            return diff;
        } catch (IOException e) {
            throw new RuntimeException("JSONs comparison error:" + e);
        }
    }

    public static String prettify(String jsonString) {
        if (new Utils().isTagExist(ScenarioTag.NOPRETTIFY.get())) return jsonString;

        try {
            return JsonPath.with(jsonString)
                    .using(new JsonPathConfig(JsonPathConfig.NumberReturnType.BIG_DECIMAL)).prettify();
        } catch (Exception e) {
            return jsonString;
        }
    }
}
