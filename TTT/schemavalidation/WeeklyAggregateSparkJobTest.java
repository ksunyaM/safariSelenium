package schemavalidation;

import com.oneleo.test.automation.core.XCucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(XCucumber.class)
@CucumberOptions(plugin = "json:target/reports/weeklyAggregateSparkJobTest.json")

public class WeeklyAggregateSparkJobTest {
}
