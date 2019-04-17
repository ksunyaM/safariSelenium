package com.wba.rxdata.test;
import com.datastax.shaded.json.JSONException;
import com.datastax.shaded.json.JSONObject;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wba.test.utils.DataBaseUtils;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.mocks.MockUtils;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;



public class RxDataStep extends _BaseStep {



    private DataBaseUtils dbUtils = new DataBaseUtils();


    @Before
    public void init() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant ins = timestamp.toInstant();
        dataStorage.add("~SINCE_TIME", ins.toString());
        dataStorage.add("uniqueIDforCorrelation", UUID.randomUUID());
    }

    private long convLong(Object value){
        return Double.valueOf(value.toString()).longValue();
    }

    private HashMap<String,String> jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }
        return map;

    }
    private void validateExternalIdentifiersWithoutSchema(String eventName, String eventDBData) throws  JSONException{
        if((defineValue(eventDBData + ".external_source"))!=null && !(defineValue(eventName + ".externalSource[\"com.wba.rxpdata.prescription.external.transferin.event.avro.ExternalSource\"]")).equals(eventName+".externalSource[\"com.wba.rxpdata.prescription.external.transferin.event.avro.ExternalSource\"]"))
         Assert.assertEquals(defineValue(eventName + ".externalSource[\"com.wba.rxpdata.prescription.external.transferin.event.avro.ExternalSource\"]"), defineValue(eventDBData + ".external_source"));
        else
         Assert.assertEquals(defineValue(eventName + ".externalSource"), defineValue(eventDBData + ".external_source"));
        if((defineValue(eventDBData + ".external_identifiers"))!=null) {
            HashMap<String,String> values = jsonToMap((defineValue(eventDBData + ".external_identifiers").toString()));
            for(int i=0;i< values.keySet().size();i++) {
                for (String key : values.keySet()) {
                    if(defineValue(eventName + ".externalIdentifiers["+i+"].name").toString().equals(key)){
                        Assert.assertEquals(defineValue(eventName + ".externalIdentifiers["+i+"].value"),values.get(key));
                    }
                }
            }
        }
    }

    @When("^update the event with the PrescriberDetails for Event \"([^\"]*)\"$")
    public void updatePrescriberDetails(String eventName)  {
        eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.prescriberCode", dataStorage.unmask("PRESCRIBER_CODE"));
        eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.prescriberLocationCode",dataStorage.unmask("PRESCRIBER_LOCATION_CODE"));
    }


    @And("^store the \"([^\"]*)\" RxNumber with \"([^\"]*)\" data$")
    public void get_previous_rxNumber(String value,String externalPrescription){
         if(value.equals("old") | value.equals("min") ) {
             String cql = "select last_taken_seq_number  from seq_num_generator where  seq_name= ?;";
             List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql,"rx_Number"+"|"+defineValue(externalPrescription+".dispense.location.locationNumber")+"|"+defineValue(externalPrescription+".dispense.location.locationType"));

             if(dbrows.size() == 0)
                dataStorage.add("rxSequenceNumber", 1999999);
             else if( dbrows.get(0).get("last_taken_seq_number").equals("9499999"))
                 dataStorage.add("rxSequenceNumber", 0);
             else
                dataStorage.add("rxSequenceNumber", dbrows.get(0).get("last_taken_seq_number"));
        }
        if(value.equals("max")){
            String cql = "update seq_num_generator set last_taken_seq_number = 9499999 where seq_name= ?;";
            dbUtils.runCassandraQuery(cql,"rx_Number"+"|"+defineValue(externalPrescription+".dispense.location.locationNumber")+"|"+defineValue(externalPrescription+".dispense.location.locationType"));
            dataStorage.add("rxSequenceNumber", 0);
        }
          if(value.equals("newloc")){

              eventStorage.getLastProduced().replaceBodyAttribute("dispense.location.locationNumber","LOC::4");
              eventStorage.getLastProduced().replaceBodyAttribute("dispense.location.locationType","Pharmacy");
              eventStorage.getLastProduced().replaceBodyAttribute("prescription.locationIdentifier.locationNumber",defineValue(externalPrescription+".dispense.location.locationNumber").toString());
              eventStorage.getLastProduced().replaceBodyAttribute("prescription.locationIdentifier.locationType","Pharmacy");
              dataStorage.add("rxSequenceNumber", 1999999);
          }
        if(value.equals("new")){
            String cql = "select last_taken_seq_number  from seq_num_generator where  seq_name= ?;";
            List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql,"rx_Number"+"|"+defineValue(externalPrescription+".dispense.location.locationNumber")+"|"+defineValue(externalPrescription+".dispense.location.locationType"));

            Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
            dataStorage.add("rxSequenceNumberNew", asInt(dbrows.get(0).get("last_taken_seq_number").toString()));
        }
    }

    @And("^Verify that no record is created in the database for \"([^\"]*)\"$")
    public void checkNewEntryDB(String valueGot){

        String[] paramsVals = valueGot.split(",");
        String value = paramsVals[0];
        String params = "" ;
         if(paramsVals.length > 1)
             params =  paramsVals[1];

        if(value.equals("Refill")){
            new RxPDataSnapshot().snapshotCreateRxData();
            Assert.assertEquals(1,defineValue("rxPDataSnapshot1.disp_by_prescription_code.length()"));
        }else  if(value.equals("Transfer")){
            String cql = "select json * from prescription_by_code where  external_identifiers contains ? Allow filtering;";
            List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql, defineValue(params+".prescription.externalIdentifiers[0].value").toString());
            Assert.assertEquals("Check for Row count should be ONE", 0, dbrows.size());
        }
    }


    @And("^update the event \"([^\"]*)\" with the \"([^\"]*)\" location$")
    public void updateLocation(String eventName,String dispLoc){
        if(dispLoc.equals("same")) {
            eventStorage.findEvent(eventName).replaceBodyAttribute("dispensingLocation.locationNumber", defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].location_number"));
            eventStorage.findEvent(eventName).replaceBodyAttribute("dispensingLocation.locationType", defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].location_type"));
        }else if(dispLoc.equals("diffOld")){
            eventStorage.findEvent(eventName).replaceBodyAttribute("dispensingLocation.locationNumber", defineValue("random::3"));
            String cql = "insert into seq_num_generator(seq_name,last_taken_seq_number) values(?,2002000);";
            dbUtils.runCassandraQuery(cql,"rx_Number"+"|"+defineValue(eventName+".dispensingLocation.locationNumber")+"|"+defineValue(eventName+".dispensingLocation.locationType"));
        }else if(dispLoc.equals("diffNewLoc")){
            eventStorage.findEvent(eventName).replaceBodyAttribute("dispensingLocation.locationNumber", defineValue("random::3"));
        }else if(dispLoc.equals("diffMax")){
            eventStorage.findEvent(eventName).replaceBodyAttribute("dispensingLocation.locationNumber", defineValue("random::3"));
            String cql = "update seq_num_generator set last_taken_seq_number = 9499999 where seq_name= ?;";
            dbUtils.runCassandraQuery(cql,"rx_Number"+"|"+defineValue(eventName+".dispensingLocation.locationNumber")+"|"+defineValue(eventName+".dispensingLocation.locationType"));
        }
    }


    // This will fail if multiple events come as updating happens as a batch
    @And("^verify rxNumber has been updated$")
    public void verify_rxNumber(){
        if(asInt(dataStorage.unmask("rxSequenceNumber").toString()) == 9499999)
            Assert.assertEquals(1,dataStorage.unmask("rxSequenceNumberNew"));
        else
            //will fail when multiple events come
            Assert.assertEquals(asInt(dataStorage.unmask("rxSequenceNumber").toString())+1,(dataStorage.unmask("rxSequenceNumberNew")));
    }


    @And("^verify the published RxNumber for \"([^\"]*)\"$")
    public void verfiyEventRxNumber(String eventName){
        String disp_by_prescription_code = "rxPDataSnapshot0.disp_by_prescription_code[0]";
        Assert.assertEquals(defineValue(disp_by_prescription_code + ".rx_number"), (defineValue(eventName + ".dispense.rxNumber")));
    }

    @Then("^the response body contains \"([^\"]*)\"$")
    public void the_response_body_contains(String message) {
        String field = "<PATIENT_CODE_VALUE>";
        if (message.contains("<PATIENT_CODE_VALUE>")) {
            message = message.replace(field, dataStorage.unmask("patient_code").toString());
        }
        Assert.assertEquals(message, eventStorage.getLastConsumed().getBodyAttribute("message", String.class));

    }

    @And("^Load the Persist Data of \"([^\"]*)\" into the Event \"([^\"]*)\" with parameters \"([^\"]*)\"$")
    public void loadPersisitDataToEvent(String loadEvent, String event, String params)  {

        switch (loadEvent) {
            case "PrescriptionByRxNumber":
                String cql = "select json * from prescription_by_rx_number where prescription_code =? allow Filtering;";
                List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql,UUID.fromString(defineValue(params).toString()));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(event).build());
                break;
            case "PrescriptionByExternalRxNumber":
                cql = "select json * from prescription_by_code where  external_identifiers contains ? Allow filtering;";
                dbrows = dbUtils.runCassandraQuery(cql, defineValue(params+".prescription.externalIdentifiers[0].value").toString());
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(event).build());
                break;

            case "DispenseByPrescriptionCode":
                cql = "select json * from disp_by_prescription_code where prescription_code = ?;";
                dbrows = dbUtils.runCassandraQuery(cql,UUID.fromString(defineValue(params).toString()));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(event).build());
                break;
            case "GetPrescription":
                cql = "select json * from  prescription_by_code where prescription_code=? ALLOW FILTERING";
                dbrows = dbUtils.runCassandraQuery(cql, UUID.fromString(defineValue(params).toString()));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(event).build());
                break;
            default:

            }
        }


    @And("^Load the Persist Data for event \"([^\"]*)\" in \"([^\"]*)\"$")
    public void loadThePersistDataForEventIntoEvent(String loadEvent, String name) throws Throwable {
        switch (loadEvent) {
            case "prescription":
                String cql = "select json * from prescription_by_code where prescription_code = ?;";
                List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql,UUID.fromString(defineValue("prescription.prescription.prescription_code").toString()));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(name).build());
                break;
            case "dispense":

                cql = "select json * from disp_by_prescription_code where prescription_code = ?;";
                dbrows = dbUtils.runCassandraQuery(cql,UUID.fromString(defineValue("rxPDataSnapshot1.prescription_by_code[0].prescription_code").toString()));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(name).build());
                break;
            case "refill":

                cql = "select json * from disp_by_prescription_code where prescription_code=? AND dispense_number=? ALLOW FILTERING";
                dbrows = dbUtils.runCassandraQuery(cql, UUID.fromString(defineValue("rxPDataSnapshot0.prescription_by_code[0].prescription_code").toString()),((int)Double.parseDouble(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].dispense_number").toString())+1));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(name).build());
                break;
            case "refillPrescription":
                cql = "select json * from  prescription_by_code where prescription_code=? ALLOW FILTERING";
                dbrows = dbUtils.runCassandraQuery(cql, UUID.fromString(defineValue("prescription.prescription.prescription_code").toString()));
                Assert.assertEquals("Check for Row count should be ONE", 1, dbrows.size());
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(0).get("[json]").toString()).name(name).build());
                break;
            case "multipleDispenses":
                cql = "select json * from disp_by_prescription_code where prescription_code = ?;";
                dbrows = dbUtils.runCassandraQuery(cql,UUID.fromString(defineValue("prescription.prescription.prescription_code").toString()));
                Assert.assertNotEquals("Check for Row count should be more than zero", 0, dbrows.size());
//                dbrows.sort(Comparator.comparing());
                for(int i=0;i<dbrows.size();i++)
                eventStorage.addProduced(new EventBuilder().body(dbrows.get(i).get("[json]").toString()).name(name+i).build());
                break;

            default:
                throw new Exception("No event defined");
        }
    }


    @Given("^updating the event \"([^\"]*)\" with the dispenseType data \"([^\"]*)\"$")
    public void dispenseTypePrescription(String eventName,String dispenseType){
        switch (dispenseType) {
            case "unlimitedRefill":

                eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.unlimitedFill", "boolean::true");
                eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.externalQuantityRemaining", "double::90");

                break;

            case "inadequateRemainingQuantity":
                eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.unlimitedFill", "boolean::false");
                eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.externalQuantityRemaining", "double::27");
                break;

            case "normal":
                eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.unlimitedFill", "boolean::false");
                eventStorage.findEvent(eventName).replaceBodyAttribute("prescription.externalQuantityRemaining", "double::120");
                break;
            default:
        }

        }

    @Then("^verify failed dispense for \"([^\"]*)\" has data$")
    public void verifyErrorCodeAndMessage(String failedEvent, DataTable data) throws Throwable {
        Map<String, String> event = new HashMap<>();
        data.asMap(String.class, String.class).forEach((k, v) -> event.put(k, defineValue(v).toString()));
        String failureReasonCode = event.get("failureReasonCode");
        String failureMessage = event.get("failureMessage");

        Assert.assertEquals(defineValue(failedEvent + ".failureReasonCode"), failureReasonCode);
        Assert.assertEquals(defineValue(failedEvent+ ".failureMessage"),failureMessage);

    }


    @Then("^verify new \"([^\"]*)\" refill has been created with data$")
    public void verify_new_refill_has_been_created_with_data(String refillType, DataTable data) throws Throwable {
        Map<String, String> event = new HashMap<>();
        data.asMap(String.class, String.class).forEach((k, v) -> event.put(k, defineValue(v).toString()));
        new RxPDataSnapshot().snapshotCreateRxData();
        String snapshotName = event.get("snapshot");
        String eventName = event.get("event");
        String firstSnapshot="rxPDataSnapshot0";
        String dispLoc = event.get("dispLoc");
        String prescriber = event.get("prescriber");
        String dispense;
        // snapshot provides the two dispenses in unsorted order. so taking the 2nd Dispense from snapshot
        if(defineValue(snapshotName+".disp_by_prescription_code[0].create_datetime").toString().equals(defineValue(firstSnapshot+".disp_by_prescription_code[0].create_datetime").toString()))
            dispense = ".disp_by_prescription_code[1]";
        else
            dispense = ".disp_by_prescription_code[0]";
        //adding the dispenseCode to dataStorage for finding the new Refill Dispense in case of publish event
        dataStorage.add("refillDispenseCode",defineValue(snapshotName+dispense+".dispense_code"));
        //For 2 sold life cycle
        dataStorage.add("dispCode",defineValue(snapshotName+dispense+".dispense_code"));
        dataStorage.add("rxNumberRefill",defineValue(snapshotName+dispense+".rx_number"));


           checkPrescriberDetailsinDispense(prescriber, snapshotName + dispense);
        Assert.assertEquals(defineValue(snapshotName+dispense+".prescription_code"),defineValue(firstSnapshot+".disp_by_prescription_code[0].prescription_code"));
        Assert.assertEquals(defineValue(snapshotName+dispense+".dispense_status"),"REVIEWED");

        Assert.assertEquals(defineValue(snapshotName + dispense + ".data_enterer"), defineValue(eventName + ".dataEnteredBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapshotName + dispense + ".data_enterer_type"), defineValue(eventName + ".dataEnteredBy.employeeType"));

        Assert.assertEquals(defineValue(snapshotName + dispense + ".product_data_reviewer"), null);
        Assert.assertEquals(defineValue(snapshotName + dispense + ".product_data_reviewer_type"),null);

        Assert.assertEquals(defineValue(snapshotName + dispense + ".pharmacist_of_record"), defineValue(eventName + ".pharmacistOfRecord.employeeNumber"));
        Assert.assertEquals(defineValue(snapshotName + dispense + ".pharmacist_of_record_employee_type"), defineValue(eventName + ".pharmacistOfRecord.employeeType"));

        if(defineValue(eventName+".refillDispense")!=null)
            Assert.assertEquals(defineValue(snapshotName+dispense+".days_supply"),defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].daysSupply"));
        else
            Assert.assertEquals(defineValue(snapshotName+dispense+".days_supply"),defineValue(firstSnapshot+".disp_by_prescription_code[0].days_supply"));
        if(defineValue(eventName+".refillDispense")!=null && defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].generalRecipientNumber")!=null)
            Assert.assertEquals(defineValue(snapshotName+dispense+".general_recipient_number"),defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].generalRecipientNumber.string"));
        else
            Assert.assertEquals(defineValue(snapshotName+dispense+".general_recipient_number"), defineValue(firstSnapshot+".disp_by_prescription_code[0].general_recipient_number"));
        if(defineValue(eventName+".refillDispense")!=null && defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].payCode")!=null)
            Assert.assertEquals(defineValue(snapshotName+dispense+".pay_code"),defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].payCode.string"));
        else
            Assert.assertEquals(defineValue(snapshotName+dispense+".pay_code"), defineValue(firstSnapshot+".disp_by_prescription_code[0].pay_code"));
        if(defineValue(eventName+".refillDispense")!=null && defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].paymentMethod")!=null)
            Assert.assertEquals(defineValue(snapshotName+dispense+".payment_method"),defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].paymentMethod[\"com.wba.rxpdata.dispense.refill.create.event.avro.PaymentMethod\"]"));
        else
            Assert.assertEquals(defineValue(snapshotName+dispense+".payment_method"), defineValue(firstSnapshot+".disp_by_prescription_code[0].payment_method"));
        if(defineValue(eventName+".refillDispense")!=null && defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].planCode")!=null)
            Assert.assertEquals(defineValue(snapshotName+dispense+".plan_code"),defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].planCode.string"));
        else
            Assert.assertEquals(defineValue(snapshotName+dispense+".plan_code"), defineValue(firstSnapshot+".disp_by_prescription_code[0].plan_code"));



        Assert.assertEquals(defineValue(snapshotName + dispense + ".location_number"), defineValue(eventName + ".dispensingLocation.locationNumber"));
        Assert.assertEquals(defineValue(snapshotName + dispense + ".location_type"), defineValue(eventName + ".dispensingLocation.locationType"));


        if(dispLoc.equals("diffOld")||dispLoc.equals("diffNewLoc")|| dispLoc.equals("diffMax")){
            Assert.assertEquals(defineValue(snapshotName+dispense+".dispense_number").toString(),"1.0");
            String cql = "select last_taken_seq_number  from seq_num_generator where  seq_name= ?;";
            List<Map<String, Object>> dbrows = dbUtils.runCassandraQuery(cql,"rx_Number"+"|"+defineValue(snapshotName+dispense+".location_number")+"|"+defineValue(snapshotName+dispense+".location_type"));
           if(dispLoc.equals("diffMax")){
               Assert.assertEquals(defineValue(snapshotName + dispense + ".rx_number"),"0000001");
               Assert.assertEquals(1, asInt(dbrows.get(0).get("last_taken_seq_number").toString()),0);
           }else {
               if(String.valueOf((asInt(dbrows.get(0).get("last_taken_seq_number").toString()))).length()<7){
                   Assert.assertEquals(defineValue(snapshotName + dispense + ".rx_number"),"000000"+String.valueOf((asInt(dbrows.get(0).get("last_taken_seq_number").toString()))));
               }else
               Assert.assertEquals(defineValue(snapshotName + dispense + ".rx_number"), String.valueOf((asInt(dbrows.get(0).get("last_taken_seq_number").toString()))));
           }
        } else if(dispLoc.equals("same")){
            Assert.assertEquals(defineValue(snapshotName+dispense+".dispense_number"),Double.parseDouble(defineValue(firstSnapshot+".disp_by_prescription_code[0].dispense_number").toString())+1);
            Assert.assertEquals(defineValue(snapshotName+dispense+".rx_number"),defineValue(firstSnapshot+".disp_by_prescription_code[0].rx_number"));
        }

        double remainingQuantity = 0;


        if(!refillType.equals("inadequateRemainingQuantity"))
            remainingQuantity =  Double.parseDouble(defineValue(snapshotName+".prescription_by_code[0].quantity_remaining").toString());
        double originalQuantity = Double.parseDouble(defineValue(firstSnapshot+".prescription_by_code[0].quantity_remaining").toString());
        double quantityDispensed = Double.parseDouble(defineValue(snapshotName+dispense+".quantity_dispensed").toString());
        double refillQuantityDispensed;
        if(defineValue(eventName+".refillDispense") !=null)
          refillQuantityDispensed = Double.parseDouble(defineValue(eventName+".refillDispense[\"com.wba.rxpdata.dispense.refill.create.event.avro.RefillDispense\"].quantityDispensed").toString());
        else
          refillQuantityDispensed = Double.parseDouble(defineValue(firstSnapshot+".disp_by_prescription_code[0].quantity_dispensed").toString());

        switch (refillType) {
            case "unlimitedRefill":
                Assert.assertEquals(remainingQuantity, originalQuantity,0);
                Assert.assertEquals(quantityDispensed, refillQuantityDispensed,0);
                break;
            case "inadequateRemainingQuantity":
              //  Assert.assertEquals(remainingQuantity, 0);
                Assert.assertEquals(quantityDispensed, originalQuantity,0);
                break;
            case "normal":
                Assert.assertEquals(remainingQuantity, Precision.round(originalQuantity - refillQuantityDispensed,3),0);
                Assert.assertEquals(quantityDispensed, refillQuantityDispensed,0);
                break;
            default:
                throw new Exception("No refill type defined");
        }

    }


    //Extra method needed for checking the Prescription Data as the Transfer Prescription Event Fields are Different than Publish Prescription.
    @Then("^verify the event response \"([^\"]*)\" and the persist data \"([^\"]*)\" for prescription TransferPrescription \"([^\"]*)\" Type$")
    public void verify_the_event_response_and_the_persist_data_for_new_prescription(String eventName,String eventDBData,String dispenseType) throws JSONException{

        double quantityRemainingDB =  Double.parseDouble(defineValue(eventDBData + ".quantity_remaining").toString());
        double quantityRemainingEvent =  Double.parseDouble(defineValue(eventName + ".externalQuantityRemaining").toString());

        String[] eventName_Disp = eventName.split("\\.");
        double quanityDispensed = Double.parseDouble(defineValue(eventName_Disp[0]+ ".dispense.quantityDispensed").toString());
        switch (dispenseType) {
            case "unlimitedRefill":
                Assert.assertEquals(quantityRemainingEvent,quantityRemainingDB,0);
                 break;
            case "inadequateRemainingQuantity":
                //   the quantity dispensed is getting updated with the remaining quantity when the  quantityRemaining < dispensedQuantity before the refill dispense data is updated in the DB
                Assert.assertEquals(0, quantityRemainingDB,0);
                  break;
            case "normal":
                Assert.assertEquals(Precision.round(quantityRemainingEvent-quanityDispensed,3), quantityRemainingDB,0);
                 break;
            default:
                break;
        }

//        Assert.assertTrue(UUID.fromString(defineValue(eventDBData + ".prescription_code").toString()) instanceof UUID);

        Assert.assertEquals(false,defineValue(eventDBData+".spec_immutable"));
            Assert.assertEquals(defineValue(eventName+".externalQuantityRemaining"),defineValue(eventDBData+".external_quantity_remaining"));
        validateExternalIdentifiersWithoutSchema(eventName,eventDBData);
        if(defineValue(eventName+".externalDataEnteredBy")!=null) {
            Assert.assertEquals(defineValue(eventName + ".externalDataEnteredBy.firstName"), defineValue(eventDBData + ".external_data_enterer_first_name"));
            Assert.assertEquals(defineValue(eventName + ".externalDataEnteredBy.lastName"), defineValue(eventDBData + ".external_data_enterer_last_name"));
        }
        if(defineValue(eventDBData + ".external_data_enterer_initials")!=null)
            Assert.assertEquals(defineValue(eventName + ".externalDataEnteredBy.initials.string"), defineValue(eventDBData + ".external_data_enterer_initials"));
        if(defineValue(eventDBData + ".external_data_enterer_middle_initial")!=null)
            Assert.assertEquals(defineValue(eventName + ".externalDataEnteredBy.middleNameInitial.string"), defineValue(eventDBData + ".external_data_enterer_middle_initial"));
        if(defineValue(eventDBData + ".external_data_enterer_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(eventName+".externalDataEnteredDateTime.long"))),asMillSec(defineValue(eventDBData+".external_data_enterer_datetime")));
        if(defineValue(eventDBData+".external_last_dispense_entered_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(eventName+".externalLastDispenseEnteredDateTime.long"))),asMillSec(defineValue(eventDBData+".external_last_dispense_entered_datetime")));
        if(defineValue(eventName+".externalLastPatientPrescriberDataReviewedBy")!=null) {
            Assert.assertEquals(defineValue(eventName + ".externalLastPatientPrescriberDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].firstName"), defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_first_name"));
            Assert.assertEquals(defineValue(eventName + ".externalLastPatientPrescriberDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].lastName"), defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_last_name"));
        }
        if(defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_initials")!=null)
           Assert.assertEquals(defineValue(eventName + ".externalLastPatientPrescriberDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].initials.string"), defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_initials"));
        if(defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_middle_initial")!=null)
           Assert.assertEquals(defineValue(eventName + ".externalLastPatientPrescriberDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].middleNameInitial.string"), defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_middle_initial"));
        if(defineValue(eventDBData + ".external_last_patient_prescriber_data_reviewer_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(eventName+".externalLastPatientPrescriberDataReviewedDateTime.long"))),asMillSec(defineValue(eventDBData+".external_last_patient_prescriber_data_reviewer_datetime")));
        if(defineValue(eventName+".externalLastProductDataReviewedBy")!=null) {
            Assert.assertEquals(defineValue(eventName + ".externalLastProductDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].firstName"), defineValue(eventDBData + ".external_last_product_data_reviewer_first_name"));
            Assert.assertEquals(defineValue(eventName + ".externalLastProductDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].lastName"), defineValue(eventDBData + ".external_last_product_data_reviewer_last_name"));
        }
        if(defineValue(eventDBData + ".external_last_product_data_reviewer_initials")!=null)
            Assert.assertEquals(defineValue(eventName + ".externalLastProductDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].initials.string"), defineValue(eventDBData + ".external_last_product_data_reviewer_initials"));
        if(defineValue(eventDBData + ".external_last_product_data_reviewer_middle_initial")!=null)
            Assert.assertEquals(defineValue(eventName + ".externalLastProductDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].middleNameInitial.string"), defineValue(eventDBData + ".external_last_product_data_reviewer_middle_initial"));
        if(defineValue(eventDBData + ".external_last_product_data_reviewer_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(eventName+".externalLastProductDataReviewedDateTime.long"))),asMillSec(defineValue(eventDBData+".external_last_product_data_reviewer_datetime")));
        if(defineValue(eventName + ".externalRxScannedBy")!=null) {
            Assert.assertEquals(defineValue(eventName + ".externalRxScannedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].firstName"), defineValue(eventDBData + ".external_rx_scanner_first_name"));
            Assert.assertEquals(defineValue(eventName + ".externalRxScannedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].lastName"), defineValue(eventDBData + ".external_rx_scanner_last_name"));
        }
        if(defineValue(eventDBData + ".external_rx_scanner_initials")!=null)
            Assert.assertEquals(defineValue(eventName + ".externalRxScannedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].initials.string"), defineValue(eventDBData + ".external_rx_scanner_initials"));
        if(defineValue(eventDBData + ".external_rx_scanner_middle_initial")!=null)
            Assert.assertEquals(defineValue(eventName + ".externalRxScannedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.Name\"].middleNameInitial.string"), defineValue(eventDBData + ".external_rx_scanner_middle_initial"));
        if(defineValue(eventName + ".specOriginalScannedDateTime")!=null)
        Assert.assertEquals(asMillSec(convLong(defineValue(eventName+".specOriginalScannedDateTime.long"))),asMillSec(defineValue(eventDBData+".spec_original_scanned_datetime")));
        if(defineValue(eventDBData + ".rx_authorizer")!=null)
        Assert.assertEquals(defineValue(eventName+".authorizer.string"),defineValue(eventDBData+".rx_authorizer"));
        Assert.assertEquals(defineValue(eventName+".autoRefill"),defineValue(eventDBData+".auto_refill"));
        if(defineValue(eventName+".comments") != null)
            Assert.assertEquals(defineValue(eventName+".comments.string"),defineValue(eventDBData+".rx_comments"));
        Assert.assertEquals(defineValue(eventName+".genericSubstitutePreference"),defineValue(eventDBData+".generic_substitute_preference"));
        if(defineValue(eventName+".intermediaryAuthorizationId") != null)
            Assert.assertEquals(defineValue(eventName+".intermediaryAuthorizationId.string"),defineValue(eventDBData+".intermediary_authorization_id"));
        Assert.assertEquals(defineValue(eventName+".printGenericProductName"),defineValue(eventDBData+".print_generic_product_name"));
        Assert.assertEquals(defineValue(eventName+".printSavings"),defineValue(eventDBData+".print_savings"));
        if(defineValue(eventName+".publicSafetyDepartmentNumber") != null)
            Assert.assertEquals(defineValue(eventName+".publicSafetyDepartmentNumber.string"),defineValue(eventDBData+".public_safety_department_number"));
        Assert.assertEquals("ACTIVE",defineValue(eventDBData+".rx_status"));
        if(defineValue(eventName+".substituteType") != null)
            Assert.assertEquals(defineValue(eventName+".substituteType[\"com.wba.rxpdata.prescription.external.transferin.event.avro.RxSubstituteType\"]"),defineValue(eventDBData+".substitute_type"));
        Assert.assertEquals(defineValue(eventName+".direction"),defineValue(eventDBData+".spec_direction"));
        Assert.assertEquals(defineValue(eventName+".dispenseAsWritten"),defineValue(eventDBData+".spec_dispense_as_written"));
        Assert.assertEquals(defineValue(eventName+".locationIdentifier.locationNumber"),defineValue(eventDBData+".spec_location_number"));
        Assert.assertEquals(defineValue(eventName+".locationIdentifier.locationType"),defineValue(eventDBData+".spec_location_type"));
        Assert.assertEquals(defineValue(eventName+".numberRefill"),defineValue(eventDBData+".spec_number_refill"));
        if(defineValue(eventName+".originalQuantityDispensed") != null)
            Assert.assertEquals(defineValue(eventName+".originalQuantityDispensed.double"),defineValue(eventDBData+".spec_original_quantity_dispensed"));
        Assert.assertEquals(defineValue(eventName+".originalQuantityPrescribed"),defineValue(eventDBData+".spec_original_quantity_prescribed"));
        if(defineValue(eventName+".originCode") != null)
            Assert.assertEquals(defineValue(eventName+".originCode[\"com.wba.rxpdata.prescription.external.transferin.event.avro.SpecificationOriginCode\"]"),defineValue(eventDBData+".spec_origin_code"));
        Assert.assertEquals(defineValue(eventName+".patientCode"),defineValue(eventDBData+".spec_patient_code"));
        if(defineValue(eventName+".prescribedActualProductPackCode") != null)
             Assert.assertEquals(defineValue(eventName+".prescribedActualProductPackCode.string"),defineValue(eventDBData+".spec_prescribed_actual_product_pack_code"));
        Assert.assertEquals(defineValue(eventName+".prescriberCode"),defineValue(eventDBData+".spec_prescriber_code"));
        if(defineValue(eventName+".prescriberDeaNumber") != null)
             Assert.assertEquals(defineValue(eventName+".prescriberDeaNumber.string"),defineValue(eventDBData+".spec_prescriber_dea_number"));
        if(defineValue(eventName+".prescriberDeaSuffix") != null)
            Assert.assertEquals(defineValue(eventName+".prescriberDeaSuffix.string"),defineValue(eventDBData+".spec_prescriber_dea_suffix"));
        Assert.assertEquals(defineValue(eventName+".prescriberLocationCode"),defineValue(eventDBData+".spec_prescriber_location_code"));
        if(defineValue(eventName+".prescriberSupervisorFirstName") != null)
            Assert.assertEquals(defineValue(eventName+".prescriberSupervisorFirstName.string"),defineValue(eventDBData+".spec_prescriber_supervisor_first_name"));
        if(defineValue(eventName+".prescriberSupervisorLastName") != null)
            Assert.assertEquals(defineValue(eventName+".prescriberSupervisorLastName.string"),defineValue(eventDBData+".spec_prescriber_supervisor_last_name"));
        if(defineValue(eventName+".fillExpirationDate") != null)
            Assert.assertEquals(asMillSec(asInt(defineValue(eventName+".fillExpirationDate.int").toString())),asMillSec(defineValue(eventDBData+".spec_fill_expiration_date")));
        if(defineValue(eventName+".totalQuantityPrescribed") != null)
            Assert.assertEquals(defineValue(eventName+".totalQuantityPrescribed.double"),defineValue(eventDBData+".spec_total_quantity_prescribed"));
        Assert.assertEquals(defineValue(eventName+".unlimitedFill"),defineValue(eventDBData+".spec_unlimited_fill"));
        Assert.assertEquals(asMillSec(asInt(defineValue(eventName+".writtenDate").toString())),asMillSec(defineValue(eventDBData+".spec_written_date")));

    }

    @Then("^verify the event response \"([^\"]*)\" and the persist data \"([^\"]*)\" for dispense \"([^\"]*)\" Type$")
    public void event_DBFieldValidation_Dispense(String eventName,String eventDBData,String dispenseType) {

        Assert.assertEquals(defineValue(eventDBData+".prescription_code"),(defineValue("rxPDataSnapshot0.prescription_by_code[0].prescription_code")));
        Assert.assertEquals(defineValue(eventName + ".actualProductPackCode"), defineValue(eventDBData + ".actual_product_pack_code"));
        if(defineValue(eventName + ".dataEnteredBy") != null) {
            Assert.assertEquals(defineValue(eventName + ".dataEnteredBy.employeeNumber"), defineValue(eventDBData + ".data_enterer"));
            Assert.assertEquals(defineValue(eventName + ".dataEnteredBy.employeeType"), defineValue(eventDBData + ".data_enterer_type"));
        }
        Assert.assertEquals(defineValue(eventName + ".daysSupply"),defineValue(eventDBData+".days_supply"));
        Assert.assertEquals(defineValue(eventName+".coupon"),defineValue(eventDBData+".coupon"));
        if(defineValue(eventName + ".couponActualProductPackCode") != null)
            Assert.assertEquals(defineValue(eventName+".couponActualProductPackCode.string"),defineValue(eventDBData+".coupon_actual_product_pack_code"));
        if(defineValue(eventName + ".generalRecipientNumber") != null)
            Assert.assertEquals(defineValue(eventName + ".generalRecipientNumber.string"),defineValue(eventDBData+".general_recipient_number"));
        Assert.assertEquals(defineValue(eventName + ".location.locationNumber"),defineValue(eventDBData+".location_number"));
        Assert.assertEquals(defineValue(eventName + ".location.locationType"),defineValue(eventDBData+".location_type"));
        //Assert.assertEquals("WALKIN",defineValue(eventDBData+".origin_code"));
        // Assert.assertEquals("PHARMACY",defineValue(eventDBData+".service_place"));
        if(defineValue(eventName + ".originCode") != null)
            Assert.assertEquals(defineValue(eventName + ".originCode[\"com.wba.rxpdata.prescription.external.transferin.event.avro.DispenseOriginCode\"]"),defineValue(eventDBData+".origin_code"));
        if(defineValue(eventName + ".patPbrDataReviewedBy") != null) {
            Assert.assertEquals(defineValue(eventName + ".patPbrDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.EmployeeIdentifier\"].employeeNumber"), defineValue(eventDBData + ".patient_prescriber_data_reviewer"));
            Assert.assertEquals(defineValue(eventName + ".patPbrDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.EmployeeIdentifier\"].employeeType"), defineValue(eventDBData + ".patient_prescriber_data_reviewer_type"));
            }
        if(defineValue(eventName + ".paymentMethod") != null)
            Assert.assertEquals(defineValue(eventName + ".paymentMethod[\"com.wba.rxpdata.prescription.external.transferin.event.avro.PaymentMethod\"]"),defineValue(eventDBData+".payment_method"));
        if(defineValue(eventName + ".payCode") != null)
            Assert.assertEquals(defineValue(eventName + ".payCode.string"),defineValue(eventDBData+".pay_code"));
        if(defineValue(eventName + ".pharmacistOfRecord") != null) {
            Assert.assertEquals(defineValue(eventName + ".pharmacistOfRecord.employeeNumber"), defineValue(eventDBData + ".pharmacist_of_record"));
            Assert.assertEquals(defineValue(eventName + ".pharmacistOfRecord.employeeType"), defineValue(eventDBData + ".pharmacist_of_record_employee_type"));
            }
            if(defineValue(eventName + ".planCode") != null)
            Assert.assertEquals(defineValue(eventName + ".planCode.string"),defineValue(eventDBData+".plan_code"));
            if(defineValue(eventName + ".priorAuthorizationCode") != null)
            Assert.assertEquals(defineValue(eventName + ".priorAuthorizationCode.string"),defineValue(eventDBData+".prior_authorization_code"));
            if(defineValue(eventName + ".priorAuthorizationNumber") != null)
            Assert.assertEquals(defineValue(eventName + ".priorAuthorizationNumber.string"),defineValue(eventDBData+".prior_authorization_number"));
            if(defineValue(eventName + ".productDataReviewedBy") != null) {
                Assert.assertEquals(defineValue(eventName + ".productDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.EmployeeIdentifier\"].employeeNumber"), defineValue(eventDBData + ".product_data_reviewer"));
                Assert.assertEquals(defineValue(eventName + ".productDataReviewedBy[\"com.wba.rxpdata.prescription.external.transferin.event.avro.EmployeeIdentifier\"].employeeType"), defineValue(eventDBData + ".product_data_reviewer_type"));
            }
            Assert.assertEquals("REVIEWED", defineValue(eventDBData + ".dispense_status"));
            Assert.assertEquals(1.0, defineValue(eventDBData + ".dispense_number"));

//            Assert.assertTrue(UUID.fromString(defineValue(eventDBData + ".dispense_code").toString()) instanceof UUID);
          if(defineValue(eventName + ".servicePlace") != null)
            Assert.assertEquals(defineValue(eventName + ".servicePlace[\"com.wba.rxpdata.prescription.external.transferin.event.avro.DispenseServicePlace\"]"),defineValue(eventDBData+".service_place"));

        //rxNumber assert will break if multiple events comes
        if(asInt(dataStorage.unmask("rxSequenceNumberNew").toString())>=2000000){
            Assert.assertEquals((dataStorage.unmask("rxSequenceNumberNew").toString()), defineValue(eventDBData + ".rx_number"));
        }else{
//            String str = dataStorage.unmask("rxSequenceNumberNew").toString();
            String zeros = "";
            for(int i=0;i<7-(int)(dataStorage.unmask("rxSequenceNumberNew").toString()).length();i++){
                zeros = zeros+"0";
            }
            Assert.assertEquals(zeros+dataStorage.unmask("rxSequenceNumberNew").toString(), defineValue(eventDBData + ".rx_number"));
        }

       String[] presEvent = eventName.split("\\.");
       if(dispenseType.equals("inadequateRemainingQuantity")){
            Assert.assertEquals(defineValue(presEvent[0] + ".prescription.externalQuantityRemaining"), defineValue(eventDBData + ".quantity_dispensed"));
        }else {
            Assert.assertEquals(defineValue(presEvent[0] + ".dispense.quantityDispensed"), defineValue(eventDBData + ".quantity_dispensed"));
        }
    }

    @And("verify the prescriber details \"([^\"]*)\" are updated in the persist data \"([^\"]*)\"$")
    public void checkPrescriberDetailsinDispense(String eventName,String eventDBData) {
        // oneleo returns the variable and not null .so checking aganist the variable
        if(!defineValue(eventName + ".type").equals(eventName + ".type"))
          Assert.assertEquals(defineValue(eventName + ".type"),defineValue(eventDBData+".prescriber_type"));
        if(!defineValue(eventName + ".nationalProviderIdentifier").equals(eventName + ".nationalProviderIdentifier"))
           Assert.assertEquals(defineValue(eventName + ".nationalProviderIdentifier"),defineValue(eventDBData+".prescriber_npi"));
        if(!defineValue(eventName + ".prescriberLocations[0].prescriberLocationAddress").equals(eventName + ".prescriberLocations[0].prescriberLocationAddress")) {
            Assert.assertEquals(defineValue(eventName + ".prescriberLocations[0].prescriberLocationAddress.addressLine1"), defineValue(eventDBData + ".prescriber_address_line1"));
            Assert.assertEquals(defineValue(eventName + ".prescriberLocations[0].prescriberLocationAddress.addressLine2"), defineValue(eventDBData + ".prescriber_address_line2"));
            Assert.assertEquals(defineValue(eventName + ".prescriberLocations[0].prescriberLocationAddress.city"), defineValue(eventDBData + ".prescriber_city"));
            Assert.assertEquals(defineValue(eventName + ".prescriberLocations[0].prescriberLocationAddress.state"), defineValue(eventDBData + ".prescriber_state"));
            Assert.assertEquals(asInt(defineValue(eventName + ".prescriberLocations[0].prescriberLocationAddress.zipCode").toString()).toString(), defineValue(eventDBData + ".prescriber_zip_code"));
        }
        if(!defineValue(eventName + ".prescriberLocations[0].prescriberContact").equals(eventName + ".prescriberLocations[0].prescriberContact"))
         Assert.assertEquals(asInt(defineValue(eventName + ".prescriberLocations[0].prescriberContact.phoneNumber").toString()).toString(),defineValue(eventDBData+".prescriber_phone_number"));
        Assert.assertEquals(defineValue(eventName + ".name.firstName"),defineValue(eventDBData+".prescriber_first_name"));
        Assert.assertEquals(defineValue(eventName + ".name.lastName"),defineValue(eventDBData+".prescriber_last_name"));
        if(!defineValue(eventName + ".name.suffix").equals(eventName + ".name.suffix"))
        Assert.assertEquals(defineValue(eventName + ".name.suffix"),defineValue(eventDBData+".prescriber_suffix"));
    }

    @And("delete the old mock data and create a new mock data$")
    public void deleteMockandCreateNew(){
        new MockUtils().deleteMocks();
    }

    @And("new Rx Number for new Prescription is generated$")
    public void newExternalReferenceForTransferPRescription() {
            eventStorage.getLastProduced().replaceBodyAttribute("prescription.externalIdentifiers[0].value", "::7");
            eventStorage.getLastProduced().replaceBodyAttribute("contextId",UUID.randomUUID());
    }

    @And("^verify the status is correctly updated in \"([^\"]*)\"$")
    public void getDispenseRecord(String snapshotEvent){
        String[] snapShotArrEventType = snapshotEvent.split(",");
        String snapshot =snapShotArrEventType[0];
        String eventStatus =snapShotArrEventType[1];
        boolean flag = false;
        for(int i=0;i<3;i++){
            if(defineValue(snapshot+".disp_by_prescription_code["+i+"].dispense_code").equals(this.dataStorage.unmask("dispCode"))) {
                Assert.assertEquals(eventStatus, defineValue(snapshot + ".disp_by_prescription_code[" + i + "].dispense_status").toString());
                flag = true;
                break;
            }
        }
        if(!flag){
            Assert.fail(" Status not changed");
        }
    }

    @And("^save the DispenseCode \"([^\"]*)\" to be changed in DataStorage  \"([^\"]*)\"$")
    public void saveDispenseStatus(String dispCodeStatusEvent,String snapshot){
        String[] dispCodeStatusEventArr = dispCodeStatusEvent.split(",");
        String dispCode =dispCodeStatusEventArr[0];
        String event =dispCodeStatusEventArr[1];
        if(defineValue(snapshot+".disp_by_prescription_code[0].dispense_status").equals(event))
              this.dataStorage.add(dispCode,defineValue(snapshot+".disp_by_prescription_code[0].dispense_code"));
        else  if(defineValue(snapshot+".disp_by_prescription_code[1].dispense_status").equals(event))
            this.dataStorage.add(dispCode,defineValue(snapshot+".disp_by_prescription_code[1].dispense_code"));
    }
    public String extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    @And("^verify the Dispense codes are updated for other tables for \"([^\"]*)\" for \"([^\"]*)\"$")
    public void checkCreateRefillInAllTables(String snapshot,String dispStatusLst){
        String[] lst = dispStatusLst.split(",");
        String dispStatus = lst[0];
        String AssemblyFinalCode = "";
        if(lst.length>1)
            AssemblyFinalCode = lst[1];
        String prescription_by_code = snapshot+".prescription_by_code";
        String disp_by_prescription_code = snapshot+".disp_by_prescription_code";
        String prescription_by_disp_code = snapshot+".prescription_by_disp_code";
        String prescription_by_pat_code = snapshot+".prescription_by_pat_code";
        String prescription_by_rx_number = snapshot+".prescription_by_rx_number";
        String refillDispense;
        String snapshotOld ="";

        if(!snapshot.equals("rxPDataSnapshot0")) {
            int val1 = Integer.valueOf(extractDigits(snapshot)) - 1;
            String val2 = snapshot.replaceAll("[^A-Za-z]", "");
            StringBuffer val3 = new StringBuffer(val2);
            snapshotOld = val3.append(val1).toString();
        }else{
            snapshotOld = "rxPDataSnapshot0";
        }


        int tableLength = asInt(defineValue(disp_by_prescription_code+".length()").toString());
        int oldTableLength = asInt(defineValue(snapshotOld+".disp_by_prescription_code"+".length()").toString());
        if(dispStatus.equals("REVIEWED"))
        Assert.assertTrue(tableLength==oldTableLength+1);
        refillDispense=snapshot+".disp_by_prescription_code"+"[0]";
        String[] dispCodes = new String[tableLength];
        int k=0;

            for(int j=0;j<tableLength;j++){
                if(AssemblyFinalCode.equals("")) {
                    if (Timestamp.valueOf(defineValue(disp_by_prescription_code + "[" + j + "].create_datetime").toString().replace("Z", "")).after(Timestamp.valueOf(defineValue(refillDispense + ".create_datetime").toString().replace("Z", ""))))
                        refillDispense = snapshot + ".disp_by_prescription_code[" + j + "]";
                }else if(defineValue(disp_by_prescription_code + "[" + j + "].dispense_code").toString().equals(dataStorage.unmask(AssemblyFinalCode))){
                    refillDispense = snapshot + ".disp_by_prescription_code[" + j + "]";
                }
                    dispCodes[k] = defineValue(disp_by_prescription_code + "[" + j + "].dispense_code").toString();
                    k++;
            }

//        if(Timestamp.valueOf(defineValue(disp_by_prescription_code+"[0].create_datetime").toString().replace("Z","")).after(Timestamp.valueOf(defineValue(disp_by_prescription_code+"[1].create_datetime").toString().replace("Z",""))))
//        refillDispense=snapshot+".disp_by_prescription_code"+"[0]";
//        else
//        refillDispense=snapshot+".disp_by_prescription_code"+"[1]";

        tableLength = asInt(defineValue(prescription_by_pat_code+".length()").toString());
        oldTableLength = asInt(defineValue(snapshotOld+".prescription_by_pat_code"+".length()").toString());
        int oldDispenseLength=0;
        if(oldTableLength<=tableLength)
        {
            for(int i=0;i<oldTableLength;i++){
                if(defineValue(snapshotOld+".prescription_by_pat_code["+i+"].dispense_status").toString().equals(dispStatus))
                    oldDispenseLength= asInt(defineValue(snapshotOld+".prescription_by_pat_code["+i+"].dispense_codes.length()").toString());
            }
        }
        int dispenseLength;
        boolean present=false;
        String[] patDispCodes =new String[dispCodes.length];
        k=0;
        for(int j=0;j<tableLength;j++)
        {
            if(defineValue(prescription_by_pat_code+"["+j+"].dispense_status").toString().equals(dispStatus))
            {
                dispenseLength=asInt(defineValue(prescription_by_pat_code+"["+j+"].dispense_codes.length()").toString());
                Assert.assertEquals(dispenseLength,oldDispenseLength+1);
                for(int i = 0;i<dispenseLength;i++){
                    patDispCodes[k]= defineValue(prescription_by_pat_code+"["+j+"]"+".dispense_codes["+i+"]").toString();
                    k++;
                    if(defineValue(prescription_by_pat_code+"["+j+"]"+".dispense_codes["+i+"]").toString().equals(defineValue(refillDispense+".dispense_code")))
                        present=true;
                }
            }
            else{
                dispenseLength=asInt(defineValue(prescription_by_pat_code+"["+j+"].dispense_codes.length()").toString());
                for(int i = 0;i<dispenseLength;i++) {
                    patDispCodes[k] = defineValue(prescription_by_pat_code + "[" + j + "]" + ".dispense_codes[" + i + "]").toString();
                    k++;
                }
            }
        }
        Assert.assertTrue(present);
        present=false;
        tableLength = asInt(defineValue(prescription_by_rx_number+".length()").toString());
        oldTableLength = asInt(defineValue(snapshotOld+".prescription_by_rx_number"+".length()").toString());
        oldDispenseLength=0;
        if(oldTableLength<=tableLength)
        {
            for(int i=0;i<oldTableLength;i++){
                if(defineValue("rxPDataSnapshot0.prescription_by_rx_number["+i+"].rx_number").toString().equals(defineValue(refillDispense+".rx_number")))
                    oldDispenseLength=asInt(defineValue("rxPDataSnapshot0.prescription_by_rx_number["+i+"].dispense_codes.length()").toString());
            }
        }

        String[] rxDispCodes =new String[dispCodes.length];
        k=0;
        for(int j=0;j<tableLength;j++)
        {
            if(defineValue(prescription_by_rx_number+"["+j+"].rx_number").toString().equals(defineValue(refillDispense+".rx_number")))
            {
                dispenseLength=asInt(defineValue(prescription_by_rx_number+"["+j+"].dispense_codes.length()").toString());
                if(dispStatus.equals("REVIEWED"))
                Assert.assertTrue(dispenseLength==oldDispenseLength+1);
                for(int i=0;i<dispenseLength;i++){
                    rxDispCodes[k]=defineValue(prescription_by_rx_number+"["+j+"].dispense_codes["+i+"]").toString();
                    k++;
                    if(defineValue(prescription_by_rx_number+"["+j+"].location_type").toString().equals(defineValue(refillDispense+".location_type"))
                        && defineValue(prescription_by_rx_number+"["+j+"].location_number").toString().equals(defineValue(refillDispense+".location_number"))
                        && defineValue(prescription_by_rx_number+"["+j+"].dispense_codes["+i+"]").toString().equals(defineValue(refillDispense+".dispense_code")))
                        present = true;

                }
            }
            else{
                dispenseLength=asInt(defineValue(prescription_by_rx_number+"["+j+"].dispense_codes.length()").toString());
                for(int i=0;i<dispenseLength;i++) {
                    rxDispCodes[k] = defineValue(prescription_by_rx_number + "[" + j + "].dispense_codes[" + i + "]").toString();
                    k++;
                }
            }
        }
        Assert.assertTrue(present);
        present=false;
        tableLength = asInt(defineValue(prescription_by_disp_code+".length()").toString());
        oldTableLength = asInt(defineValue(snapshotOld+".prescription_by_disp_code"+".length()").toString());
        if(dispStatus.equals("REVIEWED"))
        Assert.assertTrue(tableLength==oldTableLength+1);
        for(int j=0;j<tableLength;j++){
            if(defineValue(prescription_by_disp_code+"["+j+"].dispense_code").toString().equals(defineValue(refillDispense+".dispense_code")))
                present=true;
        }
        Assert.assertTrue(present);

        Arrays.sort(dispCodes);
        Arrays.sort(patDispCodes);
        Arrays.sort(rxDispCodes);
        Assert.assertArrayEquals(dispCodes,patDispCodes);
        for(String s : rxDispCodes)
            LOGGER.info("rxDisp"+ s);

        for(String r : dispCodes)
            LOGGER.info("DispCodes"+r);

        Assert.assertArrayEquals(dispCodes,rxDispCodes);

    }


    @And("^verify the transfer prescription for other tables$")
    public void checkTransferCompleteInAllTables(){

        String prescription_by_code = "rxPDataSnapshot0.prescription_by_code[0]";
        String disp_by_prescription_code = "rxPDataSnapshot0.disp_by_prescription_code[0]";
        String prescription_by_disp_code = "rxPDataSnapshot0.prescription_by_disp_code[0]";
        String prescription_by_pat_code = "rxPDataSnapshot0.prescription_by_pat_code[0]";
        String prescription_by_rx_number = "rxPDataSnapshot0.prescription_by_rx_number[0]";

        // will fail if multiple  events come as we cannot track the new number in the sequence table
         if(asInt(dataStorage.unmask("rxSequenceNumberNew").toString())>=2000000){
             Assert.assertEquals((dataStorage.unmask("rxSequenceNumberNew").toString()), defineValue(disp_by_prescription_code + ".rx_number"));
         }else{
            String str = dataStorage.unmask("rxSequenceNumberNew").toString();
            String zeros = "";
            for(int i=0;i<7-(int)(dataStorage.unmask("rxSequenceNumberNew").toString()).length();i++){
                zeros = zeros+ "0";
            }
             Assert.assertEquals(zeros+dataStorage.unmask("rxSequenceNumberNew").toString(),defineValue(disp_by_prescription_code + ".rx_number"));
         }

        Assert.assertEquals(defineValue(prescription_by_code+".prescription_code"),(defineValue(disp_by_prescription_code+".prescription_code")));

        Assert.assertEquals(defineValue(prescription_by_code+".prescription_code"),(defineValue(prescription_by_disp_code+".prescription_code")));
        Assert.assertEquals(defineValue(disp_by_prescription_code+".dispense_code"),(defineValue(prescription_by_disp_code+".dispense_code")));

        ArrayNode patDisp = (ArrayNode)defineValue(prescription_by_pat_code+".dispense_codes");
        Assert.assertEquals(1, patDisp.size());
        Assert.assertEquals(defineValue(disp_by_prescription_code+".dispense_code"),(defineValue(prescription_by_pat_code+".dispense_codes[0]")));
        Assert.assertEquals(defineValue(disp_by_prescription_code+".dispense_status"),(defineValue(prescription_by_pat_code+".dispense_status")));
        Assert.assertEquals(defineValue(prescription_by_code+".spec_patient_code"),(defineValue(prescription_by_pat_code+".patient_code")));
        Assert.assertEquals(defineValue(prescription_by_code+".prescription_code"),(defineValue(prescription_by_pat_code+".prescription_code")));

        ArrayNode rxDisp = (ArrayNode)defineValue(prescription_by_rx_number+".dispense_codes");
        Assert.assertEquals(1, rxDisp.size());
        Assert.assertEquals(defineValue(disp_by_prescription_code+".dispense_code"),(defineValue(prescription_by_rx_number+".dispense_codes[0]")));
        Assert.assertEquals(defineValue(disp_by_prescription_code+".location_number"),(defineValue(prescription_by_rx_number+".location_number")));
        Assert.assertEquals(defineValue(disp_by_prescription_code+".location_type"),(defineValue(prescription_by_rx_number+".location_type")));
        Assert.assertEquals(defineValue(disp_by_prescription_code+".rx_number"),(defineValue(prescription_by_rx_number+".rx_number")));
        Assert.assertEquals(defineValue(disp_by_prescription_code+".prescription_code"),(defineValue(prescription_by_rx_number+".prescription_code")));


    }

    @Then("^verify the prescriber is returned between \"([^\"]*)\" and \"([^\"]*)\"$")
    public void prescriberInDispenseCreated(String eventName,String eventDBData){
      //  Assert.assertEquals(defineValue(eventName + ".prescriberType"),defineValue(eventDBData+".prescriber_type"));
        Assert.assertEquals(defineValue(eventName + ".prescriberNpi"),defineValue(eventDBData+".prescriber_npi"));
        Assert.assertEquals(defineValue(eventName + ".prescriberAddressLine1"),defineValue(eventDBData+".prescriber_address_line1"));
        Assert.assertEquals(defineValue(eventName + ".prescriberAddressLine2"),defineValue(eventDBData+".prescriber_address_line2"));
        Assert.assertEquals(defineValue(eventName + ".prescriberCity"),defineValue(eventDBData+".prescriber_city"));
        Assert.assertEquals(defineValue(eventName + ".prescriberState"),defineValue(eventDBData+".prescriber_state"));
        Assert.assertEquals(defineValue(eventName + ".prescriberZipCode"),defineValue(eventDBData+".prescriber_zip_code"));
        Assert.assertEquals(defineValue(eventName + ".prescriberPhoneNumber"),defineValue(eventDBData+".prescriber_phone_number"));
        Assert.assertEquals(defineValue(eventName + ".prescriberFirstName"),defineValue(eventDBData+".prescriber_first_name"));
        Assert.assertEquals(defineValue(eventName + ".prescriberLastName"),defineValue(eventDBData+".prescriber_last_name"));
        Assert.assertEquals(defineValue(eventName + ".prescriberSuffix"),defineValue(eventDBData+".prescriber_suffix"));
    }

    @Then("^verify \"([^\"]*)\" returned between \"([^\"]*)\" and \"([^\"]*)\"$")
    public void verify_returned_between_and(String type, String event, String snapShotArr) throws Throwable {
        String[] snapShotArrEventType = snapShotArr.split(",");
        String snapShot = snapShotArrEventType[0];
        String dispenseType ="";
        if(snapShotArrEventType.length>1){
             dispenseType = snapShotArrEventType[1];
        }
        switch (type){
            case "dispenseAPI":
                dispenseApiVerification(event,snapShot);
                break;
            case "prescriptionAPI":
                prescriptionApiVerification(event,snapShot);
                break;
            case "dispenseEvents":
                String dispense;
                if(defineValue("rxPDataSnapshot1.disp_by_prescription_code[0].create_datetime").toString().equals(defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].create_datetime").toString()))
                    dispense = ".disp_by_prescription_code[1]";
                else
                    dispense = ".disp_by_prescription_code[0]";
                dispenseEventVerification(event,snapShot+dispense);
                break;
            case "prescriptionEvent":
                prescriptionEventVerification(event,snapShot,dispenseType);
                break;
            case "dispenseListAPI":
                int noOfDispenses = asInt(defineValue(event+".dispenses.length()").toString());
                int noOfDBDispenses = asInt(defineValue(snapShot+".disp_by_prescription_code.length()").toString());
                Assert.assertTrue("No of returned dispenses more than present in DB",noOfDispenses<=noOfDBDispenses);
                for(int i = 0; i<noOfDispenses;i++) {
                    String currentDispense = event+".dispenses[" + i + "]";
                    if (i + 1 != noOfDispenses) {
                        Timestamp currentDispenseCreateDateTime = Timestamp.valueOf(defineValue(currentDispense + ".createdDateTime").toString().replaceAll("[a-zA-Z]", " "));
                        Timestamp oldDispenseCreateDateTime = Timestamp.valueOf(defineValue(event+".dispenses[" + (i + 1) + "]" + ".createdDateTime").toString().replaceAll("[a-zA-Z]", " "));
                        Assert.assertTrue("Not sorted based on create date time",currentDispenseCreateDateTime.after(oldDispenseCreateDateTime));
                    }
                    if(defineValue(currentDispense+".code").toString().equals(defineValue(snapShot+".disp_by_prescription_code[0].dispense_code").toString()))
                        dispenseApiVerification(currentDispense,snapShot+".disp_by_prescription_code[0]");
                    else
                        dispenseApiVerification(currentDispense,snapShot+".disp_by_prescription_code[1]");
                }
                break;
            case "prescriptionListAPI":
                int noOfPrescriptions = asInt(defineValue(event+".prescriptions.length()").toString());
                RxPDataSnapshot rxPDataSnapshot ;
                String snapShotName;
                String dispenseStatus = snapShot;
                for(int i =0; i<noOfPrescriptions;i++){
                    rxPDataSnapshot = new RxPDataSnapshot();
                    dataStorage.reset();
                    dataStorage.add("~prescription_code", asUUID(defineValue(event+".prescriptions["+i+"].code").toString()));
                    rxPDataSnapshot.snapshotCreateRxData();
                    snapShotName=eventStorage.getLastEventNameProduced();
                    prescriptionApiVerification(event+".prescriptions["+i+"]",snapShotName+".prescription_by_code[0]");
                    noOfDispenses = asInt(defineValue(event+".prescriptions["+i+"].dispenses.length()").toString());
                    if(!dispenseStatus.equals("null::"))
                    for(int j=0;j<noOfDispenses;j++){
                        Assert.assertEquals(defineValue(event+".prescriptions["+i+"].dispenses["+j+"].status"),dispenseStatus);
                    }

                    verify_returned_between_and("dispenseListAPI",event+".prescriptions["+i+"]",snapShotName);

                }
                break;
            default:
                throw new Exception("No entity type type defined");
        }

    }


    private void prescriptionEventVerification(String event, String snapShot, String dispenseType) throws  JSONException{

        double refillsRemaining = Double.valueOf(defineValue(snapShot+".quantity_remaining").toString())/Double.valueOf(defineValue(snapShot+".spec_original_quantity_prescribed").toString());
        if(dispenseType.equals("inadequateRemainingQuantity")){
          //  Assert.assertEquals(defineValue(event+".refillsRemaining"),0.0);
        }else{
            Assert.assertEquals(defineValue(event+".refillsRemaining"), Precision.round(refillsRemaining,3));
        }
        validateExternalIdentifiersWithoutSchema(event,snapShot);

        Assert.assertEquals(defineValue(event+".code"),defineValue(snapShot+".prescription_code"));
        Assert.assertEquals(defineValue(event+".externalQuantityRemaining"),defineValue(snapShot+".external_quantity_remaining"));
        if(defineValue(event+".externalDataEnteredBy")!=null) {
            Assert.assertEquals(defineValue(event + ".externalDataEnteredBy.firstName"), defineValue(snapShot + ".external_data_enterer_first_name"));
            Assert.assertEquals(defineValue(event + ".externalDataEnteredBy.lastName"), defineValue(snapShot + ".external_data_enterer_last_name"));
            Assert.assertEquals(defineValue(event + ".externalDataEnteredBy.initials"), defineValue(snapShot + ".external_data_enterer_initials"));
            Assert.assertEquals(defineValue(event + ".externalDataEnteredBy.middleNameInitial"), defineValue(snapShot + ".external_data_enterer_middle_initial"));
        }
        if(defineValue(snapShot+".external_data_enterer_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(event+".externalDataEnteredDateTime"))),asMillSec(defineValue(snapShot+".external_data_enterer_datetime")));
        if(defineValue(snapShot+".external_last_dispense_entered_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(event+".externalLastDispenseEnteredDateTime"))),asMillSec(defineValue(snapShot+".external_last_dispense_entered_datetime")));
        if(defineValue(event+".externalLastPatientPrescriberDataReviewedBy")!=null){
            Assert.assertEquals(defineValue(event+".externalLastPatientPrescriberDataReviewedBy.firstName"),defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_first_name"));
            Assert.assertEquals(defineValue(event+".externalLastPatientPrescriberDataReviewedBy.lastName"),defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_last_name"));
            Assert.assertEquals(defineValue(event+".externalLastPatientPrescriberDataReviewedBy.initials"),defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_initials"));
            Assert.assertEquals(defineValue(event+".externalLastPatientPrescriberDataReviewedBy.middleNameInitial"),defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_middle_initial"));
        }
        if(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(event+".externalLastPatientPrescriberDataReviewedDateTime"))),asMillSec(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_datetime")));
        if(defineValue(event+".externalLastProductDataReviewedBy")!=null) {
            Assert.assertEquals(defineValue(event + ".externalLastProductDataReviewedBy.firstName"), defineValue(snapShot + ".external_last_product_data_reviewer_first_name"));
            Assert.assertEquals(defineValue(event + ".externalLastProductDataReviewedBy.lastName"), defineValue(snapShot + ".external_last_product_data_reviewer_last_name"));
            Assert.assertEquals(defineValue(event + ".externalLastProductDataReviewedBy.initials"), defineValue(snapShot + ".external_last_product_data_reviewer_initials"));
            Assert.assertEquals(defineValue(event + ".externalLastProductDataReviewedBy.middleNameInitial"), defineValue(snapShot + ".external_last_product_data_reviewer_middle_initial"));
        }
        if(defineValue(snapShot+".external_last_product_data_reviewer_datetime")!=null)
        Assert.assertEquals(asMillSec(convLong(defineValue(event+".externalLastProductDataReviewedDateTime"))),asMillSec(defineValue(snapShot+".external_last_product_data_reviewer_datetime")));
        if(defineValue(event+".externalRxScannedBy")!=null) {
            Assert.assertEquals(defineValue(event + ".externalRxScannedBy.firstName"), defineValue(snapShot + ".external_rx_scanner_first_name"));
            Assert.assertEquals(defineValue(event + ".externalRxScannedBy.lastName"), defineValue(snapShot + ".external_rx_scanner_last_name"));
            Assert.assertEquals(defineValue(event + ".externalRxScannedBy.initials"), defineValue(snapShot + ".external_rx_scanner_initials"));
            Assert.assertEquals(defineValue(event + ".externalRxScannedBy.middleNameInitial"), defineValue(snapShot + ".external_rx_scanner_middle_initial"));
        }
        Assert.assertEquals(defineValue(event+".rxAuthorizer"),defineValue(snapShot+".rx_authorizer"));
        Assert.assertEquals(defineValue(event+".autoRefill"),defineValue(snapShot+".auto_refill"));
        Assert.assertEquals(defineValue(event+".rxComments"),defineValue(snapShot+".rx_comments"));
        Assert.assertEquals(defineValue(event+".genericSubstitutePreference"),defineValue(snapShot+".generic_substitute_preference"));
        Assert.assertEquals(defineValue(event+".intermediaryAuthorizationId"),defineValue(snapShot+".intermediary_authorization_id"));
        Assert.assertEquals(defineValue(event+".printGenericProductName"),defineValue(snapShot+".print_generic_product_name"));
        Assert.assertEquals(defineValue(event+".printSavings"),defineValue(snapShot+".print_savings"));
        Assert.assertEquals(defineValue(event+".publicSafetyDepartmentNumber"),defineValue(snapShot+".public_safety_department_number"));
        Assert.assertEquals(defineValue(event+".quantityRemaining"),defineValue(snapShot+".quantity_remaining"));
        Assert.assertEquals(defineValue(event+".rxStatus"),defineValue(snapShot+".rx_status"));
        Assert.assertEquals(defineValue(event+".substituteType"),defineValue(snapShot+".substitute_type"));
        Assert.assertEquals(defineValue(event+".specDirection"),defineValue(snapShot+".spec_direction"));
        Assert.assertEquals(defineValue(event+".specExpandedDirection"),"TO THE AFFECTED AREA");
        Assert.assertEquals(defineValue(event+".specDispenseAsWritten"),defineValue(snapShot+".spec_dispense_as_written"));
        if(defineValue(snapShot+".spec_fill_expiration_date")!=null)
        Assert.assertEquals(asMillSec(asInt(defineValue(event+".specFillExpirationDate").toString())),asMillSec(defineValue(snapShot+".spec_fill_expiration_date")));
        Assert.assertEquals(defineValue(event+".specImageId"),defineValue(snapShot+".spec_image_id"));
        Assert.assertEquals(defineValue(event+".specImmutable"),defineValue(snapShot+".spec_immutable"));
        Assert.assertEquals(defineValue(event+".specLocation.locationNumber"),defineValue(snapShot+".spec_location_number"));
        Assert.assertEquals(defineValue(event+".specLocation.locationType"),defineValue(snapShot+".spec_location_type"));
        Assert.assertEquals(defineValue(event+".specNumberRefill"),defineValue(snapShot+".spec_number_refill"));
        Assert.assertEquals(defineValue(event+".specOriginalQuantityDispensed"),defineValue(snapShot+".spec_original_quantity_dispensed"));
        Assert.assertEquals(defineValue(event+".specOriginalQuantityPrescribed"),defineValue(snapShot+".spec_original_quantity_prescribed"));
        if(defineValue(snapShot+".spec_original_scanned_datetime")!=null)
        Assert.assertEquals(asMillSec(convLong(defineValue(event+".specOriginalScannedDateTime"))),asMillSec(defineValue(snapShot+".spec_original_scanned_datetime")));
        Assert.assertEquals(defineValue(event+".specOriginCode"),defineValue(snapShot+".spec_origin_code"));
        Assert.assertEquals(defineValue(event+".specPatientCode"),defineValue(snapShot+".spec_patient_code"));
        Assert.assertEquals(defineValue(event+".specPrescribedActualProductPackCode"),defineValue(snapShot+".spec_prescribed_actual_product_pack_code"));
        Assert.assertEquals(defineValue(event+".specPrescriberCode"),defineValue(snapShot+".spec_prescriber_code"));
        Assert.assertEquals(defineValue(event+".specPrescriberDeaNumber"),defineValue(snapShot+".spec_prescriber_dea_number"));
        Assert.assertEquals(defineValue(event+".specPrescriberDeaSuffix"),defineValue(snapShot+".spec_prescriber_dea_suffix"));
        Assert.assertEquals(defineValue(event+".specPrescriberLocationCode"),defineValue(snapShot+".spec_prescriber_location_code"));
        Assert.assertEquals(defineValue(event+".specPrescriberSupervisorFirstName"),defineValue(snapShot+".spec_prescriber_supervisor_first_name"));
        Assert.assertEquals(defineValue(event+".specPrescriberSupervisorLastName"),defineValue(snapShot+".spec_prescriber_supervisor_last_name"));
        Assert.assertEquals(defineValue(event+".specTotalQuantityPrescribed"),defineValue(snapShot+".spec_total_quantity_prescribed"));
        Assert.assertEquals(defineValue(event+".specUnlimitedFill"),defineValue(snapShot+".spec_unlimited_fill"));
        Assert.assertEquals(asMillSec(asInt(defineValue(event+".specWrittenDate").toString())),asMillSec(defineValue(snapShot+".spec_written_date")));
    }

    private void prescriptionApiVerification(String event, String snapShot) throws Exception{

        if((defineValue(snapShot + ".external_identifiers"))!=null) {
            HashMap<String,String> values = jsonToMap((defineValue(snapShot + ".external_identifiers").toString()));
            for(int i=0;i< values.keySet().size();i++) {
                for (String key : values.keySet()) {
                    if(defineValue(event + ".externalReference.externalIdentifiers["+i+"].name").toString().equals(key)){
                        Assert.assertEquals(defineValue(event + ".externalReference.externalIdentifiers["+i+"].value"),values.get(key));
                    }
                }
            }
        }

        Assert.assertEquals(defineValue(snapShot+".rx_closer"),defineValue(event+".rxClosedBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".rx_closer_type"),defineValue(event+".rxClosedBy.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".rx_closed_reason"),defineValue(event+".rxClosedReason"));
        Assert.assertEquals(asMillSec(defineValue(snapShot+".rx_closed_datetime")),asMillSec(defineValue(event+".rxClosedDateTime")));
        Assert.assertEquals(defineValue(snapShot+".rx_closed_comment"),defineValue(event+".rxClosedComment"));

        Assert.assertEquals(defineValue(snapShot+".external_quantity_remaining"),defineValue(event+".externalQuantityRemaining"));
        Assert.assertEquals(defineValue(snapShot + ".external_source"),defineValue(event + ".externalSource"));
        Assert.assertEquals(defineValue(snapShot+".prescription_code"),defineValue(event+".code"));
        Assert.assertEquals(defineValue(snapShot+".rx_authorizer"),defineValue(event+".rxAuthorizer"));
        Assert.assertEquals(defineValue(snapShot+".auto_refill"),defineValue(event+".autoRefill"));
        Assert.assertEquals(defineValue(snapShot+".rx_comments"),defineValue(event+".rxComments"));
        Assert.assertEquals(defineValue(snapShot+".external_data_enterer_first_name"),defineValue(event+".externalDataEnteredBy.firstName"));
        Assert.assertEquals(defineValue(snapShot+".external_data_enterer_initials"),defineValue(event+".externalDataEnteredBy.initials"));
        Assert.assertEquals(defineValue(snapShot+".external_data_enterer_last_name"),defineValue(event+".externalDataEnteredBy.lastName"));
        Assert.assertEquals(defineValue(snapShot+".external_data_enterer_middle_initial"),defineValue(event+".externalDataEnteredBy.middleNameInitial"));
        Assert.assertEquals(defineValue(snapShot+".external_data_enterer_datetime"),defineValue(event+".externalDataEnteredDatetime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".external_last_dispense_entered_datetime"),defineValue(event+".externalLastDispenseEnteredDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_first_name"),defineValue(event+".externalLastPatientPrescriberDataReviewedBy.firstName"));
        Assert.assertEquals(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_last_name"),defineValue(event+".externalLastPatientPrescriberDataReviewedBy.lastName"));
        Assert.assertEquals(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_initials"),defineValue(event+".externalLastPatientPrescriberDataReviewedBy.initials"));
        Assert.assertEquals(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_middle_initial"),defineValue(event+".externalLastPatientPrescriberDataReviewedBy.middleNameInitial"));
        Assert.assertEquals(defineValue(snapShot+".external_last_patient_prescriber_data_reviewer_datetime"),defineValue(event+".externalLastPatientPrescriberDataReviewedDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".external_last_product_data_reviewer_first_name"),defineValue(event+".externalLastProductDataReviewedBy.firstName"));
        Assert.assertEquals(defineValue(snapShot+".external_last_product_data_reviewer_initials"),defineValue(event+".externalLastProductDataReviewedBy.initials"));
        Assert.assertEquals(defineValue(snapShot+".external_last_product_data_reviewer_last_name"),defineValue(event+".externalLastProductDataReviewedBy.lastName"));
        Assert.assertEquals(defineValue(snapShot+".external_last_product_data_reviewer_middle_initial"),defineValue(event+".externalLastProductDataReviewedBy.middleNameInitial"));
        Assert.assertEquals(defineValue(snapShot+".external_last_product_data_reviewer_datetime"),defineValue(event+".externalLastProductDataReviewedDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".external_rx_scanner_first_name"),defineValue(event+".externalRxScannedBy.firstName"));
        Assert.assertEquals(defineValue(snapShot+".external_rx_scanner_initials"),defineValue(event+".externalRxScannedBy.initials"));
        Assert.assertEquals(defineValue(snapShot+".external_rx_scanner_last_name"),defineValue(event+".externalRxScannedBy.lastName"));
        Assert.assertEquals(defineValue(snapShot+".external_rx_scanner_middle_initial"),defineValue(event+".externalRxScannedBy.middleNameInitial"));
        Assert.assertEquals(defineValue(snapShot+".generic_substitute_preference"),defineValue(event+".genericSubstitutePreference"));
        Assert.assertEquals(defineValue(snapShot+".intermediary_authorization_id"),defineValue(event+".intermediaryAuthorizationId"));
        Assert.assertEquals(defineValue(snapShot+".print_generic_product_name"),defineValue(event+".printGenericProductName"));
        Assert.assertEquals(defineValue(snapShot+".print_savings"),defineValue(event+".printSavings"));
        Assert.assertEquals(defineValue(snapShot+".public_safety_department_number"),defineValue(event+".publicSafetyDepartmentNumber"));
        Assert.assertEquals(defineValue(snapShot+".quantity_remaining"),defineValue(event+".quantityRemaining"));
        double refillRemaining= Double.parseDouble(defineValue(snapShot+"quantity_remaining").toString())/Double.parseDouble(defineValue(snapShot+".spec_original_quantity_prescribed").toString());
        Assert.assertEquals(refillRemaining,defineValue(event+".refillsRemaining"));
        Assert.assertEquals(defineValue(snapShot+".rx_status"),defineValue(event+".rxStatus"));
        Assert.assertEquals(defineValue(snapShot+".substitute_type"),defineValue(event+".substituteType"));
//??        Assert.assertEquals(defineValue(snapShot+".quantity_dispensed"),defineValue(event+".rxTotalQuantityDispensed"));
        Assert.assertEquals(defineValue(snapShot+".spec_direction"),defineValue(event+".specDirection"));
        Assert.assertEquals("TO THE AFFECTED AREA",defineValue(event+".specExpandedDirection"));
        Assert.assertEquals(defineValue(snapShot+".spec_dispense_as_written"),defineValue(event+".specDispenseAsWritten"));
        Assert.assertEquals(defineValue(snapShot+".spec_image_id"),defineValue(event+".specImageId"));
        Assert.assertEquals(defineValue(snapShot+".spec_immutable"),defineValue(event+".specImmutable"));
        Assert.assertEquals(defineValue(snapShot+".spec_location_number"),defineValue(event+".specLocation.locationNumber"));
        Assert.assertEquals(defineValue(snapShot+".spec_location_type"),defineValue(event+".specLocation.locationType"));
        Assert.assertEquals(defineValue(snapShot+".spec_number_refill"),defineValue(event+".specNumberRefill"));
        Assert.assertEquals(defineValue(snapShot+".spec_original_scanned_datetime"),defineValue(event+".specOriginalScannedDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".spec_original_quantity_dispensed"),defineValue(event+".specOriginalQuantityDispensed"));
        Assert.assertEquals(defineValue(snapShot+".spec_original_quantity_prescribed"),defineValue(event+".specOriginalQuantityPrescribed"));
        Assert.assertEquals(defineValue(snapShot+".spec_origin_code"),defineValue(event+".specOriginCode"));
        Assert.assertEquals(defineValue(snapShot+".spec_patient_code"),defineValue(event+".specPatientCode"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescribed_actual_product_pack_code"),defineValue(event+".specPrescribedActualProductPackCode"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescriber_code"),defineValue(event+".specPrescriberCode"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescriber_dea_number"),defineValue(event+".specPrescriberDeaNumber"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescriber_dea_suffix"),defineValue(event+".specPrescriberDeaSuffix"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescriber_location_code"),defineValue(event+".specPrescriberLocationCode"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescriber_supervisor_first_name"),defineValue(event+".specPrescriberSupervisorFirstName"));
        Assert.assertEquals(defineValue(snapShot+".spec_prescriber_supervisor_last_name"),defineValue(event+".specPrescriberSupervisorLastName"));
        Assert.assertEquals(defineValue(snapShot+".spec_fill_expiration_date").toString().replace(" 00:00:00.000Z",""),defineValue(event+".specFillExpirationDate").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".spec_total_quantity_prescribed"),defineValue(event+".specTotalQuantityPrescribed"));
        Assert.assertEquals(defineValue(snapShot+".spec_unlimited_fill"),defineValue(event+".specUnlimitedFill"));
        Assert.assertEquals(defineValue(snapShot+".spec_written_date").toString().replace(" 00:00:00.000Z",""),defineValue(event+".specWrittenDate").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".transfer_to_pharmacy_name"),defineValue(event+".transferToPharmacyName"));
        Assert.assertEquals(defineValue(snapShot+".transfer_to_pharmacy_pharmacist_name"),defineValue(event+".transferToPharmacyPharmacistName"));
        Assert.assertEquals(defineValue(snapShot+".transfer_to_pharmacy_phone_number"),defineValue(event+".transferToPharmacyPhoneNumber"));
        Assert.assertEquals(defineValue(snapShot+".transfer_to_pharmacy_store_number"),defineValue(event+".transferToPharmacyStoreNumber"));


    }

    private void dispenseEventVerification(String event, String snapShot){
        
        prescriberInDispenseCreated(event +".prescriber",snapShot);

//        Assert.assertEquals(defineValue(event +".actualProductPackCode"), defineValue(snapShot + ".actual_product_pack_code"));
        Assert.assertEquals(defineValue(event +".authorizer"), defineValue(snapShot + ".authorizer"));
        Assert.assertEquals(defineValue(event+".coupon"),defineValue(snapShot+".coupon"));
        Assert.assertEquals(defineValue(event+".couponActualProductPackCode"),defineValue(snapShot+".coupon_actual_product_pack_code"));
        Assert.assertEquals(defineValue(event + ".code"), defineValue(snapShot + ".dispense_code"));
        Assert.assertEquals(asMillSec(convLong(defineValue(event + ".createdDateTime"))), asMillSec(defineValue(snapShot + ".create_datetime")));
        if(defineValue(event+".dataEnteredBy")!= null) {
            Assert.assertEquals(defineValue(event + ".dataEnteredBy.employeeNumber"), defineValue(snapShot + ".data_enterer"));
            Assert.assertEquals(defineValue(event + ".dataEnteredBy.employeeType"), defineValue(snapShot + ".data_enterer_type"));
        }
        Assert.assertEquals(defineValue(event+".daysSupply"),defineValue(snapShot+".days_supply"));
        Assert.assertEquals(defineValue(event + ".dispenseNumber"), defineValue(snapShot + ".dispense_number"));
        Assert.assertEquals(defineValue(event + ".generalRecipientNumber"), defineValue(snapShot + ".general_recipient_number"));
        Assert.assertEquals(defineValue(event+".location.locationNumber"),defineValue(snapShot+".location_number"));
        Assert.assertEquals(defineValue(event+".location.locationType"),defineValue(snapShot+".location_type"));
        Assert.assertEquals(defineValue(event+".originCode"),defineValue(snapShot+".origin_code"));
        Assert.assertEquals(defineValue(event+".patientResponsibility"),defineValue(snapShot+".patient_responsibility"));
        if(defineValue(event+".patPbrDataReviewedBy")!= null) {
            Assert.assertEquals(defineValue(event + ".patPbrDataReviewedBy.employeeNumber"), defineValue(snapShot + ".patient_prescriber_data_reviewer"));
            Assert.assertEquals(defineValue(event + ".patPbrDataReviewedBy.employeeType"), defineValue(snapShot + ".patient_prescriber_data_reviewer_type"));
        }
        Assert.assertEquals(defineValue(event+".payCode"),defineValue(snapShot+".pay_code"));
        Assert.assertEquals(defineValue(event+".paymentMethod"),defineValue(snapShot+".payment_method"));
        if(defineValue(event+".pharmacistOfRecord")!= null) {
            Assert.assertEquals(defineValue(event + ".pharmacistOfRecord.employeeNumber"), defineValue(snapShot + ".pharmacist_of_record"));
            Assert.assertEquals(defineValue(event + ".pharmacistOfRecord.employeeType"), defineValue(snapShot + ".pharmacist_of_record_employee_type"));
        }
        Assert.assertEquals(defineValue(event+".planCode"),defineValue(snapShot+".plan_code"));
        Assert.assertEquals(defineValue(event + ".prescriptionCode"), defineValue(snapShot + ".prescription_code"));
        if(defineValue(snapShot + ".price_override_amount")!=null)
        Assert.assertEquals(defineValue(event+".priceOverrideAmount.amount"),defineValue(snapShot+".price_override_amount"));
        Assert.assertEquals(defineValue(event+".priorAuthorizationCode"),defineValue(snapShot+".prior_authorization_code"));
        Assert.assertEquals(defineValue(event+".priorAuthorizationNumber"),defineValue(snapShot+".prior_authorization_number"));
        if(defineValue(event+".productDataReviewedBy")!= null) {
            Assert.assertEquals(defineValue(event + ".productDataReviewedBy.employeeNumber"), defineValue(snapShot + ".product_data_reviewer"));
            Assert.assertEquals(defineValue(event + ".productDataReviewedBy.employeeType"), defineValue(snapShot + ".product_data_reviewer_type"));
        }
        Assert.assertEquals(defineValue(event + ".quantityDispensed"), defineValue(snapShot + ".quantity_dispensed"));
        Assert.assertEquals(defineValue(event + ".rxNumber"), defineValue(snapShot + ".rx_number"));
        Assert.assertEquals(defineValue(event+".servicePlace"),defineValue(snapShot+".service_place"));
        if(defineValue(snapShot + ".sold_amount")!=null)
            Assert.assertEquals(defineValue(event + ".soldAmount.amount"), defineValue(snapShot + ".sold_amount"));
//        Assert.assertEquals(defineValue(event + ".soldAmount.isoCode"), defineValue(snapShot + ".sold_amount_iso_code"));
        if(defineValue(snapShot + ".sold_datetime")!=null)
            Assert.assertEquals(asMillSec(convLong(defineValue(event + ".soldDateTime"))), asMillSec(defineValue(snapShot + ".sold_datetime")));
        Assert.assertEquals(defineValue(event + ".status"), defineValue(snapShot + ".dispense_status"));

    }

    private void dispenseApiVerification(String event, String snapShot){

        Assert.assertEquals(defineValue(snapShot+".sold_by"),defineValue(event+".soldBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".sold_by_type"),defineValue(event+".soldBy.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_address_line1"),defineValue(event+".prescriber.prescriberAddressLine1"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_address_line2"),defineValue(event+".prescriber.prescriberAddressLine2"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_city"),defineValue(event+".prescriber.prescriberCity"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_first_name"),defineValue(event+".prescriber.prescriberFirstName"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_last_name"),defineValue(event+".prescriber.prescriberLastName"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_npi"),defineValue(event+".prescriber.prescriberNpi"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_phone_number"),defineValue(event+".prescriber.prescriberPhoneNumber"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_suffix"),defineValue(event+".prescriber.prescriberSuffix"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_state"),defineValue(event+".prescriber.prescriberState"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_type"),defineValue(event+".prescriber.prescriberType"));
        Assert.assertEquals(defineValue(snapShot+".prescriber_zip_code"),defineValue(event+".prescriber.prescriberZipCode"));

        Assert.assertEquals(defineValue(snapShot+".actual_product_pack_code"),defineValue(event+".actualProductPackCode"));
        Assert.assertEquals(defineValue(snapShot+".authorizer"),defineValue(event+".authorizer"));
        Assert.assertEquals(defineValue(snapShot+".dispense_code"),defineValue(event+".code"));
        Assert.assertEquals(defineValue(snapShot+".coupon"),defineValue(event+".coupon"));
        Assert.assertEquals(defineValue(snapShot+".coupon_actual_product_pack_code"),defineValue(event+".couponActualProductPackCode"));
        Assert.assertEquals(defineValue(snapShot+".create_datetime"),defineValue(event+".createdDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".data_enterer"),defineValue(event+".dataEnteredBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".data_enterer_type"),defineValue(event+".dataEnteredBy.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".days_supply"),defineValue(event+".daysSupply"));
        Assert.assertEquals(defineValue(snapShot+".dispense_number"),defineValue(event+".dispenseNumber"));
        Assert.assertEquals(defineValue(snapShot+".deleter"),defineValue(event+".deletedBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".deleter_type"),defineValue(event+".deletedBy.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".deleted_datetime"),defineValue(event+".deletedDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".deleted_reason"),defineValue(event+".deletedReason"));
        Assert.assertEquals(defineValue(snapShot+".general_recipient_number"),defineValue(event+".generalRecipientNumber"));
        Assert.assertEquals(defineValue(snapShot+".location_number"),defineValue(event+".location.locationNumber"));
        Assert.assertEquals(defineValue(snapShot+".location_type"),defineValue(event+".location.locationType"));
        Assert.assertEquals(defineValue(snapShot+".origin_code"),defineValue(event+".originCode"));
        Assert.assertEquals(defineValue(snapShot+".patient_responsibility"),Double.parseDouble(defineValue(event+".patientResponsibility.amount").toString()));
        Assert.assertEquals(defineValue(snapShot+".patient_prescriber_data_reviewer"),defineValue(event+".patPbrDataReviewedBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".patient_prescriber_data_reviewer_type"),defineValue(event+".patPbrDataReviewedBy.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".pay_code"),defineValue(event+".payCode"));
        Assert.assertEquals(defineValue(snapShot+".payment_method"),defineValue(event+".paymentMethod"));
        Assert.assertEquals(defineValue(snapShot+".pharmacist_of_record"),defineValue(event+".pharmacistOfRecord.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".pharmacist_of_record_employee_type"),defineValue(event+".pharmacistOfRecord.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".plan_code"),defineValue(event+".planCode"));
        Assert.assertEquals(defineValue(snapShot+".prescription_code"),defineValue(event+".prescriptionCode"));
        Assert.assertEquals(defineValue(snapShot+".price_override_amount"),Double.parseDouble(defineValue(event+".priceOverrideAmount.amount").toString()));
        Assert.assertEquals(defineValue(snapShot+".prior_authorization_code"),defineValue(event+".priorAuthorizationCode"));
        Assert.assertEquals(defineValue(snapShot+".prior_authorization_number"),defineValue(event+".priorAuthorizationNumber"));
        Assert.assertEquals(defineValue(snapShot+".product_data_reviewer"),defineValue(event+".productDataReviewedBy.employeeNumber"));
        Assert.assertEquals(defineValue(snapShot+".product_data_reviewer_type"),defineValue(event+".productDataReviewedBy.employeeType"));
        Assert.assertEquals(defineValue(snapShot+".quantity_dispensed"),defineValue(event+".quantityDispensed"));
        Assert.assertEquals(defineValue(snapShot+".rx_number"),defineValue(event+".rxNumber"));
        Assert.assertEquals(defineValue(snapShot+".service_place"),defineValue(event+".servicePlace"));
        Assert.assertEquals(defineValue(snapShot+".sold_amount"),Double.parseDouble(defineValue(event+".soldAmount.amount").toString()));
        Assert.assertEquals(defineValue(snapShot+".sold_datetime"),defineValue(event+".soldDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(snapShot+".dispense_status"),defineValue(event+".status"));

    }

    @Given("^verify order voided is updated in \"([^\"]*)\"$")
    public void verify_order_voided(String snapshot) throws Throwable {
        Assert.assertEquals(defineValue(snapshot+".sold_by"),null);
        Assert.assertEquals(defineValue(snapshot+".sold_by_type"),null);
        Assert.assertEquals(defineValue(snapshot+".sold_amount"),null);
        Assert.assertEquals(defineValue(snapshot+".sold_datetime"),null);
        Assert.assertEquals(defineValue(snapshot+".dispense_status").toString(),"READY");
    }


    @Given("^verify order voided for \"([^\"]*)\" is notified in \"([^\"]*)\"$")
    public void verify_order_voided_notify(String eventName,String snapshot) throws Throwable {
        Assert.assertEquals(defineValue(snapshot+".prescription_code"),defineValue(eventName+".prescriptionCode"));
        Assert.assertEquals(defineValue(snapshot+".dispense_code"),defineValue(eventName+".dispenseCode"));
        Assert.assertEquals(defineValue(snapshot+".actual_product_pack_code").toString(),defineValue(eventName+".dispensedActualProductPackCode"));
        Assert.assertEquals(defineValue(snapshot+".location_type"),defineValue(eventName+".dispensingLocation.locationType"));
        Assert.assertEquals(defineValue(snapshot+".location_number"),defineValue(eventName+".dispensingLocation.locationNumber"));
        Assert.assertEquals(defineValue(snapshot+".dispense_number"),defineValue(eventName+".dispenseNumber"));
        Assert.assertEquals(defineValue(snapshot+".rx_number"),defineValue(eventName+".rxNumber"));
        Assert.assertEquals(defineValue(snapshot+".quantity_dispensed"),defineValue(eventName+".dispensedQuantity"));
    }

        @Given("^verify delivered dispense for \"([^\"]*)\" for \"([^\"]*)\"$")
    public void verify_delivered_dispense_for(String type,String snapshotLst) throws Throwable {

        String[] snapshotVals = snapshotLst.split(",");
        String snapshot = snapshotVals[0];
        String params = "" ;
        String  eventDelivered = "RxPDispenseDelivered";
        if(snapshotVals.length > 1)
            eventDelivered =  snapshotVals[1];

        String dispense = ".disp_by_prescription_code[0]";

        for(int i=0;i<asInt(defineValue(snapshot+".disp_by_prescription_code.length()").toString());i++){
            if(defineValue(snapshot+".disp_by_prescription_code["+i+"].dispense_code").equals(this.dataStorage.unmask("dispCode"))) {
                dispense = ".disp_by_prescription_code[" + i + "]";
                break;
            }
        }


        switch (type){
         case "Cassandra":
             Assert.assertEquals(defineValue(snapshot+dispense+".dispense_code"),defineValue("OrderLineDelivered.dispenseCode"));
             if(defineValue("OrderLineDelivered.soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber").toString().equals("OrderLineDelivered.soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber")){
                 Assert.assertTrue(null == defineValue(snapshot+dispense+".sold_by"));
             }
             else
             {
                 Assert.assertEquals(defineValue(snapshot+dispense+".sold_by"),defineValue("OrderLineDelivered.soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber"));
                 Assert.assertEquals(defineValue(snapshot+dispense+".sold_by_type"),defineValue("OrderLineDelivered.soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeType"));
             }
             Assert.assertEquals(defineValue(snapshot+dispense+".sold_amount"),defineValue("OrderLineDelivered.soldAmount.decimalValue"));
             Assert.assertEquals(asMillSec(defineValue(snapshot+dispense+".sold_datetime")),asMillSec(convLong(defineValue("OrderLineDelivered.soldDateTime"))));
             Assert.assertEquals(defineValue(snapshot+dispense+".dispense_status").toString(),"DELIVERED");
             break;
         case "NotifyEvent":
             Assert.assertEquals(defineValue(snapshot+".prescription_by_code[0].prescription_code"),defineValue(eventDelivered+".prescriptionCode"));
             Assert.assertEquals(defineValue(snapshot+dispense+".dispense_code"),defineValue(eventDelivered+".dispenseCode"));
             if(null == defineValue(snapshot+dispense+".sold_by")){
                 Assert.assertTrue(null == defineValue(eventDelivered+".soldBy"));
             }
             else{
                 Assert.assertEquals(defineValue(snapshot+dispense+".sold_by"),defineValue(eventDelivered+".soldBy.employeeNumber"));
                 Assert.assertEquals(defineValue(snapshot+dispense+".sold_by_type"),defineValue(eventDelivered+".soldBy.employeeType"));
             }

             Assert.assertEquals(defineValue(snapshot+dispense+".sold_amount"),defineValue(eventDelivered+".soldAmount.decimalValue"));
             Assert.assertEquals(asMillSec(defineValue(snapshot+dispense+".sold_datetime")),asMillSec(convLong(defineValue(eventDelivered+".soldDateTime"))));
             Assert.assertEquals(defineValue(snapshot+dispense+".actual_product_pack_code").toString(),defineValue(eventDelivered+".dispensedActualProductPackCode"));
             Assert.assertEquals(defineValue(snapshot+dispense+".location_type"),defineValue(eventDelivered+".dispensingLocation.locationType"));
             Assert.assertEquals(defineValue(snapshot+dispense+".location_number"),defineValue(eventDelivered+".dispensingLocation.locationNumber"));
             Assert.assertEquals(defineValue(snapshot+dispense+".dispense_number"),defineValue(eventDelivered+".dispenseNumber"));
             Assert.assertEquals(defineValue(snapshot+dispense+".rx_number"),defineValue(eventDelivered+".rxNumber"));
             Assert.assertEquals(defineValue(snapshot+dispense+".quantity_dispensed"),defineValue(eventDelivered+".dispensedQuantity"));
             break;
         default:
             throw new Exception("No validation type defined");
     }

    }

 @When("^Produce 500 events for Transfer$")
  public void produceEvents(){
        for(int i=0;i<800;i++){
            _CommonStep cstep = new _CommonStep();
            _CommonStepSimple csStepSimple = new _CommonStepSimple();
            cstep.a_event_for_topic("newPrescriptionDispense","rxpdata_prescription_external_transferin");
            updatePrescriberDetails("newPrescriptionDispense");
            eventStorage.findEvent("newPrescriptionDispense").replaceBodyAttribute("contextId",UUID.randomUUID());
            newExternalReferenceForTransferPRescription();
            csStepSimple.the_event_is_produced_by_test();
        }
  }


    @When("^Produce 500 events for Refill")
    public void produceEventsRefill() throws Throwable {
        for(int i=0;i<50;i++){
           RxPDataSnapshot snap = new RxPDataSnapshot();
            CustomStep cs =  new CustomStep();
            _CommonStep cstep = new _CommonStep();
            _CommonStepSimple csStepSimple = new _CommonStepSimple();
            snap.an_existing_prescription_in_RXPData_database_with("normal");
            cstep.a_event_for_topic("refillRequest","rxpdata_create_refill_dispense");
            eventStorage.findEvent("refillRequest").replaceBodyAttribute("prescriptionCode", defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code"));
            eventStorage.findEvent("refillRequest").replaceBodyAttribute("lastDispenseCode", defineValue("rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code"));
            eventStorage.findEvent("refillRequest").replaceBodyAttribute("contextId",UUID.randomUUID());
            csStepSimple.the_event_is_produced_by_test();
        }
    }

}



