/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils.commonsteps;

import com.wba.test.utils.ApiUtils;
import com.wba.test.utils.kafka.Event;
import cucumber.api.DataTable;
import org.junit.Assert;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ApiStep extends BaseStep {

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

    public void prepareAndSendAPICallToWithData(String resourceName, DataTable data) throws Throwable {
        new ApiUtils().sendApiCall(resourceName, null, data.asMap(String.class, String.class));
    }

    public void prepareAndSendAPICallTo(String resourceName) throws Throwable {
        new ApiUtils().sendApiCall(resourceName, null, new HashMap<>());
    }

    public void prepareAndSendAPICallToWithData(String eventName, String resourceName, DataTable data) throws Throwable {
        new ApiUtils().sendApiCall(resourceName, eventName, data.asMap(String.class, String.class));
    }

    public void prepareApiCallWithData(String eventName, String resourceName, DataTable data) {
        new ApiUtils().prepareApiCall(resourceName, eventName, data.asMap(String.class, String.class));
    }

    public void prepareApiCall(String eventName, String resourceName) {
        new ApiUtils().prepareApiCall(resourceName, eventName, new HashMap<>());
    }

    public void prepareApiCallWithData(String resourceName, DataTable data) {
        new ApiUtils().prepareApiCall(resourceName, null, data.asMap(String.class, String.class));
    }

    public void prepareApiCall(String resourceName) {
        new ApiUtils().prepareApiCall(resourceName, null, new HashMap<>());
    }

    public void apiCallHasDataAs(String apiCallName, DataTable data) {
        new CommonStep().eventHasDataAs(apiCallName, data);
    }

    public void apiCallHasHeadersAs(String apiCallName, DataTable headers) {
        new CommonStep().eventHasHeadersAs(apiCallName, headers);
    }

    public void sendApiCall(String requestEventName) throws Throwable {
        Event request = eventStorage.getProduced(requestEventName);
        new ApiUtils().sendPreparedApiCall(request, requestEventName);
    }

    public void sendApiCall() throws Throwable {
        sendApiCall(eventStorage.getLastProduced().getName());
    }

    public void responseStatusCodeIs(int statusCode) {
        assertEquals("Status code is wrong",
                "" + statusCode,
                eventStorage.getLastConsumed().getHeaders().get("StatusCode"));
    }

    public void responseMessageIs(String messageExpected) {
        Assert.assertEquals(messageExpected,
                eventStorage.getLastConsumed().getHeaders().get("StatusLine"));
    }

    public void responseMessageIs(String messageExpected, DataTable dataTable) {
        Assert.assertEquals("Verify API response message",
                prepareText(messageExpected, dataTable),
                eventStorage.getLastConsumed().getHeaders().get("StatusLine"));
    }
}
