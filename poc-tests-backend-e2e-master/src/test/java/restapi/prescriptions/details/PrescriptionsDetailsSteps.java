package restapi.prescriptions.details;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Assert;
import restapi.HttpMethods;
import java.time.LocalDateTime;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class PrescriptionsDetailsSteps {

    private Response response;
    private Exception actualException;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private PrescriptionsDetailsTest prescriptionsDetailsTest = new PrescriptionsDetailsTest();
    private String patientId = prescriptionsDetailsTest.sharePatientIdFromResponseBody();
    private String prescriptionDraftId = prescriptionsDetailsTest.sharePrescriptionDraftIdFromResponseBody();
    private String prescriptionIssuedId = prescriptionsDetailsTest.sharePrescriptionIssuedIdFromResponseBody();
    private String prescriptionExpiredId = prescriptionsDetailsTest.sharePrescriptionExpiredIdFromResponseBody();
    private String prescriptionDeletedId = prescriptionsDetailsTest.sharePrescriptionDeletedIdFromResponseBody();
    private String filePathForCreatePrescription = rootPath + "features\\prescriptions\\create\\createPrescriptionRequestBody.json";

    @Given("^Doctor selects patient John Smith$")
    public void doctorSelectsPatientJohnSmith(){
        response = httpMethods.getRequest(realPatientsUrl + "/" + patientId);
        response.then().statusCode(SC_OK);
        response.then().body("prescriptions", everyItem(is(not(empty()))));
    }

    @Given("^Doctor is logged in system$")
    public void doctorIsLoggedInSystem(){
        httpMethods.getRequest(realPatientsUrl).then().statusCode(SC_OK);
    }

    @When("^Doctor is going to prescription module and select (.*) prescription$")
    public void doctorIsGoingToPrescriptionModuleAndSelectStatePrescription(String state) {

        if("DRAFT".equals(state)){
            response = httpMethods.getRequest(realPrescriptionsUrl + "/" + prescriptionDraftId);
        }
        if("ISSUED".equals(state)){
            response = httpMethods.getRequest(realPrescriptionsUrl + "/" + prescriptionIssuedId);
        }
        if("EXPIRED".equals(state)){
            response = httpMethods.getRequest(realPrescriptionsUrl + "/" + prescriptionExpiredId);
        }
        response.then().statusCode(SC_OK);
    }

    @Then("^Doctor checks that sees correct data for selected (.*) prescription for patient$")
    public void doctorChecksThatSeesCorrectDataForSelectedStatePrescriptionForPatient(String state) {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePrescription);

        Assert.assertEquals(state,response.then().extract().path("state").toString());
        Assert.assertEquals(jsonObject.get("validityDate").toString(),response.then().extract().path("validityDate").toString());
        Assert.assertEquals(jsonObject.get("issuedBy").toString(),response.then().extract().path("issuedBy").toString());
        Assert.assertEquals(jsonObject.get("diseaseId").toString(),response.then().extract().path("diseaseId").toString());
        Assert.assertEquals(jsonObject.get("note").toString(),response.then().extract().path("note").toString());
    }

    @When("^Doctor tries get details of prescription with does not exist ID$")
    public void doctorTriesGetDetailsOfPrescriptionWithDoesNotExistID() {
        response = httpMethods.getRequest(realPrescriptionsUrl + "/not-found-" + LocalDateTime.now());
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor tries get details of deleted draft prescriptions$")
    public void doctorTriesGetDetailsOfDeletedDraftPrescriptions() {
        response = httpMethods.getRequest(realPrescriptionsUrl + "/" + prescriptionDeletedId);
    }

    @When("^Doctor tries get details of all prescriptions$")
    public void doctorTriesGetDetailsOfAllPrescriptions() {
        response = httpMethods.getRequest(realPrescriptionsUrl);
    }


////////////////////////////////////////////////////////////////////////////////////////

    @When("^Doctor tries get information about selected prescription with timeout$")
    public void doctorTriedGetInformationAboutSelectedPrescriptionWithTimeout() {

        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));

        try {

            // TODO: raczej timeout bedzie problemowy do testow, bo i tak odpowie serwer, bo to juz nie jest stub, w ktorym mamy zaszyta informacje ze ma odpowiedziec po paru seknudach

            response = httpMethods.getRequest(realPrescriptionsUrl + "/" + prescriptionDraftId);

        } catch (Exception e){
            actualException = e;
        } finally {
            RestAssured.reset();
        }

    }


    @When("^Doctor is going to prescription module to get prescription for John Smith$")
    public void doctorIsGoingToPrescriptionModuleToGetPrescriptionForJohnSmith() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // TODO implement
        throw new PendingException();
    }

    @Then("^Doctor checks that sees all prescriptions for selected patient$")
    public void doctorChecksThatSeesAllPrescriptionsForSelectedPatient() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        // TODO implement
        throw new PendingException();
    }


}
