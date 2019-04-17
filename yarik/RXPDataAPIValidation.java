package com.wba.rxdata.test;

import com.wba.test.utils.*;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventStorage;
import com.wba.test.utils.kafka.EventUtils;
import com.wba.test.utils.kafka.consumer.ConsumerRecordsToEventsMapper;
import com.wba.test.utils.kafka.consumer.ConsumerService;
import com.wba.test.utils.kafka.producer.ProducerService;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Assert;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;


public class RXPDataAPIValidation extends _BaseStep {

    @Then("^verify returned \"([^\"]*)\" for api call to get \"([^\"]*)\"$")
    public void verify_returned_for_api_call_to_get(String apiResponse, String dataEvent) throws Throwable {
        switch (apiResponse){
            case "dispenseList":
                verifyDispenseList(dataEvent);
                break;
            default:
                break;
        }

    }

    @Then("^verify response for api call \"([^\"]*)\"$")
    public void verify_response_for_api_call(String apiResponse) throws Throwable{
        /*switch (apiResponse){
            case "prescription": verifyPrescription();
                                 break;
            case "dispense"    : verifyDispense();
                                 break;
            default:  throw new Exception("API Call not found");
        }*/

    }
    @Then("^verify response for api call \"([^\"]*)\" with \"([^\"]*)\"$")
    public void verify_response_for_api_call_with(String apiEvent, String dataEvent) throws Throwable {
        switch (apiEvent){
            case "getPrescription": verifyPrescription(apiEvent,dataEvent);
                break;
            case "dispense"    : verifyDispense();
                break;
            default:  throw new Exception("API Call not found");
        }

    }

    public void verifyPrescription(String apiEvent,String dataEvent)
    {
        Assert.assertEquals(defineValue(dataEvent+".prescription_code"),defineValue(apiEvent+".code"));
        Assert.assertEquals(defineValue(dataEvent+".rx_authorizer"),defineValue(apiEvent+".rxAuthorizer"));
        Assert.assertEquals(defineValue(dataEvent+".rx_auto_refill"),defineValue(apiEvent+".rxAutoRefill"));
        Assert.assertEquals(defineValue(dataEvent+".rx_comments"),defineValue(apiEvent+".rxComments"));
        Assert.assertEquals(defineValue(dataEvent+".rx_coupon"),defineValue(apiEvent+".rxCoupon"));
        Assert.assertEquals(defineValue(dataEvent+".rx_coupon_actual_product_pack_code"),defineValue(apiEvent+".rxCouponActualProductPackCode"));
        Assert.assertEquals(defineValue(dataEvent+".rx_generic_substitute_preference"),defineValue(apiEvent+".rxGenericSubstitutePreference"));
        Assert.assertEquals(defineValue(dataEvent+".rx_intermediary_authorization_id"),defineValue(apiEvent+".rxIntermediaryAuthorizationId"));
        Assert.assertEquals(defineValue(dataEvent+".rx_last_dispense_number"),defineValue(apiEvent+".rxLastDispenseNumber"));
        Assert.assertEquals(defineValue(dataEvent+".rx_print_generic_product_name"),defineValue(apiEvent+".rxPrintGenericProductName"));
        Assert.assertEquals(defineValue(dataEvent+".rx_print_savings"),defineValue(apiEvent+".rxPrintSavings"));
        Assert.assertEquals(defineValue(dataEvent+".rx_public_safety_department_number"),defineValue(apiEvent+".rxPublicSafetyDepartmentNumber"));
        Assert.assertEquals(defineValue(dataEvent+".rx_quantity_remaining"),defineValue(apiEvent+".rxQuantityRemaining"));
        Assert.assertEquals(defineValue(dataEvent+".rx_refills_remaining"),defineValue(apiEvent+".rxRefillsRemaining"));
        Assert.assertEquals(defineValue(dataEvent+".rx_secondary_presciber_code"),defineValue(apiEvent+".rxSecondaryPrescriberCode"));
        Assert.assertEquals(defineValue(dataEvent+".rx_secondary_prescriber_qualifier"),defineValue(apiEvent+".rxSecondaryPrescriberQualifier"));
        Assert.assertEquals(defineValue(dataEvent+".rx_status"),defineValue(apiEvent+".rxStatus"));
        Assert.assertEquals(defineValue(dataEvent+".rx_substitute_type"),defineValue(apiEvent+".rxSubstituteType"));
        Assert.assertEquals(defineValue(dataEvent+".total_quantity_dispensed"),defineValue(apiEvent+".rxTotalQuantityDispensed"));
        Assert.assertEquals(defineValue(dataEvent+".spec_direction"),defineValue(apiEvent+".specDirection"));
        Assert.assertEquals(defineValue(dataEvent+".spec_dispense_as_written"),defineValue(apiEvent+".specDispenseAsWritten"));
        Assert.assertEquals(defineValue(dataEvent+".spec_image_id"),defineValue(apiEvent+".specImageId"));
        Assert.assertEquals(defineValue(dataEvent+".spec_immutable"),defineValue(apiEvent+".specImmutable"));
        Assert.assertEquals(defineValue(dataEvent+".spec_location_number"),defineValue(apiEvent+".specLocationNumber"));
        Assert.assertEquals(defineValue(dataEvent+".spec_location_type"),defineValue(apiEvent+".specLocationType"));
        Assert.assertEquals(defineValue(dataEvent+".spec_number_refill"),defineValue(apiEvent+".specNumberRefill"));
        Assert.assertEquals(defineValue(dataEvent+".spec_original_entered_datetime"),defineValue(apiEvent+".specOriginalEnteredDateTime").toString().replace("T"," "));
        Assert.assertEquals(defineValue(dataEvent+".spec_original_quantity_dispensed"),defineValue(apiEvent+".specOriginalQuantityDispensed"));
        Assert.assertEquals(defineValue(dataEvent+".spec_original_quantity_prescribed"),defineValue(apiEvent+".specOriginalQuantityPrescribed"));
        Assert.assertEquals(defineValue(dataEvent+".spec_origin_code"),defineValue(apiEvent+".specOriginCode"));
        Assert.assertEquals(defineValue(dataEvent+".spec_patient_code"),defineValue(apiEvent+".specPatientCode"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescribed_actual_product_pack_code"),defineValue(apiEvent+".specPrescribedActualProductPackCode"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescriber_code"),defineValue(apiEvent+".specPrescriberCode"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescriber_dea_number"),defineValue(apiEvent+".specPrescriberDeaNumber"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescriber_dea_suffix"),defineValue(apiEvent+".specPrescriberDeaSuffix"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescriber_location_code"),defineValue(apiEvent+".specPrescriberLocationCode"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescriber_supervisor_first_name"),defineValue(apiEvent+".specPrescriberSupervisorFirstName"));
        Assert.assertEquals(defineValue(dataEvent+".spec_prescriber_supervisor_last_name"),defineValue(apiEvent+".specPrescriberSupervisorLastName"));
        Assert.assertEquals(defineValue(dataEvent+".spec_fill_expiration_date").toString().replace(" 00:00:00.000Z",""),defineValue(apiEvent+".specFillExpirationDate").toString().replace("T"," "));
        Assert.assertEquals(defineValue(dataEvent+".spec_total_quantity_prescribed"),defineValue(apiEvent+".specTotalQuantityPrescribed"));
        Assert.assertEquals(defineValue(dataEvent+".spec_unlimited_fill"),defineValue(apiEvent+".specUnlimitedFill"));
        Assert.assertEquals(defineValue(dataEvent+".spec_written_date").toString().replace(" 00:00:00.000Z",""),defineValue(apiEvent+".specWrittenDate").toString().replace("T"," "));


    }

    public void verifyDispense()
    {
        int noOfDispenses = asInt(defineValue("C.dispenses.length()").toString());
        for(int i = 0; i<noOfDispenses;i++){

        }
    }

    public void verifyDispenseList(String dataEvent){
        int noOfDispenses = asInt(defineValue("C.dispenses.length()").toString());

        for(int i = 0; i<noOfDispenses;i++){
            String currentDispense = "C.dispenses["+i+"]";
            String currentDBDispense = dataEvent+i;
            if(i+1 != noOfDispenses){
                Timestamp currentDispenseSoldDateTime = Timestamp.valueOf(defineValue(currentDispense+".soldDateTime").toString().replaceAll("[a-zA-Z]"," "));
                Timestamp oldDispenseSoldDateTime =  Timestamp.valueOf(defineValue("C.dispenses["+(i+1)+"]"+".soldDateTime").toString().replaceAll("[a-zA-Z]"," "));

                Assert.assertTrue(currentDispenseSoldDateTime.after(oldDispenseSoldDateTime));
            }

            Assert.assertEquals(defineValue(currentDBDispense+".actual_product_pack_code"),defineValue(currentDispense+".actualProductPackCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".adjudication_code"),defineValue(currentDispense+".adjudicationCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".authorizer"),defineValue(currentDispense+".authorizer"));
            Assert.assertEquals(defineValue(currentDBDispense+".claim_reference_number"),defineValue(currentDispense+".claimReferenceNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".cash_discount_saving_amount_amount"),Double.parseDouble(defineValue(currentDispense+".cashDiscountSavingAmount.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".dispense_code"),defineValue(currentDispense+".code"));
            Assert.assertEquals(defineValue(currentDBDispense+".copay_amount_amount"),Double.parseDouble(defineValue(currentDispense+".copayAmount.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".clinical_reviewer"),defineValue(currentDispense+".clinicalReviewedBy.employeeNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".clinical_reviewer_type"),defineValue(currentDispense+".clinicalReviewedBy.employeeType"));
            Assert.assertEquals(defineValue(currentDBDispense+".data_enterer"),defineValue(currentDispense+".dataEnteredBy.employeeNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".data_enterer_type"),defineValue(currentDispense+".dataEnteredBy.employeeType"));
            Assert.assertEquals(defineValue(currentDBDispense+".days_supply"),defineValue(currentDispense+".daysSupply"));
            Assert.assertEquals(defineValue(currentDBDispense+".discount_amount_amount"),Double.parseDouble(defineValue(currentDispense+".discountAmount.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".discount_type"),defineValue(currentDispense+".discountType"));
            Assert.assertEquals(defineValue(currentDBDispense+".dispense_number"),defineValue(currentDispense+".dispenseNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".create_datetime"),defineValue(currentDispense+".createdDateTime").toString().replace("T"," "));
            Assert.assertEquals(defineValue(currentDBDispense+".general_recipient_number"),defineValue(currentDispense+".generalRecipientNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".generic_substitution_saving_amount"),Double.parseDouble(defineValue(currentDispense+".genericSubstitutionSaving.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".location_number"),defineValue(currentDispense+".locationNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".location_type"),defineValue(currentDispense+".locationType"));
            Assert.assertEquals(defineValue(currentDBDispense+".origin_code"),defineValue(currentDispense+".originCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".patient_payment_amount_amount"),Double.parseDouble(defineValue(currentDispense+".patientPaymentAmount.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".patient_prescriber_data_reviewer"),defineValue(currentDispense+".patPbrDataReviewedBy.employeeNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".patient_prescriber_data_reviewer_type"),defineValue(currentDispense+".patPbrDataReviewedBy.employeeType"));
            Assert.assertEquals(defineValue(currentDBDispense+".pay_code"),defineValue(currentDispense+".payCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".payment_method"),defineValue(currentDispense+".paymentMethod"));
            Assert.assertEquals(defineValue(currentDBDispense+".pharmacist_of_record"),defineValue(currentDispense+".pharmacistOfRecord.employeeNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".pharmacist_of_record_employee_type"),defineValue(currentDispense+".pharmacistOfRecord.employeeType"));
            Assert.assertEquals(defineValue(currentDBDispense+".plan_code"),defineValue(currentDispense+".planCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".prescription_code"),defineValue(currentDispense+".prescriptionCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".price_override_amount_amount"),Double.parseDouble(defineValue(currentDispense+".priceOverrideAmount.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".prior_authorization_code"),defineValue(currentDispense+".priorAuthorizationCode"));
            Assert.assertEquals(defineValue(currentDBDispense+".prior_authorization_number"),defineValue(currentDispense+".priorAuthorizationNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".product_average_wholesale_price_amount"),Double.parseDouble(defineValue(currentDispense+".productAverageWholesalePrice.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".product_data_reviewer"),defineValue(currentDispense+".productDataReviewedBy.employeeNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".product_data_reviewer_type"),defineValue(currentDispense+".productDataReviewedBy.employeeType"));
            Assert.assertEquals(defineValue(currentDBDispense+".quantity_dispensed"),defineValue(currentDispense+".quantityDispensed"));
            Assert.assertEquals(defineValue(currentDBDispense+".retail_price_amount"),Double.parseDouble(defineValue(currentDispense+".retailPrice.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".rx_number"),defineValue(currentDispense+".rxNumber"));
            Assert.assertEquals(defineValue(currentDBDispense+".service_place"),defineValue(currentDispense+".servicePlace"));
            Assert.assertEquals(defineValue(currentDBDispense+".sold_amount_amount"),Double.parseDouble(defineValue(currentDispense+".soldAmount.amount").toString()));
            Assert.assertEquals(defineValue(currentDBDispense+".sold_datetime"),defineValue(currentDispense+".soldDateTime").toString().replace("T"," "));
            Assert.assertEquals(defineValue(currentDBDispense+".dispense_status"),defineValue(currentDispense+".status"));
            Assert.assertEquals(defineValue(currentDBDispense+".submitted_copay_amount_amount"),Double.parseDouble(defineValue(currentDispense+".submittedCopayAmount.amount").toString()));


        }
    }

    @Then("^Consumed Event \"([^\"]*)\" has values$")
    public void an_prescription_in_RxPData_database_with_status_and_quantity(String dispenseQuantity, DataTable data) throws Throwable {
        Map<String, String> parameters = data.asMap(String.class, String.class);

        SoftAssert2 sa = new SoftAssert2();
        sa.on();

        for(Map.Entry<String, String> field : parameters.entrySet()) {
            sa.equals(field.getKey(), field.getValue());
        }

        sa.failIfErrors();
    }
}
