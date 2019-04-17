package com.wba.dataanalytics.api.test.common.steps;

import com.wba.test.utils.ResourceUtils;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Implementation of test steps w/o mention event name. Last produced or consumed event name is used instead.
 * It allows to provide more clear and simple wording in feature files.
 */
public class _CommonStepSimple extends _BaseStep {

    @Given("^an event for topic \"([^\"]+)\"$")
    public void a_event_for_topic(String topicName) throws Throwable {
        new _CommonStep().a_event_for_topic(null, topicName);
    }

    @Given("^the schema name is \"([^\"]*)\"$")
    public void the_schema_name_is(String schemaName) {
        new _CommonStep().verify_that_schema_exists(schemaName);
        eventStorage.getLastProduced().setSchemaName(schemaName);
    }

    @Given("^the event has data as$")
    public void update_json(DataTable data) {
        updateJson(eventStorage.getLastProduced(), data);
    }

    @Given("^the event has json \"([^\"]*)\"$")
    public void the_event_has_json(String jsonFileName) throws Throwable {
        eventStorage.getLastProduced().setBody(
                ResourceUtils.getResourceAsString(RESOURCES_FOLDER_PATH + jsonFileName));
    }

    @When("^the user produces the event$")
    public void the_event_is_produced_by_test() throws Throwable {
        new _CommonStep().the_event_is_produced_by_test(eventStorage.getLastEventNameProduced());
    }

    @Then("^the system produces a new event to topic \"([^\"]*)\"$")
    public void a_new_event_is_produced_to_topic(String topicName) throws Throwable {
        new _CommonStep().a_new_event_is_produced_to_topic(null, topicName);
    }

    @Then("^the system produces a new event$")
    public void a_new_event_is_produced() throws Throwable {
        new _CommonStep().a_new_event_is_produced_to_topic(null, eventStorage.getLastTopicName());
    }

    @Then("^the system produces a new event to topic \"([^\"]*)\" with data$")
    public void a_new_event_is_produced_to_topic_with_data(String topicName, DataTable data) throws Throwable {
        new _CommonStep().a_new_event_is_produced_to_topic_with_data(null, topicName, data);
    }

    @Then("^the system produces a new event to topic \"([^\"]*)\" with data from event$")
    public void a_new_event_is_produced_to_topic_with_data_from_event(String topicName, DataTable data) throws Throwable {
        new _CommonStep().a_new_event_is_produced_to_topic_with_data_from_event(null, topicName, eventStorage.getLastEventNameProduced(), data);
    }
}
