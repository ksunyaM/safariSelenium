/*
 * Copyright 2018 Walgreen Co.
 */

package com.wba.test.utils.mocks;

import com.wba.test.utils.BaseStep;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.PropertiesReader;
import com.wba.test.utils.RegExp;
import com.wba.test.utils.kafka.EventBuilder;
import cucumber.runtime.CucumberException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Properties;

import static com.oneleo.test.automation.core.ApiUtils.api;

public class MockUtils extends BaseStep {

    public static final String dataStorageKeyMock = "~~Mocks"; // used for store created mock IDs to delete them @After step
    private static final String MOCK_SERVER_URI_PROPERTY = "mockserver.uri";
    private static final String DEFAULT_API_PROPERTIES = "default-api.properties";
    private static final String MOCK_SERVER_ERROR = "set mockServer field in MockBuilder or add " + MOCK_SERVER_URI_PROPERTY + " in " + DEFAULT_API_PROPERTIES;

    public static String quote(String resp) {
        if (null != resp) {
            resp = RegExp.replaceAll(resp, "\n", "\\\\n");
            resp = RegExp.replaceAll(resp, "\r", "\\\\r");
            resp = RegExp.replaceAll(resp, "\"", "\\\\\\\"");
        }
        return resp;
    }

    private String getMockServiceUrl(DataMock dataMock) {
        String serverUrl = dataMock.getServerURL();

        if (serverUrl == null) {
            serverUrl = Optional.ofNullable(PropertiesReader.read(DEFAULT_API_PROPERTIES, true).getProperty(MOCK_SERVER_URI_PROPERTY))
                    .orElseThrow(() -> new CucumberException("Mock server is not defined: " + MOCK_SERVER_ERROR));
        } else if (serverUrl.contains("{")) {
            final Properties properties = PropertiesReader.read("default-api.properties", true);
            serverUrl = RegExp.replaceAll(serverUrl, "(\\{.*\\})", s -> Optional.ofNullable(properties.getProperty(s.replaceAll("\\{|\\}", ""))).orElse(s));
        }

        dataMock.setServerURL(serverUrl);
        return serverUrl;
    }

    /**
     * This method created to support tests that used old version of MockServer. Use the {@link MockBuilder} instead.
     *
     * @param mockData     json
     * @param replacements - array of replacements
     */
    public void createMock(String mockData, Object... replacements) {
        createMock(new DataMock("{apiserver.uri}/mockservice/mockservice/", String.format(mockData, replacements), "dummy", false));
    }

    public void createMock(DataMock dataMock) {
        final RequestSpecification request = api().template()
                .body(dataMock.getRequestJson())
                .headers(getHeaders())
                .and().log().all();

        final String mockServiceUrl = getMockServiceUrl(dataMock);
        final Response response = dataMock.getMockId() == null ?
                request.post(mockServiceUrl) :
                request.put(mockServiceUrl + "/" + dataMock.getMockId());

        response.then().log().all();

        final int statusCode = response.statusCode();
        final String responseBody = response.getBody().print();
        if (statusCode == 201 || statusCode == 200) {

            //Store event id in dataStorage (will be used for mock deletion after test done)
            dataStorage.map().computeIfAbsent(dataStorageKeyMock, o -> new LinkedHashMap<String, DataMock>());
            //noinspection unchecked
            ((LinkedHashMap) dataStorage.map().get(dataStorageKeyMock)).put(
                    JsonUtils.getJsonValue(responseBody, "id", Integer.class),
                    dataMock);

            // Store event output as produced event
            String eventName = dataMock.getName();
            int number = eventStorage.getNextEventNumber(eventName);
            eventStorage.addProduced(new EventBuilder()
                    .name(eventName + (number == 0 ? "" : number))
                    .body(JsonUtils.getJsonValue(dataMock.getRequestJson(), "output", String.class))
                    .build());

        } else {

            if (statusCode == 400 && responseBody.contains("This Mock request already Present, Change the input Data!!!")) {
                final Integer mockId = JsonUtils.getJsonValue(responseBody, "virtualServiceRequest.id", Integer.class);
                if (dataMock.isOverwrite()) { // need to use the same mock with different output
                    LOGGER.debug("Mock will be overwritten " + mockId);
                    dataMock.setMockId(mockId);
                    createMock(dataMock);
                } else {
                    LOGGER.debug("Mock already exists " + mockId);
                }
            } else if (statusCode == 400 && responseBody.contains("JSON parse error: Unrecognized field \\\"type\\\"")) {
                LOGGER.error("Old version of mock server is not supported. Use virtualan-ui and " + MOCK_SERVER_ERROR);
            } else {
                throw new CucumberException("Mock creation error:" + response.then().log().all());
            }

        }
    }

    public void deleteMocks() {
        if (dataStorage.has(dataStorageKeyMock)) {
            //noinspection unchecked
            ((LinkedHashMap<Integer, DataMock>) dataStorage.unmask(dataStorageKeyMock)).forEach((id, dataMock) -> {
                LOGGER.debug("Going to delete mock with id = " + id);
                api().template().headers(getHeaders())
                        .delete(dataMock.getServerURL() + "/" + id)
                        .then()
                        .log().all()
                        .assertThat().statusCode(204);
            });
        }
    }

    private HashMap<String, String> getHeaders() {
        return new HashMap<String, String>() {{
            put("Content-Type", "application/json;charset=UTF-8");
            put("Accept", "application/json, text/plain, */*");
        }};
    }
}
