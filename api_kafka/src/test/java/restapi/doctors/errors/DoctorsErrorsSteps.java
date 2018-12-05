package restapi.doctors.errors;

import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import restapi.HttpMethods;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.RestAssuredConfig.newConfig;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.junit.Assert.assertEquals;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class DoctorsErrorsSteps {
    private Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private Exception actualException;
    private RestApiProvider provider = new RestApiProvider();
    private StubProvider stubUrl = new StubProvider();
    private String url = stubUrl.setupBaseUrlWithPort(provider);

    @When("^The Administrator requests for (.*) response$")
    public void theAdministratorRequestsForEmptyResponse(String responseType){
        try {
            response = httpMethods.getRequest(url + "/doctors/" + responseType);
        }
        catch (Exception e) {
            actualException = e;
        }
    }
    @When("^The Administrator requests a response with timeout$")
    public void theAdministratorIsAskingForResponseWithTimeout() {
        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));
        try {
            response = httpMethods.getRequest(url + "/doctors/timeout");
        } catch (Exception e){
            actualException = e;
        } finally {
            RestAssured.reset();
        }
    }

    @Then("^The Administrator receives an exception: (.*)$")
    public void theAdministratorReceivesAnException(String exception){
        assertEquals(exception,actualException.getClass().getSimpleName());
    }

    @When("^The Administrator requests the Doctors and receives 503 for response$")
    public void theAdministratorRequestsTheDoctorAndReceives503ForResponse() {
        response = httpMethods.getRequest(url + "/doctors/retry");
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Then("^The Administrator is retrying to get a response with code 200")
    public void theAdministratorIsRetryingToGetAResponseWithCode200() {
        response = httpMethods.getRequest(url + "/doctors/retry");
        assertEquals(SC_OK, response.getStatusCode());
    }

    @When("^The Administrator requests for post response with timeout$")
    public void theAdministratorRequestsForPostResponseWithTimeout() {
        RestAssured.config = newConfig()
                .httpClient(httpClientConfig()
                        .setParam("http.socket.timeout", 2000));
        try {
            response = httpMethods.postRequest(url + "/doctors/create-timeout","{}");
        } catch (Exception e){
            actualException = e;
        } finally {
            RestAssured.reset();
        }
    }
}
