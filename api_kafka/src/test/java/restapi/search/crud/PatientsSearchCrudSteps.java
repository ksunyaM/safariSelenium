package restapi.search.crud;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import restapi.HttpMethods;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PatientsSearchCrudSteps {

    private Response response;
    private Response responsejan;
    private Response responseJAN;
    private Response responsejAn;
    private Response responsejAN;
    private Response responseJan;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private String realSearchUrl = apiUtils.getApiUrl(ApiMicroservicesType.SEARCH);
    private String type = "&type=patient";

    @Given("^Doctor login to system to search patient$")
    public void doctorLoginToSystemToSearchPatient() {
        httpMethods.getRequest(realPatientsUrl).then().statusCode(SC_OK);
    }

    @When("^Doctor types phrase (.*) and click Search button$")
    public void doctorTypesPhrasePhraseAndClickSearchButton(String phrase) {
        response = httpMethods.getRequest(realSearchUrl + "?q=" + phrase + type);
        response.then().statusCode(SC_OK);
    }

    @Then("^Doctor sees patient or patients contains searched phrase (.*)$")
    public void doctorSeesPatientOrPatientsContainsSearchedPhrasePhrase(String phrase) {

        List<String> names = response.then().extract().body().path("name");

        for(int i = 0; i < names.size(); i++){
            assertTrue(names.get(i).toLowerCase().contains(phrase.toLowerCase()));
        }
    }

    @When("^Doctor verified search with phrases: jan, JAN, jAn, jAN, Jan$")
    public void doctorVerifiedSearchWithPhrasesJanJANJAnJANJan() {

        responsejan = httpMethods.getRequest(realSearchUrl + "?q=jan" + type);
        responseJAN = httpMethods.getRequest(realSearchUrl + "?q=JAN" + type);
        responsejAn = httpMethods.getRequest(realSearchUrl + "?q=jAn" + type);
        responsejAN = httpMethods.getRequest(realSearchUrl + "?q=jAN" + type);
        responseJan = httpMethods.getRequest(realSearchUrl + "?q=Jan" + type);
        responsejan.then().statusCode(SC_OK);
        responseJAN.then().statusCode(SC_OK);
        responsejAn.then().statusCode(SC_OK);
        responsejAN.then().statusCode(SC_OK);
        responseJan.then().statusCode(SC_OK);
    }

    @Then("^Doctor sees the same results for each phrase$")
    public void doctorSeesTheSameResultsForEachPhrase() {
        List<String> namesjan = responsejan.then().extract().body().path("name");
        List<String> namesJAN = responseJAN.then().extract().body().path("name");
        List<String> namesjAn = responsejAn.then().extract().body().path("name");
        List<String> namesjAN = responsejAN.then().extract().body().path("name");
        List<String> namesJan = responseJan.then().extract().body().path("name");

        assertEquals(namesjan.size(),namesJAN.size());
        assertEquals(namesjan.size(),namesjAn.size());
        assertEquals(namesjan.size(),namesjAN.size());
        assertEquals(namesjan.size(),namesJan.size());
    }

    @Then("^Doctor sees empty results$")
    public void doctorSeesEmptyResults() {
        response.then().body("",hasSize(0));
    }

    @When("^Developer does not put all parameters in search$")
    public void developerDoesNotPutAllParametersInSearch() {
        response = httpMethods.getRequest(realSearchUrl + "?q=test");
    }

    @Then("^System returns error: (\\d+) (.*)$")
    public void systemReturnsErrorBadRequest(int errorCode, String errorMessage)  {
        response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
        assertEquals(errorMessage,response.then().extract().body().path("error"));
    }

    @Then("^System does not return deleted user contains (.*)$")
    public void systemDoesNotReturnDeletedUserContainsDeletedUser(String phrase) {

        List<String> names = response.then().extract().body().path("name");

        for(int i = 0; i < names.size(); i++){
            assertFalse(names.get(i).toLowerCase().contains(phrase.toLowerCase()));
        }
    }

}
