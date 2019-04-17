/*
 * Copyright 2018 Walgreen Co.
 */

package com.wba.test.utils.commonsteps;

import com.wba.test.utils.*;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventStorage;
import com.wba.test.utils.kafka.EventUtils;
import com.wba.test.utils.kafka.consumer.ConsumerRecordsToEventsMapper;
import com.wba.test.utils.kafka.consumer.ConsumerService;
import com.wba.test.utils.kafka.producer.ProducerService;
import com.wba.test.utils.mocks.MockUtils;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;

import java.util.*;
import java.util.function.Predicate;

public class CommonStep extends BaseStep {

    public CommonStep setResourceFolderPath(String resourceFolderPath) {
        this.resourcesFolderPath = resourceFolderPath;
        return this;
    }

    public void createDefaultData(Scenario scenario) {
        LOGGER.info("ENV: " + (Optional.ofNullable(System.getProperty("confPath")).orElse("NOT SET")));
        dataStorage.map().put("~SCENARIO", scenario);
        dataStorage.map().put("~SCENARIO_TAGS", String.join(" ", scenario.getSourceTagNames()));
        dataStorage.add("uniqueID", UUID.randomUUID().toString());
        dataStorage.add("~~startTime", asMillSec("timestamp::"));
    }

    public void eventForTopic(String eventName, String topicName) {
        Event event = new EventBuilder()
                .applyDefaultsForTopic(topicName)
                .name(eventName)
                .build();
        eventStorage.addProduced(event);

        event = eventStorage.getProduced(event.getName());
        if (JsonUtils.isJsonPathExists(event.getBody(), "correlationId"))
            event.replaceBodyAttribute("correlationId", asStr("uniqueID"));
        if (JsonUtils.isJsonPathExists(event.getBody(), "eventProducedTime"))
            event.replaceBodyAttribute("eventProducedTime", "timestamp::");
    }

    public void schemaNameIs(String eventName, String schemaName) {
        verifySchemaExists(schemaName);
        eventStorage.getProduced(eventName).setSchemaName(schemaName);
    }

    public void verifySchemaExists(String schemaName) {
        Assert.assertTrue(new SchemaRegistryUtils().isSchemaExist(schemaName));
    }

    public void eventHasDataAs(String eventName, DataTable data) {
        updateJson(eventStorage.getProduced(eventName), data);
    }

    public void eventHasJson(String eventName, String jsonFileName) {
        eventStorage.getProduced(eventName).setBody(
                ResourceUtils.getResourceAsString(resourcesFolderPath + jsonFileName));
    }

    public void eventHasKeyAs(String eventName, DataTable data) {
        updateEventKey(eventStorage.getProduced(eventName), data.asMap(String.class, String.class));
    }

    public void eventHasHeadersAs(String eventName, DataTable headers) {
        headers.asMap(String.class, String.class)
                .forEach((k, v) -> eventStorage.findEvent(eventName).setHeader(k, v));
    }

    public void eventIsProducedByTest(String eventName) {
        final Event event = eventStorage.getProduced(eventName);
        EventUtils.doEventAction(event, Event.Action.REPLACE_VARIABLES);
        EventUtils.doEventAction(event, Event.Action.ENCRYPT_FIELDS);
        new ProducerService().produce(event);
    }

    public void newEventIsProducedToTopic(String eventName, String topicName) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("correlationId", eventStorage.getLastProduced().getBodyAttribute("$.correlationId", String.class));

        consumeEvent(eventName, topicName, filter, 1);
    }

    public void newEventIsProducedToTopicWithData(String eventName, String topicName, DataTable data) {
        Map<String, Object> filter = new HashMap<>();
        data.asMap(String.class, String.class).forEach((k, v) -> filter.put(defineValue(k).toString(), defineValue(v)));

        consumeEvent(eventName, topicName, filter, 1);
    }

    public void newEventIsNotProducedToTopic(String topicName) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("correlationId", eventStorage.getLastProduced().getBodyAttribute("$.correlationId", String.class));

        consumeEvent(null, topicName, filter, 0);
    }

    public void newEventIsNotProducedToTopicWithData(String topicName, DataTable data) {
        Map<String, Object> filter = dataStorage.unmaskAsMap(data);

        consumeEvent(null, topicName, filter, 0);
    }

    public void newEventIsProducedToTopicWithDataFromEvent(String newEventName, String topicName, String eventName, DataTable data) {

        Predicate<ConsumerRecord<Object, Object>> predicate =
                ConsumerService.buildFilterBasedOnDataTableAndEvent(data, eventStorage.findEvent(eventName));

        List<ConsumerRecord<Object, Object>> records = EventUtils.consumeRecordsAll(topicName, predicate);
        Assert.assertEquals(1, records.size());

        Event event = ConsumerRecordsToEventsMapper.map(records).get(0);
        event.setName(newEventName);
        eventStorage.addConsumed(event);
    }

    public void waitSec(Integer sec) {
        LOGGER.debug("Going to wait " + sec + " second(s)\n");
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        new ZephyrUtils().addScenarioResultToCycle(((Scenario) dataStorage.unmask("~SCENARIO")).getSourceTagNames(),
                ((Scenario) dataStorage.unmask("~SCENARIO")).getStatus());

        new MockUtils().deleteMocks();
        EventStorage.reset();
        dataStorage.reset();
    }

    public void eventHasEncryptedFields(String eventName) {
        new EncryptionUtils().encryptEvent(eventStorage.findEvent(eventName));
    }

    public void eventHasDecryptedFields(String eventName) {
        new EncryptionUtils().decryptEvent(eventStorage.findEvent(eventName));
    }
}
