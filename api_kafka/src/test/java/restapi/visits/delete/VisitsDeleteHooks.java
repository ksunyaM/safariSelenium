package restapi.visits.delete;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.HttpMethodsType;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import wiremock.org.apache.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static wiremock.org.apache.http.HttpStatus.SC_NOT_FOUND;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class VisitsDeleteHooks {

    private StubProvider stubUrl = new StubProvider();
    private RestApiProvider provider = new RestApiProvider();

    @Rule
    private WireMockRule wireMockRule = new WireMockRule();

    @Before("@StubForDeletePatientWithID123")
    public void setStubForDeletePatientWithId123(){
        log.info("Setup Stub for PatientsDetailsTest -> StubForDeletePatientWithID123 and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-Type");
        provider.setContentType("application/json");

        String bodyForPatient123 = "{\"id\":\"123\",\"name\":\"Jan Brzeczyszczykiewicz\",\"dateOfBirth\":\"01.02.1998\",\"gender\":\"M\",\"email\":\"jan.b@test.pl\",\"country\":\"PL\",\"phone\":\"123-456-789\",\"note\":\"First patient\",\"avatar\":\"smile\",\"prescriptions\":[]}";
        provider.setBody(bodyForPatient123);
        provider.setApiName("/patients/123");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);

        // Delete scenario stubs
        wireMockRule.stubFor(delete(urlEqualTo("/patients/delete/123"))
                .inScenario("Delete patient with id 123")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(SC_OK)
                        .withHeader("Content-Type", "application/json"))
                .willSetStateTo("DELETE SUCCESS"));

        wireMockRule.stubFor(get(urlEqualTo("/patients/123"))
                .inScenario("Delete patient with id 123")
                .whenScenarioStateIs("DELETE SUCCESS")
                .willReturn(aResponse()
                        .withStatus(SC_NOT_FOUND)
                        .withHeader("Content-Type", "application/json")));
    }

    @Before("@StubForDeleteAllPatients")
    public void setStubForDeleteAllPatients(){
        log.info("Setup Stub for PatientsDetailsTest -> StubForDeleteAllPatients and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-Type");
        provider.setContentType("application/json");

        String bodyForPatient123 = "{\"id\":\"123\",\"name\":\"Jan Brzeczyszczykiewicz\",\"dateOfBirth\":\"01.02.1998\",\"gender\":\"M\",\"email\":\"jan.b@test.pl\",\"country\":\"PL\",\"phone\":\"123-456-789\",\"note\":\"First patient\",\"avatar\":\"smile\",\"prescriptions\":[]}";
        provider.setBody(bodyForPatient123);
        provider.setApiName("/patients/123");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);

        // Delete scenario stubs
        wireMockRule.stubFor(delete(urlEqualTo("/patients/delete"))
                .inScenario("Delete all patients")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(SC_OK)
                        .withHeader("Content-Type", "application/json"))
                .willSetStateTo("DELETE SUCCESS"));

        wireMockRule.stubFor(get(urlEqualTo("/patients/123"))
                .inScenario("Delete all patients")
                .whenScenarioStateIs("DELETE SUCCESS")
                .willReturn(aResponse()
                        .withStatus(SC_NOT_FOUND)
                        .withHeader("Content-Type", "application/json")));
    }

    @Before("@RealData")
    public void realData(){
        log.info("Setup RealData");
    }

    @After
    public void tearDown() {
        wireMockRule.stop();
        log.info("Stop mock server");
    }

}
