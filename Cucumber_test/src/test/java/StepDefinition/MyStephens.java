package StepDefinition;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.List;

public class MyStephens {
    @Given("^User us is on NetbBanking landing page$")
    public void userUsIsOnNetbBankingLandingPage() throws Throwable 
    {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("User us is on NetbBanking landing page");
        //throw new PendingException();
    }

    @When("^User login into application with username and password$")
    public void userLoginIntoApplicationWithUsernameAndPassword() throws Throwable
    {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
        System.out.println("User login into application with username and password");
    }

    @Then("^Home page is populated$")
    public void homePageIsPopulated() throws Throwable
    {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
        System.out.println("Home page is populated");
    }

    @And("^Cards are displayed$")
    public void cardsAreDisplayed() throws Throwable
    {
        // Write code here that turns the phrase above into concrete actions
       // throw new PendingException();
        System.out.println("Cards are displayed");
    }

    @When("^User login into application with username \"([^\"]*)\" and password  \"([^\"]*)\"$")
    public void userLoginIntoApplicationWithUsernameAndPassword(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        System.out.printf("user name: %s, password: %s %n", arg0, arg1);
    }


    @When("^User sign up with following details$")
    public void userSignUpWithFollowingDetails(DataTable tabl1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        List<List<String>> list=tabl1.raw();
        for (List<String> row: list)
        {
            for (String column: row)
                System.out.println(column);
        }
    }

    @When("^User login in application with username (.+) and password (.+)$")
    public void user_login_in_application_with_username_and_password(String username, String password) throws Throwable {
        System.out.printf(username+" "+password+"%n");
    }

    @Given("^validate browser$")
    public void validate_browser() throws Throwable {
        System.out.printf("validate browser");
    }

    @When("^Browser is trigerred$")
    public void browser_is_trigerred() throws Throwable {
        System.out.printf("Browser is trigerred");
    }

    @Then("^check if browser started$")
    public void check_if_browser_started() throws Throwable {
        System.out.printf("check if browser started");
    }


}
