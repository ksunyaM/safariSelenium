/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.commonsteps;

import com.wba.test.utils.commonsteps.ApiStep;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class _ApiStep extends _BaseStep {

    private ApiStep apiStep = new ApiStep();

    /**
     * For sending API call prepare<br>
     * - YAML file:<br>
     * =================<br>
     * headers:<br>
     * contentType: application/json<br>
     * accept: application/json<br>
     * requestType: GET<br>
     * <p>
     * serviceName: /rxdata/api/v2/dispenses/%s<br>
     * defaultBodyPath: com/wba/rxdata/test/data/TestGet.json<br>
     * ==================<br>
     * <p></p>
     * - JSON file.<br>
     * If request type is GET json should be a map and will be used as url parameters.<br>
     * For POST and others it will be a body.<br>
     * - Service name may contains a replacement parts. To replace with real value update a DataTable with keys like ~param1, ~param2<br>
     * The request will be added to EventStorage as produced event and response as consumed event.
     * Response status and status line can be found as headers of consumed event.
     *
     * @param resourceName - name of .yaml file
     * @param data         - DataTable
     * @throws Throwable -
     */

    @When("^user sends API call to \"([^\"]*)\" with data$")
    public void prepareAndSendAPICallToWithData(String resourceName, DataTable data) throws Throwable {
        apiStep.prepareAndSendAPICallToWithData(resourceName, data);
    }

    @When("^user sends API call to \"([^\"]*)\"$")
    public void prepareAndSendAPICallTo(String resourceName) throws Throwable {
        apiStep.prepareAndSendAPICallTo(resourceName);
    }

    @When("^user sends API call \"([^\"]*)\" to \"([^\"]*)\" with data$")
    public void prepareAndSendAPICallToWithData(String eventName, String resourceName, DataTable data) throws Throwable {
        apiStep.prepareAndSendAPICallToWithData(eventName, resourceName, data);
    }

    @Given("^an API call \"([^\"]*)\" for \"([^\"]*)\" with data$")
    public void prepareApiCallWithData(String eventName, String resourceName, DataTable data) {
        apiStep.prepareApiCallWithData(eventName, resourceName, data);
    }

    @Given("^an API call \"([^\"]*)\" for \"([^\"]*)\"$")
    public void prepareApiCall(String eventName, String resourceName) {
        apiStep.prepareApiCall(eventName, resourceName);
    }

    @Given("^an API call for \"([^\"]*)\" with data$")
    public void prepareApiCallWithData(String resourceName, DataTable data) {
        apiStep.prepareApiCallWithData(resourceName, data);
    }

    @Given("^an API call for \"([^\"]*)\"$")
    public void prepareApiCall(String resourceName) {
        apiStep.prepareApiCall(resourceName);
    }

    @Given("^the API call \"([^\"]*)\" has data as$")
    public void theApiCallHasDataAs(String apiCallName, DataTable data) {
        apiStep.apiCallHasDataAs(apiCallName, data);
    }

    @Given("^the API call \"([^\"]*)\" has headers as$")
    public void theApiCallHasHeadersAs(String apiCallName, DataTable headers) {
        apiStep.apiCallHasHeadersAs(apiCallName, headers);
    }

    @When("^user sends the API call \"([^\"]*)\"$")
    public void sendApiCall(String requestEventName) throws Throwable {
        apiStep.sendApiCall(requestEventName);
    }

    @When("^user sends the API call$")
    public void sendApiCall() throws Throwable {
        apiStep.sendApiCall();
    }

    @Then("^the response status code is \"([^\"]*)\"$")
    public void theResponseStatusCodeIs(int statusCode) {
        apiStep.responseStatusCodeIs(statusCode);
    }

    @Then("the response message is \"([^\"]*)\"$")
    public void theResponseMessageIs(String messageExpected) {
        apiStep.responseMessageIs(messageExpected);
    }

    @Then("the response message is \"([^\"]*)\" with data$")
    public void theResponseMessageIs(String messageExpected, DataTable dataTable) {
        apiStep.responseMessageIs(messageExpected, dataTable);
    }
}