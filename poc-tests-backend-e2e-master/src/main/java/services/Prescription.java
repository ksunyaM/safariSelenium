package services;

import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Setter
public class Prescription {

    private String id;
    private String patientId;
    private Long issuedDate;
    private Long validityDate;
    private String state;
    private String issuedBy;
    private String diseaseId;
     @Singular
    private List<String> drugs;
    private String note;





}
