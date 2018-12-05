package restapi.prescriptions.update;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import restapi.HttpMethods;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/prescriptions/update",
        glue = {"restapi.prescriptions.update"},
        plugin = {"pretty","html:target/reports/PrescriptionsUpdateReport.html","json:target/reports/PrescriptionsUpdateReport.json"},
        tags = {"@RealData,~@Ignore"}
)
@Slf4j
public class PrescriptionsUpdateTest {

    private static Response response;
    private static Response responsePatient;
    private static Response responsePrescriptionDraft;
    private static Response responsePrescriptionIssued;
    private static Response responsePrescriptionExpired;
    private static Response responsePrescriptionDeleted;
    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private static String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setup() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e patient with prescriptions");

        String filePathForCreatePatient = rootPath + "features\\prescriptions\\create\\createPatientRequestBody.json";
        String filePathForCreatePrescription = rootPath + "features\\prescriptions\\create\\createPrescriptionRequestBody.json";

        responsePatient = httpMethods.postRequest(realPatientsUrl,testDataUtils.mappingJsonToString(filePathForCreatePatient));
        responsePatient.then().statusCode(SC_CREATED);
        //response.then().statusCode(SC_CREATED);
        String patientId = responsePatient.then().extract().body().path("id").toString();

        // add draft prescription
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);
        jsonObject.remove("state");
        jsonObject.put("state","DRAFT");

        responsePrescriptionDraft = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());
        responsePrescriptionDraft.then().statusCode(SC_CREATED);

        // add issued prescription
        jsonObject.remove("state");
        jsonObject.put("state","ISSUED");

        responsePrescriptionIssued = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());
        responsePrescriptionIssued.then().statusCode(SC_CREATED);

        // add expired prescription
        jsonObject.remove("state");
        jsonObject.put("state","EXPIRED");

        responsePrescriptionExpired = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());
        responsePrescriptionExpired.then().statusCode(SC_CREATED);

        // add deleted draft
        responsePrescriptionDeleted = httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString());
        responsePrescriptionDeleted.then().statusCode(SC_CREATED);

        response = httpMethods.deleteRequest(realPrescriptionsUrl + "/" + responsePrescriptionDeleted.then().extract().body().path("prescriptionId"));
        //response.then().statusCode(SC_NO_CONTENT);
        //httpMethods.getRequest(realPrescriptionsUrl + "/" + responsePrescriptionDeleted.then().extract().body().path("id")).then().statusCode(SC_NOT_FOUND);
        // TODO: do odkomentowanie, po refaktorze dla mikroservice

        log.info("Created e2e patient");
    }

    public String sharePatientIdFromResponseBody(){
        return responsePatient.then().extract().body().path("id").toString();
    }

    public String sharePrescriptionDraftIdFromResponseBody(){
        return responsePrescriptionDraft.then().extract().body().path("prescriptionId").toString();
    }

    public String sharePrescriptionIssuedIdFromResponseBody(){
        return responsePrescriptionIssued.then().extract().body().path("prescriptionId").toString();
    }

    public String sharePrescriptionExpiredIdFromResponseBody(){
        return responsePrescriptionExpired.then().extract().body().path("prescriptionId").toString();
    }

    public String sharePrescriptionDeletedIdFromResponseBody(){
        return responsePrescriptionDeleted.then().extract().body().path("prescriptionId").toString();
    }

    @AfterClass
    public static void teardown() {
        System.out.println("____________________________________ AFTER ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Deleting e2e patient");

        httpMethods.deleteRequest(realPatientsUrl + "/" + responsePatient.then().extract().body().path("id")).then().statusCode(SC_NO_CONTENT);

        log.info("Deleted e2e patient");
    }
}
