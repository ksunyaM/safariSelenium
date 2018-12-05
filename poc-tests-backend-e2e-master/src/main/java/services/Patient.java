package services;

import gherkin.deps.com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Patient {

    private String id;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String email;
    private String password;
    private String country;
    private String phone;
    private String note;
    private String avatar;
    @Singular
    private List<Prescription> prescriptions;


    public Patient[] accessPatientDataArray(String filePath) throws FileNotFoundException {
//        "C:\\projects\\poc-tests-backend-e2e\\src\\test\\resources\\features\\patients\\create\\createRequestBody.json"
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        FileReader jsonToPost = new FileReader(rootPath + filePath);
        return new Gson().fromJson(jsonToPost, Patient[].class);

    }



    public Patient filterByPatientData(Patient [] patients){
      //  Patient patient = new Patient();
       // Arrays.stream(patients).filter(patient -> )
        for(Patient p : patients){
           p.getId();
           p.getName();
           p.getCountry();
           p.getDateOfBirth();
           p.getPrescriptions();
           p.getGender();
           p.getAvatar();
           p.getPhone();
           p.getEmail();
           p.getNote();

        }


        return this;

    }





}
