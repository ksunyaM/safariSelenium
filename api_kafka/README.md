# poc-tests-backend-e2e Automation framework #

Main information:
- The core framework is implemented using **Java** language;
- **Maven** as a build automation tool;
- **Cucumber** to run tests written in a behavior-driven development (*BDD*) style. The basic approach is a simple parser of a language called *Gherkin*. It allows expected software behaviors to be specified in a logical language that customers can understand <https://cucumber.io/>;
- **WireMock** as an HTTP mock server. Stubs can be configured via JSON files  <http://wiremock.org/>;
- API testing is based on **REST Assured** Java library that provides a domain-specific language (DSL) for writing powerful, maintainable tests for RESTful APIs <http://rest-assured.io/>;

## Package with tests (example of using mocked data) ##

###### 1. Create a feature file #######

In **_"test -> resources -> features"_** create a directory with the name of the test package and then create another directory if needed and then create a feature file for scenarios writing in *Gherkin* language (for *Cucumber*).

E.g. for **tests api for patients** - get details, create:
1. _directory:_ Patients
2. _sub-directory:_ Details
3. _feature file:_ PatientsDetails.feature

In the feature file starts write scenarios:
```   
Feature: Doctor can get details about patients
  As a Doctor
  I want to get all patients or selected patient from the list
  So that I can verify my patients and check their details

  @StubForGetPatientDetailsForId123
  Scenario: Doctor wants to check information about patient with id 123
    Given Doctor gets information about patient with id 123
    When Doctor receives information about patient(s)
    Then Doctor checks that name is Jan Kowalski
```

###### 2. Create Test, Steps and Hooks java classes ######

In **_test -> java -> restapi_** prepare new package e.g. "**patients**". In this package prepare separatly packages for each global tests, e.g.: _create_, _delete_, _update_, _details_, etc.
In this package prepare three classes:
- _Hooks_ - for preparing stubs
- _Steps_ - for implement scenario steps
- _Tests_ - configuration file to run Cucumber tests

Below you can find some examples:
**PatientsDetailsHooks**:
```
    @Before("@StubForGetPatientDetailsForId123")
    public void setStubForGetPatientDetailsForId123() {
        log.info("Setup Stub for PatientsDetailsTest -> StubForGetPatientDetailsForId123 and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-Type");
        provider.setContentType("application/json");

        // Stub for patient with id 123
        String bodyForPatient123 = "{\"id\":\"123\",\"name\":\"Jan Kowalski\",\"dateOfBirth\":\"01.02.1998\",\"gender\":\"M\",\"email\":\"jan.k@test.pl\",\"country\":\"PL\",\"phone\":\"123-456-789\",\"note\":\"First patient\",\"avatar\":\"smile\",\"prescriptions\":[]}";
        provider.setBody(bodyForPatient123);
        provider.setApiName("/patients/123");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);
    }

    @After
    public void tearDown() {
        wireMockRule.stop();
        log.info("Stop mock server");
    }
```
**PatientsDetailsTest**:   
```
package restapi.patients.details;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/patients/details",
        glue = {"restapi.patients.details"},
        plugin = {"pretty","html:target/reports/PatientsDetailsReport.html","json:target/reports/PatientsDetailsReport.json"},
        tags = {"@StubForGetAllPatientsWithAndWithoutPrescriptions,@StubForGetPatientDetailsForId123,@StubForGetPatientDetailsForId666AndStatus404,@StubForGetAllPatientsWithoutPrescriptions,@StubForGetPatientDetailsForId234AndStatus200,@StubForGetPatientDetailsForId345AndStatus500,@StubForGetAllPatientsWithPrescriptions"}
)
public class PatientsDetailsTest {
}
```
Implement each steps from **Scenarios** (from _feature_ file)

**PatientsDetailsSteps**:
```
    //Given Doctor gets information about patient with id 123
    @Given("^Doctor gets information about patient with id (.*)$")
    public void doctorGetsInformationAboutPatientWithId(String patientId){
        response = httpMethods.getRequest(url + "/patients/" + patientId);
    }

    //When Doctor receives information about patient(s)
    @When("^Doctor receives information about patient\\(s\\)$")
    public void doctorReceivesInformationAboutPatient(){
        response.then().statusCode(SC_OK);
    }

     //Then Doctor checks that name is Jan Kowalski
    @Then("^Doctor checks date name is (.*)$")
    public void doctorChecksThatNameIsValid(String value) {
        String responseValue = response.then().extract().response().path("name");
        Assert.assertEquals(value, responseValue);
    }
```
###### 3. Verify that the tests are running ######

Select Test file (**PatientsDetailsTest**) and using right mouse button select: 
_Run_ -> '**PatientsDetailsTest**' (or use Ctrl+Shift+F10)
Check, that tests are passed
