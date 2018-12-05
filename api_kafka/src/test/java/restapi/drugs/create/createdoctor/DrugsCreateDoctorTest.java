package restapi.drugs.create.createdoctor;


import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.junit.Cucumber;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.runner.RunWith;
import restapi.HttpMethods;

import static org.apache.http.HttpStatus.SC_CREATED;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/drugs/create/DrugDoctorCreate.feature",
        glue = {"restapi.drugs.create.createdoctor"},
        plugin = {"pretty", "html:target/reports/DrugsCreateReport.html", "json:target/reports/DrugsCreateDoctorReport.json"},
        tags = {"@RealData"}
)
public class DrugsCreateDoctorTest {

}
