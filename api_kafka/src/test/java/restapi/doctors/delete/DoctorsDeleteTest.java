package restapi.doctors.delete;

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

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/doctors/delete",
        glue = {"restapi.doctors.delete"},
        plugin = {"pretty", "html:target/reports/DoctorsDeleteReport.html", "json:target/reports/DoctorsDeleteReport.json"},
        tags = {"@RealData"}
)
@Slf4j
public class DoctorsDeleteTest {

    private static Response responseDoctor1;
    private static Response responseDoctor2;
    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realDoctorsURL = apiUtils.getApiUrl(ApiMicroservicesType.DOCTORS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setUp() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e - create two doctors");

        String filePathForCreateDoctor = rootPath + "features\\doctors\\create\\createDoctorRequestBody.json";
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreateDoctor);

        //add doctor 1
        jsonObject.remove("surname");
        jsonObject.put("surname", "Doctor_" + testDataUtils.getRandomValue());
        responseDoctor1 = httpMethods.postRequest(realDoctorsURL, jsonObject.toJSONString());
        responseDoctor1.then().statusCode(SC_CREATED);

        //add doctor 2
        jsonObject.remove("surname");
        jsonObject.put("surname", "Doctor_" + testDataUtils.getRandomValue());
        responseDoctor2 = httpMethods.postRequest(realDoctorsURL, jsonObject.toJSONString());
        responseDoctor2.then().statusCode(SC_CREATED);

        log.info("Created e2e doctors");
    }

    @AfterClass
    public static void teardown() {
        System.out.println("____________________________________ AFTER ALL TESTS (ONCE TIME) ________________________________________");
    }

    public List<String> shareDoctorsIds() {
        List<String> doctorsIds = new ArrayList<>();
        doctorsIds.add(0, responseDoctor1.then().extract().body().path("id"));
        doctorsIds.add(1, responseDoctor2.then().extract().body().path("id"));
        return doctorsIds;
    }


}


