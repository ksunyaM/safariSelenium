/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventStorage;
import com.wba.test.utils.kafka.EventUtils;
import com.wba.test.utils.kafka.consumer.ConsumerRecordsToEventsMapper;
import com.wba.test.utils.kafka.consumer.ConsumerService;
import cucumber.api.DataTable;
import cucumber.runtime.CucumberException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static com.oneleo.test.automation.core.LogUtils.log;
import static com.wba.test.utils.EnvironmentNameTransformUtils.transformName;
import static com.wba.test.utils.JsonUtils.setJsonAttribute;
import static com.wba.test.utils.ValueTemplateUtils.createValue;

@SuppressWarnings("WeakerAccess")
public class BaseStep {
    protected static final Logger LOGGER = log(BaseStep.class);

    protected EventStorage eventStorage = EventStorage.getInstance();

    protected DataStorage dataStorage = DataStorage.getInstance();

    protected void updateJson(Event event, DataTable data) {
        event.setBody(updateJson(event.getBody(), data));
    }

    protected void updateJson(Event event, Map<String, String> data) {
        event.setBody(updateJson(event.getBody(), data));
    }

    protected void updateJson(Event event, String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        updateJson(event, map);
    }

    /**
     * Update json in event.
     *
     * @param eventPath path to element starting from event name
     * @param value     new value. Value will be resolved via defineValue()
     */
    protected void updateJson(String eventPath, String value) {
        final String[] p = eventPath.split("\\.", 2);
        updateJson(eventStorage.findEvent(p[0]), p[1], value);
    }

    protected String updateJson(String json, DataTable data) {
        return updateJson(json, data.asMap(String.class, String.class));
    }

    protected String updateJson(String json, Map<String, String> data) {
        for (String k : data.keySet()) {
            String key = asStr(k);
            Object value = defineValue(data.get(k));
            json = JsonUtils.setJsonAttribute(json, key, value);
        }

        return JsonUtils.prettify(json);
    }

    protected void updateEventKey(Event event, Map<String, String> data) {
        String json = Optional.ofNullable(event.getBodyKey())
                .orElseThrow(() -> new RuntimeException("Event does't have key body for update"));
        event.setBodyKey(updateJson(json, data));
    }

    protected void updateEventKey(String key, String value) {
        updateEventKey(eventStorage.getLastProduced(), Collections.singletonMap(key, value));
    }

    /**
     * Create a new element(s) in Json array
     *
     * @param jsonPath path to array started from event name.
     * @param number   [optional] number of element should be added or deleted if number is negative. Default is 1.
     * @return new array size
     */
    public int addArrayElements(String jsonPath, Object... number) {
        final Integer num = new Integer("" + Utils.defaultOrFirst(1, number));
        final Integer size = asInt(jsonPath + ".length()");
        final String[] p = RegExp.getMatches(jsonPath, "^([^.]+)\\.(.*)").get(0);

        if (num > 0) {
            final ObjectNode node = (ObjectNode) defineValue(jsonPath + "[0]");
            String json = eventStorage.findEvent(p[0]).getBody();
            for (int i = 0; i < num; i++) {
                json = setJsonAttribute(json, p[1] + "[" + (size + i) + "]", node);
            }
            eventStorage.findEvent(p[0]).setBody(JsonUtils.prettify(json));
        } else if (num < 0) {
            for (int i = 0; i < (-1 * num); i++) {
                updateJson(jsonPath + "[0]", "delete::");
            }
        }
        return asInt(jsonPath + ".length()");
    }

    protected void consumeEvent(String eventName, String topicName, Map<String, Object> filter, int eventsAmountExpected) {
        List<ConsumerRecord<Object, Object>> records =
                EventUtils.consumeRecordsAll(transformName(topicName), ConsumerService.buildFilterBasedOnMap(filter));
        Assert.assertEquals(String.format("Expected amount of new events are not matching, filter is %s", filter), eventsAmountExpected, records.size());
        if (!records.isEmpty()) {
            Event event = ConsumerRecordsToEventsMapper.map(records).get(0);
            if (eventName != null) {
                event.setName(eventName);
            }
            eventStorage.addConsumed(event);
        }
    }

    protected List<Event> consumeEvents(String eventName, String topicName, Map<String, Object> filter, int eventsAmountExpected, Boolean... verifyAmount) {
        final String _topicName = transformName(topicName);
        List<ConsumerRecord<Object, Object>> records =
                EventUtils.consumeRecordsAll(_topicName, ConsumerService.buildFilterBasedOnMap(filter));
        if (Utils.defaultOrFirst(true, verifyAmount)) {
            Assert.assertEquals(
                    String.format("Expected amount of new events are not matching, topic is %s, filter is %s", _topicName, filter),
                    eventsAmountExpected, records.size());
        }
        if (records.isEmpty()) return new ArrayList<>();
        final List<Event> events = ConsumerRecordsToEventsMapper.map(records);
        if (eventName != null) {
            for (int i = 0; i < events.size(); i++) {
                events.get(i).setName(eventName + i);
            }
        }
        return events;
    }

    /**
     * The method return value if key is present in EventStorage(<eventName[::key].jsonPath>) or in DataStorage, otherwise the key will be returned as is.
     * If event name has '::key' tail - search will be provided in event key, otherwise in event body.
     *
     * @param key string like jsonPath - started with event name or key in dataStorage.
     * @return value (from json or DataStorage) or the key if key is not be resolved.
     */
    protected Object defineValue(String key) {
        if (null == key) return null;
        String uuid = UUID.randomUUID().toString();
        Object jsonValue = uuid;
        Object dsValue;
        boolean doNotCreateValue = false;
        String[] e = key.split("\\.", 2);
        if (e.length > 1) {
            try {
                final String[] n = e[0].split("::", 2);
                if (n.length > 1) {
                    if (n[1].equalsIgnoreCase("key")) {
                        doNotCreateValue = true; // ::key text should not be resolved as ValueTemplate.
                        jsonValue = VerificationUtils.convert(JsonUtils.getJsonValue(eventStorage.findEvent(n[0]).getBodyKey(), e[1]));
                    }
                } else {
                    jsonValue = VerificationUtils.convert(JsonUtils.getJsonValue(eventStorage.findEvent(e[0]).getBody(), e[1]));
                }
            } catch (Exception ignore) {
            }
        }
        dsValue = dataStorage.unmask(key);
        return jsonValue != uuid ? jsonValue : dsValue != key ? dsValue : doNotCreateValue ? key : createValue(key);
    }

    /**
     * Verify if [json]path exists in dataStorage or EventStorage. JsonPath with conditions supported also.
     *
     * @param path - to value in eventStorage or DataStorage
     * @return boolean value
     */
    public boolean isValueExists(String path) {
        final Object o = defineValue(path);
        return o == null
                || !(o.toString().equals(path)
                || ((path.contains("[?(@") || path.contains("..")) && o.toString().equals("[]")));
    }

    protected boolean isTagPresent(String... tag) {
        return Arrays.stream(tag).anyMatch(s -> dataStorage.unmask("~SCENARIO_TAGS").toString().contains(s.toLowerCase()));
    }

    protected Integer asInt(String key) {
        String value = asStr(key);
        if (null == value) return null;
        if (value.matches("^\\d+\\.0+$")) {
            value = value.replaceFirst("\\.0+$", "");
        }
        if (value.matches("\\d+")) return Integer.parseInt(value);
        else
            throw new NumberFormatException(String.format("Value %s cannot be converted to Integer (key = %s)", value, key));
    }

    protected Long asLong(String key) {
        return Optional.ofNullable(defineValue(key))
                .map(o -> Long.parseLong(o.toString()))
                .orElse(null);
    }

    protected BigDecimal asBigDec(Object key) {
        return Optional.ofNullable(key)
                .map(o -> asStr(key.toString()))
                .map(VerificationUtils::toBigDecimal)
                .orElse(null);
    }

    protected String asCurr(Object key) {
        return Optional.ofNullable(key)
                .map(o -> asStr(key.toString()))
                .map(s -> VerificationUtils.toBigDecimal(s).round(new MathContext(4)).toPlainString())
                .map(s -> RegExp.replaceAll(s, "\\.0$", "\\.00"))
                .orElse(null);
    }

    protected String asStr(String key) {
        return Optional.ofNullable(defineValue(key)).map(o -> {
            if (o instanceof ArrayNode) {
                if (((ArrayNode) o).size() != 1) {
                    throw new CucumberException("expecting one element: " + o);
                } else if (((ArrayNode) o).get(0) instanceof NullNode) {
                    return null;
                } else {
                    return ((ArrayNode) o).get(0).asText();
                }
            }
            return o.toString();
        }).orElse(null);
    }

    protected Long asMillSec(Object key) {
        if (key == null) return null;
        Object realValue = defineValue(key.toString());
        if (realValue == null) return null;
        String v = realValue instanceof Double ? "" + ((Double) realValue).longValue() : realValue.toString();
        if (v.matches("^\\d{1,5}$")) return Long.parseLong(v) * 24 * 3600 * 1000; // in days 17702
        if (v.matches("^\\d{6,}$")) return Long.parseLong(v); // already in milliseconds 1528394013601
        if (v.matches("^\\d{4}-\\d{2}-.*")) {
            if (v.length() == 10) { // 2018-10-11
                v += "T00:00:00.000Z";
                return DateUtils.convertStringToMilli(v, FormatTo.CAS_DT_T);
            }
            if (v.contains("T")) {
                if (!v.endsWith("Z")) { // This is incorrect format that occurs in Patient API responses. 1945-01-18T00:00:00
                    v = v + ".000Z";
                }
                return DateUtils.convertStringToMilli(v, FormatTo.CAS_DT_T);
            } else {
                return DateUtils.convertStringToMilli(v, FormatTo.CAS_DT);
            }
        }
        if (v.matches("^\\w{3} .*")) {
            return DateUtils.convertStringToMilli(v, FormatTo.DATETIME01);
        }
        throw new CucumberException("can not convert to milli sec: " + key + "/" + v);
    }

    protected UUID asUUID(String key) {
        return UUID.fromString(defineValue(key).toString());
    }

    protected List<JsonNode> asList(String path) {
        final Object o = defineValue(path);
        if (o instanceof ArrayNode) {
            List<JsonNode> res = new ArrayList<>();
            ((ArrayNode) o).forEach(res::add);
            LOGGER.debug("asList: " + res.toString());
            return res;
        } else {
            throw new CucumberException("List is not created: " + o.toString());
        }
    }
}

