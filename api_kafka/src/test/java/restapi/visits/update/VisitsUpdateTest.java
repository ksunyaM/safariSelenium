package restapi.visits.update;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore("Should stick to real service, but it doesn't")
@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/visit/update",
        glue = {"restapi.visits.update"},
        plugin = {"pretty","html:target/reports/VisitsUpdateReport.html","json:target/reports/VisitsUpdateReport.json"}
      // tags = {"@StubForPostVisit,~@Ignore"}
)
public class VisitsUpdateTest {
}