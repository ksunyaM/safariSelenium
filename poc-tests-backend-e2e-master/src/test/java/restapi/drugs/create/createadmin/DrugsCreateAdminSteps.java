package restapi.drugs.create.createadmin;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import restapi.HttpMethods;
import restapi.drugs.DrugsBase;
import services.Drug;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

public class DrugsCreateAdminSteps {

    private Response response;
    private String drugId;
    private Drug drugService = new Drug();
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private String realDrugsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DRUGS);
    private DrugsBase drugsBase = new DrugsBase();


    @Given("^Drug admin log in to the drug's module$")
    public void drugAdminLogInToTheDrugSModule() {
        httpMethods.getRequest(realDrugsUrl).then().statusCode(SC_OK);
    }

    @When("^Drug admin add the introduced (.*) with drugCode (.*) in (.*) with default dosage (.*) and (.*)$")
    public void drugAdminAddTheIntroducedDrugNameCodeCodeInFormWithDefaultDosageDosageAndComment(String drug, String drugCode, String dosageForm, String dosage, String comment) {
        drugService.setParticularDrugForTest(drug, drugCode, dosageForm, dosage, comment);
        response = httpMethods.postRequest(realDrugsUrl, drugService.mappingJsonCreateDrug(drugService));
        response.then().statusCode(SC_CREATED);
        drugId = drugsBase.resposeDrugIdAfterPostCreateDrug(response);
    }

    @Then("^Drug admin checks the drug created$")
    public void drugAdminChecksTheDrugCreated() {
        response = httpMethods.getRequest(realDrugsUrl + "/" + drugId);
        response.then().statusCode(SC_OK);
        String getDrugId = response.body().jsonPath().get("id").toString();

        Assert.assertEquals(drugId, getDrugId);
    }

    @After
    public void ClearDrug() {
        httpMethods.deleteRequest(realDrugsUrl + "/" + drugId);
    }

}
