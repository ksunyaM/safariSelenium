package restapi.search.patientSearch;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import restapi.HttpMethods;


import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class PatientsSearchSteps {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private RestApiProvider provider = new RestApiProvider();
    private StubProvider stubUrl = new StubProvider();
    private String url = stubUrl.setupBaseUrlWithPort(provider);
    private ApiUtils apiUtils = new ApiUtils();
    private String realUrl = apiUtils.getApiUrl(ApiMicroservicesType.SEARCH);


    @When("^A Doctor log in to the main page$")
    public void doctorLogInToTheMainPage() {
        response = httpMethods.getRequest(url + "/search");
    }

    @Then("^Response Header Data for (.*) Should Be (.*)$")
    public void responseShouldBeCorrect(String headerName, String expectedName) {
        response.then().assertThat().contentType(ContentType.JSON).statusCode(HttpStatus.SC_OK).header(headerName, equalTo(expectedName));
    }

    @When("^A Doctor looks for patients by name beginning with (.*) and type as a (.*)$")
    public void doctorLooksForPatientsWithSpecificNameAndType(String searchTerm, String type) {
        response = httpMethods.getRequest(url + "/search" + "?q=" + searchTerm + "&type=" + type);
    }

    @Then("^A Doctor receives all patients with (.*) (.*), Rob\\*ert, Rob\\*erta, Rob\\*in$")
    public void doctorReceivesAllPatientsWithSpecificName(String name, String startWith) {
        response.then().assertThat().body(name, everyItem(startsWith(startWith)));
    }

    @When("^A Doctor looks for the patients by name$")
    public void doctorLooksForThePatientsByName() {
        response = httpMethods.getRequest(url + "/search");
        response.then().statusCode(HttpStatus.SC_OK).contentType(ContentType.JSON);
    }

    @Then("^A Doctor doesn't receive a response during (.*) milliseconds from the server with the list of patients$")
    public void doctorDoesNotReceiveResponseFromTheServer(long timeOut) {
        response.then().assertThat().time(lessThan(timeOut));
    }

    @When("^A Doctor looks for the patients by name (.*) and type (.*) which does not exist in the system$")
    public void doctorLooksForThePatientsByNameWhichIsNotInTheSystem(String searchTerm, String type) {
        response = httpMethods.getRequest(url + "/search" + "?q=" + searchTerm + "&type=" + type);
    }

    @Then("^A Doctor receives blank results$")
    public void doctorReceivesBlankResults() {
        response.then().assertThat().body("name", isEmptyString());
    }

    @Given("^A Doctor gets information about patient$")
    public void doctorGetsInformationAboutPatientWithId() {
        response = httpMethods.getRequest(url + "/search/info");
    }

    @When("^A Doctor receives information about patient\\(s\\)$")
    public void doctorReceivesInformationAboutPatient() {
        response.then().statusCode(SC_OK);
    }

    @Then("^A Doctor checks data (.*) and (.*) are correct$")
    public void doctorChecksDataAreCorrect(String fieldName, String value) {
        String responseValue = response.then().extract().response().path(fieldName);
        log.info("Value \"" + value + "\" in field \"" + fieldName + "\" - from response is: " + responseValue);
        Assert.assertEquals(value, responseValue);
    }

    ///////////////////// REAL DATA ////////////////////////

    @When("^RD a Doctor looks for patients by name beginning with (.*) and type as a (.*)$")
    public void rdDoctorLooksForPatientsWithSpecificNameAndType(String searchTerm, String type) {
        String q = "?q=";
        String typeLink = "&type=";
        response = httpMethods.getRequest(realUrl + q + searchTerm + typeLink + type);
    }

    @Then("^RD a Doctor receives all patients with (.*) (.*), Jan\\*ert, Jan\\*erta, Jan\\*in$")
    public void rdDoctorReceivesAllPatientsWithSpecificName(String name, String startWith) {
//        response.then().assertThat().body(name, everyItem(startsWith(startWith)));
        List<String> names = response.then().extract().path(name);
        assertTrue(names.stream().allMatch(s -> s.toLowerCase().startsWith(startWith.toLowerCase())));

    }

    @Given("^RD a Doctor gets information about (.*) - (.*)")
    public void rdDoctorGetsInformationAboutPatientWithId(String type, String searchTerm) {
        String q = "?q=";
        String typeLink = "&type=";
        response = httpMethods.getRequest(realUrl + q + searchTerm + typeLink + type);
    }

    @When("^RD a Doctor receives information about patient\\(s\\)$")
    public void rdDoctorReceivesInformationAboutPatient() {
        response.then().contentType(ContentType.JSON).statusCode(SC_OK);
    }

    @Then("^RD a Doctor checks data (.*) and (.*) are correct$")
    public void rdDoctorChecksDataAreCorrect(String fieldName, String value) {
        List<String> name = response.then().extract().path(fieldName);
        assertThat(name, contains(value));
    }

    @When("^RD a Doctor looks for the patients by name (.*) and type (.*) which does not exist in the system$")
    public void rdADoctorLooksForThePatientsByNameSherlokAndTypePatientWhichDoesNotExistInTheSystem(String searchTerm, String type) {
        String q = "?q=";
        String typeLink = "&type=";
        response = httpMethods.getRequest(realUrl + q + searchTerm + typeLink + type);
    }

    @Then("^RD a Doctor receives blank results$")
    public void rdADoctorReceivesBlankResults() {
        String pathName = "name";
        List<String> name = response.then().extract().response().path(pathName);
        assertEquals(0, name.size());
    }
}
