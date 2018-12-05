package restapi.patients.create;

import static org.hamcrest.Matchers.containsString;
import static wiremock.org.apache.http.HttpStatus.SC_CREATED;
import static wiremock.org.apache.http.HttpStatus.SC_NOT_FOUND;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

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

@Slf4j
public class PatientsCreateSteps {

    private Response response;
    private Response responsePatient;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForCreatePatient = rootPath + "features\\patients\\create\\createRequestBody.json";

    @Given("^Doctor opens Patients Management View$")
    public void doctorOpensPatientsManagementView() {
        httpMethods.getRequest(realPatientsUrl).then().statusCode(SC_OK);
    }

    @When("^Doctor creates the new patient$")
    public void doctorCreatesTheNewPatient() {
        response = httpMethods.postRequest(realPatientsUrl,testDataUtils.mappingJsonToString(filePathForCreatePatient));
    }

    @Then("^Doctor checks the patient is created$")
    public void aDoctorAssertPatientCreated() {
        response.then().statusCode(SC_CREATED);
        responsePatient = httpMethods.getRequest(realPatientsUrl +"/"+ response.then().extract().body().path("id"));

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);

        Assert.assertNotNull(responsePatient.then().extract().path("id"));
        Assert.assertEquals(jsonObject.get("name"),responsePatient.then().extract().path("name"));
        Assert.assertEquals(jsonObject.get("email"),responsePatient.then().extract().path("email"));
        Assert.assertEquals(jsonObject.get("gender"),responsePatient.then().extract().path("gender"));
        Assert.assertEquals(jsonObject.get("dateOfBirth"),responsePatient.then().extract().path("dateOfBirth"));
        Assert.assertEquals(jsonObject.get("country"),responsePatient.then().extract().path("country"));
        Assert.assertEquals(jsonObject.get("phone"),responsePatient.then().extract().path("phone"));
        Assert.assertEquals(jsonObject.get("note"),responsePatient.then().extract().path("note"));
    }

    @When("^Doctor creates incomplete the new patient$")
    public void doctorCreatesIncompleteTheNewPatient() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        response = httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString());
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor creates the new patient with the same email$")
    public void doctorCreatesTheNewPatientWithTheSameEmail() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.put("email","emailexists@test.pl");
        httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString()).then().statusCode(SC_CREATED);
        response = httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString());
    }

    @When("^Doctor creates the new patient with the same email what was used for deleted patient$")
    public void doctorCreatesTheNewPatientWithTheSameEmailWhatWasUsedForDeletedPatient() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.put("email","emailexists1@test.pl");
        response = httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString());
        response.then().statusCode(SC_CREATED);
        httpMethods.deleteRequest(realPatientsUrl + "/" + response.then().extract().body().path("id"));
        httpMethods.getRequest(realPatientsUrl +"/"+ response.then().extract().body().path("id")).then().statusCode(SC_NOT_FOUND);
        response = httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString());
    }

}
