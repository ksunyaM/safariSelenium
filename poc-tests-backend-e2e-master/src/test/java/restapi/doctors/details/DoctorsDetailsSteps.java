package restapi.doctors.details;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.Assert;
import restapi.HttpMethods;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class DoctorsDetailsSteps {

    private Response response;
    private HttpMethods httpMethods = new HttpMethods();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private ApiUtils apiUtils = new ApiUtils();
    private String realDoctorsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DOCTORS);
    private DoctorsDetailsTest doctorsDetailsTest = new DoctorsDetailsTest();
    private List<String> doctorsIds = doctorsDetailsTest.shareDoctorsIds();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForCreateDoctor = rootPath + "features\\doctors\\create\\createDoctorRequestBody.json";

    @Given("^The Administrator login to the system$")
    public void theAdministratorLoginToTheSystem() {
        httpMethods.getRequest(realDoctorsUrl).then().statusCode(SC_OK);
    }

    @Given("^The Administrator opens Doctors Management Module$")
    @When("^The Administrator opens Doctors Management View$")
    public void theAdministratorOpensDoctorManagementView() {
        for (int i = 0; i < doctorsIds.size(); i++) {
            response = httpMethods.getRequest(realDoctorsUrl + "/" + doctorsIds.get(i));
            response.then().statusCode(SC_OK);
        }
    }

    @Then("^The Administrator sees list of all doctors$")
    public void theAdministratorSeesListOfAllDoctors() {
        for (int i = 0; i < doctorsIds.size(); i++) {
            httpMethods.getRequest(realDoctorsUrl + "/" + doctorsIds.get(i)).
                    then().body("id", containsString(doctorsIds.get(i)));
        }
    }

    @When("^The Administrator selects Doctor 1$")
    public void theAdministratorSelectsDoctor() {

        response = httpMethods.getRequest(realDoctorsUrl + "/" + doctorsIds.get(0));
        response.then().statusCode(SC_OK);
    }

    @Then("^The Administrator sees all details of Doctor 1$")
    public void theAdministratorSeesAllDetailsOfDoctor() {

        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDoctor);
        jsonObject.remove("surname");
        jsonObject.put("surname", "Doctor 1");

        Assert.assertEquals(doctorsIds.get(0), response.then().extract().path("id").toString());
        Assert.assertEquals(jsonObject.get("name").toString(), response.then().extract().path("name").toString());
        Assert.assertEquals(jsonObject.get("surname").toString(), response.then().extract().path("surname").toString());
        Assert.assertEquals(jsonObject.get("email").toString(), response.then().extract().path("email").toString());
        //Assert.assertEquals(jsonObject.get("specializations").toString(), response.then().extract().path("specializations").toString());
    }

    @When("^The Administrator tries open not existing doctor$")
    public void theAdministratorTriesOpenNotExistingDoctor() {
        response = httpMethods.getRequest(realDoctorsUrl + "/not-found-" + LocalDateTime.now());
        response.getBody().prettyPrint();
    }

    @Then("^System informs administrator about error: (\\d+) (.*)$")
    public void systemInformsAdministratorAboutErrorNotFound(int errorCode, String errorMessage) {
        //response.then().statusLine(containsString(errorMessage));
        response.then().statusCode(errorCode);
        //Assert.assertEquals(errorMessage, response.then().extract().body().path("error"));
    }

}
