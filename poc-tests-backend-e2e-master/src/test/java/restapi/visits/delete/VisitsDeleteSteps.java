package restapi.visits.delete;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;

import static io.restassured.RestAssured.given;

@Slf4j
public class VisitsDeleteSteps {

    private RequestSpecification request = given();
    private RestApiProvider provider = new RestApiProvider();
    private StubProvider stubUrl = new StubProvider();
    private String url = stubUrl.setupBaseUrlWithPort(provider);
    private ApiUtils apiUtils = new ApiUtils();
    private String realUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);

    @Rule
    WireMockRule wireMockRule = new WireMockRule();

     @Given("^Doctor logged to his application module page$")
    public void doctorLoggedToHisApplicationModulePage() {
        System.out.println("Test in TDD methodology");
    }

    @When("^Doctor selects visit to delete$")
    public void doctorSelectsVisitToDelete() {
        System.out.println("Test in TDD methodology");
    }

    @Then("^Doctor checks that the visit is not deleted$")
    public void doctorChecksThatTheVisitIsNotDeleted() {
        System.out.println("Test in TDD methodology");
    }

//    @When("^Doctor deletes the patient with id: (\\d+)$")
//    public void doctorDeletesThePatientWithId(String patientId) {
//        request.delete(url + "/patients/delete/" + patientId).then().statusCode(SC_OK);
//    }
//
//    @Then("^Doctor checked patient with id: (\\d+) does not exist$")
//    public void doctorCheckedPatientWithIdDoesNotExist(String patientId) {
//        request.get(url + "/patients/" + patientId).then().statusCode(SC_NOT_FOUND);
//    }
//
//    @When("^A doctor delete all patients$")
//    public void aDoctorDeleteAllPatients() {
//        request.delete(url + "/patients/delete").then().statusCode(SC_OK);
//    }
//
//    @Then("^All patients are deleted$")
//    public void allPatientsAreDeleted() {
//        request.get(url + "/patients/123").then().statusCode(SC_NOT_FOUND);
//    }
//
//    ///////////////////// REAL SERVICE ////////////
//
//    @Given("^RS Doctor sets wrong patient data$")
//    public void doctorSetsWrongPatientDataRealService() {
//        String bodyForRequestPost = "{\"id\":\"TD:123\",\"name\":\"Jan Kowalski\",\"dateOfBirth\":\"01.09.2000\",\"email\":\"jkowalski@test.pl\",\"gender\":\"M\",\"country\":\"Poland\",\"phone\":\"123-456-789\",\"note\":\"add user\",\"avatar\": null,\"prescriptions\": []}";
//        RestAssured.given().header("Content-Type", "application/json")
//                .body(bodyForRequestPost)
//                .post(realUrl).then().statusCode(SC_ACCEPTED);
//    }
//
//    @When("^RS Doctor deletes the patient with id (.*)$")
//    public void doctorDeletesThePatientWithIdRealService(String patientId) {
//        request.delete(realUrl + "/" + patientId).then().statusCode(SC_ACCEPTED);
//    }
//
//    @Then("^RS Doctor checked patient with id (.*) does not exist$")
//    public void doctorCheckedPatientWithIdDoesNotExistRealService(String patientId) {
//        request.get(realUrl + "/" + patientId).then().statusCode(SC_NOT_FOUND);
//    }

}
