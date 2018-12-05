package restapi.patients.update;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.Assert;
import restapi.HttpMethods;
import restapi.patients.delete.PatientsDeleteTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static wiremock.org.apache.http.HttpStatus.SC_NO_CONTENT;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class PatientsUpdateSteps {

    private Response response;
    private Response responsePatient;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private String realPatientsUrl= apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private PatientsUpdateTest patientsUpdateTest = new PatientsUpdateTest();
    private List<String> patientsIds = patientsUpdateTest.sharePatientsIds();
    private String unDeletedPatientId = patientsIds.get(0);
    private String deletedPatientId = patientsIds.get(1);
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForCreatePatient = rootPath + "features\\patients\\create\\createRequestBody.json";

    @Given("^Doctor opens Patients Management View$")
    public void doctorOpensPatientsManagementView() {
        httpMethods.getRequest(realPatientsUrl).then().statusCode(SC_OK);
    }

    @When("^Doctor selects Patient, changes data and click Save button$")
    public void doctorSelectsPatientChangesDataAndClickSaveButton() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.put("email","test_edit@e2e.com");
        jsonObject.remove("name");
        jsonObject.put("name","Test 2 update");
        jsonObject.remove("note");
        jsonObject.put("note","Updated user");

        response = httpMethods.putRequest(realPatientsUrl + "/" + unDeletedPatientId,jsonObject.toJSONString());
    }

    @Then("^Doctor checks Patient is updated$")
    public void doctorChecksPatientIsUpdated() {
        responsePatient = httpMethods.getRequest(realPatientsUrl + "/" + unDeletedPatientId);
        response.then().statusCode(SC_NO_CONTENT);
        Assert.assertEquals("Test 2 update",responsePatient.then().extract().path("name"));
        Assert.assertEquals("test_edit@e2e.com",responsePatient.then().extract().path("email"));
        Assert.assertEquals("Updated user",responsePatient.then().extract().path("note"));
    }

    @When("^Doctor selects Patient, changes incomplete data and click Save button$")
    public void doctorSelectsPatientChangesIncompleteDataAndClickSaveButton() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.remove("name");

        response = httpMethods.putRequest(realPatientsUrl + "/" + unDeletedPatientId,jsonObject.toJSONString());
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor selects Patient, changes email for the existing email in db, and click Save button$")
    public void doctorSelectsPatientChangesEmailForTheExistingEmailInDbAndClickSaveButton() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.put("email","johntest"+ testDataUtils.getRandomValue() +"@test.pl");

        httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString());
        response = httpMethods.putRequest(realPatientsUrl + "/" + unDeletedPatientId,jsonObject.toJSONString());
    }

    @When("^Doctor tries update not existing Patient$")
    public void doctorTriesUpdateNotExistingPatient() {
        response = httpMethods.putRequest(realPatientsUrl + "/not-found-" + LocalDateTime.now(),testDataUtils.mappingJsonToString(filePathForCreatePatient));
    }

    @When("^Doctor tries update deleted Patient$")
    public void doctorTriesUpdateDeletedPatient() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.put("email","johntest"+ testDataUtils.getRandomValue() +"@test.pl");

        response = httpMethods.putRequest(realPatientsUrl + "/" + deletedPatientId,jsonObject.toJSONString());
    }

    @When("^Doctor tries update Patient without ID in url$")
    public void doctorTriesUpdatePatientWithoutIDInUrl() {
        response = httpMethods.putRequest(realPatientsUrl,testDataUtils.mappingJsonToString(filePathForCreatePatient));
    }

    @When("^Doctor selects Patient, update (.*) using (.*) and click Save button$")
    public void doctorSelectsPatientUpdateFieldNameUsingValueAndClickSaveButton(String fieldName, String value) {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove(fieldName);
        jsonObject.put(fieldName,value);

        response = httpMethods.putRequest(realPatientsUrl + "/" + unDeletedPatientId,jsonObject.toJSONString());
        response.then().statusCode(SC_NO_CONTENT);
    }

    @Then("^Doctor checks that (.*) is updated with new (.*)$")
    public void doctorChecksThatFieldNameIsUpdatedWithNewValue(String fieldName, String value) {
        response = httpMethods.getRequest(realPatientsUrl + "/" + unDeletedPatientId);
        Assert.assertEquals(value,response.then().extract().path(fieldName));
    }

}
