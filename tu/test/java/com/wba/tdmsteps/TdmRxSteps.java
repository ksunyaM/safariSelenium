/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.tdmsteps;

import com.wba.test.utils.BaseStep;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.tdm.prescription.RxCreator;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class TdmRxSteps extends BaseStep {

    public static final String TDM_RX_PRESCRIPTON_JSON = "data/tdm/RxPrescripton.json";

    @Given("^patient has Rx with data$")
    public void patientHasRxInStatus(DataTable dataTable) {
        String rx = new RxCreator(TDM_RX_PRESCRIPTON_JSON)
                .withParameters(dataTable)
                .create();

        dataStorage.map().put("~~tdmRxNumber", JsonUtils.getJsonValue(rx, "rxNumber", String.class));
    }


    @When("^existing Rx with number \"([^\"]*)\" has been updated to$")
    public void rxSetInStatus(String rx, DataTable data) {
        new RxCreator(TDM_RX_PRESCRIPTON_JSON)
                .withParameters(data)
                .update(asStr(rx));
    }
}
