package restapi.prescriptions.create;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Assert;
import restapi.HttpMethods;
import services.Patient;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class PrescriptionsCreateSteps {

    private Response response;
    private Exception actualException;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private String prescriptionId;
    private String filePathForCreateDraftPrescription = rootPath + "features\\prescriptions\\create\\createPrescriptionRequestBody.json";

    private PrescriptionsCreateTest prescriptionsCreateTest = new PrescriptionsCreateTest();
    private String patientId = prescriptionsCreateTest.sharePatientIdFromResponseBody();

    @Given("^Doctor selects patient John Smith$")
    public void doctorSelectsPatientJohnSmith(){
        response = httpMethods.getRequest(realPatientsUrl + "/" + patientId);
        response.then().statusCode(SC_OK);
        response.then().body("prescriptions", everyItem(empty()));
    }

    @When("^Doctor creates the new prescription for patient as (.*)$")
    public void doctorCreatesTheNewPrescriptionForPatient(String prescriptionState)  {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);
        jsonObject.remove("state");
        jsonObject.put("state",prescriptionState);

        response = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());
        response.then().statusCode(SC_CREATED);
    }

    @Then("^Doctor checks that data of prescription are saved correctly as (.*)$")
    public void doctorChecksThatDataOfPrescriptionAreSavedCorrectly(String prescriptionState) {

        prescriptionId = response.then().extract().body().path("prescriptionId").toString();

        response = httpMethods.getRequest(realPrescriptionsUrl + "/" + prescriptionId);
        response.then().statusCode(SC_OK);

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);
        jsonObject.remove("state");
        jsonObject.put("state",prescriptionState);

        String patientId = jsonObject.get("patientId").toString();
        String validityDate = jsonObject.get("validityDate").toString();
        String state = jsonObject.get("state").toString();
        String issuedBy = jsonObject.get("issuedBy").toString();
        String diseaseId = jsonObject.get("diseaseId").toString();
        String note = jsonObject.get("note").toString();

        response.getBody().prettyPrint();

        Assert.assertEquals(prescriptionId,response.then().extract().path("id"));
        Assert.assertEquals(patientId,response.then().extract().path("patientId"));
        Assert.assertEquals(validityDate,response.then().extract().path("validityDate").toString());
        Assert.assertEquals(state,response.then().extract().path("state"));
        Assert.assertEquals(issuedBy,response.then().extract().path("issuedBy"));
        Assert.assertEquals(diseaseId,response.then().extract().path("diseaseId"));
        Assert.assertEquals(note,response.then().extract().path("note"));

        if("DRAFT".equals(prescriptionState)){
            response.then().body("issuedDate",isEmptyOrNullString());
        }
        if("ISSUED".equals(prescriptionState)){
            response.then().body("issuedDate",not(isEmptyOrNullString()));
        }
    }

    @When("^Doctor creates incomplete the new prescription for patient$")
    public void doctorCreatesIncompleteTheNewPrescriptionForPatient() {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDraftPrescription);
        jsonObject.remove("patientId");

        response = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @When("^Doctor creates the new prescription for patient with timeout$")
    public void doctorCreatesTheNewPrescriptionForPatientWithTimeout() {
        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));

        try {

            // TODO: raczej timeout bedzie problemowy do testow, bo i tak odpowie serwer, bo to juz nie jest stub, w ktorym mamy zaszyta informacje ze ma odpowiedziec po paru seknudach

            JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDraftPrescription);
            jsonObject.remove("patientId");
            jsonObject.put("patientId",patientId);

            response = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());

        } catch (Exception e){
            actualException = e;
        } finally {
            RestAssured.reset();
        }
    }

}
