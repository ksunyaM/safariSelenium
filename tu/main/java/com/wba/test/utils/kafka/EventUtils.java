/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.kafka;

import com.wba.test.utils.*;
import com.wba.test.utils.kafka.consumer.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;

import java.util.*;
import java.util.function.Predicate;

public class EventUtils {
    public static final int DEFAULT_CONSUMING_TIMEOUT_SECOND = 5;


    public static List<ConsumerRecord<Object, Object>> consumeRecordsAll(String topicName, Predicate<ConsumerRecord<Object, Object>> predicate) {
        try {
            ConsumerService consumerService = new ConsumerService(getConfigWithEarliestOffset());
            consumerService.subscribeOnTopics(Collections.singletonList(topicName));
            consumerService.setTimeoutSeconds(getTimeout());
            return consumerService.pollRecords(predicate, Integer.MAX_VALUE);
        } catch (SerializationException e) {
            ConsumerService consumerService = new ConsumerService(getStringDeserializationProperties());
            consumerService.subscribeOnTopics(Collections.singletonList(topicName));
            consumerService.setTimeoutSeconds(getTimeout());
            return consumerService.pollRecords(predicate, Integer.MAX_VALUE);
        }
    }

    public static Map<String, Object> getStringDeserializationProperties() {
        return new HashMap<String, Object>() {{
            put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        }};
    }

    private static Map<String, Object> getConfigWithEarliestOffset() {
        return Collections.singletonMap(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    private static int getTimeout() {
        return new Integer(
                Optional.ofNullable(new Utils().getTagValue(ScenarioTag.CTIMEOUT.get() + "="))
                        .orElseGet(() -> Optional.ofNullable(PropertiesReader.read("default-kafka", true).getProperty("consuming.timeout.seconds"))
                                .orElse("" + DEFAULT_CONSUMING_TIMEOUT_SECOND)));
    }

    public static String quoteSpecialCharacters(String json) {
        return json
                .replace("\u001E", "\\u001E")
                .replace("\u001C", "\\u001C")
                .replace("\u001D", "\\u001D");
    }

    public static void doEventAction(Event event, Event.Action action) {
        switch (action) {
            case ENCRYPT_FIELDS:
                if (isEncrypt(event)) new EncryptionUtils().encryptEvent(event);
                break;
            case DECRYPT_FIELDS:
                if (isEncrypt(event)) new EncryptionUtils().decryptEvent(event);
                break;
            case REPLACE_VARIABLES:
                event.setBody(ValueSetterUtils.jsonSetValues(event.getBody()));
                event.setBodyKey(ValueSetterUtils.jsonSetValues(event.getBodyKey()));
                break;
            case QUOTE_SPECIAL_CHARACTERS:
                event.setBody(quoteSpecialCharacters(event.getBody()));
                break;
            case PRETTIFY:
                event.setBody(JsonUtils.prettify(event.getBody()));
                break;
        }
    }

    private static boolean isEncrypt(Event event) {
        return isEncryptionEnabled() &&
                Optional.ofNullable(event.getBody()).orElse("").contains("\"keyId\"");
    }

    private static boolean isEncryptionEnabled() {
        try {
            return Boolean.valueOf(PropertiesReader.read("default-efl.properties", true).getProperty("auto.encryption.enabled"));
        } catch (Throwable e) {
            return false;
        }
    }
}
