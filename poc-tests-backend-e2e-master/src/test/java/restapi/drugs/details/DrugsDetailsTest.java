package restapi.drugs.details;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/drugs/delete",
        glue = {"restapi.drugs.delete"},
        plugin = {"pretty","html:target/reports/DrugsDeleteReport.html","json:target/reports/DrugsDeleteReport.json"}
//        tags = {"@FakeData,~@Ignore"}
)
public class DrugsDetailsTest {
}
