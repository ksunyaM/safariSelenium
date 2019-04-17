package com.wba.rxdata.test;

import com.wba.test.utils.DataBaseUtils;
import com.wba.test.utils.DataStorage;
import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.VerificationUtils;
import com.wba.test.utils.dbSnapshot.SnapshotUtils;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import org.junit.Assert;
import cucumber.api.java.en.Given;
import cucumber.runtime.CucumberException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.RowSet;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;

import com.wba.rxdata.test.RxPDataSnapshot;

import static com.oneleo.test.automation.core.DatabaseUtils.jdbc;

public class DataPreparationStep extends _BaseStep {

    private void prepare_select_query(String eventName) {
        String select = "select * from disp_by_prescription_code ";
        dataStorage.add("select_query~" + eventName, select);

        String selectJ = "select JSON * from disp_by_prescription_code ";
        dataStorage.add("selectJ_query~" + eventName, selectJ);
    }

    private void prepare_where_clause(String eventName) {
        String where =
                " where dispense_code='" + defineValue(eventName + ".dispenseCode");
        dataStorage.add("where~" + eventName, where);

    }

    @And("^validate close Prescription Reasons \"([^\"]*)\"")
    public void closePrescriptionValidations(String prescrptions) {
        Assert.assertEquals(defineValue(prescrptions + ".closeRequestedBy.employeeType"), "WAGBPO");
        Assert.assertEquals(defineValue(prescrptions + ".closeRequestedBy.employeeNumber"), "222111");
        Assert.assertEquals(defineValue(prescrptions + ".failureReasonCode"), "RXD-PNF-1000-0000");
        Assert.assertEquals(defineValue(prescrptions + ".failureMessage"), "Prescription with the Prescription code does not exist in the database");
    }

    @And("^validate close Prescription Reasons for \"([^\"]*)\"")
    public void closePrescriptionValidationsfor(String prescrptions) {
        Assert.assertEquals(defineValue(prescrptions + ".closeRequestedBy.employeeType"), "WAGBPO");
        Assert.assertEquals(defineValue(prescrptions + ".closeRequestedBy.employeeNumber"), "222111");
        Assert.assertEquals(defineValue(prescrptions + ".failureReasonCode"), "RXD-PSC-1000-0000");
        Assert.assertEquals(defineValue(prescrptions + ".failureMessage"), "Prescription with the Prescription code is already closed");
    }

    public void quantityRemainingForDelete(String snapshot) {

        StringBuffer sb = new StringBuffer(snapshot);
        int val = (int) sb.charAt(snapshot.length() - 1) - 1;
        sb.deleteCharAt(snapshot.length() - 1);
        sb.insert(snapshot.length() - 1, Character.getNumericValue(val));
        String snapshotOld = sb.toString();
        BigDecimal QRemaining = asBigDec(defineValue(snapshotOld + ".prescription_by_code[0].quantity_remaining").toString()).add(asBigDec(defineValue(snapshotOld + ".disp_by_prescription_code[0].quantity_dispensed").toString()));
        Assert.assertEquals(asBigDec(defineValue(snapshot + ".prescription_by_code[0].quantity_remaining").toString()), QRemaining);
    }

    @And("^new fields are Added Saved and Updated in RxPData Data Base \"([^\"]*)\"$")
    public void new_fields_are_Added_Saved_and_Updated_in_RxPData_Data_Base(String snapshotName) {
        new RxPDataSnapshot().snapshotCreateRxData();
        asMillSec("P.disp_by_prescription_code[0].deleted_datetime");
        Assert.assertEquals(defineValue("dispenseDelete.deletedBy.employeeNumber"), defineValue(snapshotName + ".disp_by_prescription_code[0].deleter"));
        Assert.assertEquals(defineValue("dispenseDelete.deletedBy.employeeType"), defineValue(snapshotName + ".disp_by_prescription_code[0].deleter_type"));
        Assert.assertEquals(defineValue("dispenseDelete.deletedReason.[\"com.wba.rxpdata.dispense.deleterequest.event.avro.DeletedReasonType\"]"), defineValue(snapshotName + ".disp_by_prescription_code[0].deleted_reason"));
        quantityRemainingForDelete(snapshotName);
    }

    @And("^verify the data for the closed Prescription for \"([^\"]*)\" and \"([^\"]*)\"$")
    public void checkClosedPrescription(String event, String snapShot) {
        Assert.assertEquals(defineValue(event + ".closedBy.employeeType"), defineValue(snapShot + ".rx_closer_type"));
        Assert.assertEquals(defineValue(event + ".closedBy.employeeNumber"), defineValue(snapShot + ".rx_closer"));
        Assert.assertEquals(defineValue(event + ".closedReason"), defineValue(snapShot + ".rx_closed_reason"));
        Assert.assertEquals(defineValue(event + ".closedComments"), defineValue(snapShot + ".rx_closed_comment"));
        Assert.assertEquals(defineValue(event + ".transferToPharmacyName"), defineValue(snapShot + ".transfer_to_pharmacy_name"));
        Assert.assertEquals(defineValue(event + ".transferToPharmacyPharmacistName"), defineValue(snapShot + ".transfer_to_pharmacy_pharmacist_name"));
        Assert.assertEquals(defineValue(event + ".transferToPharmacyPhoneNumber"), defineValue(snapShot + ".transfer_to_pharmacy_phone_number"));
        Assert.assertEquals(defineValue(event + ".transferToPharmacyStoreNumber"), defineValue(snapShot + ".transfer_to_pharmacy_store_number"));

    }

    @And("^verify RxStatus \"([^\"]*)\"$")
    public void verifyRxStatus(String snapShot) {
        Assert.assertEquals(defineValue(snapShot + ".rx_status").toString(), "CLOSED");
    }


    @And("^get the last dispense code for deleting \"([^\"]*)\"$")
    public void checkDeletedDispenseCodes(String snapshot) {
        String disp_by_prescription_code = snapshot + ".disp_by_prescription_code";
        int tableLength = asInt(defineValue(disp_by_prescription_code + ".length()").toString());
        String latestDispenseCode = defineValue(disp_by_prescription_code + "[0].dispense_code").toString();
        String latestCreatedDateTime = defineValue(disp_by_prescription_code + "[0].create_datetime").toString();
        for (int j = 0; j < tableLength; j++) {
            if (Timestamp.valueOf(defineValue(disp_by_prescription_code + "[" + j + "].create_datetime").toString().replace("Z", "")).after(Timestamp.valueOf(latestCreatedDateTime.replace("Z", "")))) {
                latestDispenseCode = defineValue(disp_by_prescription_code + "[" + j + "].dispense_code").toString();
                latestCreatedDateTime = defineValue(disp_by_prescription_code + "[" + j + "].create_datetime").toString();

            }
        }
        dataStorage.add("DeleteDispenseLastCode", latestDispenseCode);
    }

    @And("^Find not deleted Dispense \"([^\"]*)\"$")
    public void findNotDeltedDispense(String snapshotName) {
        new RxPDataSnapshot().snapshotCreateRxData();

        String disp_by_prescription_code = snapshotName + ".disp_by_prescription_code";
        int tableLength = asInt(defineValue(disp_by_prescription_code + ".length()").toString());
        for (int j = 0; j < tableLength; j++) {
            if (!defineValue(disp_by_prescription_code + "[" + j + "].dispense_code").toString().equals(dataStorage.unmask("DeleteDispenseLastCode"))) {
                dataStorage.add("NotDeletedDisepsneCode", defineValue(disp_by_prescription_code + "[" + j + "].dispense_code").toString());
                break;
            }
        }
    }


    @And("^Deleter Added Saved and Updated in RxPData Data Base \"([^\"]*)\"$")
    public void findDeletedDispense(String snapshotName) {
        new RxPDataSnapshot().snapshotCreateRxData();

        String disp_by_prescription_code = snapshotName + ".disp_by_prescription_code";
        int tableLength = asInt(defineValue(disp_by_prescription_code + ".length()").toString());
        int index = 0;
        for (int j = 0; j < tableLength; j++) {
            if (defineValue(disp_by_prescription_code + "[" + j + "].dispense_code").toString().equals(dataStorage.unmask("DeleteDispenseLastCode"))) {
                index = j;
            }
        }
        asMillSec("P.disp_by_prescription_code[" + index + "].deleted_datetime");
        Assert.assertEquals(defineValue("dispenseDelete.deletedBy.employeeNumber"), defineValue(snapshotName + ".disp_by_prescription_code[" + index + "].deleter"));
        Assert.assertEquals(defineValue("dispenseDelete.deletedBy.employeeType"), defineValue(snapshotName + ".disp_by_prescription_code[" + index + "].deleter_type"));
        Assert.assertEquals(defineValue("dispenseDelete.deletedReason.[\"com.wba.rxpdata.dispense.deleterequest.event.avro.DeletedReasonType\"]"), defineValue(snapshotName + ".disp_by_prescription_code[" + index + "].deleted_reason"));
    }

}