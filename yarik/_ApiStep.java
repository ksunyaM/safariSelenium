package com.wba.rxdata.test;

import com.wba.test.utils.ApiUtils;
import com.wba.test.utils.kafka.Event;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class _ApiStep extends _BaseStep {

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
        new ApiUtils().sendApiCall(resourceName, null, data.asMap(String.class, String.class));
    }

    @When("^user sends API call to \"([^\"]*)\"$")
    public void prepareAndSendAPICallTo(String resourceName) throws Throwable {
        new ApiUtils().sendApiCall(resourceName, null, new HashMap<>());
    }

    @When("^user sends API call \"([^\"]*)\" to \"([^\"]*)\" with data$")
    public void prepareAndSendAPICallToWithData(String eventName, String resourceName, DataTable data) throws Throwable {
        new ApiUtils().sendApiCall(resourceName, eventName, data.asMap(String.class, String.class));
    }

    @Given("^an API call \"([^\"]*)\" for \"([^\"]*)\" with data$")
    public void prepareApiCallWithData(String eventName, String resourceName, DataTable data) {
        new ApiUtils().prepareApiCall(resourceName, eventName, data.asMap(String.class, String.class));
    }

    @Given("^an API call \"([^\"]*)\" for \"([^\"]*)\"$")
    public void prepareApiCall(String eventName, String resourceName) {
        new ApiUtils().prepareApiCall(resourceName, eventName, new HashMap<>());
    }

    @Given("^an API call for \"([^\"]*)\" with data$")
    public void prepareApiCallWithData(String resourceName, DataTable data) {
        new ApiUtils().prepareApiCall(resourceName, null, data.asMap(String.class, String.class));
    }

    @Given("^an API call for \"([^\"]*)\"$")
    public void prepareApiCall(String resourceName) {
        new ApiUtils().prepareApiCall(resourceName, null, new HashMap<>());
    }

    @Given("^the API call \"([^\"]*)\" has data as$")
    public void theApiCallHasDataAs(String apiCallName, DataTable data) throws Throwable {
        new _CommonStep().the_event_has_data_as(apiCallName, data);
    }

    @Given("^the API call \"([^\"]*)\" has headers as$")
    public void theApiCallHasHeadersAs(String apiCallName, DataTable headers) throws Throwable {
        new _CommonStep().the_event_has_headers_as(apiCallName, headers);
    }

    @When("^user sends the API call \"([^\"]*)\"$")
    public void sendApiCall(String requestEventName) throws Throwable {
        Event request = eventStorage.getProduced(requestEventName);
        new ApiUtils().sendPreparedApiCall(request, requestEventName);
    }

    @When("^user sends the API call$")
    public void sendApiCall() throws Throwable {
        sendApiCall(eventStorage.getLastProduced().getName());
    }

    @Then("^the response status code is \"([^\"]*)\"$")
    public void theResponseStatusCodeIs(int statusCode) throws Throwable {
        assertEquals("Status code is wrong",
                "" + statusCode,
                eventStorage.getLastConsumed().getHeaders().get("StatusCode"));
    }

    @Then("the response message is \"([^\"]*)\"$")
    public void theResponseMessageIs(String messageExpected) {
        Assert.assertEquals(messageExpected,
                eventStorage.getLastConsumed().getHeaders().get("StatusLine"));
    }

    @Then("the response message is \"([^\"]*)\" with data$")
    public void theResponseMessageIs(String messageExpected, DataTable dataTable) {
        Assert.assertEquals("Verify API response message",
                prepareText(messageExpected, dataTable),
                eventStorage.getLastConsumed().getHeaders().get("StatusLine"));
    }

}
