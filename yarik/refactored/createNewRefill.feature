@featureid=RXRPRP-3228 @capabilityid=RXRPRP-3240 @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-40888

Feature: As a RXPDATA kit,
  I want to be able to create new refill
  when I get last dispense code and prescription code
  through create refill dispense event
  and publish dispense created event once a refill is done


  @acceptance @auto @jiraid=RXRPD-35899
  Scenario Outline: RXPData kit consumes event to create new dispense as per received prescription code and dispense code for same location
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an existing prescription in RXPData database with "<prescriptionType>"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has json "<json>"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    And update the event "refillRequest" with the "<disploc>" location
    When the user produces the event
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    Then verify new "<prescriptionType>" refill has been created with data
      |snapshot |rxPDataSnapshot1|
      |event    |refillRequest   |
      |dispLoc  | <disploc>      |
      |prescriber  | Prescriber   |
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot1" for "REVIEWED"
    Examples:
      |prescriptionType           | json                            |  disploc  |
      |normal                     | createRefillDispense_nonM.json  |  same     |
      |inadequateRemainingQuantity| createRefillDispense_nonM.json  |  same     |
      |unlimitedRefill            | createRefillDispense_nonM.json  |  same     |
      |normal                     | createRefillDispense.json       |  same     |
      |inadequateRemainingQuantity| createRefillDispense.json       |  same     |
      |unlimitedRefill            | createRefillDispense.json       |  same     |
      |normal                     | createRefillDispense_nonM.json  |diffOld    |
      |inadequateRemainingQuantity| createRefillDispense_nonM.json  |diffNewLoc |
      |unlimitedRefill            | createRefillDispense_nonM.json  |diffMax    |
      |normal                     | createRefillDispense.json       |diffOld    |
      |inadequateRemainingQuantity| createRefillDispense.json       |diffNewLoc |
      |unlimitedRefill            | createRefillDispense.json       |diffMax    |



  @acceptance @auto @smoke @jiraid=RXRPD-38115
  Scenario Outline:  RXPData kit consumes event to create new dispense as per received prescription code and dispense code and publish dispense created event
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an existing prescription in RXPData database with "<prescriptionType>"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has json "<json>"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    When the user produces the event
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    And get RxpData snapshot
    And the system produces a new event "RxPDispenseCreated" to topic "rxpdata_dispense_createresponse" with data
      |prescription.code|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
    Then verify that "RxPDispenseCreated.contextId == refillRequest.contextId"
    Then verify "prescriptionEvent" returned between "RxPDispenseCreated.prescription" and "rxPDataSnapshot1.prescription_by_code[0],<prescriptionType>"
    Then verify "dispenseEvents" returned between "RxPDispenseCreated.dispense" and "rxPDataSnapshot1"
    And verify that "RxPDispenseCreated.dispense.fillType == MANUAL_REFILL"
    Examples:
      |prescriptionType           | json                            |
      |normal                     | createRefillDispense_nonM.json  |
      |inadequateRemainingQuantity| createRefillDispense_nonM.json  |
      |unlimitedRefill            | createRefillDispense_nonM.json  |
      |normal                     | createRefillDispense.json       |
      |inadequateRemainingQuantity| createRefillDispense.json       |
      |unlimitedRefill            | createRefillDispense.json       |



  @acceptance @auto @smoke @jiraid=RXRPD-34256
  Scenario Outline: RXPData kit doesn't consume event to create new dispense for invalid format prescriptionCode and dispenseCode and logs error
    Given an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|<prescriptionCode>|
      |lastDispenseCode|<dispenseCode>|
    When the user produces the event
    Then wait for "5" seconds
    And verify the following messages: "<message>" in the application log
    Examples:
      |prescriptionCode                           |dispenseCode                           | message                                        |
      |132123213132                               |c0e75a60-c362-11e8-a355-529269fb1459   |supplied prescription Code is not in UUID format|
      |c0e75a60-c362-11e8-a355-529269fb1459       |12312312                               |supplied dispense Code is not in UUID format    |