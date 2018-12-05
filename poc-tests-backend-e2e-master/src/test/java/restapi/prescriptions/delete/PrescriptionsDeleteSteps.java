package restapi.prescriptions.delete;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import restapi.HttpMethods;
import restapi.prescriptions.create.PrescriptionsCreateTest;

import java.time.LocalDateTime;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class PrescriptionsDeleteSteps {

    private Response response;
    private Exception actualException;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private PrescriptionsDeleteTest prescriptionsDeleteTest = new PrescriptionsDeleteTest();
    private String patientId = prescriptionsDeleteTest.sharePatientIdFromResponseBody();
    private String prescriptionDraftId = prescriptionsDeleteTest.sharePrescriptionDraftIdFromResponseBody();
    private String prescriptionIssuedId = prescriptionsDeleteTest.sharePrescriptionIssuedIdFromResponseBody();
    private String prescriptionExpiredId = prescriptionsDeleteTest.sharePrescriptionExpiredIdFromResponseBody();
    private String prescriptionDeletedId = prescriptionsDeleteTest.sharePrescriptionDeletedIdFromResponseBody();

    @Given("^Doctor selects patient John Smith$")
    public void doctorSelectsPatientJohnSmith(){
        response = httpMethods.getRequest(realPatientsUrl + "/" + patientId);
        response.then().statusCode(SC_OK);
        response.then().body("prescriptions", everyItem(is(not(empty()))));
    }

    @When("^Doctor selects (.*) prescription and click Remove button$")
    public void doctorSelectsStatePrescriptionAndClickRemoveButton(String state) {
        if("DRAFT".equals(state)){
            response = httpMethods.deleteRequest(realPrescriptionsUrl + "/" + prescriptionDraftId);
        }

        if("ISSUED".equals(state)){
            response = httpMethods.deleteRequest(realPrescriptionsUrl + "/" + prescriptionIssuedId);
        }

        if("EXPIRED".equals(state)){
            response = httpMethods.deleteRequest(realPrescriptionsUrl + "/" + prescriptionExpiredId);
        }

    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor tries remove all prescriptions$")
    public void doctorTriesRemoveAllPrescriptions() {
        response = httpMethods.deleteRequest(realPrescriptionsUrl);
    }

    @When("^Doctor tries remove of prescription with not existing ID$")
    public void doctorTriesRemoveOfPrescriptionWithNotExistingID() {
        response = httpMethods.deleteRequest(realPrescriptionsUrl + "/not-found-" + LocalDateTime.now());
    }

    @When("^Doctor tries remove deleted prescription$")
    public void doctorTriesRemoveDeletedPrescription() {
        response = httpMethods.deleteRequest(realPrescriptionsUrl + "/" + prescriptionDeletedId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @When("^Doctor tries delete selected prescription with timeout$")
    public void doctorTriesDeleteSelectedPrescriptionWithTimeout() {

        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));

        try {

            // TODO: raczej timeout bedzie problemowy do testow, bo i tak odpowie serwer, bo to juz nie jest stub, w ktorym mamy zaszyta informacje ze ma odpowiedziec po paru seknudach

            response = httpMethods.deleteRequest(realPrescriptionsUrl + "/" + prescriptionDraftId); // TODO zamienic na deleted

        } catch (Exception e){
            actualException = e;
        } finally {
            RestAssured.reset();
        }
    }


}
