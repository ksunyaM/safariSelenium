import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources",glue = "StepDefinition",strict = true,
        tags = "@smoke,@functional", plugin = {"pretty","html:target/cucumber","junit:target/cukes.xml"})

public class TestRunner
{
}
