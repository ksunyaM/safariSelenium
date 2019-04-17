package com.wba.dataanalytics.api.test.common.custom;

import com.wba.test.utils.DataBaseUtils;
import com.wba.test.utils.DataStorage;
import com.wba.test.utils.kafka.EventStorage;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryDBAndHiveVerificationSteps {

    private DataBaseUtils db = new DataBaseUtils();
    private String EXISTING_ORDER_CODE = "select order_code, order_capturing_location_type, " +
            "order_capturing_location_number from order_line_by_order_code limit 1;";

    private String EXISTING_RX_DATA = "select prescription_code, rx_comments, coupon_app_code, rx_status " +
            "from disp_by_prescription_code limit 1;";

    private static DataStorage dataStorage = DataStorage.getInstance();

    @When("^existing data is extracted from the \"([^\"]*)\" db$")
    public void get_existing_data_from_db(String dbName) {
        List<Map<String, Object>> resultSet = getResultSet(dbName);
        if(dbName.equalsIgnoreCase("orderdata")) {
            for (Map<String,Object> map: resultSet) {
                dataStorage.add("orderCodeValue", map.get("order_code").toString());
                dataStorage.add("locationNumberValue", map.get("order_capturing_location_number").toString());
                dataStorage.add("locationTypeValue", map.get("order_capturing_location_type").toString());
            }
        } else if(dbName.equalsIgnoreCase("rxda")) {
            for (Map<String,Object> map: resultSet) {
                dataStorage.add("prescriptionCode", map.get("prescription_code").toString());
                dataStorage.add("rxComments", map.get("rx_comments").toString());
                dataStorage.add("couponAppCode", map.get("coupon_app_code").toString());
                dataStorage.add("rxStatus", map.get("rx_status").toString());
            }
        }
    }

    private List<Map<String, Object>> getResultSet(String db_name) {
        DataBaseUtils cassandra  = new DataBaseUtils(db_name);
        String query = "";
        if(db_name.equalsIgnoreCase("orderdata")) {
            query = EXISTING_ORDER_CODE;
        } else if(db_name.equalsIgnoreCase("rxdata")) {
            query = EXISTING_RX_DATA;
        }
        return cassandra.runCassandraQuery(query);
    }

    @When("^verify details in hive and kafka match for event \"([^\"]*)\"$")
    public void verify_details_in_hive_and_kafka_match(String eventName) {
        ArrayList<String> fieldsToCheck = new ArrayList<>();
        if (eventName.equalsIgnoreCase("PrescriptionCreated")) {
            fieldsToCheck.add("prescription.code");
            fieldsToCheck.add("prescription.rxComments.string");
            fieldsToCheck.add("prescription.rxCouponActualProductPack.string");
            fieldsToCheck.add("prescription.rxStatus");
        } else if(eventName.equalsIgnoreCase("RxIntakeERxNewRxStarted") ||
                eventName.equalsIgnoreCase("CreateERxMockup")) {
            fieldsToCheck.add("orderCode");
            fieldsToCheck.add("capturingLocation.locationType");
            fieldsToCheck.add("capturingLocation.locationNumber");
        }

        for(String field: fieldsToCheck) {
            String result = EventStorage.getInstance().findEvent(eventName).getBodyAttribute(field).toString();
            if (result.startsWith("\"")) {
                result = result.replace("\"","");
            }
            Assert.assertEquals(result, DataStorage.getInstance().unmask(result));
        }
    }
}
