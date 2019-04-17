/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.commonsteps;

import com.wba.test.utils.commonsteps.CommonStep;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class _CommonStep extends _BaseStep {

    private CommonStep commonStep = new CommonStep();

    @Before(order = 100)
    public void create_default_data(Scenario scenario) {
        commonStep.createDefaultData(scenario);
    }

    @Given("^an event \"([^\"]*)\" for topic \"([^\"]*)\"$")
    public void a_event_for_topic(String eventName, String topicName) {
        commonStep.eventForTopic(eventName, topicName);
    }

    @Given("^the event \"([^\"]*)\" has schema name \"([^\"]*)\"$")
    public void the_schema_name_is(String eventName, String schemaName) {
        commonStep.schemaNameIs(eventName, schemaName);
    }

    @Given("^an existing schema \"([^\"]*)\"$")
    public void verify_that_schema_exists(String schemaName) {
        commonStep.verifySchemaExists(schemaName);
    }

    @Given("^the event \"([^\"]*)\" has data as$")
    public void the_event_has_data_as(String eventName, DataTable data) {
        commonStep.eventHasDataAs(eventName, data);
    }

    @Given("^the event \"([^\"]*)\" has json \"([^\"]*)\"$")
    public void the_event_has_json(String eventName, String jsonFileName) {
        commonStep.setResourceFolderPath(RESOURCES_FOLDER_PATH).eventHasJson(eventName, jsonFileName);
    }

    @Given("^the event \"([^\"]*)\" has key as$")
    public void the_event_has_key_as(String eventName, DataTable data) {
        commonStep.eventHasKeyAs(eventName, data);
    }

    @Given("^the event \"([^\"]*)\" has headers as$")
    public void the_event_has_headers_as(String eventName, DataTable headers) {
        commonStep.eventHasHeadersAs(eventName, headers);
    }

    @And("^the event \"([^\"]*)\" has encrypted fields$")
    public void eventHasEncryptedFields(String eventName) {
        commonStep.eventHasEncryptedFields(eventName);
    }

    @And("^the event \"([^\"]*)\" has decrypted fields$")
    public void eventHasDecryptedFields(String eventName) {
        commonStep.eventHasDecryptedFields(eventName);
    }

    @When("^the user produces the event \"([^\"]*)\"$")
    public void the_event_is_produced_by_test(String eventName) {
        commonStep.eventIsProducedByTest(eventName);
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic(String eventName, String topicName) {
        commonStep.newEventIsProducedToTopic(eventName, topicName);
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_produced_to_topic_with_data(String eventName, String topicName, DataTable data) {
        commonStep.newEventIsProducedToTopicWithData(eventName, topicName, data);
    }

    @Then("^the system does not produce a new event to topic \"([^\"]*)\"$")
    public void a_new_event_is_not_produced_to_topic(String topicName) {
        commonStep.newEventIsNotProducedToTopic(topicName);
    }

    @Then("^the system does not produce a new event to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_not_produced_to_topic_with_data(String topicName, DataTable data) {
        commonStep.newEventIsNotProducedToTopicWithData(topicName, data);
    }

    @Then("^the system produces a new event \"([^\"]*)\" to topic \"([^\"]*)\" with data from event \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic_with_data_from_event(String newEventName, String topicName, String eventName, DataTable data) {
        commonStep.newEventIsProducedToTopicWithDataFromEvent(newEventName, topicName, eventName, data);
    }

    @Then("^wait for \"([^\"]*)\" seconds$")
    public void wait_sec(Integer sec) {
        commonStep.waitSec(sec);
    }

    @After(order = 100)
    public void clear() {
        commonStep.clear();
    }
}
