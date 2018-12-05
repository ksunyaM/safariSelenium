package restapi.doctors.delete;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.RestApiProvider;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import restapi.HttpMethods;

import java.util.List;

import static io.restassured.RestAssured.given;
import static wiremock.org.apache.http.HttpStatus.SC_NOT_FOUND;
import static wiremock.org.apache.http.HttpStatus.SC_NO_CONTENT;

@Slf4j
public class DoctorsDeleteSteps {

    private static Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private RequestSpecification request = given();
    private RestApiProvider provider = new RestApiProvider();
    private String realDoctorsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DOCTORS);
    private DoctorsDeleteTest doctorsDeleteTest = new DoctorsDeleteTest();
    private List<String> doctorsIds = doctorsDeleteTest.shareDoctorsIds();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();


    @Given("^The Administrator sets wrong Doctor data$")
    @When("^The Administrator deletes the Doctor$")
    public void theAdministratorDeletesTheDoctorWithId() {
        for (int i = 0; i < doctorsIds.size(); i++) {
            response = httpMethods.deleteRequest(realDoctorsUrl + "/" + doctorsIds.get(i));
            response.then().statusCode(SC_NO_CONTENT);
        }
    }

    @Then("^The Administrator checks that the deleted Doctor is not found")
    public void theAdministratorChecksThatTheDoctorWithIdIsNotExist() {
        for (int i = 0; i < doctorsIds.size(); i++) {
            response = httpMethods.getRequest(realDoctorsUrl + "/" + doctorsIds.get(i));
            response.then().statusCode(SC_NOT_FOUND);
        }
    }

    @When("^The Administrator tries to delete the doctor that was already deleted$")
    @Then("^System informs the Administrator that operation cannot be performed$")
    public void systemInformsTheAdministratorThatOperationCannotBePerformed() {
        for (int i = 0; i < doctorsIds.size(); i++) {
            response = httpMethods.deleteRequest(realDoctorsUrl + "/" + doctorsIds.get(i));
            response.then().statusCode(SC_NO_CONTENT);
        }
    }
/*
    @Given("^The Administrator selects all the doctors$")
    public void theAdministratorSelectsAllTheDoctors() {
        response = httpMethods.getRequest(realDoctorsUrl);
        response.then().statusCode(SC_OK);
    }

    @When("^The Administrator tries to remove all doctors$")
    public void theAdministratorTriesToRemoveAllDoctors() {
        response = httpMethods.deleteRequest(realDoctorsUrl);
    }

    @Then("^System informs the Administrator about error: (.*) (.*)$")
    public void systemInformsTheAdministratorAboutErrorMethodNotAllowed(int errorCode, String errorMessage) {
        response.then().statusCode(errorCode);
        response.then().statusLine(containsString(errorMessage));
    }*/
}
