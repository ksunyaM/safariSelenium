package com.wba.rxdata.test;

import com.wba.test.utils.ResourceUtils;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.mocks.MockBuilder;
import com.wba.test.utils.mocks.MockServer;
import com.wba.test.utils.swagger.SwaggerUtils;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.Assert;


import java.util.*;

public class MocksCreationSteps extends _BaseStep {
    static final Map<String, MockBuilder> builders = new HashMap<>();

    public MocksCreationSteps() {
    }

    @Given("^User prepare the \"([^\"]*)\" mock with params$")
    public void User_prepare_the_mock_with_params(String mockJson, DataTable table) throws Exception {
        final MockBuilder mockBuilder = new MockBuilder();


        if(mockJson.equals("RxPlan")||mockJson.equals("Product")){
          switch (mockJson) {
            case "RxPlan":
                mockBuilder.url("/rxplan/{planCode}").mockServer(MockServer.RXP).operationId("readPlanByCode");
                break;
            case "Product":
                mockBuilder.url("/rxproducts/{code}").mockServer(MockServer.RXP).operationId("readRxProductByCode");
                break;
          }
        }else{
            mockBuilder.url("/rxprescribers/{prescriberCode}/prescriberlocations/{prescriberLocationCode}").operationId("readPrescriberLocationByCode");
        }

        Map<String, String> tabledata = table.asMap(String.class, String.class);
        tabledata.forEach((key, value) -> {
            if (key.equalsIgnoreCase("excludeList"))
                mockBuilder.excludeList(tabledata.get(key));
            else if (dataStorage.map().get(value) != null)
                mockBuilder.addParam(key, dataStorage.map().get(value).toString());
            else {
                mockBuilder.addParam(key, tabledata.get(key));
               // dataStorage.add(key, tabledata.get(key));
            }
        });
        String json = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "mocks/" + mockJson + ".json");
        mockBuilder.output(json);
        if(mockJson.equals("PrescriberCodeNotFound")||mockJson.equals("PrescriberLocationNotFound")){
            mockBuilder.httpStatusCode("400");
        }else if(mockJson.equals("PrescriberUnreachable")){
            mockBuilder.httpStatusCode("404");
        }else if(mockJson.equals("Prescriber500Server")){
            mockBuilder.httpStatusCode("500");
        }else if(mockJson.equals("PrescriberUnexpected")){
            mockBuilder.httpStatusCode("500");
        }else if(mockJson.equals("PrescriberDBDown")){
            mockBuilder.httpStatusCode("500");
        }else{
            mockBuilder.httpStatusCode("200");
        }
        builders.put(mockJson, mockBuilder);
        eventStorage.addProduced(new EventBuilder().body(json).name(mockJson).build());
    }

    @When("^Upload all Mock to server$")
    public void uploadAllMockToServer() throws Throwable {
        builders.forEach((k, v) -> {
            LOGGER.info("Adding Mock for :" + k);
            v.build();
        });
        builders.clear();
    }

    @When("^update the \"([^\"]*)\" response Json with Data$")
    public void updateResponseMockJson(String mockJson) {

        eventStorage.findEvent(mockJson).replaceBodyAttribute("code", dataStorage.unmask("PRESCRIBER_CODE"));
        eventStorage.findEvent(mockJson).replaceBodyAttribute("prescriberLocations[0].code", dataStorage.unmask("PRESCRIBER_LOCATION_CODE"));

        if(mockJson.equals("Prescriber")) {
            eventStorage.findEvent(mockJson).replaceBodyAttribute("name.firstName", "randomstring::7");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("name.lastName", "randomstring::7");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("name.suffix", "randomstring::2");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("nationalProviderIdentifier", "randomstring::5");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("prescriberLocations[0].prescriberLocationAddress.addressLine1","randomstring::10");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("prescriberLocations[0].prescriberLocationAddress.addressLine2", "randomstring::10");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("prescriberLocations[0].prescriberLocationAddress.city","randomstring::5");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("prescriberLocations[0].prescriberLocationAddress.zipCode", "int::5");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("prescriberLocations[0].prescriberContact.phoneNumber", "int::10");
        }else  if(mockJson.equals("PrescriberReq")){
            eventStorage.findEvent(mockJson).replaceBodyAttribute("name.firstName", "randomstring::7");
            eventStorage.findEvent(mockJson).replaceBodyAttribute("name.lastName", "randomstring::7");
        }else{

        }

        builders.get(mockJson).output(eventStorage.getProduced(mockJson).getBody());

    }

    @When("^update plan \"([^\"]*)\" response mock json with Data")
    public void updatePlanResponseMockJson(String mockJson) {
        eventStorage.findEvent(mockJson).replaceBodyAttribute("governmentPlan", "true");
        builders.get(mockJson).output(eventStorage.getProduced(mockJson).getBody());
    }

    @And("^update plan \"([^\"]*)\" response mock json with certified plan Data$")
    public void updatePlanResponseMockJsonWithCertifiedPlanCertifiedPlanData(String mockJson, String certifiedPlan) {
        eventStorage.findEvent(mockJson).replaceBodyAttribute("certifiedPlan", certifiedPlan);
        builders.get(mockJson).output(eventStorage.getProduced(mockJson).getBody());
    }


    @When("^User Update the \"([^\"]*)\" response with params$")
    public void userUpdateTheMockWithParams(String mockJson, DataTable updatedValues) throws Throwable {
        String json = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "mocks/" + mockJson + ".json");
        builders.get(mockJson).output(updateJson(json, updatedValues));
    }

    @And("Product Mock is created with params")
    public void productMockWithParams(DataTable data) {
        Map<String, String> mockParameters = data.asMap(String.class, String.class);
        String eventName = "Product";
        String mockOutput = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "/mocks/" + "ProductForRefillablilty.json");

        String statusCode = mockParameters.getOrDefault("statusCode", "200");

        if (statusCode.equals("200")) {
            eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());

            _CommonStep commonStep = new _CommonStep();
            commonStep.the_event_has_data_as(eventName, data);

            verifyMockAgainstYaml("rxProductAPI", "", eventStorage.getLastProduced().getBody(), "v1/rxproducts/{code}", "GET");
        } else {
            mockOutput = "{ }";
            eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());
        }

        new MockBuilder()
   //             .mockServer(MockServer.RXP)
                .httpStatusCode(statusCode)
                .method("GET")
                .url("/rxproducts/{code}")
                 .output(eventStorage.getLastProduced().getBody())
                .addParam("code", mockParameters.get("actualProductPackCode"))
                .build();
    }

    @And("Prescriber Mock is created with params")
    public void prescriberMockWithParams(DataTable data) {
        Map<String, String> mockParameters = data.asMap(String.class, String.class);
        String eventName = "Prescriber";
        String mockOutput = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "/mocks/" + "PrescriberForRefillablilty.json");

        String statusCode = mockParameters.getOrDefault("statusCode", "200");

        if (statusCode.equals("200")) {

            eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());

            _CommonStep commonStep = new _CommonStep();
            commonStep.the_event_has_data_as(eventName, data);

            verifyMockAgainstYaml("rxPrescriberAPI", "", eventStorage.getLastProduced().getBody(), "v1/rxprescribers/{prescriberCode}", "GET");
        } else {
            mockOutput = "{ }";
            eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());
        }


        new MockBuilder()
 //               .mockServer(MockServer.RXP)
                .httpStatusCode(statusCode)
                .method("GET")
                .url("/rxprescribers/{prescriberCode}")
//                .operationId("readPrescriberByCode")
                .output(eventStorage.getLastProduced().getBody())
                .addParam("prescriberCode", mockParameters.get("code"))
                .build();

    }

    @And("Location Mock is created with params")
    public void locationMockWithParams(DataTable data) {
        Map<String, String> mockParameters = data.asMap(String.class, String.class);
        String eventName = "Location";
        String mockOutput = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "/mocks/" + "LocationForRefillability.json");

        String statusCode = mockParameters.getOrDefault("statusCode", "200");

        if (statusCode.equals("200")) {
            eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());

            _CommonStep commonStep = new _CommonStep();
            commonStep.the_event_has_data_as(eventName, data);

            verifyMockAgainstYaml("rxLocationAPI", "", eventStorage.getLastProduced().getBody(), "rxlocations/{locationNumber}/{locationType}", "GET");
        } else {
            mockOutput = "{ }";
            eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());
        }

        new MockBuilder()
                //             .mockServer(MockServer.RXP)
                .httpStatusCode(statusCode)
                .method("GET")
                .url("/rxlocations/{locationNumber}/{locationType}")
                .output(eventStorage.getLastProduced().getBody())
                .addParam("locationNumber", mockParameters.get("locationNumber"))
                .addParam("locationType", mockParameters.get("locationType"))
                .build();
    }

    @And("Plan Mock is created with params")
    public void planMockWithParams(DataTable data) {
        Map<String, String> mockParameters = data.asMap(String.class, String.class);
        String eventName = "Plan";
        String mockOutput = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "/mocks/" + "PlanForRefillability.json");

        eventStorage.addProduced(new EventBuilder().body(mockOutput).name(eventName).build());

        _CommonStep commonStep = new _CommonStep();
        commonStep.the_event_has_data_as(eventName, data);

//        verifyMockAgainstYaml("rxProductAPI", "", eventStorage.getLastProduced().getBody(), "v1/rxproducts/{code}", "GET");

        new MockBuilder()
  //              .mockServer(MockServer.RXP)
                .httpStatusCode("200")
                .method("POST")
                .url("/rxplan/{planCode}")
                .operationId("readPlanByCode")
                .output(eventStorage.getLastProduced().getBody())
                .addParam("planCode", mockParameters.get("planCode"))
                .build();
    }

    private void verifyMockAgainstYaml(String yamlFileName, String request, String responce, String serviceName, String methodName) {
        String yaml = ResourceUtils.getResourceAsString(_BaseStep.RESOURCES_FOLDER_PATH + "/yaml/" + yamlFileName + ".yaml");

        if (!request.equals("")) {
            verifyJsonRequestAgaistYaml(yaml, request, serviceName, methodName);
        }

        if (!responce.equals("")) {
            verifyJsonResponseAgaistYaml(yaml, responce, serviceName, methodName);
        }
    }

    public static void verifyJsonResponseAgaistYaml(String yaml, String json, String serviceName, String methodName) {
        StringBuilder messages = new StringBuilder();

        boolean validationResult = SwaggerUtils.validateResponse(yaml, json, serviceName, methodName, messages);

        Assert.assertTrue(messages.toString(), validationResult);
    }

    public static void verifyJsonRequestAgaistYaml(String yaml, String json, String serviceName, String methodName) {
        StringBuilder messages = new StringBuilder();

        boolean validationResult = SwaggerUtils.validateRequest(yaml, json, serviceName, methodName, messages);

        Assert.assertTrue(messages.toString(), validationResult);
    }
}
