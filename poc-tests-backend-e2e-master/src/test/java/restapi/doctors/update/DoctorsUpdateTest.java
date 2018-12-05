package restapi.doctors.update;

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

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/doctors/update",
        glue = {"restapi.doctors.update"},
        plugin = {"pretty", "html:target/reports/DoctorsUpdateReport.html", "json:target/reports/DoctorsUpdateReport.json"},
        tags = {"@RealData"}
)
@Slf4j
public class DoctorsUpdateTest {

    private static Response response;
    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realDoctorsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DOCTORS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setup() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e - create doctor");

        String filePathForCreateDoctor = rootPath + "features\\doctors\\create\\createDoctorRequestBody.json";
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDoctor);
        response = httpMethods.postRequest(realDoctorsUrl, jsonObject.toJSONString());
        response.then().statusCode(SC_CREATED);

        log.info("Created e2e doctor");
    }

    @AfterClass
    public static void teardown() {
        System.out.println("____________________________________ AFTER ALL TESTS (ONCE TIME) ________________________________________");
    }

    public String shareDoctorId() {
        return response.then().extract().body().path("id");
    }


}
