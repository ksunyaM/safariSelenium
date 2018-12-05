package restapi.patients.delete;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import restapi.HttpMethods;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static wiremock.org.apache.http.HttpStatus.*;

@Slf4j
public class PatientsDeleteSteps {

    private Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private String realPatientsUrl= apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private PatientsDeleteTest patientsDeleteTest = new PatientsDeleteTest();
    private List<String> patientsIds = patientsDeleteTest.sharePatientsIds();
    private String unDeletedPatientId = patientsIds.get(0);
    private String deletedPatientId = patientsIds.get(1);

    @Given("^Doctor opens Patients Management View$")
    public void doctorOpensPatientsManagementView() {
        httpMethods.getRequest(realPatientsUrl).then().statusCode(SC_OK);
    }

    @When("^Doctor selects undeleted Patient and click Delete button$")
    public void doctorSelectsUnDeletedPatientAndClickDeleteButton() {
        response = httpMethods.deleteRequest(realPatientsUrl + "/" + unDeletedPatientId);
    }

    @Then("^Doctor checks Patient is deleted and cannot get his details information$")
    public void doctorChecksPatientIsDeletedAndCannotGetHisDetailsInformation() {
        response.then().statusCode(SC_NO_CONTENT);
        httpMethods.getRequest(realPatientsUrl + "/" + unDeletedPatientId).then().statusCode(SC_NOT_FOUND);
    }

    @When("^Doctor tries delete deleted Patient$")
    public void doctorTriesDeleteDeletedPatient() {
        response = httpMethods.deleteRequest(realPatientsUrl + "/" + deletedPatientId);
    }

    @Then("^System informs doctor about error: (\\d+) (.*)$")
    public void systemInformsDoctorAboutError(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
    }

    @When("^Doctor tries delete not existing Patient$")
    public void doctorTriesDeleteNotExistingPatient() {
        response = httpMethods.deleteRequest(realPatientsUrl + "/not-found-" + LocalDateTime.now());
    }

    @When("^Doctor tries delete all patients$")
    public void doctorTriesDeleteAllPatients() {
        response = httpMethods.deleteRequest(realPatientsUrl);
    }

}
