package schemavalidation;

import com.oneleo.test.automation.core.annotation.Api;
import org.junit.runner.RunWith;

import com.oneleo.test.automation.core.XCucumber;

import cucumber.api.CucumberOptions;

@RunWith(XCucumber.class)
@CucumberOptions(plugin = "json:target/reports/schemaValidationRawZone.json")

@Api
public class SchemaValidationRawZoneTest {
}
