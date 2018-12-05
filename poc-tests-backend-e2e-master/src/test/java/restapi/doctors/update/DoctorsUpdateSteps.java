package restapi.doctors.update;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.junit.Assert;
import restapi.HttpMethods;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class DoctorsUpdateSteps {

    private Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private ApiUtils apiUtils = new ApiUtils();
    private String realDoctorsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DOCTORS);
    private DoctorsUpdateTest doctorsUpdateTest = new DoctorsUpdateTest();
    private String doctorId = doctorsUpdateTest.shareDoctorId();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForUpdateDoctor = rootPath + "features\\doctors\\create\\createDoctorRequestBody.json";
    private String nameNew = "E2E_name";
    private String surnameNew = "E2E_surname";
    private String emailNew = "E2E_email@e2e.pl";
    private List<String> specializationNew = new ArrayList<>(Arrays.asList("Cardiologist"));

    @Given("^The Administrator opens Doctors Management View$")
    public void theAdministratorOpensDoctorsManagementView() {
        //httpMethods.getRequest(realDoctorsUrl).then().statusCode(SC_OK);
        // TODO: GET all is not implemented yet
    }

    @When("^The Administrator selects Doctor and update all data$")
    public void theAdministratorSelectsDoctorAndUpdateAllData() {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDoctor);

        jsonObject.remove("name");
        jsonObject.put("name", nameNew);

        jsonObject.remove("surname");
        jsonObject.put("surname", surnameNew);

        jsonObject.remove("email");
        jsonObject.put("email", emailNew);

        jsonObject.remove("specializations");
        jsonObject.put("specializations", specializationNew);

        response = httpMethods.putRequest(realDoctorsUrl + "/" + doctorId, jsonObject.toJSONString());
        response.then().statusCode(SC_OK);
    }

    @Then("^The Administrator sees all updated details of Doctor$")
    public void theAdministratorSeesAllUpdatedDetailsOfDoctor() {
        Assert.assertEquals(doctorId, response.then().extract().path("id"));
        Assert.assertEquals(nameNew, response.then().extract().path("name"));
        Assert.assertEquals(surnameNew, response.then().extract().path("surname"));
        Assert.assertEquals(emailNew, response.then().extract().path("email"));
        Assert.assertEquals(specializationNew.stream().sorted().collect(Collectors.toList()), response.then().extract().path("specializations"));
    }

    @When("^The Administrator selects Doctor and update only (.*) with (.*)$")
    public void theAdministratorSelectsDoctorAndUpdateOnlyFieldNameWithValue(String fieldName, String value) {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDoctor);
        jsonObject.remove(fieldName);
        jsonObject.put(fieldName, value);

        response = httpMethods.putRequest(realDoctorsUrl + "/" + doctorId, jsonObject.toJSONString());
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Then("^The Administrator sees updated (.*) with new (.*)$")
    public void theAdministratorSeesUpdatedFieldNameWithNewValue(String fieldName, String value) {
        Assert.assertEquals(value, response.then().extract().path(fieldName).toString());
    }

    @When("^The Administrator tries edit doctor with not existing ID$")
    public void theAdministratorTriesEditDoctorWithNotExistingID() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDoctor);
        response = httpMethods.putRequest(realDoctorsUrl + "/not-found-" + LocalDateTime.now(), jsonObject.toJSONString());
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage) {
        //response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
        //Assert.assertEquals(errorMessage,response.then().extract().body().path("error"));
    }

    @Given("^The Administrator selects and deletes the Doctor$")
    public void theAdministratorSelectsAndDeletesTheDoctor() {
        response = httpMethods.deleteRequest(realDoctorsUrl + "/" + doctorId);
        response.then().statusCode(SC_NO_CONTENT);
    }

    @When("^The administrator is trying to update the information of the doctor who is deleted$")
    public void theAdministratorIsTryingToUpdateTheInformationOfTheDoctorWhoIsDeleted() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDoctor);
        response = httpMethods.putRequest(realDoctorsUrl + "/" + doctorId, jsonObject.toJSONString());
    }

}
