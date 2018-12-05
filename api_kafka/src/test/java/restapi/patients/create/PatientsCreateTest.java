package restapi.patients.create;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/patients/create",
        glue = {"restapi.patients.create"},
        plugin = {"pretty","html:target/reports/PatientsCreateReport.html","json:target/reports/PatientsCreateReport.json"},
        tags = {"@RealData"}
)
public class PatientsCreateTest {
}