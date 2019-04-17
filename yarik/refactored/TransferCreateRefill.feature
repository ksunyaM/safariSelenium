@featureid=RXRPRP- @capabilityid=RXRPRP-3240 @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-31743

Feature:RxpData gets the External Prescription and does a Refill for the same


  @acceptance @auto  @jiraid=RXRPD-44405
  Scenario Outline:  RXPData kit Transfer Rx and Create Refill Integrated
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "<dispenseType>"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    And the system produces a new event "dispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|prescriptionData.prescription_code|
      |dispense.code|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    Then verify "prescriptionEvent" returned between "dispenseCreated.prescription" and "rxPDataSnapshot0.prescription_by_code[0],<dispenseType>"
    Then verify "dispenseEvents" returned between "dispenseCreated.dispense" and "rxPDataSnapshot0"
    And verify that "dispenseCreated.dispense.fillType == NEW_RX_IMPORT"
    When an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    And update the event "refillRequest" with the "<disploc>" location
    When the user produces the event
    Then wait for "5" seconds
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    Then verify new "<dispenseType>" refill has been created with data
      |snapshot|rxPDataSnapshot1|
      |event   |refillRequest   |
      |dispLoc | <disploc>      |
      |prescriber  | Prescriber |
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot1" for "REVIEWED"
    And the system produces a new event "RxPDispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|rxPDataSnapshot1.prescription_by_code[0].prescription_code|
      # refillDispenseCode is added in the dataStorage
      |dispense.code    |refillDispenseCode|
    Then verify "prescriptionEvent" returned between "RxPDispenseCreated.prescription" and "rxPDataSnapshot1.prescription_by_code[0],<dispenseType>"
    Then verify "dispenseEvents" returned between "RxPDispenseCreated.dispense" and "rxPDataSnapshot1"
    And verify that "RxPDispenseCreated.dispense.fillType == MANUAL_REFILL"
    Examples:
      |dispenseType                |  disploc   |
      |normal                      |  same      |
      |inadequateRemainingQuantity |  same      |
      |unlimitedRefill             |  same      |
      |normal                      | diffNewLoc |
      |inadequateRemainingQuantity | diffNewLoc |
      |unlimitedRefill             | diffNewLoc |



  @acceptance @auto  @jiraid=RXRPD-50159
  Scenario:  RXPData kit Transfer Rx and Create Refill Integrated with new Prescriber for Refill
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    And delete the old mock data and create a new mock data
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    When an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    And update the event "refillRequest" with the "same" location
    When the user produces the event
    Then wait for "5" seconds
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    Then verify new "normal" refill has been created with data
      |snapshot|rxPDataSnapshot1   |
      |event   |refillRequest      |
      |dispLoc |   same       |
      |prescriber  | Prescriber   |
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot1" for "REVIEWED"
    And the system produces a new event "RxPDispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|rxPDataSnapshot1.prescription_by_code[0].prescription_code|
      # refillDispenseCode is added in the dataStorage
      |dispense.code    |refillDispenseCode|
    Then verify "prescriptionEvent" returned between "RxPDispenseCreated.prescription" and "rxPDataSnapshot1.prescription_by_code[0],normal"
    Then verify "dispenseEvents" returned between "RxPDispenseCreated.dispense" and "rxPDataSnapshot1"
    And verify that "RxPDispenseCreated.dispense.fillType == MANUAL_REFILL"
