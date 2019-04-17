/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.PathNotFoundException;
import com.oneleo.test.automation.core.kafka.KafkaPropertiesReader;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.PropertiesReader;
import com.wba.test.utils.kafka.Event;
import cucumber.api.DataTable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static com.oneleo.test.automation.core.LogUtils.log;

public class ConsumerService {

    private static final int DEFAULT_CONSUMING_TIMEOUT_SECOND = 20;
    private static final int DEFAULT_EXPECTED_RECORDS_AMOUNT = 1;
    private static final int CONSUMER_POLL_TIMEOUT = 500;
    private static final Logger LOGGER = log(ConsumerService.class);
    private Integer timeoutSeconds;
    private KafkaConsumer<Object, Object> consumer;

    public ConsumerService(Map<String, Object> properties) {
        Properties consumerProperties = KafkaPropertiesReader.read().get("default");
        if (Boolean.parseBoolean(consumerProperties.getProperty("kafka.security.enabled"))) {
            Properties sslProperties = PropertiesReader.readFromAbsolutePath(consumerProperties.getProperty("kafka.producer.security.properties.path"));
            consumerProperties.putAll(sslProperties);
        }
        consumerProperties.put("group.id", UUID.randomUUID().toString());
        consumerProperties.putAll(properties);
        consumer = new KafkaConsumer<>(consumerProperties);
    }

    public static Predicate<ConsumerRecord<Object, Object>> buildFilterBasedOnMap(Map<String, Object> data) {
        return (record) -> {
            String json = record.value().toString();
            final List<String> flag = new ArrayList<>();
            data.forEach((k, v) -> {
                try {
                    JsonNode value = JsonUtils.getJsonValue(json, k);
                    try {
                        if ((value == null && v == null) || value.asText().equals(v.toString())) {
                            flag.add(k);
                        }
                    } catch (NullPointerException ignored) {
                    }
                } catch (PathNotFoundException e) {
                    LOGGER.warn("Path not found: " + k);
                }
            });
            return flag.size() == data.keySet().size();
        };
    }

    public static Predicate<ConsumerRecord<Object, Object>> buildFilterBasedOnDataTableAndEvent(DataTable data, Event event) {
        HashMap<String, Object> map = new HashMap<>();
        List<List<String>> lists = data.asLists(String.class);
        for (int i = 0; i < lists.get(0).size(); i++) {
            map.put(lists.get(0).get(i), lists.get(1).get(i));
        }
        return ConsumerService.buildFilterBasedOnMap(map);
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Subscribe on specified topics
     *
     * @param topics collection of topic names
     */
    public void subscribeOnTopics(Collection<String> topics) {
        consumer.subscribe(topics);
        LOGGER.info("Subscribe on topic: " + topics.toString());
    }

    /**
     * Retrieve list of all kafka topics
     *
     * @return Set of topic names
     */
    public Set<String> getListOfTopics() {
        return consumer.listTopics().keySet();
    }

    /**
     * Check if topic exists in Kafka
     *
     * @param topicName topic name
     * @return true if topic exists
     */
    public boolean isTopicExist(String topicName) {
        return getListOfTopics().contains(topicName);
    }

    /**
     * Get the current subscription.
     *
     * @return set of topic names
     */
    public Set<String> getSubscription() {
        return consumer.subscription();
    }

    /**
     * Poll one record by specified filter
     *
     * @param consumingFilter Predicate of ConsumerRecord<Object, Object>/
     * @return List of Event objects
     */
    public List<Event> pollRecordsAsEvents(Predicate<ConsumerRecord<Object, Object>> consumingFilter) {
        return pollRecordsAsEvents(consumingFilter, DEFAULT_EXPECTED_RECORDS_AMOUNT);
    }

    /**
     * Poll all records by specified filter
     *
     * @param consumingFilter       Predicate of ConsumerRecord
     * @param expectedRecordsAmount amount of records
     * @return List of Event objects
     */
    public List<Event> pollRecordsAsEvents(Predicate<ConsumerRecord<Object, Object>> consumingFilter, int expectedRecordsAmount) {
        List<ConsumerRecord<Object, Object>> consumerRecords = pollRecords(consumingFilter, expectedRecordsAmount);
        return ConsumerRecordsToEventsMapper.map(consumerRecords);
    }

    public List<ConsumerRecord<Object, Object>> pollRecords(Predicate<ConsumerRecord<Object, Object>> predicate, int expectedRecordsAmount) {
        if (getSubscription().isEmpty()) {
            throw new IllegalStateException("Consumer is not subscribed to any topics");
        }

        List<ConsumerRecord<Object, Object>> result = new ArrayList<>();
        int consumeIterations = getConsumeIterations();
        for (int i = 0; i < consumeIterations; i++) {
            ConsumerRecords<Object, Object> records = consumer.poll(CONSUMER_POLL_TIMEOUT);
            LOGGER.debug("Attempt #{}: found {} record(s)", i, records.count());
            for (ConsumerRecord<Object, Object> record : records) {
                if (predicate.test(record)) {
                    result.add(record);
                }
            }
            if (result.size() >= expectedRecordsAmount) {
                break;
            }
        }
        LOGGER.info("Polled {} records: {}", result.size(), ConsumerRecordsToEventsMapper.map(result));
        return result;
    }

    private int getConsumeIterations() {
        return Optional.ofNullable(timeoutSeconds).orElse(DEFAULT_CONSUMING_TIMEOUT_SECOND) * 1000 / CONSUMER_POLL_TIMEOUT;
    }

    /**
     * Close KafkaConsumer
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (consumer != null) {
            consumer.close();
        }
    }
}
