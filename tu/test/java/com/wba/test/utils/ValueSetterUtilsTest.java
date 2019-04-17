/*
 * Copyright 2019 Walgreen Co.
 */

package com.wba.test.utils;

import org.testng.annotations.Test;

public class ValueSetterUtilsTest {

    private final String getJson = "{\n" +
            "  \"correlationId\": \"$$v:uniqueID\",\n" +
            "  \"eventProducedTime\": 0,\n" +
            "  \"patientCode\": \"$$patCode:random::10\",\n" +
            "  \"patientNumber\": \"$!v:random::4\",\n" +
            "  \"patientPlans\" : [\n" +
            "    {\n" +
            "      \"planCode\": \"$$v:random::8\",\n" +
            "      \"planName\": \"plan1\",\n" +
            "      \"PatientCode\": \"$$patCode\",\n" +
            "      \"PatientFirstName\": \"$$fn:randomString::10\", \n" +
            "      \"PatientLastName\": \"$$ln\",\n" +
            "    },\n" +
            "    {\n" +
            "      \"planCode\": \"$$v:random::8\",\n" +
            "      \"planName\": \"plan2\",\n" +
            "      \"PatientCode\": \"$$patCode\",\n" +
            "      \"PatientFirstName\": \"$$fn\", \n" +
            "      \"PatientLastName\": \"$$ln:randomString::10\",\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    @Test
    public void t01() {
        final String json = ValueSetterUtils.jsonSetValues(getJson);
        new SoftAssert2()
                .isTrue(JsonUtils.getJsonValue(json, "correlationId").toString().equals("\"uniqueID\""))
                .equals(JsonUtils.getJsonValue(json, "patientPlans[0].PatientLastName"),
                        JsonUtils.getJsonValue(json, "patientPlans[1].PatientLastName"))
                .isTrue(JsonUtils.getJsonValue(json, "patientCode").toString().length() == 10 + 2)
                .isTrue(JsonUtils.getJsonValue(json, "patientNumber").toString().length() == 4)
        ;
    }

}