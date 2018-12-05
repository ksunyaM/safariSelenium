package restapi.doctors.errors;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static wiremock.org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

@Slf4j
public class DoctorsErrorsHooks {

    @Rule
    private WireMockRule wireMockRule = new WireMockRule();

    @Before("@StubForEmptyResponse")
    public void setStubsForEmptyResponse() {
        log.info("Setup Stub for DoctorsErrorsTest -> StubForEmptyResponse and start server");
        wireMockRule.start();
        wireMockRule.stubFor(get(urlPathEqualTo("/doctors/empty"))
                .willReturn(aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)));
    }

    @Before("@StubForMalwaredResponse")
    public void setStubsForMalwaredResponse() {
        log.info("Setup Stub for DoctorsErrorsTest -> StubForMalwaredResponse and start server");
        wireMockRule.start();
        wireMockRule.stubFor(get(urlPathEqualTo("/doctors/malwared"))
                .willReturn(aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
    }

    @Before("@StubForRandomDataResponse")
    public void setStubsForRandomDataResponse() {
        log.info("Setup Stub for DoctorsErrorsTest -> StubForRandomDataResponse and start server");
        wireMockRule.start();
        wireMockRule.stubFor(get(urlPathEqualTo("/doctors/random-data"))
                .willReturn(aResponse()
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
    }

    @Before("@StubForTimeoutResponse")
    public void setStubsForTimeoutResponse() {
        log.info("Setup Stub for DoctorsErrorsTest -> StubForTimeoutResponse and start server");
        wireMockRule.start();
        wireMockRule.stubFor(get(urlEqualTo("/doctors/timeout")).willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(3000)));
    }

    @Before("@StubForRetryResponse")
    public void setStubsForFaults() {
        log.info("Setup Stub for DoctorsDetailsTest -> StubForRetryResponse and start server");
        wireMockRule.start();

        // Retry
        // First StubMapping
        wireMockRule.stubFor(get(urlEqualTo("/doctors/retry"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(aResponse()
                        .withStatus(SC_INTERNAL_SERVER_ERROR) // request unsuccessful with status code 503
                        .withBody("{}"))
                .willSetStateTo("SUCCESS"));

        // Second StubMapping
        wireMockRule.stubFor(get(urlEqualTo("/doctors/retry"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("SUCCESS")
                .willReturn(aResponse()
                        .withStatus(SC_OK)  // request successful with status code 200
                        .withBody("{}")));
    }

    @Before("@StubForTimeoutPostResponse")
    public void setStubsForTimeoutPostResponse() {
        log.info("Setup Stub for DoctorsErrorsTest -> StubForTimeoutPostResponse and start server");
        wireMockRule.start();
        wireMockRule.stubFor(post(urlEqualTo("/doctors/create-timeout")).willReturn(aResponse()
                .withStatus(200)
                .withFixedDelay(3000)));
    }

    @After
    public void tearDown() {
        wireMockRule.stop();
        log.info("Stop mock server");
    }
}
