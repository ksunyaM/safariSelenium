@featureid=RXRPRP-  @capabilityid=RXRPRP- @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-46441

Feature: RXPData kit will consume the Prescriber API and Update in Dispense Table for Transfer

  @acceptance @auto  @jiraid=RXRPD-50161
  Scenario Outline: Rxp Data queries the Prescriber API and updates the details while transfer
  Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
   Given update the "<Prescriber>" response Json with Data
   And Upload all Mock to server
   When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
   And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
   And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
   And new Rx Number for new Prescription is generated
   When the user produces the event
   And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
   And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
   And load the snapshot for the "prescriptionData.prescription_code" given Prescription
   And verify the prescriber details "<Prescriber>" are updated in the persist data "rxPDataSnapshot0.disp_by_prescription_code[0]"
   And the system produces a new event "dispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|prescriptionData.prescription_code|
   Then verify the prescriber is returned between "dispenseCreated.dispense.prescriber" and "rxPDataSnapshot0.disp_by_prescription_code[0]"
  Examples:
  |   Prescriber       |
  |  Prescriber        |
  |  PrescriberReq     |


  @acceptance @auto  @jiraid=RXRPD-50162
  Scenario Outline: Rxp Data queries the Prescriber API and fails for not finding the required Prescriber Fields
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    Given update the "<Prescriber>" response Json with Data
    And Upload all Mock to server
    When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And Verify that no record is created in the database for "transfer,newPrescriptionDispense"
    Then verify the following messages: "Prescriber information is not availabe" in the application log
    Examples:
      |   Prescriber           |
      |  PrescriberNonReq1     |
      |  PrescriberNonReq2     |




  @acceptance @auto  @jiraid=RXRPD-50163
  Scenario: Rxp Data queries the Prescriber API and fails when mock not found
    When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And Verify that no record is created in the database for "transfer,newPrescriptionDispense"
    Then verify the following messages: "Prescriber information is not availabe" in the application log

