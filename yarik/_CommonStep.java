package com.wba.rxdata.test;

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
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class _CommonStep extends _BaseStep {



    @Before
    public void create_default_data(Scenario scenario) throws Exception{

        dataStorage.map().put("~SCENARIO", scenario);
        dataStorage.map().put("~SCENARIO_TAGS", String.join(" ", scenario.getSourceTagNames()));
        dataStorage.add("uniqueID", UUID.randomUUID().toString());
        dataStorage.add("~~start_time", asStr("timestamp::"));
        dataStorage.add("PRESCRIBER_CODE",ValueTemplateUtils.createValue("randomstring::7"));
        dataStorage.add("PRESCRIBER_LOCATION_CODE",ValueTemplateUtils.createValue("randomstring::7"));

       }


    @Given("^an event \"([^\"]*)\" for topic \"([^\"]*)\"$")
    public void a_event_for_topic(String eventName, String topicName) {
        Event event = new EventBuilder()
                .applyDefaultsForTopic(topicName)
                .name(eventName)
                .build();
        eventStorage.addProduced(event);

        event.setHeader("spanId", asStr("uniqueID"));
        event.setHeader("spanTraceId", asStr("uniqueID"));
        if (JsonUtils.isJsonPathExists(event.getBody(), "correlationId"))
            event.replaceBodyAttribute("correlationId", asStr("uniqueID"));
        if (JsonUtils.isJsonPathExists(event.getBody(), "eventProducedTime"))
            event.replaceBodyAttribute("eventProducedTime", "timestamp::");
    }

    @Given("^the event \"([^\"]*)\" has schema name \"([^\"]*)\"$")
    public void the_schema_name_is(String eventName, String schemaName) {
        verify_that_schema_exists(schemaName);
        eventStorage.getProduced(eventName).setSchemaName(schemaName);
    }

    @Given("^an existing schema \"([^\"]*)\"$")
    public void verify_that_schema_exists(String schemaName) {
        Assert.assertTrue(new SchemaRegistryUtils().isSchemaExist(schemaName));
    }

    @Given("^the event \"([^\"]*)\" has data as$")
    public void the_event_has_data_as(String eventName, DataTable data) {
        updateJson(eventStorage.getProduced(eventName), data);
    }

    @Given("^the event \"([^\"]*)\" has json \"([^\"]*)\"$")
    public void the_event_has_json(String eventName, String jsonFileName) {
        eventStorage.getProduced(eventName).setBody(
                ResourceUtils.getResourceAsString(RESOURCES_FOLDER_PATH + jsonFileName));
    }

    @Given("^the event \"([^\"]*)\" has headers as$")
    public void the_event_has_headers_as(String eventName, DataTable headers) {
        headers.asMap(String.class, String.class)
                .forEach((k, v) -> eventStorage.findEvent(eventName).setHeader(k, v));
    }

    @When("^the user produces the event \"([^\"]*)\"$")
    public void the_event_is_produced_by_test(String eventName) {
        new ProducerService().produce(eventStorage.getProduced(eventName));
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic(String eventName, String topicName) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("correlationId", eventStorage.getLastProduced().getBodyAttribute("$.correlationId", String.class));

        consumeEvent(eventName, topicName, filter, 1);
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_produced_to_topic_with_data(String eventName, String topicName, DataTable data) {
        Map<String, Object> filter = new HashMap<>();
        data.asMap(String.class, String.class).forEach((k, v) -> filter.put(defineValue(k).toString(), defineValue(v)));

        consumeEvent(eventName, topicName, filter, 1);
    }

    @Then("^the system does not produce a new event to topic \"([^\"]*)\"$")
    public void a_new_event_is_not_produced_to_topic(String topicName) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("correlationId", eventStorage.getLastProduced().getBodyAttribute("$.correlationId", String.class));

        consumeEvent(null, topicName, filter, 0);
    }

    @Then("^the system does not produce a new event to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_not_produced_to_topic_with_data(String topicName, DataTable data) {
        Map<String, Object> filter = dataStorage.unmaskAsMap(data);

        consumeEvent(null, topicName, filter, 0);
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\" with data from event \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic_with_data_from_event(String newEventName, String topicName, String eventName, DataTable data) {

        Predicate<ConsumerRecord<GenericRecord, GenericRecord>> predicate =
                ConsumerService.buildFilterBasedOnDataTableAndEvent(data, eventStorage.findEvent(eventName));

        List<ConsumerRecord<GenericRecord, GenericRecord>> records = EventUtils.consumeRecordsAll(topicName, predicate);
        Assert.assertEquals(1, records.size());

        Event event = ConsumerRecordsToEventsMapper.map(records).get(0);
        event.setName(newEventName);
        eventStorage.addConsumed(event);
    }

    @Then("^wait for \"([^\"]*)\" seconds$")
    public void wait_sec(Integer sec) {
        LOGGER.debug("Going to wait " + sec + " second(s)");
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void clear() {
        new ZephyrUtils().addScenarioResultToCycle(((Scenario) dataStorage.unmask("~SCENARIO")).getSourceTagNames(),
                ((Scenario) dataStorage.unmask("~SCENARIO")).getStatus());
        try {
            new MockUtils().deleteMocks();
        }catch (Throwable t){
         LOGGER.info("Log not Deleted!!!!!"+t.getMessage());
        }
        EventStorage.reset();
        dataStorage.reset();
    }
}

