package services;


import lombok.Getter;
import lombok.Setter;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import java.util.Collections;


@Getter
@Setter
public class Drug {

    private String id;
    private String name;
    private String drugCode;
    private String dosageForm;
    private String dosage;
    private String comment;

    public String mappingJsonCreateDrug(Drug drug) {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(Collections.emptyMap());
        JsonObject publicationDateObject = builderFactory.createObjectBuilder()
                .add("comment", drug.getComment())
                .add("dosage", drug.getDosage())
                .add("dosageForm", drug.getDosageForm())
                .add("drugCode", drug.getDrugCode())
                .add("name", drug.getName())
                .build();
        return publicationDateObject.toString();
    }

    public void setParticularDrugForTest(String name, String drugeCode, String dosageForm, String dosage, String comment) {
        this.setComment(comment);
        this.setDosage(dosage);
        this.setDosageForm(dosageForm);
        this.setDrugCode(drugeCode);
        this.setName(name);
    }
}
