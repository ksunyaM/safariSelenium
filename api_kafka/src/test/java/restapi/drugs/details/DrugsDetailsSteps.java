package restapi.drugs.details;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DrugsDetailsSteps {


    @Given("^Drug admin logged to his application landing page$")
    public void drugAdminLoggedToHisApplicationLandingPage() {
        System.out.println("StubTest");
    }


    @Given("^Doctor is going to the prescription's module$")
    public void doctorIsGoingToThePrescriptionSModule() {
        System.out.println("Test phase TDD");
    }

    @When("^Doctor selects the drug from the list$")
    public void doctorSelectsTheDrugFromTheList() {
        System.out.println("Test phase TDD");
    }

    @Then("^Drug is selected properly$")
    public void drugIsSelectedProperly() {
        System.out.println("Test phase TDD");
    }

    @Given("^Doctor is going to prescription module$")
    public void doctorIsGoingToPrescriptionModule() {
        System.out.println("Test phase TDD");
    }

    @Then("^Doctor checks that sees correct data for selected drug for patient$")
    public void doctorChecksThatSeesCorrectDataForSelectedDrugForPatient() {
        System.out.println("Test phase TDD");
    }

    @When("^Doctor select the drug that not have been existed on the list$")
    public void doctorSelectTheDrugThatNotHaveBeenExistedOnTheList() {
        System.out.println("Test phase TDD");
    }

    @Then("^Doctor checks the empty list$")
    public void doctorChecksTheEmptyList() {
        System.out.println("Test phase TDD");
    }

    @Given("^Doctor selects drug$")
    public void doctorSelectsDrug() {
        System.out.println("Test phase TDD");
    }

    @When("^Doctor tries get details for non-existent drug$")
    public void doctorTriesGetDetailsForNonExistentDrug() {
        System.out.println("Test phase TDD");
    }

    @Then("^System display graceful information with not found error$")
    public void systemDisplayGracefulInformationWithNotFoundError() {
        System.out.println("Test phase TDD");
    }

    @Given("^Doctor select drug$")
    public void doctorSelectDrug() {
        System.out.println("Test phase TDD");
    }

    @When("^Doctor tries get details from drug module to get proper drug$")
    public void doctorTriesGetDetailsFromDrugModuleToGetProperDrug() {
        System.out.println("Test phase TDD");
    }

    @Then("^System display graceful information with timeout error$")
    public void systemDisplayGracefulInformationWithTimeoutError() {
        System.out.println("Test phase TDD");
    }
}