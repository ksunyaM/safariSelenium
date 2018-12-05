package restapi.search.patientSearch;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.*;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import services.Prescription;
import wiremock.org.apache.http.HttpStatus;

import java.util.List;

import static wiremock.org.apache.http.HttpStatus.SC_ACCEPTED;

@Slf4j
public class PatientsSearchHooks {

    private StubProvider stubUrl = new StubProvider();
    private RestApiProvider provider = new RestApiProvider();
    private String realUrl;

    @Rule
    private WireMockRule wireMockRule = new WireMockRule();

    @Before("@StubForGetSearchPatient")
    public void setStubForGetSearchPatient() {
        log.info("Setup Stub for PatientsSearchTest -> StubForGetSearchPatient and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-type");
        provider.setContentType("application/json");

        //STUB JsonBody
        String JsonBody = "[{\"id\":\"PA:123\",\"name\":\"Rob Brzeczyszczykiewicz\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]},{\"id\":\"PA:234\",\"name\":\"Robert Piltza\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]},{\"id\":\"PA:345\",\"name\":\"Robertinho Biznesu\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]},{\"id\":\"PA:456\",\"name\":\"John Smith\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]}]";
        provider.setBody(JsonBody);
        provider.setApiName("/search");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);
    }

    @Before("@StubForGetSearchPatientWithParam")
    public void setStubForGetSearchPatientWithParam() {
        log.info("Setup Stub for PatientsSearchTest -> StubForGetSearchPatientWithParam and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-type");
        provider.setContentType("application/json");

        //STUB StubForGetSearchPatientWithParam
        String qQuery = "[{\"id\":\"PA:123\",\"name\":\"Rob Brzeczyszczykiewicz\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]},{\"id\":\"PA:234\",\"name\":\"Robert Piltza\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]},{\"id\":\"PA:345\",\"name\":\"Robertinho Biznesu\",\"dateOfBirth\":null,\"gender\":null,\"email\":null,\"country\":null,\"phone\":null,\"note\":null,\"avatar\":null,\"prescriptions\":[]}]";
        provider.setBody(qQuery);
        provider.setApiName("/search?q=Rob&type=patient");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);
    }

    @Before("@StubForGetSearchPatientDoesNotExist")
    public void StubForGetSearchPatientDoesNotExist() {
        log.info("Setup Stub for PatientsSearchTest -> StubForGetSearchPatientDoesNotExist and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-type");
        provider.setContentType("application/json");

        //STUB StubForGetSearchPatientDoesNotExist
        String qQueryNoPatient = "{\"id\":\"PA:123\",\"name\":\"\"}";
        provider.setBody(qQueryNoPatient);
        provider.setApiName("/search?q=Sherlok&type=patient");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);
    }

    @Before("@StubForGetSearchPatientInfo")
    public void StubForGetSearchPatientInfo() {
        log.info("Setup Stub for PatientsSearchTest -> StubForGetSearchPatientInfo and start server");
        wireMockRule.start();
        provider.setContentTypeHeader("Content-type");
        provider.setContentType("application/json");

        //STUB StubForGetSearchPatientInfo
        String qQueryNoPatient = "{\"id\":\"PA:123\",\"name\":\"Rob Brzeczyszczykiewicz\",\"dateOfBirth\":\"12.08.1880\"}";
        provider.setBody(qQueryNoPatient);
        provider.setApiName("/search/info");
        stubUrl.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, HttpStatus.SC_OK);
    }

    @Before("@RealDataJ")
    public void realData(){
        log.info("Setup RealData");
        ApiUtils apiUtils = new ApiUtils();
        realUrl = apiUtils.getApiUrl(ApiMicroservicesType.SEARCH);

        RestAssured.given().header("Content-Type", "application/json")
                .when()
                .get(realUrl);

    }

    @After("@RealDataJ")
    public void tearDownRealService() {
        log.info("Stop test");
    }

    @After
    public void tearDown() {
        wireMockRule.stop();
        log.info("Stop mock server");
    }

}
