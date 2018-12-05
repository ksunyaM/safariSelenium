package restapi.visits.update;

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
public class VisitsUpdateSteps {

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


    @Given("^Doctor select patient with visit status <visit>$")
    public void doctorSelectPatientWithVisitStatusVisit() {
        System.out.println("Test in TDD");
    }

    @When("^Doctor selects the visit and update all data$")
    public void doctorSelectsTheVisitAndUpdateAllData() {
        System.out.println("Test in TDD");
    }

    @Then("^Doctor checks that visit is updated$")
    public void doctorChecksThatVisitIsUpdated() {
        System.out.println("Test in TDD");
    }

    @Given("^Doctor select patient with <visit> visit$")
    public void doctorSelectPatientWithVisitVisit() {
        System.out.println("Test in TDD");
    }

    @When("^Doctor updates selected data <data> with <value>$")
    public void doctorUpdatesSelectedDataDataWithValue() {
        System.out.println("Test in TDD");
    }

    @Then("^Doctor checks visit is updated$")
    public void doctorChecksVisitIsUpdated() {
        System.out.println("Test in TDD");
    }

    @Given("^Doctor select visit with status <visit>$")
    public void doctorSelectVisitWithStatusVisit() {
        System.out.println("Test in TDD");
    }

    @When("^Doctor update state to <status>$")
    public void doctorUpdateStateToStatus() {
        System.out.println("Test in TDD");
    }

    @Then("^Doctor checks visit status is updated properly$")
    public void doctorChecksVisitStatusIsUpdatedProperly() {
        System.out.println("Test in TDD");
    }
}
