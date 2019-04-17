package com.wba.rxdata.test;

import com.wba.test.utils.ResourceUtils;
import com.wba.test.utils.dbSnapshot.SnapShotBuilder;
import com.wba.test.utils.dbSnapshot.SnapshotUtils;
import com.wba.test.utils.kafka.EventBuilder;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.assertj.core.util.Compatibility;

import java.util.*;
import java.util.stream.IntStream;

public class RxPDataSnapshot extends _BaseStep {

    public static final String RXP_DATA_SNAPSHOT = "rxPDataSnapshot";
    public static final String RXP_DATA_TWO_DISPENSE_SNAPSHOT = "RxPDataTwoDispenseSnapshot";
    public static final String RXP_DATA_FIVE_DISPENSE_SNAPSHOT = "RxPDataFiveDispenseSnapshot";
    public static final String RXP_DATA_ALL_FIELDS = "rxPDataSnapshotNM";
    public static final String DS_PRESCRIPTION_CODE = "~prescription_code";
    private static final String RXP_DATA_RESOURCE_NAME = "rxpData";
    private final SnapshotUtils rxpData = new SnapshotUtils().setResourceName(RXP_DATA_RESOURCE_NAME);
    private final SnapshotUtils rxpDataTD = new SnapshotUtils().setResourceName(RXP_DATA_TWO_DISPENSE_SNAPSHOT);
    private final SnapshotUtils rxpDataFD = new SnapshotUtils().setResourceName(RXP_DATA_FIVE_DISPENSE_SNAPSHOT);

    private SnapShotBuilder getBuilder() {

        UUID prescriptionCode = asUUID(DS_PRESCRIPTION_CODE);

        return new SnapShotBuilder().forCassandra().resourceName(RXP_DATA_RESOURCE_NAME).name(RXP_DATA_SNAPSHOT)
                .addItems("select json * from prescription_by_code where prescription_code = ? allow filtering;", prescriptionCode)
                .addItems("select json * from prescription_by_disp_code where prescription_code = ? allow filtering;", prescriptionCode)
                .addItems("select json * from prescription_by_pat_code where prescription_code = ? allow filtering;", prescriptionCode)
                .addItems("select json * from prescription_by_rx_number where prescription_code = ? allow filtering;", prescriptionCode)
                .addItems("select json * from disp_by_prescription_code where prescription_code = ? allow filtering;", prescriptionCode)
                ;
    }

    @And("get RxpData snapshot")
    public void snapshotCreateRxData() {
        rxpData.createSnapshotAndDiff(getBuilder());
    }

    private void snapshotInsert() {
        rxpData.insertSnapshot(eventStorage.getLastProduced().getBody());
    }

    private void snapshotRead() {
        String SNAPSHOT_FOLDER_PATH = RESOURCES_FOLDER_PATH + "snapshot/";
        String json = ResourceUtils.getResourceAsString(SNAPSHOT_FOLDER_PATH + RXP_DATA_SNAPSHOT + ".json");
        eventStorage.addProduced(new EventBuilder().body(json).name(new SnapshotUtils().getNextEventName(RXP_DATA_SNAPSHOT)).build());
    }

    private void snapshotRead(String snapshotName) {

        String SNAPSHOT_FOLDER_PATH = RESOURCES_FOLDER_PATH + "snapshot/";
        String json = ResourceUtils.getResourceAsString(SNAPSHOT_FOLDER_PATH + snapshotName + ".json");
        eventStorage.addProduced(new EventBuilder().body(json).name(new SnapshotUtils().getNextEventName(RXP_DATA_SNAPSHOT)).build());
    }

    private void snapshotReadF() {

        String SNAPSHOT_FOLDER_PATH = RESOURCES_FOLDER_PATH + "snapshot/";
        String json = ResourceUtils.getResourceAsString(SNAPSHOT_FOLDER_PATH + RXP_DATA_FIVE_DISPENSE_SNAPSHOT + ".json");
        eventStorage.addProduced(new EventBuilder().body(json).name(new SnapshotUtils().getNextEventName(RXP_DATA_SNAPSHOT)).build());
    }

    private void makeSnapshotUnique() {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();

        if (!dataStorage.has("~prescription_code")) { // create a new prescription
            String newPrescriptionCode = asStr("uuid::");
            data.put("prescription_code", newPrescriptionCode);
            dataStorage.add("~prescription_code", newPrescriptionCode);

            data.put("rx_number", asStr("random::7"));
            final String patientCode = asStr("uuid::");
            data.put("patient_code", patientCode);
            data.put("spec_patient_code", patientCode);
        } else { // add new dispense to existing prescription
            data.put("prescription_code", asStr("~prescription_code"));
            data.put("rx_number", asStr("~rx_number"));
            data.put("patient_code", asStr("~patient_code"));
            data.put("spec_patient_code", asStr("~patient_code"));
        }
        dataStorage.map().putIfAbsent("~~app_code", asStr("uuid::"));
        data.put("actual_product_pack_code", "~~app_code");
        data.put("coupon_actual_product_pack_code", "~~app_code");
        data.put("spec_prescribed_actual_product_pack_code", "~~app_code");

//        final String dispenseCode = asStr("uuid::");

        data.put("dispense_code", "~dispense_code");
        data.put("location_number", "~~locationNumber");
        data.put("location_type", "~~locationType");
        data.put("rx_comments", "auto test: added via snapshot " + asStr("timestamp::"));

        rxpData.updateSnapshot(data);
    }


    @Given("^dispense with statuses \"([^\"]*)\"$")
    public void dispensesWithStatuses(String statuses) {
        final RxPDataSnapshot rxPDataSnapshot = new RxPDataSnapshot();
        final String[] _statuses = statuses.split("\\s*,\\s*");
        final List<String> codes = new ArrayList<>();
        final Map<String, Integer> counter = new HashMap<>();
        IntStream.range(0, _statuses.length).forEach(i ->
        {
            codes.add(asStr("uuid::"));
            counter.putIfAbsent(_statuses[i], 0);
        });

        Map<String, Map<String, String>> dispenseByStatus = new HashMap<>();
        Map<String, String> dispenseByRxNumber = new HashMap<>();
        IntStream.range(0, _statuses.length).forEach(j -> {
            final String status = _statuses[j];
            final String code = codes.get(j);
            int c = counter.get(status);
            dispenseByStatus.putIfAbsent(status, new HashMap<>());
            dispenseByStatus.get(status).put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", c), code);
            dispenseByRxNumber.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", j), code);
            counter.put(status, ++c);
        });

        LOGGER.info("ok");


        for (int i = 0; i < _statuses.length; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("dispense_status", _statuses[i]);
            data.put("dispense_code", codes.get(i));
            data.putAll(dispenseByStatus.get(_statuses[i]));
            data.putAll(dispenseByRxNumber);
            data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
            data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());

            snapshotRead();
            makeSnapshotUnique();
            rxpData.updateSnapshot(data);
            snapshotInsert();
        }

        rxPDataSnapshot.snapshotCreateRxData();
    }


    // ================== RxPFulfillment
//
//    @Test
//    public void tmp() {
//        dataStorage.add(DS_PRESCRIPTION_CODE, "d93ff3ed-0fd3-4b42-9a0b-0b9030846c43");
//        snapshotCreateRxData();
//    }

    public String getDispenseRowByStatus(String status) {
        return new SnapshotUtils().getRowByValue("disp_by_prescription_code", "dispense_status", status, eventStorage.getLastSnapshotName(RXP_DATA_SNAPSHOT));
    }

    @Given("^load the snapshot for the \"([^\"]*)\" given Prescription$")
    public void callSnapshotForTransferPrescription(String prescription) {
        RxPDataSnapshot rxPDataSnapshot = new RxPDataSnapshot();
        if(!dataStorage.has(DS_PRESCRIPTION_CODE))
           dataStorage.add(DS_PRESCRIPTION_CODE, defineValue(prescription));
        rxPDataSnapshot.snapshotCreateRxData();
    }
    @Given("^the event has data as random prescriptionCode$")
    public void updatePrescriptionCode() {

        eventStorage.getLastProduced().replaceBodyAttribute("prescriptionCode", asStr("uuid::"));
    }
    @Given("^an existing prescription in RXPData database with RxStatus \"([^\"]*)\"$")
    public void prescriptionStatusinDB(String statuses) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        String dispenseCode = asStr("uuid::");
        String prescriptionCode = asStr("uuid::");
        data.put("prescription_code",prescriptionCode);
        data.put("dispense_code",dispenseCode);
        data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
        data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
        Object loc_num = defineValue( "random::7");
        data.put("location_number",loc_num.toString());
        data.put("location_type", "~~locationType");
        data.put("spec_location_number",loc_num.toString());
        data.put("spec_location_type","~~locationType");
        snapshotRead();
        makeSnapshotUnique();
        data.put("rx_status",statuses);
        rxpData.updateSnapshot(data);
        snapshotInsert();
    }


    @Given("^an existing prescription in RXPData database with \"([^\"]*)\"$")
    public void an_existing_prescription_in_RXPData_database_with(String type) throws Throwable {

        String dispenseCode = asStr("uuid::");
        LinkedHashMap<String, String> data;
        switch (type){
            case "normal":
                snapshotRead();
                makeSnapshotUnique();
                data = new LinkedHashMap<>();
                data.put("dispense_code",dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                Object loc_num = defineValue( "random::7");
                data.put("location_number",loc_num.toString());
                data.put("location_type", "~~locationType");
                data.put("spec_location_number",loc_num.toString());
                data.put("spec_location_type","~~locationType");
                data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
                data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
                rxpData.updateSnapshot(data);
                snapshotInsert();
                break;
            case "inadequateRemainingQuantity":
                snapshotRead();
                makeSnapshotUnique();
                data = new LinkedHashMap<>();
                data.put("dispense_code",dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                loc_num = defineValue( "random::7");
                data.put("location_number",loc_num.toString());
                data.put("location_type", "~~locationType");
                data.put("spec_location_number",loc_num.toString());
                data.put("spec_location_type","~~locationType");
                data.put("quantity_remaining","27");
                data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
                data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
                rxpData.updateSnapshot(data);
                snapshotInsert();
                break;
            case "unlimitedRefill":
                snapshotRead();
                makeSnapshotUnique();
                data = new LinkedHashMap<>();
                data.put("dispense_code",dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                loc_num = defineValue( "random::7");
                data.put("location_number",loc_num.toString());
                data.put("location_type", "~~locationType");
                data.put("spec_location_number",loc_num.toString());
                data.put("spec_location_type","~~locationType");
                data.put("spec_unlimited_fill","true");
                data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
                data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
                rxpData.updateSnapshot(data);
                snapshotInsert();
                break;
            case "TwoDispenses":
                snapshotRead(RXP_DATA_TWO_DISPENSE_SNAPSHOT);
                makeSnapshotUnique();
                data = new LinkedHashMap<>();
                data.put("dispense_code",dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_code", "prescription_by_disp_code"), dispenseCode);
                dispenseCode=asStr("uuid::");
                data.put("disp_by_prescription_code[1].dispense_code",dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "1"), dispenseCode);
                data.put(String.format("%s[1].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[1].dispense_code", "prescription_by_disp_code"), dispenseCode);
                loc_num = defineValue( "random::7");
                data.put("location_number",loc_num.toString());
                data.put("location_type", "~~locationType");
                data.put("spec_location_number",loc_num.toString());
                data.put("spec_location_type","~~locationType");
                data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
                data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
                rxpData.updateSnapshot(data);
                snapshotInsert();
                break;
            case "TwoPrescription":
                final String patientCode = asStr("uuid::");
                for(int i=0;i<2;i++) {
                    data = new LinkedHashMap<>();
                    snapshotRead(RXP_DATA_TWO_DISPENSE_SNAPSHOT);
                    dispenseCode = asStr("uuid::");
                    data.put("prescription_code", asStr("uuid::"));
                    data.put("rx_number", asStr("random::7"));
                    data.put("patient_code", patientCode);
                    data.put("spec_patient_code", patientCode);
                    data.put("location_number", "~~locationNumber");
                    data.put("location_type", "~~locationType");
                    data.put("rx_comments", "auto test: added via snapshot " + asStr("timestamp::"));
                    data.put("dispense_code", dispenseCode);
                    data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                    data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                    data.put(String.format("%s[0].dispense_code", "prescription_by_disp_code"), dispenseCode);
                    dispenseCode = asStr("uuid::");
                    data.put("disp_by_prescription_code[1].dispense_code", dispenseCode);
                    data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "1"), dispenseCode);
                    data.put(String.format("%s[1].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                    data.put(String.format("%s[1].dispense_code", "prescription_by_disp_code"), dispenseCode);
                    loc_num = defineValue("random::7");
                    data.put("location_number", loc_num.toString());
                    data.put("location_type", "~~locationType");
                    data.put("spec_location_number", loc_num.toString());
                    data.put("spec_location_type", "~~locationType");
                    data.put("spec_prescriber_code", dataStorage.unmask("PRESCRIBER_CODE").toString());
                    data.put("spec_prescriber_location_code", dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
                    rxpData.updateSnapshot(data);
                    snapshotInsert();
                }
                break;
            case "allFields":
                snapshotRead(RXP_DATA_ALL_FIELDS);
                makeSnapshotUnique();
                data = new LinkedHashMap<>();
                data.put("dispense_code",dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                loc_num = defineValue( "random::7");
                data.put("location_number",loc_num.toString());
                data.put("location_type", "~~locationType");
                data.put("spec_location_number",loc_num.toString());
                data.put("spec_location_type","~~locationType");
                data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
                data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
                rxpData.updateSnapshot(data);
                snapshotInsert();
                break;
            default:
                throw new Exception("No event defined");
        }

    }


    @Given("^an prescription in RXPData database with \"([^\"]*)\"$")
    public void an_prescription_in_RxPData_database_with(String type) throws Throwable {
        String dispenseCode = asStr("uuid::");
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("spec_prescriber_code",dataStorage.unmask("PRESCRIBER_CODE").toString());
        data.put("spec_prescriber_location_code",dataStorage.unmask("PRESCRIBER_LOCATION_CODE").toString());
        switch (type) {

            case "FiveDispenses":
                snapshotRead(RXP_DATA_FIVE_DISPENSE_SNAPSHOT);
                makeSnapshotUnique();
                String rxNumber = asStr("random::7");
                data.put("dispense_code", dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[0].dispense_code", "prescription_by_disp_code"), dispenseCode);
                data.put(String.format("%s[0].rx_number", "prescription_by_rx_number"), rxNumber);
                data.put(String.format("%s[0].rx_number", "disp_by_prescription_code"), rxNumber);
                dispenseCode = asStr("uuid::");
              //  rxNumber = asStr("random::7");
                data.put("disp_by_prescription_code[1].dispense_code", dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "1"), dispenseCode);
                data.put(String.format("%s[1].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[1].dispense_code", "prescription_by_disp_code"), dispenseCode);
               // data.put(String.format("%s[1].rx_number", "prescription_by_rx_number"), rxNumber);
                data.put(String.format("%s[1].rx_number", "disp_by_prescription_code"), rxNumber);
                dispenseCode = asStr("uuid::");
                //rxNumber = asStr("random::7");
                data.put("disp_by_prescription_code[2].dispense_code", dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "2"), dispenseCode);
                data.put(String.format("%s[2].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[2].dispense_code", "prescription_by_disp_code"), dispenseCode);
               // data.put(String.format("%s[2].rx_number", "prescription_by_rx_number"), rxNumber);
                data.put(String.format("%s[2].rx_number", "disp_by_prescription_code"), rxNumber);
                dispenseCode = asStr("uuid::");
               // rxNumber = asStr("random::7");
                data.put("disp_by_prescription_code[3].dispense_code", dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "3"), dispenseCode);
                data.put(String.format("%s[3].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[3].dispense_code", "prescription_by_disp_code"), dispenseCode);
               // data.put(String.format("%s[3].rx_number", "prescription_by_rx_number"), rxNumber);
                data.put(String.format("%s[3].rx_number", "disp_by_prescription_code"), rxNumber);
                dispenseCode = asStr("uuid::");
               // rxNumber = asStr("random::7");
                data.put("disp_by_prescription_code[4].dispense_code", dispenseCode);
                data.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "4"), dispenseCode);
                data.put(String.format("%s[4].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
                data.put(String.format("%s[4].dispense_code", "prescription_by_disp_code"), dispenseCode);
               // data.put(String.format("%s[4].rx_number", "prescription_by_rx_number"),rxNumber);
                data.put(String.format("%s[4].rx_number", "disp_by_prescription_code"), rxNumber);
                data.put("location_number", "random::7");
                data.put("location_type", "~~locationType");
                data.put("spec_unlimited_fill", "true");
                rxpData.updateSnapshot(data);
                snapshotInsert();
                break;
            default:
                throw new Exception("No event defined");
        }
    }

    @Given("^a prescription with \"([^\"]*)\" dispenses in RXPData database$")
    public void an_prescription_in_RxPData_database_with_status_and_quantity(String dispenseQuantityString, DataTable data) throws Throwable {
        Map<String, String> parameters = data.asMap(String.class, String.class);
        String dispenseCode = asStr("uuid::");
        dataStorage.add("~dispense_code", dispenseCode);
        LinkedHashMap<String, String> updateShapshotData;

        if (dispenseQuantityString.equals("1")) {
            snapshotRead();
        } else if (dispenseQuantityString.equals("5")) {
            snapshotRead(RXP_DATA_FIVE_DISPENSE_SNAPSHOT);
        } else if (dispenseQuantityString.equals("2")) {
            snapshotRead(RXP_DATA_TWO_DISPENSE_SNAPSHOT);
        }
        makeSnapshotUnique();
        updateShapshotData = new LinkedHashMap<>();
        //updateShapshotData.put("dispense_code",dispenseCode);
        //      updateShapshotData.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "0"), dispenseCode);
        //updateShapshotData.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "0"), dispenseCode);
//        dataStorage.add("~dispenseCode1", dispenseCode);
//        Object loc_num = defineValue( "random::7");
//        updateShapshotData.put("location_number",loc_num.toString());
//        updateShapshotData.put("location_type", "~~locationType");
//        updateShapshotData.put("spec_location_number",loc_num.toString());
//        updateShapshotData.put("spec_location_type","~~locationType");

        int dispenseQuantity = Integer.parseInt(dispenseQuantityString);
        for (int i=0; i < dispenseQuantity; i++) {
            dispenseCode=asStr("uuid::");
            dataStorage.add("~dispenseCode" + i, dispenseCode);
            updateShapshotData.put(String.format("disp_by_prescription_code[%d].dispense_code", i),dispenseCode);
            updateShapshotData.put(String.format("prescription_by_disp_code[%d].dispense_code", i),dispenseCode);
            updateShapshotData.put(String.format("%s[0].dispense_codes[%d]", "prescription_by_rx_number", i), dispenseCode);
            updateShapshotData.put(String.format("%s[%d].dispense_codes[0]", "prescription_by_pat_code", i), dispenseCode);
        }

        updateShapshotData.put("location_number", "59511");
        updateShapshotData.put("location_type", "Pharmacy");
        updateShapshotData.put("spec_location_number", "59511");
        updateShapshotData.put("spec_location_type", "Pharmacy");

//        if (!dispenseQuantityString.equals("1")) {
//            dispenseCode=asStr("uuid::");
//            updateShapshotData.put("disp_by_prescription_code[1].dispense_code",dispenseCode);
//            updateShapshotData.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_rx_number", "1"), dispenseCode);
//            updateShapshotData.put(String.format("%s[0].dispense_codes[%s]", "prescription_by_pat_code", "1"), dispenseCode);
//        }

        updateShapshotData.putAll(parameters);

//        updateShapshotData.put("rx_status", status);
//        updateShapshotData.put("quantity_remaining", quantity);

        rxpData.updateSnapshot(updateShapshotData);
        snapshotInsert();

        dataStorage.add("~prescriptionCode1", asStr("~prescription_code"));
    }
}


