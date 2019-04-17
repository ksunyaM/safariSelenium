package com.wba.rxdata.test;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.runtime.CucumberException;

public class CustomStep extends _BaseStep {


    @Before
    public void init() {
        dataStorage.add("~~rxNumber", asStr("random::7"));
        dataStorage.add("~~locationNumber", asStr("59511"));
        dataStorage.add("~~locationType", asStr("Pharmacy"));
        dataStorage.add("~invalidDispenseCode", asStr("uuid::"));
    }


    @Given("^set app_code as \"([^\"]*)\"$")
    public void setApp_codeAs(String nullMedId) {
        if (nullMedId.equalsIgnoreCase("Null")) {
            dataStorage.add("~~app_code", "null");
            /*Update the app_code when you are running the test in QE Env*/
        } else if (nullMedId.equalsIgnoreCase("Blank")) {
            dataStorage.add("~~app_code", " ");
        } else {
            throw new CucumberException("action is not implemented: " + nullMedId);
        }
    }
}
