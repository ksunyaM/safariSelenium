package restapi.drugs.delete;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.junit.Assert;
import restapi.HttpMethods;
import restapi.drugs.DrugsBase;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.*;

public class DrugsDeleteSteps {

    private String drugId;
    private Response response;
    private DrugsBase drugsBase = new DrugsBase();
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private String realDrugsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DRUGS);
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForCreatePenicillinDrug = rootPath + "features\\drugs\\create\\createDrugRequestBodyPenicillin.json";
    //For test delete
    private String noExistDrug = "66666666-6666-6666-404b-271cd4b66f90";

    @Given("^Drug admin logged to his application module page$")
    public void drugAdminLoggedToHisApplicationModulePage() {
        httpMethods.getRequest(realDrugsUrl).then().statusCode(SC_OK);
    }

    @When("^Drug admin selects/create drug to delete$")
    public void drugAdminSelectsCreateDrugToDelete() {
        response = drugsBase.postRequestCreateDrug(filePathForCreatePenicillinDrug);
        response.then().statusCode(SC_CREATED);
        drugId = drugsBase.resposeDrugIdAfterPostCreateDrug(response);
    }

    @Then("^The drug is delete from the system$")
    public void theDrugIsDeleteFromTheSystem() {
        httpMethods.deleteRequest(realDrugsUrl + "/" + drugId);
    }

    @And("^Check drug exist in system$")
    public void checkDrugExistInSystem() {
        response = httpMethods.getRequest(realDrugsUrl + "/" + drugId);
        response.then().statusCode(SC_NOT_FOUND);
        String message = response.body().jsonPath().get("message").toString();

        Assert.assertEquals("drug with given id not found", message);
    }

    @Given("^Drug admin selects deleted drug$")
    public void drugAdminSelectsDeletedDrug() {
        httpMethods.getRequest(realDrugsUrl + "/" + noExistDrug).then().statusCode(SC_NOT_FOUND);
    }

    @When("^Drug admin tries to delete deleted drug$")
    public void drugAdminTriesToDeleteDeletedDrug() {
        response = httpMethods.deleteRequest(realDrugsUrl + "/" + noExistDrug);
    }

    @Then("^System informs admin gracefully about error (\\d+) Not Found$")
    public void systemInformsAdminGracefullyAboutErrorNotFound(int arg0) {
        response.then().statusCode(SC_NOT_FOUND);
        String message = response.body().jsonPath().get("error").toString();

        Assert.assertEquals("Not Found", message);
    }

    @When("^Drug admin tries to delete all drugs$")
    public void drugAdminTriesToDeleteAllDrugs() {
        response = httpMethods.deleteRequest(realDrugsUrl);
    }

    @Then("^System informs admin gracefully about error (\\d+) method not allowed$")
    public void systemInformsAdminGracefullyAboutErrorMethodNotAllowed(int arg0) {
        response.then().statusCode(SC_METHOD_NOT_ALLOWED);
        String message = response.body().jsonPath().get("error").toString();

        Assert.assertEquals("Method Not Allowed", message);
    }
}
