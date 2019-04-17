@featureid=RXRPRP-  @capabilityid=RXRPRP- @process=RxpData @auto @api @domain=RxProcessing  @jiraid=RXRPD-50776 @jiraid=RXRPD-50598

Feature: Failure Scenarios for Create Dispense

  @acceptance @smoke @jiraid=RXRPD-54805
  Scenario Outline: Rxp Data queries the Prescriber API and fails for not finding the required Prescriber Fields
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    Given update the "<Prescriber>" response Json with Data
    And Upload all Mock to server
    When an existing prescription in RXPData database with "normal"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |contextId | randomstring::7                                                 |
    When the user produces the event
    Then verify the following messages: "Publishing RxPDispenseRefillFailed event" in the application log
    And wait for "5" seconds
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|refillRequest.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |  <failureCode>   |
      |failureMessage    | <failureMessage> |
    Examples:
      |   Prescriber           |   failureCode          | failureMessage                                                |
      |  PrescriberNonReq1     |  RXD-EVAF-1000-0001    |RxPrescriber firstname/lastname are not obtained in API call   |
      |  PrescriberNonReq2     |  RXD-EVAF-1000-0001    |RxPrescriber firstname/lastname are not obtained in API call   |


  @acceptance @jiraid=RXRPD-54806
  Scenario Outline: Rxp Data queries the Prescriber API and fails for not Unreachable code and Location Not found
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an existing prescription in RXPData database with "normal"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |contextId | randomstring::7                                                 |
    When the user produces the event
    Then verify the following messages: "Publishing RxPDispenseRefillFailed event" in the application log
    And wait for "5" seconds
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|refillRequest.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |  <failureCode>   |
      |failureMessage    | <failureMessage> |
    Examples:
      |   Prescriber                | failureCode          | failureMessage                   |
      |  PrescriberCodeNotFound     |RXD-ERNF-1000-0001    |rxprescriber resource not found   |
      |  PrescriberLocationNotFound |RXD-ERNF-1000-0001    |rxprescriber resource not found   |
     #|  PrescriberUnreachable      |                      |                                  |



  @acceptance @jiraid=RXRPD-54807
  Scenario Outline: Rxp Data queries the Prescriber API and fails for prescriber retry
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an existing prescription in RXPData database with "normal"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |contextId | randomstring::7                                                 |
    When the user produces the event
    Then verify the following messages: "Publishing RxPDispenseRefillFailed event" in the application log
    Then verify the following messages: "Retryable method getPrescriberInfo threw 3th exception" in the application log
    And wait for "5" seconds
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|refillRequest.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |  <failureCode>   |
      |failureMessage    | <failureMessage> |
    Examples:
      |   Prescriber                | failureCode          | failureMessage                    |
      |  PrescriberDBDown           | RXD-EURE-1000-0000   | RxPrescriber service unreachable  |
      |  PrescriberUnexpected       | RXD-EUNK-1000-0001   | Prescriber External Unknown Error |



  @acceptance @jiraid=RXRPD-54808
  Scenario: Create Refill fails when the dispense status code is DELETED
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given dispense with statuses "DELETED"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |contextId | randomstring::7                                                 |
    When the user produces the event
    And wait for "5" seconds
    And the system produces a new event "DispenseFailed" to topic "rxpdata_dispense_refillfail" with data
      |contextId|refillRequest.contextId|
    And verify failed dispense for "DispenseFailed" has data
      |failureReasonCode |RXD-VAF-1100-0001|
      |failureMessage    |Create Refill failure - previous Dispense code is not correct or Dispense status is Deleted|




  @acceptance @jiraid=RXRPD-54809
  Scenario Outline: Rxp Data queries the Prescriber API fails and gives retry and updates the details for Create Refill
    Given User prepare the "<Prescriber>" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    And Upload all Mock to server
    When an existing prescription in RXPData database with "normal"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |contextId | randomstring::7                                                 |
    And update the event "refillRequest" with the "same" location
    When the user produces the event
    And delete the old mock data and create a new mock data
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Then verify the following messages: "Retryable method getPrescriberInfo threw 1th exception" in the application log
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    And wait for "5" seconds
    Then verify new "normal" refill has been created with data
      |snapshot    |rxPDataSnapshot1|
      |event       |refillRequest   |
      |dispLoc     | same           |
      |prescriber  | Prescriber     |
    And the system produces a new event "RxPDispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|rxPDataSnapshot1.prescription_by_code[0].prescription_code|
      # refillDispenseCode is added in the dataStorage
      |dispense.code    |refillDispenseCode|
    Then verify "dispenseEvents" returned between "RxPDispenseCreated.dispense" and "rxPDataSnapshot1"
    Examples:
      |   Prescriber         |
      |  PrescriberDBDown    |
