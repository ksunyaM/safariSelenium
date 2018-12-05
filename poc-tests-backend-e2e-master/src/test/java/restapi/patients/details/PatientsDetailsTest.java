package restapi.patients.details;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import restapi.HttpMethods;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/patients/details",
        glue = {"restapi.patients.details"},
        plugin = {"pretty","html:target/reports/PatientsDetailsReport.html","json:target/reports/PatientsDetailsReport.json"},
        tags = {"@RealData,@StubForGetAllPatientsWithAndWithoutPrescriptions,@StubForGetPatientDetailsForId123,@StubForGetPatientDetailsForId666AndStatus404,@StubForGetAllPatientsWithoutPrescriptions,@StubForGetPatientDetailsForId234AndStatus200,@StubForGetPatientDetailsForId345AndStatus500,@StubForGetAllPatientsWithPrescriptions"}
)
@Slf4j
public class PatientsDetailsTest {

    private static Response responsePatient1;
    private static Response responsePatient2;
    private static Response responsePatient3;
    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private static String realPrescriptionsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PRESCRIPTIONS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setup() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e patients");

        String filePathForCreatePatient = rootPath + "features\\prescriptions\\create\\createPatientRequestBody.json";
        String filePathForCreatePrescription = rootPath + "features\\prescriptions\\create\\createPrescriptionRequestBody.json";

        JSONObject jsonObjectPatient = testDataUtils.mappingJsonToObject(filePathForCreatePatient);

        // add patient without prescriptions
        jsonObjectPatient.remove("email");
        jsonObjectPatient.put("email","test_" + testDataUtils.getRandomValue() + "@e2e.pl");
        responsePatient1 = httpMethods.postRequest(realPatientsUrl,jsonObjectPatient.toJSONString());
        responsePatient1.then().statusCode(SC_CREATED);
        //response.then().statusCode(SC_CREATED);

        // add patient with prescriptions
        jsonObjectPatient.remove("email");
        jsonObjectPatient.put("email","test_" + testDataUtils.getRandomValue() + "@e2e.pl");

        responsePatient2 = httpMethods.postRequest(realPatientsUrl,jsonObjectPatient.toJSONString());
        responsePatient2.then().statusCode(SC_CREATED);
        //response.then().statusCode(SC_CREATED);
        String patientId = responsePatient2.then().extract().body().path("id").toString();

        // add draft prescription
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePrescription);
        jsonObject.remove("patientId");
        jsonObject.put("patientId",patientId);
        jsonObject.remove("state");
        jsonObject.put("state","DRAFT");

        httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString()).then().statusCode(SC_CREATED);

        // add issued prescription
        jsonObject.remove("state");
        jsonObject.put("state","ISSUED");

        httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString()).then().statusCode(SC_CREATED);

        // add expired prescription
        jsonObject.remove("state");
        jsonObject.put("state","EXPIRED");

        httpMethods.postRequest(realPrescriptionsUrl,jsonObject.toJSONString()).then().statusCode(SC_CREATED);

        // deleted patient
        jsonObjectPatient.remove("email");
        jsonObjectPatient.put("email","test_deleted" + testDataUtils.getRandomValue() + "@e2e.pl");

        responsePatient3 = httpMethods.postRequest(realPatientsUrl,jsonObjectPatient.toJSONString());
        responsePatient3.then().statusCode(SC_ACCEPTED); // TODO: zamienic potem na CREATED (201), jak w serwisie sie zmieni
        //response.then().statusCode(SC_CREATED);

        log.info("Created e2e patient");
    }

    public List<String> sharePatientsIds(){
        List<String> patientsIds = new ArrayList<>();
        patientsIds.add(0,responsePatient1.then().extract().body().path("id"));
        patientsIds.add(1,responsePatient2.then().extract().body().path("id"));
        patientsIds.add(2,responsePatient3.then().extract().body().path("id"));
        return patientsIds;
    }

    @AfterClass
    public static void teardown() {
        System.out.println("____________________________________ AFTER ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Deleting e2e patients");

        httpMethods.deleteRequest(realPatientsUrl + "/" + responsePatient1.then().extract().body().path("id")).then().statusCode(SC_NO_CONTENT);
        httpMethods.deleteRequest(realPatientsUrl + "/" + responsePatient2.then().extract().body().path("id")).then().statusCode(SC_NO_CONTENT);

        log.info("Deleted e2e patients");
    }

}
