package restapi.visits.details;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import restapi.HttpMethods;
import services.Visit;

import static wiremock.org.apache.http.HttpStatus.SC_CREATED;

@Slf4j
public class VisitsDetailsSteps {

    //    private RequestSpecification request = given();
//    private RestApiProvider provider = new RestApiProvider();
//    private StubProvider stubUrl = new StubProvider();
//    private String url = stubUrl.setupBaseUrlWithPort(provider);
//    private ApiUtils apiUtils = new ApiUtils();
//    private String realUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private HttpMethods httpMethods = new HttpMethods();
    private RestApiProvider provider = new RestApiProvider();
    private StubProvider stub = new StubProvider();
    private String bodyBuildedFromObject;


    @Rule
    WireMockRule wireMockRule = new WireMockRule();


    @Given("^Doctor log in to his account$")
    public void doctorLogInToHisAccount() {
        //stub endpoint
//        log.info("Setup Stub for PatientsCreateTest -> StubForCreatePatient and start server");
//        wireMockRule.start();
//        provider.setContentTypeHeader("Content-Type");
//        provider.setContentType("application/json");
        Visit visit = new Visit();
//        String bodyForRequestPost = visit.setJsonTestDataForVisit();
//        wireMockRule.stubFor(post("/visits/create")
//                .withRequestBody(equalToJson(bodyForRequestPost))
//                .willReturn(aResponse()
//                        .withStatus(HttpStatus.SC_CREATED)
//                        .withBody(bodyForRequestPost) //bodyforrequest
//                        .withHeader(provider.getContentTypeHeader(), provider.getContentType())));


        //test
        Response response = RestAssured.given().header("Content-Type", "application/json")
                .body(visit.getDefaultVisit())
                .post("http://localhost:8080/visits/create");
        response.then().statusCode(SC_CREATED);
    }

    @When("^Doctor gets information about visit's details$")
    public void doctorGetsInformationAboutVisitSDetails() {
        System.out.println("Test in TDD");
    }

    @Then("^Doctor checks and confirm the <visit> status$")
    public void doctorChecksAndConfirmTheVisitStatus() {
        System.out.println("Test in TDD");
    }

    @Given("^Doctor opened his calendar with visits details$")
    public void doctorOpenedHisCalendarWithVisitsDetails() {
        System.out.println("Test in TDD");
    }

    @When("^Doctor opens referrals$")
    public void doctorOpensReferrals() {
        System.out.println("Test in TDD");
    }

    @Then("^Doctor checks that all referrals exists <type>$")
    public void doctorChecksThatAllReferralsExistsType() {
        System.out.println("Test in TDD");
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
