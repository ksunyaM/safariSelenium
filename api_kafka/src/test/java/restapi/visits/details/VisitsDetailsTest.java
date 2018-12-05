package restapi.visits.details;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore("Should stick to real service, but it doesn't")
@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/visit/details",
        glue = {"restapi.visits.details"},
        plugin = {"pretty","html:target/reports/VisitsDetailsReport.html","json:target/reports/VisitsDetailsReport.json"},
       tags = {"@StubForPostVisit,~@Ignore"}
)
public class VisitsDetailsTest {
}