package restapi.doctors.create;

import configuration.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import restapi.HttpMethods;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static wiremock.org.apache.http.HttpStatus.*;

@Slf4j
public class DoctorsCreateSteps {

    private static Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private ApiUtils apiUtils = new ApiUtils();
    private String realDoctorsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DOCTORS);
    private DoctorsCreateTest doctorsCreateTest = new DoctorsCreateTest();
    private List<String> doctorsIds = doctorsCreateTest.shareDoctorsIds();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForCreateDoctor = rootPath + "features\\doctors\\create\\createDoctorRequestBody.json";


    @Given("^The Administrator logs into the Doctor module and checks existing doctors$")
    public void theAdministratorLogsIntoTheDoctorModule() {
        for (int i = 0; i < doctorsIds.size(); i++) {
            httpMethods.getRequest(realDoctorsUrl + "/" + doctorsIds.get(i)).then().statusCode(SC_OK);
        }
    }

    @When("^The Administrator creates a new Doctor (.*) (.*)$")
    public void theAdministratorCreatesANewDoctor(String name, String surname) {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDoctor);
        jsonObject.remove("name");
        jsonObject.remove("surname");
        jsonObject.put("name", name);
        jsonObject.put("surname", surname);

        response = httpMethods.postRequest(realDoctorsUrl, jsonObject.toJSONString());
        response.then().statusCode(SC_CREATED);
    }

    @Then("^The Administrator checks that the Doctor (.*) (.*) is created$")
    public void theAdministratorChecksThatTheDoctorIsCreated(String tagName, String value) {
        assertEquals(value, response.then().extract().path(tagName));
    }

    @When("^The administrator checks that the created Doctor has a list of specializations$")
    public void theAdministratorChecksThatTheCreatedDoctorHasAListOfSpecializations() {
        response = httpMethods.getRequest(realDoctorsUrl + "/" + response.then().extract().path("id"));
        response.then().statusCode(SC_OK);
    }

    @Then("^The specialization list contains the correct specializations (.*) and (.*)$")
    public void theSpecializationListContainsTheCorrectSpecializations(String spec1, String spec2) {
        List<String> listOfSpecializations = response.then().extract().path("specializations");
        listOfSpecializations.containsAll(Arrays.asList(spec1, spec2));
    }

    @Then("^Response Header for Location Should contains the created ID$")
    public void responseHeaderForLocationShouldContainsTheCreatedID() {
        assertFalse(response.getHeader("Location").isEmpty());
    }

    @When("^The Administrator is creating a new Doctor with wrong Rest API address$")
    public void theAdministratorCreatesANewDoctorWithWrongRestAPIAddress() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDoctor);
        jsonObject.remove("name");
        jsonObject.put("name", "Michel");
        response = httpMethods.postRequest((realDoctorsUrl + "/WrongApi"), jsonObject.toJSONString());

    }

    @Then("^The Administrator checks that the Doctor is not created, due to an error: 404 - Not Found$")
    public void theAdministratorChecksThatTheDoctorIsNotCreatedDueToAnErrorNotFound() {
        response.then().statusCode(SC_NOT_FOUND);
    }



   /* @When("^The Administrator is creating a new Doctor with internal server error$")
    public void theAdministratorIsCreatingANewDoctorWithInternalServerError() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateWrongDoctor);
        jsonObject.remove("name");
        jsonObject.put("name", "Michel");
        response = httpMethods.postRequest(realDoctorsUrl, jsonObject.toJSONString());
        System.out.println(response.asString());
    }

    @Then("^The Administrator checks that the Doctor is not created, due to an error: Internal Server Error$")
    public void theAdministratorChecksThatTheDoctorIsNotCreatedDueToAnErrorInternalServerError() {
        response.then().statusCode(SC_INTERNAL_SERVER_ERROR);
    }*/

   /* @When("^The Administrator creates a Doctor that already exists in the system$")
    public void theAdministratorCreatesADoctorThatAlreadyExistsInTheSystem() {
        response = RestAssured.given().body(bodyForRequestPost).when().post(url + "/doctors/create");
    }

    @Then("^The Administrator checks that the Doctor is not created, due to an error: (.*)$")
    public void theAdministratorChecksThatTheDoctorIsNotCreatedDueToAnErrorUserAlreadyExists(String error) {
        response.then().log().all();
        assertEquals(error, response.then().extract().path("error"));
    }

    @When("^The Administrator creates a new Doctor with wrong RestAPI$")
    public void theAdministratorCreatesANewDoctorWithWrongRestAPI() {
        response = RestAssured.given().header("Content-type", "application/json")
                .body(bodyForRequestPost)
                .post(url + "/doctors/createtest");
    }

     @Then("^Timeout values should be less than (\\d+)$")
    public void timeoutValuesShouldBeLessThan(long timeoutValue) {
        response.then().assertThat().time(lessThan(timeoutValue));
    }

    @When("^The Administrator creates a new Doctor with internal server error$")
    public void theAdministratorCreatesANewDoctorWithInternalServerError() {
        response = RestAssured.given().header("Content-Type", "application/json")
                .body(bodyForRequestPost)
                .post(url + "/doctors/create-500");
    }*/
/*
    @Then("^The Administrator checks that the Doctor is not created, due to an error: (\\d+) - Internal Server Error$")
    public void theAdministratorChecksThatTheDoctorIsNotCreatedDueToAnErrorInternalServerError(int arg0) {
        // Write code here that turns the phrase above into concrete actions
    }*/
}
