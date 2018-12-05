package restapi.visits.create;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.HttpMethodsType;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import restapi.HttpMethods;
import services.Doctor;
import services.Patient;
import services.Visit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static wiremock.org.apache.http.HttpStatus.SC_OK;


/**
 * @author Katarzyna Migacz 05.10.2018
 */


@Slf4j
public class PatientVisitCreateSteps {
    private HttpMethods httpMethods = new HttpMethods();
    private RestApiProvider provider = new RestApiProvider();
    private StubProvider stub = new StubProvider();
    private String fakeEndpoint = "http://localhost:8080";
    private Visit visit = new Visit();
    @Rule
    public WireMockRule service = new WireMockRule();


    @Given("^Patient creates a new account and sees form to fill in to set up a new visit$")
    public void patientCreatesANewAccountAndSeesFormToFillInToSetUpANewVisit() {
//        stub endpoint
        Visit visit = new Visit();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        patient.setEmail("e-mail");
        patient.setPassword("password");
        doctor.setDoctorLastNameAttribute("userId");
        visit.setPatientAttribute(patient);
        visit.setDoctorAttribute(doctor);
        visit.setDateAttribute("date");
        provider.setBaseUrlWithPort("http://localhost:8080");
        provider.setContentTypeHeader("application/json");
        provider.setContentType("Content-Type");
        String body = visit.mappingObjectToJsonBuilder();
        provider.setBody(body);

        stub.filterByHttpMethodsForMockedData(HttpMethodsType.POST, service, provider, SC_OK);
        System.out.println("Test in TDD");
    }

    @When("^Patient fill in form to set up a new visit$")
    public void patientFillInFormToSetUpANewVisit() {
        given().body(provider.getBody())
                .post(provider.getBaseUrlWithPort() + "/visits");
    }


    @Then("^New visit is created$")
    public void newVisitIsCreated() {
        given().then().statusCode(202);


    }

    @Given("^Patient <accountOwnership> and fill in form to set up the visit scope$")
    public void patientAccountOwnershipAndFillInFormToSetUpTheVisitScope() throws Throwable {


    }

    @When("^Patient chooses available date and doctors propositions appear according to place of <location>$")
    public void patientChoosesAvailableDateAndDoctorsPropositionsAppearAccordingToPlaceOfLocation() {
        System.out.println("StubTest");
    }

    @Then("^Patient selects date and available doctor and confirm the visit date$")
    public void patientSelectsDateAndAvailableDoctorAndConfirmTheVisitDate() {
        System.out.println("StubTest");
    }


    @Given("^Patient creates a new account$")
    public void patientCreatesANewAccount() {
        //request for the page
        given().
                get(fakeEndpoint).
                then().
                assertThat().
                statusCode(200);
        //post login data
        String patientDataTestFromJson = visit.getDefaultVisit();
        given().body(patientDataTestFromJson)
                .post(fakeEndpoint);


        System.out.println("StubTest");
    }

    @When("^Patient try to leave empty fields <field>$")
    public void patientTryToLeaveEmptyFieldsField() {

        System.out.println("StubTest");
    }

    @Then("^Patient cannot confirm his form$")
    public void patientCannotConfirmHisForm() {

        System.out.println("StubTest");
    }


}
