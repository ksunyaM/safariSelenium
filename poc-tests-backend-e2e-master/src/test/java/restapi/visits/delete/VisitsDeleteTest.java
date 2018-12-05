package restapi.visits.delete;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/patients/delete",
        glue = {"restapi.patients.delete"},
        plugin = {"pretty","html:target/reports/PatientsDeleteReport.html","json:target/reports/PatientsDeleteReport.json"},
        tags = {"@RealData,@StubForDeletePatientWithID123,@StubForDeleteAllPatients"}
)
public class VisitsDeleteTest {
}