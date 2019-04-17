/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm.patient;

import com.wba.test.utils.tdm.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Calendar;

public class PatientConditions {
    private Logger LOGGER = LoggerFactory.getLogger(PatientConditions.class);
    private DBConnection connection = DBConnection.CONNECT;

    public boolean addHealthCondition(String storeNum, String healthConditionId, String patientId) {
        return addCondition(storeNum, healthConditionId, patientId, "HM");
    }

    public boolean addAllergyCondition(String storeNum, String allergyConditionId, String patientId) {
        return addCondition(storeNum, allergyConditionId, patientId, "AP");
    }

    public boolean addAdditionalMedication(String storeNum, String drugId, String patientId) {
        return createAdditionalMedication(storeNum, drugId, patientId);
    }

    public boolean addDrugAllergy(String storeNum, String drugAllergyConditionId, String patientId) {
        return addCondition(storeNum, drugAllergyConditionId, patientId, "AK");
    }

    private boolean addCondition(String storeNum, String conditionId, String patientId, String conditionCode) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Connection centralConn = connection.connectToCentralDB();

        try {
            ResultSet rs = connection.executeQuery(storeConn, "select active_ind from tbf0_pat_algy_hlth_cd where pat_id = ? and PAT_ALLERGY_HEALTH_CD = ? and pat_code_type = ?",
                    "s::" + patientId, "s::" + conditionId, "s::" + conditionCode);//this checks if the person already has that condition
            String active_ind = rs.next() ? rs.getString(1) : "";//if it already has this, set variable for disabling it
            //region If this condition is being added for first time
            if (active_ind.equals("")) {
                //insert statement for local
                int local = connection.updateQuery(storeConn, "insert into tbf0_pat_algy_hlth_cd(pat_id, pat_code_type, PAT_ALLERGY_HEALTH_CD, active_ind, create_user_id, create_dttm) values(?,?,?,'Y', 568564313, ?)",
                        "s::" + patientId, "s::" + conditionCode, "s::" + conditionId, "d::" + new Date(Calendar.getInstance().getTimeInMillis()));

                String partition = findPartition(patientId);
                if (partition.equals("")) {
                    LOGGER.error("Condition {} was not found in central", conditionId);
                    return false;
                }
                //execute this based on the correct partition
                int central = connection.updateQuery(centralConn, "insert into tbf" + partition + "_pat_algy_hlth_cd(pat_id, pat_code_type, PAT_ALLERGY_HEALTH_CD, active_ind, create_user_id, create_dttm) values(?,?,?,'Y', 568564313, ?)",
                        "s::" + patientId, "s::" + conditionCode, "s::" + conditionId, "d::" + new Date(Calendar.getInstance().getTimeInMillis()));
            } else {//deactivating health condition
                if (active_ind.equals("Y")) {//disabling previous condition
                    connection.updateQuery(storeConn, "update tbf0_pat_algy_hlth_cd set active_ind = 'N', deactivate_user_id = 568564313, deactivate_dttm = ? where pat_id = ? and PAT_ALLERGY_HEALTH_CD = ? and pat_code_type = ?",
                            "d::" + new Date(Calendar.getInstance().getTimeInMillis()), "s::" + patientId, "s::" + conditionId, "s::" + conditionCode);
                    String partition = findPartition(patientId);
                    if (partition.equals("")) {
                        LOGGER.error("Condition {} was not found in central", conditionId);
                        return false;
                    }
                    //execute this based on the correct partition
                    connection.updateQuery(centralConn, "update tbf" + partition + "_pat_algy_hlth_cd set active_ind = 'N', deactivate_user_id = 568564313, deactivate_dttm = ? where pat_id = ? and PAT_ALLERGY_HEALTH_CD = ? and pat_code_type = ?",
                            "d::" + new Date(Calendar.getInstance().getTimeInMillis()), "s::" + patientId, "s::" + conditionId, "s::" + conditionCode);
                    LOGGER.info("Removed health condition");
                } else {//reenabling
                    int store = connection.updateQuery(storeConn, "update tbf0_pat_algy_hlth_cd set active_ind = 'Y', deactivate_user_id = ?, deactivate_dttm = ? where pat_id = ? and PAT_ALLERGY_HEALTH_CD = ? and pat_code_type = ?",
                            "n::" + Types.INTEGER, "n::" + Types.DATE, "s::" + patientId, "s::" + conditionId, "s::" + conditionCode);
                    String partition = findPartition(patientId);
                    if (partition.equals("")) {
                        LOGGER.error("Not found in central");
                        return false;
                    }
                    //execute this based on the correct partition
                    int central = connection.updateQuery(centralConn, "update tbf" + partition + "_pat_algy_hlth_cd set active_ind = 'Y', deactivate_user_id = ?, deactivate_dttm = ? where pat_id = ? and PAT_ALLERGY_HEALTH_CD = ? and pat_code_type = ?",
                            "n::" + Types.INTEGER, "n::" + Types.DATE, "s::" + patientId, "s::" + conditionId, "s::" + conditionCode);
                    LOGGER.info("Reenabled health condition");
                }
            }
            //endregion
            LOGGER.info("Updating patient's PAT_ALGY_HLTH_TYPE_CD");
            return updatePatientHealthCode(patientId, storeNum);
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private boolean createAdditionalMedication(String storeNumber, String drugId, String patientId) {
        Connection storeConn = connection.connectToStoreDB(storeNumber);
        Connection centralConn = connection.connectToCentralDB();
        try {
            //this statement gives the abbreviation of the drug, which is needed for the insert
            ResultSet resultSet = connection.executeQuery(storeConn, "select PRODUCT_NAME_ABBR from tbf0_drug where drug_id = ?", "s::" + drugId);
            if (!resultSet.next()) return false;
            String drugAbbr = resultSet.getString(1);

            resultSet = connection.executeQuery(storeConn, "select * from tbf0_pat_external_rx_otc where pat_id = ? and drug_id = ?",
                    "s::" + patientId, "s::" + drugId);
            if (resultSet.next()) return false;

            //not already prescribed
            //this is the insert statement for local
            connection.updateQuery(storeConn,
                    "insert into tbf0_pat_external_rx_otc(active_ind, create_user_id, create_dttm, drug_id, prod_name_abbr, pat_id) values('Y',566767776, ? , ? ,? ,?)",
                    "d::" + new Date(Calendar.getInstance().getTimeInMillis()), "s::" + drugId, "s::" + drugAbbr, "s::" + patientId);//returns true if it goes through okay

            //PAT_EXT_RX_OTC_IND
            int local = connection.updateQuery(storeConn, "update tbf0_patient set PAT_EXT_RX_OTC_IND = 'Y' where pat_id = ?", "s::" + patientId);//returns true if it goes through okay
            //region Central Stuff
            //this next bit is for finding which tfb thing the patient is located in
            //then we can insert those values into the right partition
            String partition = findPartition(patientId);
            if (partition.equals("")) return false;
            //this is the statement for central
            connection.updateQuery(centralConn, "insert into tbf" + partition + "_pat_external_rx_otc(active_ind, create_user_id, create_dttm, drug_id, prod_name_abbr, pat_id) values('Y',566767776, ? , ? ,? ,?)",
                    "d::" + new Date(Calendar.getInstance().getTimeInMillis()), "s::" + drugId, "s::" + drugAbbr, "s::" + patientId); //update that stuff baby
            int central = connection.updateQuery(centralConn, "update tbf" + partition + "_patient set PAT_EXT_RX_OTC_IND = 'Y' where pat_id = ?", "s::" + patientId);//returns true if it goes through okay

            LOGGER.info("Patient {} was given the additional medication.", patientId);
            connection.wait(1);
            return (local != 0 && central != 0);
            //endregion
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    //central is divided into 4 smaller partiions. You can never be sure which one a patient is stored in however, so this will provide
    //the partition it is stored in
    private String findPartition(String patient) {
        return findPartition(connection.connectToCentralDB(), patient);
    }

    //central is divided into 4 smaller partiions. You can never be sure which one a patient is stored in however (since people
    //are constantly manually editing the db and it could be in any of the 4)
    //slightly faster than above becuase it takes in a pre exisitng connection
    private String findPartition(Connection storeConn, String patient) {
        return findPartition(storeConn, new String[]{"tbf1_patient", "tbf2_patient", "tbf3_patient", "tbf4_patient"}, patient);
    }

    private String findPartition(Connection storeConn, String[] tables, String patient) {
        try {
            ResultSet rs;
            for (int i = 0; i < tables.length; i++) {
                rs = connection.executeQuery(storeConn, "select * from " + tables[i] + " where pat_id = ?", "s::" + patient);
                if (rs.next()) {
                    return String.valueOf(i + 1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return "";
    }

    //find the partition based on the rx number instead of pat_id
    private String findRxPartition(Connection storeConn, String rx) {
        return findPartition(storeConn, new String[]{"tbf1_rx", "tbf2_rx", "tbf3_rx", "tbf4_rx"}, rx);
    }

    private boolean updatePatientHealthCode(String patId, String storeNum) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Connection centralConn = connection.connectToCentralDB();
        try {
            //query for getting all different types
            ResultSet rs = connection.executeQuery(storeConn, "select distinct pat_code_type from tbf0_pat_algy_hlth_cd where pat_id = ? and active_ind = 'Y'", "s::" + patId);

            int ap = 0;
            int ak = 0;
            int hm = 0;

            //gets all the results, and if it has the right type, set the appropriate variable
            while (rs.next()) {
                if (rs.getString(1).equals("AK"))
                    ak = 1;
                if (rs.getString(1).equals("AP"))//if allergy
                    ap = 2;
                if (rs.getString(1).equals("HM"))//if health condition
                    hm = 4;
            }
            //update patient's status code
            int status = connection.updateQuery(storeConn, "update tbf0_patient set PAT_ALGY_HLTH_TYPE_CD = ?, PAT_ALGY_HLTH_CD_INQ_IND = 'Y' where pat_id = ?",
                    "i::" + (ap + hm + ak), "s::" + patId);
            if (status == 0) {
                LOGGER.error("Something went wrong updated type_cd in local store");
                return false;
            }
            String partition = findPartition(patId);//get patient's partition location
            if (partition.equals("")) {
                LOGGER.error("Patient " + patId + " not found in central servers");
                return false;
            }
            status = connection.updateQuery(centralConn, "update tbf" + partition + "_patient set PAT_ALGY_HLTH_TYPE_CD = ?, PAT_ALGY_HLTH_CD_INQ_IND = 'Y' where pat_id = ?",
                    "i::" + (ap + hm + ak), "s::" + patId);
            if (status == 0) {
                LOGGER.error("Something went wrong updated type_cd in local store");
                return false;
            }
            LOGGER.info("Successfully updated PAT_ALGY_HLTH_TYPE_CD");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

}
