package restapi.doctors.errors;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore("Should stick to real service, but it doesn't")
@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/doctors/errors",
        glue = {"restapi.doctors.errors"},
        plugin = {"pretty","html:target/reports/DoctorsErrorsReport.html","json:target/reports/DoctorsErrorsReport.json"},
        tags = {"@StubForEmptyResponse, @StubForRandomDataResponse, @StubForTimeoutResponse, @StubForRetryResponse, @StubForTimeoutPostResponse"}
)
public class DoctorsErrorsTest {
}
