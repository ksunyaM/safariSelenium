package schemavalidation;

import org.junit.runner.RunWith;

import com.oneleo.test.automation.core.XCucumber;

import cucumber.api.CucumberOptions;

@RunWith(XCucumber.class)
@CucumberOptions(plugin = "json:target/reports/aggregateDataSparkJobTest.json")

public class AggregateDataSparkJobTest {
}
