package restapi.visits.create;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
    @CucumberOptions(
            features="src/test/resources/features/visit/create",
            glue = {"restapi.visits.create"},
            plugin = {"pretty","html:target/reports/VisitCreateReport.html","json:target/reports/VisitCreateReport.json"},
            tags = {"@StubForCreateVisit, @StubForVisitService, @FakeData, ~@Ignore"}
    )
    public class VisitCreateTest {









    }

