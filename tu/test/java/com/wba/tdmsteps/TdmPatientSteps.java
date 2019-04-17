/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.tdmsteps;

import com.wba.test.utils.BaseStep;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.tdm.patient.PatientCreator;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;

public class TdmPatientSteps extends BaseStep {
    private static final String TDM_PATIENT_JSON = "data/tdm/Patient.json";

    @Given("^new patient in ICPlus for the store \"([^\"]*)\"$")
    public void newPatientInICPlusForTheStore(String storeNumber) {
        String patient = new PatientCreator(TDM_PATIENT_JSON)
                .withStore(storeNumber)
                .create();
        dataStorage.map().put("~~tdmPatId", JsonUtils.getJsonValue(patient, "patientId", String.class));
        dataStorage.map().put("~~tdmStoreNumber", storeNumber);
    }

    @Given("^new patient in ICPlus for the store \"([^\"]*)\" with data$")
    public void newPatientInICPlusForTheStoreWithData(String storeNumber, DataTable data) {
        String patient = new PatientCreator(TDM_PATIENT_JSON)
                .withStore(storeNumber)
                .withParameters(data)
                .create();
        dataStorage.map().put("~~tdmPatId", JsonUtils.getJsonValue(patient, "patientId", String.class));
        dataStorage.map().put("~~tdmPatFirstName", JsonUtils.getJsonValue(patient, "firstName", String.class));
        dataStorage.map().put("~~tdmPatLastName", JsonUtils.getJsonValue(patient, "lastName", String.class));
        dataStorage.map().put("~~tdmPatDoB", JsonUtils.getJsonValue(patient, "dateOfBirth", String.class));
        dataStorage.map().put("~~tdmPatPhone", JsonUtils.getJsonValue(patient, "phone", String.class));
        dataStorage.map().put("~~tdmStoreNumber", storeNumber);
    }
}
