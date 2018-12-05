package restapi.patients.details;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import restapi.HttpMethods;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class PatientsDetailsSteps {

    private Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private String realPatientsUrl= apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private PatientsDetailsTest patientsDetailsTest = new PatientsDetailsTest();
    private List<String> patientsIds = patientsDetailsTest.sharePatientsIds();
    private String patientIdWithoutPrescriptions = patientsIds.get(0);
    private String patientIdWithPrescriptions = patientsIds.get(1);
    private String deletedPatientId = patientsIds.get(2);

    @Given("^Doctor opens Patients Management View$")
    public void doctorOpensPatientsManagementView() {
        response = httpMethods.getRequest(realPatientsUrl);
        response.then().statusCode(SC_OK);
        response.then().body("id", hasItem(patientIdWithoutPrescriptions));
        response.then().body("id", hasItem(patientIdWithPrescriptions));
    }

    @When("^Doctor selects patient without prescriptions$")
    public void doctorSelectsPatientWithoutPrescriptions() {
        response = httpMethods.getRequest(realPatientsUrl + "/" + patientIdWithoutPrescriptions);
    }

    @Then("^Doctor checks that patient exists and has not prescriptions$")
    public void doctorChecksThatPatientExistsAndHasNotPrescriptions() {
        response.then().statusCode(SC_OK);
        response.then().body("prescriptions", hasSize(0));
    }

    @When("^Doctor selects patient with prescriptions$")
    public void doctorSelectsPatientWithPrescriptions() {
        response = httpMethods.getRequest(realPatientsUrl + "/" + patientIdWithPrescriptions);
    }

    @Then("^Doctor checks that patient exists and has prescriptions$")
    public void doctorChecksThatPatientExistsAndHasPrescriptions() {
        response.then().statusCode(SC_OK);
        response.then().body("prescriptions", hasItem(is(not(empty()))));
        response.then().body("prescriptions.state",hasItem("DRAFT"));
        response.then().body("prescriptions.state",hasItem("ISSUED"));
        response.then().body("prescriptions.state",hasItem("EXPIRED"));
    }

    @When("^Doctor tries get details of does not exist patient$")
    public void doctorTriesGetDetailsOfDoesNotExistPatient() {
        response = httpMethods.getRequest(realPatientsUrl + "/not-found-" + LocalDateTime.now());
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor tries get details of deleted patient$")
    public void doctorTriesGetDetailsOfDeletedPatient() {
        response = httpMethods.getRequest(realPatientsUrl + "/" + deletedPatientId);
    }

}
