package com.wba.dataanalytics.api.test.common.steps;

import com.wba.test.utils.*;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventStorage;
import com.wba.test.utils.kafka.EventUtils;
import com.wba.test.utils.kafka.consumer.ConsumerRecordsToEventsMapper;
import com.wba.test.utils.kafka.consumer.ConsumerService;
import com.wba.test.utils.kafka.producer.ProducerService;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import java.util.UUID;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class _CommonStep extends _BaseStep {

    public List<String> correlationids = new ArrayList<String>();
    private static final String SELECT = "SELECT * FROM ";

    @Before
    public void create_default_data(Scenario scenario) {
        dataStorage.map().put("~SCENARIO", scenario);
        dataStorage.map().put("~SCENARIO_TAGS",
                scenario.getSourceTagNames().stream()
                        .collect(Collectors.joining(" ")));
        dataStorage.add("uniqueID", UUID.randomUUID().toString());
    }

    @Given("^an event \"([^\"]*)\" for topic \"([^\"]*)\"$")
    public void a_event_for_topic(String eventName, String topicName) throws Throwable {
        Event event = new EventBuilder()
                .applyDefaultsForTopic(topicName)
                .name(eventName)
                .build();
        eventStorage.addProduced(event);

        event = eventStorage.getProduced(event.getName());
        if (JsonUtils.isJsonPathExists(event.getBody(), "correlationId"))
            event.replaceBodyAttribute("correlationId", dataStorage.unmask("uniqueID"));
        if (JsonUtils.isJsonPathExists(event.getBody(), "eventProducedTime"))
            event.replaceBodyAttribute("eventProducedTime", "timestamp::");
    }

    @And("^publish \"([^\"]*)\" events for the topic \"([^\"]*)\"$")
    public void updateInputJson(Integer noOfEvents, String eventName) {
        dataStorage.add("numberofevents", noOfEvents);
        for (int i = 0; i < noOfEvents; i++) {
            String corrid = generateRandomUUID();
            correlationids.add(corrid);
            Map<String, String> map = new HashMap<>();
            map.put("correlationId", corrid);
            map.put("location.locationNumber", generateRandomString(5));
            map.put("location.locationType", "Store");
            map.put("favouriteLocationAdded.locationNumber", generateRandomString(5));
            map.put("favouriteLocationAdded.locationType", "Store");
            map.put("teamMember.employeeNumber", generateRandomString(5));
            map.put("teamMember.employeeType", "EMPLOYEE");
            String jsonBody = updateJson(eventStorage.getProduced(eventName+"_"+i).getBody(), map);
            EventStorage.getInstance().getProduced(eventName+"_"+i).setBody(jsonBody);
            new ProducerService().produce(eventStorage.getProduced(eventName+"_"+i));
            LOGGER.info("Business rules have been applied on input json");
        }
        dataStorage.add("correlationIdsForHive", correlationids); }

    @Given("^the event \"([^\"]*)\" has schema name \"([^\"]*)\"$")
    public void the_schema_name_is(String eventName, String schemaName) throws Throwable {
        verify_that_schema_exists(schemaName);
        eventStorage.getProduced(eventName).setSchemaName(schemaName);
    }

    @Given("^an existing schema \"([^\"]*)\"$")
    public void verify_that_schema_exists(String schemaName) {

        Assert.assertTrue(new SchemaRegistryUtils().isSchemaExist(schemaName));

    }

    @Given("^the event \"([^\"]*)\" has data as$")
    public void the_event_has_data_as(String eventName, DataTable data) throws Throwable {
        updateJson(eventStorage.getProduced(eventName), data);
    }

    @Given("^the event \"([^\"]*)\" has json \"([^\"]*)\"$")
    public void the_event_has_json(String eventName, String jsonFileName) throws Throwable {
        eventStorage.getProduced(eventName).setBody(
                ResourceUtils.getResourceAsString(RESOURCES_FOLDER_PATH + jsonFileName));
    }

    @Given("^the event \"([^\"]*)\" has headers as$")
    public void the_event_has_headers_as(String eventName, DataTable headers) throws Throwable {
        headers.asMap(String.class, String.class)
                .forEach((k, v) -> eventStorage.findEvent(eventName).setHeader(k, v));
    }

    @When("^the user produces the event \"([^\"]*)\"$")
    public void the_event_is_produced_by_test(String eventName) throws Throwable {
        new ProducerService().produce(eventStorage.getProduced(eventName));
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic(String eventName, String topicName) throws Throwable {
        Map<String, Object> filter = new HashMap<>();
        filter.put("correlationId", eventStorage.getLastProduced().getBodyAttribute("$.correlationId", String.class));

        consumeEvent(eventName, topicName, filter, 1);
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_produced_to_topic_with_data(String eventName, String topicName, DataTable data) throws Throwable {
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
    public void a_new_event_is_produced_to_topic_with_data_from_event(String newEventName, String topicName, String eventName, DataTable data) throws Throwable {

        Predicate<ConsumerRecord<Object, Object>> predicate =
                ConsumerService.buildFilterBasedOnDataTableAndEvent(data, eventStorage.findEvent(eventName));

        List<ConsumerRecord<Object, Object>> records = EventUtils.consumeRecordsAll(topicName, predicate);
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

    private String generateRandom(int len) {
        String stringRandom = StringUtils.EMPTY;
        Random r = new Random();
        int Low = 1;
        int High = 0;
        for (int i = len; i > -1; i--) {
            High = (int) (High + (9 * (Math.pow(10, (i - 1)))));
        }
        stringRandom = Integer.toString(r.nextInt(High - Low) + Low);
        return stringRandom;
    }

    public String generateRandomString(int length) {
        String valueRandom = StringUtils.EMPTY;
        Random r = new Random();
        int Low = 1;
        int High = 0;
        for (int i = length; i > -1; i--) {
            High = (int) (High + (9 * (Math.pow(10, (i - 1)))));
        }
        valueRandom = Integer.toString(r.nextInt(High - Low) + Low);
        return valueRandom;
    }

    public String generateRandomUUID() {
        UUID id = UUID.randomUUID();
        String uuid = String.valueOf(id);
        return uuid;
    }

    @After
    public void clear() {
        new ZephyrUtils().addScenarioResultToCycle(((Scenario) dataStorage.unmask("~SCENARIO")).getSourceTagNames(),
                ((Scenario) dataStorage.unmask("~SCENARIO")).getStatus());

        EventStorage.reset();
        dataStorage.reset();
    }
}

