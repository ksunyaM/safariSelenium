@featureid=RXRPRP-  @capabilityid=RXRPRP- @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-46441

Feature: RXPData kit will consume the Prescriber API and Update in Dispense Table for  Refill Dispenses

  @acceptance @auto  @jiraid=RXRPD-50164
  Scenario Outline: Rxp Data queries the Prescriber API and updates the details for Create Refill
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
    And update the event "refillRequest" with the "same" location
    When the user produces the event
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    Then verify new "normal" refill has been created with data
      |snapshot    |rxPDataSnapshot1|
      |event       |refillRequest   |
      |dispLoc     | same           |
      |prescriber  | <Prescriber>   |
    And the system produces a new event "RxPDispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|rxPDataSnapshot1.prescription_by_code[0].prescription_code|
      # refillDispenseCode is added in the dataStorage
      |dispense.code    |refillDispenseCode|
    Then verify "dispenseEvents" returned between "RxPDispenseCreated.dispense" and "rxPDataSnapshot1"
    Examples:
    |   Prescriber       |
    |  Prescriber        |
    |  PrescriberReq     |


  @acceptance @auto  @jiraid=RXRPD-50165
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
    When the user produces the event
    And Verify that no record is created in the database for "refill"
    Then verify the following messages: "Prescriber information is not availabe" in the application log
    Examples:
      |   Prescriber           |
      |  PrescriberNonReq1     |
      |  PrescriberNonReq2     |


  @acceptance @auto @jiraid=RXRPD-50166
  Scenario: Rxp Data queries the Prescriber API and fails for not finding the Prescriber
    When an existing prescription in RXPData database with "normal"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    When the user produces the event
    And Verify that no record is created in the database for "refill"
    Then verify the following messages: "Prescriber information is not availabe" in the application log