package restapi.drugs.update;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DrugsUpdateSteps {


    @Given("^System admin selects drug <drugCode>$")
    public void systemAdminSelectsDrugDrugCode() {
        System.out.println("Test phase in TDD");
    }

    @When("^System admin updates all data <name> and <dosageForm>$")
    public void systemAdminUpdatesAllDataNameAndDosageForm() {
        System.out.println("Test phase in TDD");
    }

    @Then("^System admin checks that drug is updated$")
    public void systemAdminChecksThatDrugIsUpdated() {
        System.out.println("Test phase in TDD");
    }

    @Given("^System admin selects <drugCode> drug$")
    public void systemAdminSelectsDrugCodeDrug() {
        System.out.println("Test phase in TDD");
    }

    @When("^System admin updates selected data <name> with <dosageForm>$")
    public void systemAdminUpdatesSelectedDataNameWithDosageForm() {
        System.out.println("Test phase in TDD");
    }

    @Then("^System admin checks drug is updated$")
    public void systemAdminChecksDrugIsUpdated() {
        System.out.println("Test phase in TDD");
    }

    @Given("^System admin selects <drugCode> drug with <name> and with <dosageForm>$")
    public void systemAdminSelectsDrugCodeDrugWithNameAndWithDosageForm() {
        System.out.println("Test phase in TDD");
    }

    @When("^System admin updates fields <newCode> and <newName> and <newDosageForm>$")
    public void systemAdminUpdatesFieldsNewCodeAndNewNameAndNewDosageForm() {
        System.out.println("Test phase in TDD");
    }
}
