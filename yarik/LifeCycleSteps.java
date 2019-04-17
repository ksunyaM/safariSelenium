package com.wba.rxdata.test;


import com.wba.test.utils.DataBaseUtils;
import cucumber.api.java.en.And;
import org.junit.Assert;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class LifeCycleSteps extends _BaseStep {

    private DataBaseUtils dbUtils = new DataBaseUtils();

    @And("^update the dispenseStatus to REVIEWED$")
    public void MakeDispensePrinted(){

        HashSet<UUID> set = new HashSet<UUID>();
        set.add(UUID.fromString(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code").toString()));

        String cql = "update disp_by_prescription_code set dispense_status ='REVIEWED' where prescription_code = ? and dispense_code= ?;";
        dbUtils.runCassandraQuery(cql, UUID.fromString(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code").toString()),UUID.fromString(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code").toString()));

        String cql1 = "delete from prescription_by_pat_code where prescription_code = ? and dispense_status = 'ENTERED' and patient_code = ?;";
        dbUtils.runCassandraQuery(cql1, UUID.fromString(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code").toString()),defineValue("rxPDataSnapshot0.prescription_by_code[0].spec_patient_code"));

        String cql2 = "INSERT INTO prescription_by_pat_code (patient_code, dispense_status,prescription_code, dispense_codes)VALUES (?,'REVIEWED',?,?);";
        dbUtils.runCassandraQuery(cql2,defineValue("rxPDataSnapshot0.prescription_by_code[0].spec_patient_code").toString(), UUID.fromString(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code").toString()),set);
        }


    @And("^verify if new record has been created$")
    public void checkForDispense() {
        String cql = "select *  from disp_by_prescription_code where  prescription_code= ?;";
        List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql,UUID.fromString(defineValue("rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code").toString()));
        Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
    }

}
