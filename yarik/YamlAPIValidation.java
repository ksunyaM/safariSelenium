package com.wba.rxdata.test;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wba.test.utils.ResourceUtils;
import com.wba.test.utils.SoftAssert2;
import com.wba.test.utils.swagger.SwaggerUtils;
import cucumber.api.java.en.And;
import cucumber.runtime.CucumberException;
import org.junit.Assert;


public class YamlAPIValidation extends _BaseStep {

    @And("^verify by yaml - \"([^\"]*)\"$")
    public void verifyByYaml(String action) {
        boolean validationResult;
        StringBuilder messages = new StringBuilder();
        String json = eventStorage.getLastConsumed().getBody();
        String yaml = ResourceUtils.getResourceAsString(RESOURCES_FOLDER_PATH + "yaml/rxPDataAPI.yaml");
        switch (action) {
            case "getDispense":
                validationResult = SwaggerUtils.validateResponse(yaml, json, "/prescriptions/{prescriptionCode}/dispenses/{dispenseCode}", "GET", messages);
                break;
            case "getDispenseListbyPrescriptionCode":
                validationResult = SwaggerUtils.validateResponse(yaml, json, "/prescriptions/{prescriptionCode}/dispenses", "GET", messages);
                break;
            case "readPrescription":
                validationResult = SwaggerUtils.validateResponse(yaml, json, "/prescriptions/{prescriptionCode}", "GET", messages);
                break;
            case "searchPrescriptionList":
                validationResult = SwaggerUtils.validateResponse(yaml, json, "/prescriptions/search", "POST", messages);
                break;
            default:
                throw new CucumberException("unknown action " + action);
        }
        new SoftAssert2().off().isTrue(validationResult, messages.toString());
    }
}

