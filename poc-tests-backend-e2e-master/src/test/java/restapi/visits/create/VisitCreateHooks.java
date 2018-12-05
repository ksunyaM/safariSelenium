package restapi.visits.create;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import configuration.RestApiProvider;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import services.Doctor;
import services.Patient;
import services.Visit;
import wiremock.org.apache.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Slf4j
public class VisitCreateHooks {
    private RestApiProvider provider = new RestApiProvider();
    private String realUrl;
    String bodyForRequestPost = "{\"id\":\"PW:123\",\"name\":\"Jan Kowalski\",\"dateOfBirth\":\"01.09.2000\",\"email\":\"jkowalski@test.pl\",\"gender\":\"M\",\"country\":\"Poland\",\"phone\":\"123-456-789\",\"note\":\"add user\",\"avatar\":null}";

    @Rule
    private WireMockRule wireMockRule = new WireMockRule();


    @Before("@StubForVisitService")
    public void setStubForVisitService() {
        Visit visit = new Visit();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        patient.setEmail("e-mail");
        patient.setPassword("password");
        doctor.setDoctorLastNameAttribute("userId");
        visit.setPatientAttribute(patient);
        visit.setDoctorAttribute(doctor);
        visit.setDateAttribute("date");
        provider.setBaseUrlWithPort("http://localhost:8080");
        provider.setContentTypeHeader("application/json");
        provider.setContentType("Content-Type");
        String body = visit.mappingObjectToJsonBuilder();
        provider.setBody(body);

    }


//    public Visit setVisitDataForTestsHelper(RestApiProvider provider) {
//
//        return visit;
//    }


}
