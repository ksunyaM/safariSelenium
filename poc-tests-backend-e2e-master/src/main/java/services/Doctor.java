package services;


import configuration.RestApiProvider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctor {

    private RestApiProvider provider;
    private String doctorNameAttribute;
    private String doctorLastNameAttribute;
    private String medicalSpecializationAttribute;




}

