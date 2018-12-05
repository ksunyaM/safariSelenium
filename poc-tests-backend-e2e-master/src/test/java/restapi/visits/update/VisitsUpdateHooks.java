package restapi.visits.update;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.RestApiProvider;
import configuration.StubProvider;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import services.Visit;
import wiremock.org.apache.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Slf4j
public class VisitsUpdateHooks {

    private StubProvider stubUrl = new StubProvider();
    private RestApiProvider provider = new RestApiProvider();

    @Rule
    private WireMockRule wireMockRule = new WireMockRule();

    @Before("@StubForPostVisit")
    public void setStubForCreatePatient(){
        log.info("Setup Stub for PatientsCreateTest -> StubForCreatePatient and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-Type");
        provider.setContentType("application/json");
        Visit visit = new Visit();
        String bodyForRequestPost = visit.getDefaultVisit();


        wireMockRule.stubFor(post("/visits/create")
                .withRequestBody(equalToJson(bodyForRequestPost))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_CREATED)
                        .withBody(bodyForRequestPost) //bodyforrequest
                        .withHeader(provider.getContentTypeHeader(),provider.getContentType())));
    }

    @After
    public void tearDown() {
        wireMockRule.stop();
        log.info("Stop mock server");
    }

}
