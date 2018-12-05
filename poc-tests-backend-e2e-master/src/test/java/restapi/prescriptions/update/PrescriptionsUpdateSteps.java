package restapi.prescriptions.update;

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

import java.time.LocalDateTime;
import java.util.ArrayList;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class PrescriptionsUpdateSteps {

    private Response response;
    private Exception actualException;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private PrescriptionsUpdateTest prescriptionsUpdateTest = new PrescriptionsUpdateTest();
    private String patientId = prescriptionsUpdateTest.sharePatientIdFromResponseBody();
    private String prescriptionDraftId = prescriptionsUpdateTest.sharePrescriptionDraftIdFromResponseBody();
    private String prescriptionIssuedId = prescriptionsUpdateTest.sharePrescriptionIssuedIdFromResponseBody();
    private String prescriptionExpiredId = prescriptionsUpdateTest.sharePrescriptionExpiredIdFromResponseBody();
    private String prescriptionDeletedId = prescriptionsUpdateTest.sharePrescriptionDeletedIdFromResponseBody();
    private String filePathForUpdateDraftPrescription = rootPath + "features\\prescriptions\\update\\updatePrescriptionRequestBody.json";
    private String issuedByNew = "Dr Updater";
    private long validityDateNew = 1546300800;
    private String stateNew = "DRAFT";
    private String diseaseIdNew = "AA:9999";
    private String noteNew = "New notes after update";
    private String[] drugsNew = {"DD:100","DD:200"};
    private ArrayList<String> drugsNewList = new ArrayList<>();

    @Given("^Doctor selects patient John Smith$")
    public void doctorSelectsPatientJohnSmith(){
        response = httpMethods.getRequest(realPatientsUrl + "/" + patientId);
        response.then().statusCode(SC_OK);
        response.then().body("prescriptions", everyItem(is(not(empty()))));
    }

    @When("^Doctor selects draft prescription and updates data from UI$")
    public void doctorSelectsDraftPrescriptionAndUpdateDataFromUI() {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);

        jsonObject.remove("issuedBy");
        jsonObject.put("issuedBy",issuedByNew);

        jsonObject.remove("validityDate");
        jsonObject.put("validityDate",validityDateNew);

        jsonObject.remove("state");
        jsonObject.put("state",stateNew);

        jsonObject.remove("diseaseId");
        jsonObject.put("diseaseId",diseaseIdNew);

        jsonObject.remove("note");
        jsonObject.put("note",noteNew);

        drugsNewList.add(drugsNew[0]);
        drugsNewList.add(drugsNew[1]);
        jsonObject.remove("drugsIds");
        jsonObject.put("drugsIds",drugsNewList);

        response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionDraftId,jsonObject.toJSONString());
        response.then().statusCode(SC_OK);
        // TODO: ma byc 204 - bez response body, wiec weryfikacje trzeba zrobic po get
    }

    @Then("^Doctor checks that prescription is updated$")
    public void doctorChecksThatPrescriptionIsUpdated() {
        Assert.assertEquals(prescriptionDraftId,response.then().extract().path("id"));
        Assert.assertEquals(patientId,response.then().extract().path("patientId"));
        Assert.assertEquals(Long.toString(validityDateNew),response.then().extract().path("validityDate").toString());
        Assert.assertEquals(stateNew,response.then().extract().path("state"));
        Assert.assertEquals(issuedByNew,response.then().extract().path("issuedBy"));
        Assert.assertEquals(diseaseIdNew,response.then().extract().path("diseaseId"));
        Assert.assertEquals(noteNew,response.then().extract().path("note"));
        response.then().body("drugsIds", hasItem(drugsNew[0]));
        response.then().body("drugsIds", hasItem(drugsNew[1]));
    }

    @When("^Doctor selects draft prescription and updates selected data (.*) with (.*)$")
    public void doctorSelectsDraftPrescriptionAndUpdateSelectedDataFieldNameWithValue(String fieldName, String value) {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);

        jsonObject.remove(fieldName);
        jsonObject.put(fieldName,value);

        response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionDraftId,jsonObject.toJSONString());
        response.then().statusCode(SC_OK); // TODO: need update from 200 to 204
    }

    @Then("^Doctor checks that (.*) in prescription is updated with correct (.*)$")
    public void doctorChecksThatFieldNameInPrescriptionIsUpdatedWithCorrectValue(String fieldName, String value) {
        Assert.assertEquals(value,response.then().extract().path(fieldName).toString());
    }

    @When("^Doctor selects draft prescription and update state to ISSUED$")
    public void doctorSelectsDraftPrescriptionAndUpdateStateToISSUED() {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);

        jsonObject.remove("state");
        jsonObject.put("state","ISSUED");

        response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionDraftId,jsonObject.toJSONString());
        response.then().statusCode(SC_OK); // TODO: required update from 200 to 204 and get to verify data
        response.then().body("issuedDate",is(not(empty())));
    }

    @When("^Doctor tries edit prescription with (.*) state$")
    public void doctorTriesEditPrescriptionWithState(String state) {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);

        jsonObject.remove("diseaseId");
        jsonObject.put("diseaseId","XX:999");

        if("ISSUED".equals(state)){
            response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionIssuedId,jsonObject.toJSONString());
        }

        if("EXPIRED".equals(state)){
            response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionExpiredId,jsonObject.toJSONString());
        }
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor tries edit prescriptions with does not exist ID$")
    public void doctorTriesEditPrescriptionsWithDoesNotExistID() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        response = httpMethods.putRequest(realPrescriptionsUrl + "/not-found-" + LocalDateTime.now(),jsonObject.toJSONString());
    }

    @When("^Doctor tries edit incomplete the existing draft prescription for patient$")
    public void doctorTriesEditIncompleteTheExistingDraftPrescriptionForPatient() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);
        jsonObject.remove("diseaseId");
        jsonObject.remove("state");
        jsonObject.put("state","ISSUED");

        response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionIssuedId,jsonObject.toJSONString());
    }

    @When("^Doctor tries edit deleted draft prescription$")
    public void doctorTriesEditDeletedDraftPrescription() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);
        jsonObject.remove("state");
        jsonObject.put("state","DRAFT");
        jsonObject.remove("note");
        jsonObject.put("note", "test");

        response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionDeletedId,jsonObject.toJSONString());
    }

    @When("^Doctor tries edit prescriptions without ID$")
    public void doctorTriesEditPrescriptionsWithoutID() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
        response = httpMethods.putRequest(realPrescriptionsUrl,jsonObject.toJSONString());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @When("^Doctor update the draft prescription for patient with timeout$")
    public void doctorUpdateTheDraftPrescriptionForPatientWithTimeout() {

        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));

        try {

            // TODO: raczej timeout bedzie problemowy do testow, bo i tak odpowie serwer, bo to juz nie jest stub, w ktorym mamy zaszyta informacje ze ma odpowiedziec po paru seknudach

            JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForUpdateDraftPrescription);
            jsonObject.remove("patientId");
            jsonObject.put("patientId",patientId);

            response = httpMethods.putRequest(realPrescriptionsUrl + "/" + prescriptionDraftId,jsonObject.toJSONString());

        } catch (Exception e){
            actualException = e;
        } finally {
            RestAssured.reset();
        }

    }

}
