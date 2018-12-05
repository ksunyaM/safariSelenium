package restapi.search.crud;

import static org.apache.http.HttpStatus.SC_CREATED;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import restapi.HttpMethods;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/search/crud",
        glue = {"restapi.search.crud"},
        plugin = {"pretty", "html:target/reports/PatientsSearchCrudReport.html", "json:target/reports/PatientsSearchCrudReport.json"},
        tags = {"@RealData,~@Ignore"}
)
@Slf4j
public class PatientsSearchCrudTest {

    private static HttpMethods httpMethods = new HttpMethods();
    private static ApiUtils apiUtils = new ApiUtils();
    private static TestDataUtils testDataUtils = new TestDataUtils();
    private static String realPatientsUrl = apiUtils.getApiUrl(ApiMicroservicesType.PATIENTS);
    private static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    @BeforeClass
    public static void setup() {
        System.out.println("____________________________________ BEFORE ALL TESTS (ONCE TIME) ________________________________________");
        log.info("Setup e2e - create patients");

        String filePathForCreatePatient = rootPath + "features\\patients\\create\\createRequestBody.json";
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(filePathForCreatePatient);

        // TODO: temporary - waiting for correct crud for patient, after that will be deleted
        String[] ids = {"TT:1","TT:2","TT:3","TT:4","TT:5","TT:6"};

        List<String> patientsName = new ArrayList<>();
        patientsName.add("Jan Kowalewski");
        patientsName.add("Jan Nowak-Jankiel");
        patientsName.add("Test user");
        patientsName.add("Janusz Biznesu");
        patientsName.add("jan jan");
        patientsName.add("deletedUser");

        for(int i = 0; i < patientsName.size(); i++){
            jsonObject.put("id",ids[i]);
            jsonObject.remove("name");
            jsonObject.put("name",patientsName.get(i));

            // TODO: add flag deleted for patient "deletedUser", and add tag to createRequestBody.json
//            if("deletedUser".equals(patientsName.get(i))){
//                jsonObject.remove("deleted");
//                jsonObject.put("deleted",true);
//            }

            httpMethods.postRequest(realPatientsUrl,jsonObject.toJSONString()).then().statusCode(SC_CREATED);
        }

        log.info("Created e2e patients");
    }

}