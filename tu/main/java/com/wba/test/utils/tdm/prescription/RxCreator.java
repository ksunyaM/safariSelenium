/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm.prescription;


import com.wba.test.utils.BaseStep;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.ResourceUtils;
import cucumber.api.DataTable;

import java.util.Collections;

public class RxCreator extends BaseStep {
    private String rxParameters;
    private RxHelper rxHelper = new RxHelper();

    public RxCreator(String recoursePath) {
        rxParameters = ResourceUtils.getResourceAsString(recoursePath);
    }

    public RxCreator withParameters(DataTable data) {
        rxParameters = updateJson(rxParameters, data);
        return this;
    }

    public String getStatus() {
        String s = JsonUtils.getJsonValue(rxParameters, "status", String.class);
        switch (s) {
            case "REVIEWED":
                return "UF";
            case "READY":
                return "RD";
            case "PRINTED":
                return "PR";
            case "SOLD":
                return "SD";
            default:
                return s;
        }
    }

    public String create() {
        String storeNumber = JsonUtils.getJsonValue(rxParameters, "storeNumber", String.class);
        String patientId = JsonUtils.getJsonValue(rxParameters, "patientId", String.class);
        String drugId = JsonUtils.getJsonValue(rxParameters, "drugId", String.class);
        String prescriberId = JsonUtils.getJsonValue(rxParameters, "prescriberId", String.class);
        String userId = JsonUtils.getJsonValue(rxParameters, "userId", String.class);
        String paymentType = JsonUtils.getJsonValue(rxParameters, "paymentType", String.class);
        if (storeNumber == null || storeNumber.equals("")) throw new RuntimeException("Store Number is not set.");
        if (patientId == null || patientId.equals("")) throw new RuntimeException("Patient Id is not set.");
        String rxNumber = paymentType.equals("CASH") ? rxHelper.ud32SimpleCashRXHelper(storeNumber, patientId, drugId, "1", prescriberId, userId) : "";
        boolean isUpdated = rxHelper.ud32UpdateRxStatusHelper(storeNumber, rxNumber, getStatus(), userId);
        if (!isUpdated) throw new RuntimeException("Rx has not been updated till status: " + getStatus());
        rxParameters = updateJson(rxParameters,  Collections.singletonMap("rxNumber", rxNumber));
        return rxParameters;
    }

    public String update(String rxNumber) {
        String storeNumber = JsonUtils.getJsonValue(rxParameters, "storeNumber", String.class);
        String userId = JsonUtils.getJsonValue(rxParameters, "userId", String.class);
        boolean isUpdated = rxHelper.ud32UpdateRxStatusHelper(storeNumber, rxNumber, getStatus(), userId);
        if (!isUpdated) throw new RuntimeException("Rx has not been updated till status: " + getStatus());
        rxParameters = updateJson(rxParameters,  Collections.singletonMap("rxNumber", rxNumber));
        return rxParameters;
    }
}
