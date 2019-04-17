/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventUtils;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.oneleo.test.automation.core.ApiUtils.api;

public class ApiUtils extends BaseStep {

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
    public void sendApiCall(String resourceName, String eventName, Map<String, String> data) throws Throwable {
        Event event = prepareApiCall(resourceName, eventName, data);
        sendPreparedApiCall(event, eventName);
    }

    @Deprecated
    public void sendApiCall(String resourceName, Map<String, String> data) throws Throwable {
        sendApiCall(resourceName, null, data);
    }

    public Event prepareApiCall(String resourceName, String eventName, Map<String, String> data) {
        // DataTable keys started from '~' are used as replacer for service url
        // and will not be a part of body or url parameters
        eventName = StringUtils.defaultIfEmpty(eventName, UUID.randomUUID().toString());
        Map<String, Object> paramSystem = new HashMap<>();
        Map<String, String> paramData = new HashMap<>();
        data.forEach((k, v) -> {
            if (k.toLowerCase().startsWith("~")) {
                paramSystem.put(k, defineValue(v));
            } else {
                paramData.put(k, v);
            }
        });

        // Prepare replacement data for service name
        Object[] serviceNameReplace = paramSystem.keySet().stream().sorted()
                .filter(s -> s.toLowerCase().startsWith("~param"))
                .map(paramSystem::get)
                .toArray();

        // Add request as a new event in EventStorage (produced)
        Event event = new EventBuilder()
                .applyDefaultsForTopic(resourceName)
                .buildServiceName(serviceNameReplace)
                .name(eventName)
                .build();
        eventStorage.addProduced(event);

        // Update json base on DataTable data
        event.setBody(updateJson(event.getBody(), paramData));
        return event;
    }

    public void sendPreparedApiCall(Event requestEvent, String responseEventName) throws Throwable {
        EventUtils.doEventAction(requestEvent, Event.Action.REPLACE_VARIABLES);

        Map<String, String> metaData = requestEvent.getHeaders();

        responseEventName = StringUtils.defaultIfEmpty(responseEventName, UUID.randomUUID().toString());
        if (requestEvent.getName().equals(responseEventName)) {
            final String newEventName = responseEventName + "-request";
            requestEvent.setName(newEventName);
            eventStorage.setLastEventNameProduced(newEventName);
        }

        RequestSpecification request = api().template(metaData.getOrDefault("apiServer", "default"));
        request.urlEncodingEnabled(
                Boolean.parseBoolean(metaData.getOrDefault("~urlEncodingEnabled", "true")));
        metaData.keySet().stream()
                .filter(s -> !s.startsWith("~"))
                .filter(s -> !s.equalsIgnoreCase("requestType"))
                .filter(s -> !s.equalsIgnoreCase("apiServer"))
                .forEach(s -> request.header(s, metaData.get(s)));
        request.log().all();

        // Send API call
        Response response;
        String requestType = metaData.get("requestType");
        if (requestType.equalsIgnoreCase("GET")) {
            // Build url params
            Map<String, String> urlParams = new ObjectMapper().readValue(requestEvent.getBody(), new TypeReference<Map<String, String>>() {
            });
            urlParams.forEach(request::param);

            response = request.get(requestEvent.getServiceName());
        } else if (requestType.equalsIgnoreCase("POST")) {
            response = request
                    .body(requestEvent.getBody())
                    .post(requestEvent.getServiceName());
        } else if (requestType.equalsIgnoreCase("PUT")) {
            response = request
                    .body(requestEvent.getBody())
                    .put(requestEvent.getServiceName());
        } else if (requestType.equalsIgnoreCase("PATCH")) {
            response = request
                    .body(requestEvent.getBody())
                    .patch(requestEvent.getServiceName());
        } else if (requestType.equalsIgnoreCase("DELETE")) {
            if(requestEvent.getBody().isEmpty()){
                response = request
                        .delete(requestEvent.getServiceName());
            } else {
                response = request
                        .body(requestEvent.getBody())
                        .delete(requestEvent.getServiceName());
            }
        } else {
            throw new RuntimeException("Not supported type of API call: " + requestType);
        }
        assert response != null;
        response.then().log().all();

        // Prepare map with response statuses
        Map<String, String> responseMetaData = new HashMap<>();
        response.getHeaders().asList().forEach(h -> responseMetaData.put(h.getName(), h.getValue()));

        responseMetaData.put("StatusCode", "" + response.getStatusCode());
        responseMetaData.put("StatusLine", response.getStatusLine());
        responseMetaData.put("requestType", metaData.get("requestType"));

        // Add response as a new requestEvent in EventStorage (consumed)
        Event eventResponse = new EventBuilder()
                .body(response.getBody().print())
                .headers(responseMetaData)
                .serviceName(((RequestSpecificationImpl) request).getURI())
                .name(responseEventName)
                .build();
        eventStorage.addConsumed(eventResponse);
    }
}
