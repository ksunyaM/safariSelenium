/* Copyright 2018 Walgreen Co. */
package com.wba.commonsteps;

import com.wba.test.utils.commonsteps.CommonStep;
import com.wba.test.utils.commonsteps.CommonStepSimple;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Implementation of test steps w/o mention event name. Last produced or consumed event name is used instead.
 * It allows to provide more clear and simple wording in feature files.
 */
public class _CommonStepSimple extends _BaseStep {

    private CommonStepSimple commonStepSimple = new CommonStepSimple();

    @Given("^an event for topic \"([^\"]+)\"$")
    public void a_event_for_topic(String topicName) {
        commonStepSimple.eventForTopic(topicName);
    }

    @Given("^the schema name is \"([^\"]*)\"$")
    public void the_schema_name_is(String schemaName) {
        commonStepSimple.schemaNameIs(schemaName);
    }

    @Given("^the event has data as$")
    public void update_json(DataTable data) {
        commonStepSimple.updateJson(data);
    }

    @Given("^the event has json \"([^\"]*)\"$")
    public void the_event_has_json(String jsonFileName) {
        commonStepSimple.setResourceFolderPath(RESOURCES_FOLDER_PATH).eventHasJson(jsonFileName);
    }

    @Given("the event has encrypted fields")
    public void the_event_has_encrypted_fields() {
        new CommonStep().eventHasEncryptedFields(eventStorage.getLastProduced().getName());
    }

    @Given("the event has body from event \"([^\"]*)\"$")
    public void the_event_has_json_from_event(String eventName) {
        commonStepSimple.eventHasJsonFromEvent(eventName);
    }

    @When("^the user produces the event$")
    public void the_event_is_produced_by_test() {
        commonStepSimple.eventIsProducedByTest();
    }

    @Then("^the system produces a new event to topic \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic(String topicName) {
        commonStepSimple.newEventIsProducedToTopic(topicName);
    }

    @Then("^the system produces a new event$")
    public void a_new_event_is_produced() {
        commonStepSimple.newEventIsProduced();
    }

    @Then("^the system produces a new event to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_produced_to_topic_with_data(String topicName, DataTable data) {
        commonStepSimple.newEventIsProducedToTopicWithData(topicName, data);
    }

    @Then("^the system produces a new event to topic \"([^\"]*)\" with data from event$")
    public void a_new_event_is_produced_to_topic_with_data_from_event(String topicName, DataTable data) {
        commonStepSimple.newEventIsProducedToTopicWithDataFromEvent(topicName, data);
    }

    @Given("the event has decrypted fields")
    public void the_event_has_decrypted_fields() {
        new CommonStep().eventHasDecryptedFields(eventStorage.getLastConsumed().getName());
    }
}