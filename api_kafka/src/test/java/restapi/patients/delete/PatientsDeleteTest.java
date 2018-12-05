package restapi.patients.delete;

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
        features="src/test/resources/features/patients/delete",
        glue = {"restapi.patients.delete"},
        plugin = {"pretty","html:target/reports/PatientsDeleteReport.html","json:target/reports/PatientsDeleteReport.json"},
        tags = {"@RealData"}
)
@Slf4j
public class PatientsDeleteTest {

    private static Response responsePatient1;
    private static Response responsePatient2;
    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setup() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e patients");

        String filePathForCreatePatient = rootPath + "features\\patients\\create\\createRequestBody.json";

        // undeleted patients - deleted=false
        responsePatient1 = httpMethods.postRequest(realPatientsUrl,testDataUtils.mappingJsonToString(filePathForCreatePatient));
        responsePatient1.then().statusCode(SC_CREATED);
        //response.then().statusCode(SC_CREATED);

        // deleted patients - deleted=true
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);
        jsonObject.remove("email");
        jsonObject.put("email","test2@e2e.com");
        jsonObject.remove("name");
        jsonObject.put("name","Test 2");

        responsePatient2 = httpMethods.postRequest(realPatientsUrl,testDataUtils.mappingJsonToString(filePathForCreatePatient));
        responsePatient2.then().statusCode(SC_CREATED);
        //response.then().statusCode(SC_CREATED);
        httpMethods.deleteRequest(realPatientsUrl + "/" + responsePatient2.then().extract().body().path("id")).then().statusCode(SC_NO_CONTENT);
        //httpMethods.getRequest(realPatientsUrl + "/" + responsePatient2.then().extract().body().path("id")).then().statusCode(SC_NOT_FOUND);

        log.info("Created e2e patients");
    }

    public List<String> sharePatientsIds(){
        List<String> patientsIds = new ArrayList<>();
        patientsIds.add(0,responsePatient1.then().extract().body().path("id"));
        patientsIds.add(1,responsePatient2.then().extract().body().path("id"));
        return patientsIds;
    }

    @AfterClass
    public static void teardown() {
        System.out.println("____________________________________ AFTER ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Deleting e2e patient");

        httpMethods.deleteRequest(realPatientsUrl + "/" + responsePatient1.then().extract().body().path("id")).then().statusCode(SC_NO_CONTENT);
       // httpMethods.deleteRequest(realPatientsUrl + "/" + responsePatient2.then().extract().body().path("id")).then().statusCode(SC_NOT_FOUND);

        log.info("Deleted e2e patient");
    }

}
