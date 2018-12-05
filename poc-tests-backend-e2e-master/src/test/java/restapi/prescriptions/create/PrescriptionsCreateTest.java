package restapi.prescriptions.create;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import restapi.HttpMethods;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/prescriptions/create",
        glue = {"restapi.prescriptions.create"},
        plugin = {"pretty","html:target/reports/PrescriptionsCreateReport.html","json:target/reports/PrescriptionsCreateReport.json"},
        tags = {"@RealData, ~@Ignore"}
)
@Slf4j
public class PrescriptionsCreateTest {

    private static Response response;
    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setup() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e patient");

        String filePathForCreatePatient = rootPath + "features\\prescriptions\\create\\createPatientRequestBody.json";

        response = httpMethods.postRequest(realPatientsUrl,testDataUtils.mappingJsonToString(filePathForCreatePatient));
        response.then().statusCode(SC_CREATED);
        //response.then().statusCode(SC_CREATED);

        log.info("Created e2e patient");
    }

    public String sharePatientIdFromResponseBody(){
        return response.then().extract().body().path("id").toString();
    }

    @AfterClass
    public static void teardown() {
        System.out.println("____________________________________ AFTER ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Deleting e2e patient");

        httpMethods.deleteRequest(realPatientsUrl + "/" + response.then().extract().body().path("id")).then().statusCode(SC_NO_CONTENT);

        log.info("Deleted e2e patient");
    }


}
