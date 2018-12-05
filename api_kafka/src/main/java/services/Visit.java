package services;

import configuration.MapObjectJson;
import lombok.Getter;
import lombok.Setter;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collections;

@Getter
@Setter
public class Visit implements MapObjectJson {

    private Patient patientAttribute;
    private Doctor doctorAttribute;
    private Drug drugAttribute;
    private String dateAttribute;

    @Override
    public String mappingObjectToJsonBuilder() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(Collections.emptyMap());
        JsonObject publicationDateObject = builderFactory.createObjectBuilder()
                .add(this.getPatientAttribute().getEmail(), "jan@kowalski.pl")
                .add(this.getPatientAttribute().getPassword(), "passwordHash")
                .add(this.getDoctorAttribute().getDoctorLastNameAttribute(), "123")
                .add(this.getDateAttribute(), "9.08.2018")
                .add("rok", 2018)
                .add("miesiąc", 9)
                .add("dzień", 13).build();

        return publicationDateObject.toString();
    }


    public String getDefaultVisit() {
        Visit visit = new Visit();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();
        Drug drug = new Drug();
        drug.setId("id");
        patient.setEmail("e-mail");
        patient.setPassword("password");
        doctor.setDoctorLastNameAttribute("userId");
        visit.setPatientAttribute(patient);
        visit.setDrugAttribute(drug);
        visit.setDoctorAttribute(doctor);
        visit.setDateAttribute("date");

        return visit.mappingObjectToJsonBuilder();
    }


}




