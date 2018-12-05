package restapi.search.patientSearch;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/search",
        glue = {"restapi.search.patientSearch"},
        plugin = {"pretty", "html:target/reports/PatientsSearchReport.html", "json:target/reports/PatientsSearchReport.json"},
        tags = {"@RealDataJ,@StubForGetSearchPatientWithParam,@StubForGetSearchPatientDoesNotExist,@StubForGetSearchPatient,@StubForGetSearchPatientInfo"}
)
public class PatientsSearchTest {
}