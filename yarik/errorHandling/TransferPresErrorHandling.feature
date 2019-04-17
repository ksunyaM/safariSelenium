@featureid=RXRPRP-  @capabilityid=RXRPRP- @process=RxpData @auto @api @domain=RxProcessing  @jiraid=RXRPD-50776 @jiraid=RXRPD-50598

Feature: Failure Scenarios for Transfer Prescription

  @acceptance @smoke @jiraid=RXRPD-54812
  Scenario Outline: Rxp Data queries the Prescriber API and fails for not finding the required Prescriber Fields
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    Then verify the following messages: "Publishing RxPDispenseRefillFailed event" in the application log
    And wait for "5" seconds
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|newPrescriptionDispense.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |  <failureCode>   |
      |failureMessage    | <failureMessage> |
    Examples:
      |   Prescriber           |   failureCode          | failureMessage                                                |
      |  PrescriberNonReq1     |  RXD-EVAF-1000-0001    |RxPrescriber firstname/lastname are not obtained in API call   |
      |  PrescriberNonReq2     |  RXD-EVAF-1000-0001    |RxPrescriber firstname/lastname are not obtained in API call   |




  @acceptance  @jiraid=RXRPD-54813
  Scenario Outline: Rxp Data queries the Prescriber API and get 404 errors
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    Then verify the following messages: "Publishing RxPDispenseRefillFailed event" in the application log
    And wait for "5" seconds
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|newPrescriptionDispense.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |  <failureCode>   |
      |failureMessage    | <failureMessage> |
    Examples:
      |   Prescriber                | failureCode          | failureMessage                                 |
      |  PrescriberCodeNotFound     |RXD-ERNF-1000-0001    |rxprescriber resource not found                 |
      |  PrescriberLocationNotFound |RXD-ERNF-1000-0001    |rxprescriber resource not found                 |
    # |  PrescriberUnreachable      |                      |                                                |



  @acceptance  @jiraid=RXRPD-54814
  Scenario Outline: Rxp Data queries the Prescriber API and get 500 errors
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    Then verify the following messages: "Publishing RxPDispenseRefillFailed event" in the application log
    And wait for "5" seconds
    Then verify the following messages: "Retryable method getPrescriberInfo threw 3th exception" in the application log
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|newPrescriptionDispense.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |  <failureCode>   |
      |failureMessage    | <failureMessage> |
    Examples:
      |   Prescriber                | failureCode          | failureMessage                    |
      |  PrescriberDBDown           | RXD-EURE-1000-0000   | RxPrescriber service unreachable  |
      |  PrescriberUnexpected       | RXD-EUNK-1000-0001   | Prescriber External Unknown Error |



  @acceptance  @jiraid=RXRPD-54815
  Scenario Outline: Rxp Data queries the Prescriber API and gives retry and updates the details for Transfer
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And delete the old mock data and create a new mock data
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Then verify the following messages: "Retryable method getPrescriberInfo threw 1th exception" in the application log
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And wait for "5" seconds
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    And verify the prescriber details "Prescriber" are updated in the persist data "rxPDataSnapshot0.disp_by_prescription_code[0]"
    And the system produces a new event "dispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|prescriptionData.prescription_code|
    Then verify the prescriber is returned between "dispenseCreated.dispense.prescriber" and "rxPDataSnapshot0.disp_by_prescription_code[0]"
    Examples:
      |   Prescriber         |
      |  PrescriberDBDown    |