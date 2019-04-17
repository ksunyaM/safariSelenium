/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm.patient;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wba.test.utils.BaseStep;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.ResourceUtils;
import cucumber.api.DataTable;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Random;

public class PatientCreator extends BaseStep {
    private String patientParameters;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");


    private RandomDataGenerator random = new RandomDataGenerator();
    private PatientConditions conditions = new PatientConditions();

    public PatientCreator(String resourcePath) {
        patientParameters = ResourceUtils.getResourceAsString(resourcePath);
    }

    public PatientCreator withStore(String storeNumber) {
        patientParameters = updateJson(patientParameters, Collections.singletonMap("storeNumber", storeNumber));
        return this;
    }

    public PatientCreator withParameters(DataTable data) {
        patientParameters = updateJson(patientParameters, data);
        return this;
    }

    public String getPatientParameters() {
        return patientParameters;
    }

    public String create() {
        String storeNum = JsonUtils.getJsonValue(patientParameters, "storeNumber", String.class);
        if (storeNum == null || storeNum.equals(""))
            throw new RuntimeException("Store Number is not set.");

        String gender = JsonUtils.getJsonValue(patientParameters, "gender", String.class);
        if (gender == null || !gender.equals("M") || !gender.equals("F"))
            gender = new Random().nextBoolean() ? "M" : "F";
        patientParameters = updateJson(patientParameters, Collections.singletonMap("gender", gender));

        String fName = JsonUtils.getJsonValue(patientParameters, "firstName", String.class);
        if (fName == null || fName.equals(""))
            fName = gender.equalsIgnoreCase("M") ? RealisticData.maleNames[random.nextInt(0, RealisticData.maleNames.length - 1)]
                    : RealisticData.femaleNames[random.nextInt(0, RealisticData.femaleNames.length - 1)];
        patientParameters = updateJson(patientParameters, Collections.singletonMap("firstName", fName));

        String lName = JsonUtils.getJsonValue(patientParameters, "lastName", String.class);
        if (lName == null || lName.equals(""))
            lName = RealisticData.lastNames[random.nextInt(0, RealisticData.lastNames.length - 1)];
        patientParameters = updateJson(patientParameters, Collections.singletonMap("lastName", lName));

        String dateOfBirth = JsonUtils.getJsonValue(patientParameters, "dateOfBirth", String.class);
        LocalDate dob = dateOfBirth == null || dateOfBirth.equals("")
                ? LocalDate.now().minusYears(20) : LocalDate.parse(dateOfBirth, formatter);
        patientParameters = updateJson(patientParameters, Collections.singletonMap("dateOfBirth", dob.toString()));

        String phone = RandomStringUtils.randomNumeric(7);
        patientParameters = updateJson(patientParameters, Collections.singletonMap("phone", phone));

        String address = JsonUtils.getJsonValue(patientParameters, "address", String.class);
        String areacd = JsonUtils.getJsonValue(patientParameters, "areacd", String.class);
        String payType = JsonUtils.getJsonValue(patientParameters, "payType", String.class);
        String planId = JsonUtils.getJsonValue(patientParameters, "planId", String.class);
        String planRecipient = JsonUtils.getJsonValue(patientParameters, "planRecipient", String.class);
        String planGroup = JsonUtils.getJsonValue(patientParameters, "planGroup", String.class);
        String prescriber = JsonUtils.getJsonValue(patientParameters, "prescriber", String.class);
        String petType = JsonUtils.getJsonValue(patientParameters, "petType", String.class);
        String phoneType = JsonUtils.getJsonValue(patientParameters, "phoneType", String.class);
        String nameSuffix = JsonUtils.getJsonValue(patientParameters, "nameSuffix", String.class);
        String nameMiddleInitial = JsonUtils.getJsonValue(patientParameters, "nameMiddleInitial", String.class);
        String userId = JsonUtils.getJsonValue(patientParameters, "userId", String.class);
        String residenceType = JsonUtils.getJsonValue(patientParameters, "residenceType", String.class);
        boolean vipPatient = JsonUtils.getJsonValue(patientParameters, "vipPatient", Boolean.class);
        boolean cobPlan = JsonUtils.getJsonValue(patientParameters, "cobPlan", Boolean.class);
        boolean petInd = JsonUtils.getJsonValue(patientParameters, "petInd", Boolean.class);

        String patId = new PatientHelper().ud32PatientHelperFunction(storeNum, fName, lName, gender, address, areacd, phone, dob, payType, planId, planRecipient,
                planGroup, prescriber, cobPlan, petInd, petType, phoneType, nameSuffix, nameMiddleInitial, userId, residenceType,
                vipPatient);

        patientParameters = updateJson(patientParameters, Collections.singletonMap("patientId", patId));

        addCondition("allergies");
        addCondition("health_conditions");
        addCondition("drug_allergies");
        addCondition("additional_medication");

        return patientParameters;
    }

    private void addCondition(String conditionName) {
        String storeNum = JsonUtils.getJsonValue(patientParameters, "storeNumber", String.class);
        String patId = JsonUtils.getJsonValue(patientParameters, "patientId", String.class);
        JsonUtils.getJsonValue(patientParameters, conditionName, ArrayNode.class).forEach(condition -> {
            String code = condition.get("code").asText();
            if (!code.equals("")) {
                switch (conditionName) {
                    case "allergies":
                        conditions.addAllergyCondition(storeNum, code, patId);
                        break;
                    case "health_conditions":
                        conditions.addHealthCondition(storeNum, code, patId);
                        break;
                    case "drug_allergies":
                        conditions.addDrugAllergy(storeNum, code, patId);
                        break;
                    case "additional_medication":
                        conditions.addAdditionalMedication(storeNum, code, patId);
                        break;
                    default:
                        LOGGER.error("Unknown condition {}. Please check Patient.json", condition);
                }

            }
        });
    }

}
