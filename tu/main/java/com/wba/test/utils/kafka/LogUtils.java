/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.kafka;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.slf4j.Logger;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.LogUtils.log;

public class LogUtils {

    private static final String KIBANA_SEARCH_URL = "filebeat-*/_search?q=\"%s\"";
    private static final int SEARCH_ITERATIONS = 20;

    private static final Logger LOGGER = log(LogUtils.class);

    public static String findRecordsInLog(String searchCriteria) {
        for (int i = 0; i < SEARCH_ITERATIONS; i++) {
            Response response = api().template("kibana")
                    .contentType("application/json")
                    .accept("application/json")
                    .get(String.format(KIBANA_SEARCH_URL, searchCriteria))
                    .thenReturn();
            int hits = JsonPath.from(response.body().asString()).getInt("hits.total");
            if (hits == 0) {
                LOGGER.debug("Iteration #{} did not find logs by following criteria: {}", i, searchCriteria);
                sleep(2000);
            } else {
                return response.body().prettyPrint();
            }
        }
        return "";
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}
