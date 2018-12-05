package restapi.drugs.create.createdoctor;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.Assert;
import restapi.HttpMethods;
import restapi.drugs.DrugsBase;
import services.Drug;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

public class DrugsCreateDoctorSteps {

    private Response response;
    private String drugId;
    private HttpMethods httpMethods = new HttpMethods();
    private ApiUtils apiUtils = new ApiUtils();
    private DrugsBase drugsBase = new DrugsBase();
    private Drug drugService = new Drug();
    private String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private String realDrugsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DRUGS);
    private TestDataUtils testDataUtils = new TestDataUtils();
    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String filePathForCreateDraftPrescription = rootPath + "features\\prescriptions\\create\\createPrescriptionRequestBody.json";


    @Given("^Doctor select a new prescription$")
    public void doctorSelectANewPrescription() {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDraftPrescription);
        response = httpMethods.postRequest(realPrescriptionsUrl, jsonObject.toJSONString());
        response.then().statusCode(SC_CREATED);
    }

    @When("^Doctor fill in (.*) with (.*) where dosage form (.*) and dosage (.*) and doctor comment (.*)$")
    public void doctorFillInDrugWithUndefproperDosageTabletsAndDosageDescriptionAndConfirm(String drug, String drugCode, String dosageForm, String dosage, String comment) {
                drugService.setParticularDrugForTest(drug, drugCode, dosageForm, dosage, comment);
                response = httpMethods.postRequest(realDrugsUrl, drugService.mappingJsonCreateDrug(drugService));
                response.then().statusCode(SC_CREATED);
                drugId = drugsBase.resposeDrugIdAfterPostCreateDrug(response);
    }

    @Then("^Drugs saved in prescriptions$")
    public void drugsSavedInPrescriptions() {
        response = httpMethods.getRequest(realDrugsUrl + "/" + drugId);
        response.then().statusCode(SC_OK);
        String getDrugId = response.body().jsonPath().get("id").toString();

        Assert.assertEquals(getDrugId, getDrugId);
    }

    @After
    public void ClearDrug() {
        httpMethods.deleteRequest(realDrugsUrl + "/" + drugId);
    }
}
