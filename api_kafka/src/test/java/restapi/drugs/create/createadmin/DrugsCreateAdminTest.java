package restapi.drugs.create.createadmin;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(

        features="src/test/resources/features/drugs/create/DrugAdminCreate.feature",
        glue = {"restapi.drugs.create.createadmin"},
        plugin = {"pretty","html:target/reports/DrugsCreateReport.html","json:target/reports/DrugsCreateReport.json"},
       tags = {"@RealData"}
)
public class DrugsCreateAdminTest {

}
