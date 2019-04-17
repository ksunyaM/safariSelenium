/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.wba.test.utils.kafka.Event;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class ApiTest extends BaseStep {

    //    @Test
    public void apiGetTest() throws Throwable {
        String resource = "testGET";
        Map<String, String> dataTable = new HashMap<>();
        dataTable.put("~paraM2", "2");
        dataTable.put("param3", "error");
        dataTable.put("~Param1", "posts");

        new ApiUtils().sendApiCall(resource, null, dataTable);
        Assert.assertEquals(
                eventStorage.getLastConsumed().getBodyAttribute("$.id", Integer.class),
                Integer.valueOf(2));
    }

    //    @Test
    public void apiPostTest() throws Throwable {
        String resource = "testPost";
        Map<String, String> dataTable = new HashMap<>();
        dataTable.put("title", "foo");
        dataTable.put("body", "bar");
        dataTable.put("~Param1", "posts");

        String newApiCall = "newApiCall";
        Event event = new ApiUtils().prepareApiCall(resource, newApiCall, dataTable);
        new ApiUtils().sendPreparedApiCall(event, newApiCall);
        Assert.assertEquals(eventStorage.findEvent("P").getName(), newApiCall + "-request");
        Assert.assertEquals(
                eventStorage.findEvent(newApiCall).getBodyAttribute("$.id", Integer.class),
                Integer.valueOf(101));
    }
}
