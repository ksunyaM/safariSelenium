@featureid=RXRPRP- @capabilityid=RXRPRP-3240 @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-31743

Feature: As an Rxp Data Kit
   As an event is received from RxIntake a new prescription and Dispense is created and persisted and notification event is send for both prescription and Dispense.


  @acceptance @auto @smoke @jiraid=RXRPD-39239 @jiraid=RXRPD-50156
  Scenario Outline: RxpIntake sends Transfer Prescription event from  IC+ and Rxp Data persists the data as new Prescription and Dispense
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And the event "newPrescriptionDispense" has json "<json>"
    And updating the event "newPrescriptionDispense" with the dispenseType data "<dispenseType>"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    And store the "old" RxNumber with "newPrescriptionDispense" data
    When the user produces the event
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    And store the "new" RxNumber with "newPrescriptionDispense" data
    And verify rxNumber has been updated
    Then verify the event response "newPrescriptionDispense.prescription" and the persist data "rxPDataSnapshot0.prescription_by_code[0]" for prescription TransferPrescription "<dispenseType>" Type
    Then verify the event response "newPrescriptionDispense.dispense" and the persist data "rxPDataSnapshot0.disp_by_prescription_code[0]" for dispense "<dispenseType>" Type
    And verify the prescriber details "Prescriber" are updated in the persist data "rxPDataSnapshot0.disp_by_prescription_code[0]"
    And verify the transfer prescription for other tables
    And the system produces a new event "dispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|prescriptionData.prescription_code|
    Then verify that "newPrescriptionDispense.contextId == dispenseCreated.contextId"
    Then verify "prescriptionEvent" returned between "dispenseCreated.prescription" and "rxPDataSnapshot0.prescription_by_code[0],<dispenseType>"
    Then verify "dispenseEvents" returned between "dispenseCreated.dispense" and "rxPDataSnapshot0"
    And verify that "dispenseCreated.dispense.fillType == NEW_RX_IMPORT"
    And the system produces a new event "prescriptionCreated" to topic "rxpdata_prescription_createresponse" with data
      |prescription.code|prescriptionData.prescription_code|
    Then verify that "newPrescriptionDispense.contextId == prescriptionCreated.contextId"
    Then verify "prescriptionEvent" returned between "prescriptionCreated.prescription" and "rxPDataSnapshot0.prescription_by_code[0],<dispenseType>"

    Examples:
      |dispenseType                | json                                         |
      |normal                      | transferNewPrescriptionDispense_nonM.json    |
      |inadequateRemainingQuantity | transferNewPrescriptionDispense_nonM.json    |
      |unlimitedRefill             | transferNewPrescriptionDispense_nonM.json    |
      |normal                      | transferNewPrescriptionDispense.json         |
      |inadequateRemainingQuantity | transferNewPrescriptionDispense.json         |
      |unlimitedRefill             | transferNewPrescriptionDispense.json         |




  @acceptance @auto @jiraid=RXRPD-41889
  Scenario Outline: RxNumber logic for Transfer Prescription for  min max RxNumber generation and addition logic
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    And store the "<minorMax>" RxNumber with "newPrescriptionDispense" data
    When the user produces the event
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And store the "new" RxNumber with "newPrescriptionDispense" data
    And verify rxNumber has been updated
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    And verify the transfer prescription for other tables
    And the system produces a new event "dispenseCreated" to topic "rxpdata_dispense_createresponse" with data
         |prescription.code|prescriptionData.prescription_code|
    And verify the published RxNumber for "dispenseCreated"

    Examples:
      |minorMax |
      |min      |
      |old      |
     # |max      |
      |newloc   |

