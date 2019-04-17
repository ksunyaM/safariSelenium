@jiraid=RXRPD-27090 @featureid=RXRPRP-4791 @capabilityid=RXRPRP-4433  @domain=RxPData @prettify

Feature:As a pharmacy user, I want to  able to trigger a notification to close Rx in RxMS
  when the Rx is imported to renewal system and current dispense is in progress  So that RxMS can close the respective Rx

  @jiraid=RXRPD-44406 @jiraid=RXRPD-44407 @acceptance @auto
  Scenario Outline: Validating delete dispense event for Prescription which is In progress status("ENTERED", "FILLED", "PRINTED", "REVIEWED", "READY") to generate explicit delete dispense event so that order can consume and update the order line status
    Given dispense with statuses "<Statuses>"
    And an event for topic "rxpdata_prescription_closerequest"
    And the event has data as
      | prescriptionCode | ~prescription_code |
    When the user produces the event
    Then wait for "5" seconds
    And get RxpData snapshot
    And the system produces a new event "RxPDispenseClosed" to topic "rxpdata_prescription_closedresponse" with data
      | prescriptionCode | rxPDataSnapshot2.prescription_by_code[0].prescription_code |
    Then verify the data for the closed Prescription for "RxPDispenseClosed" and "rxPDataSnapshot2.prescription_by_code[0]"
    And verify RxStatus "rxPDataSnapshot2.prescription_by_code[0]"
    Examples:
      | Statuses |
      | ENTERED  |
      | PRINTED  |
      | REVIEWED |
      | FILLED   |
      | PRINTED  |
      | DELIVERED|


  @jiraid=RXRPD-44408 @acceptance @auto
  Scenario: Validate Close rx for one prescription with multiple dispenses in progress status
    Given an prescription in RXPData database with "FiveDispenses"
    And an event for topic "rxpdata_prescription_closerequest"
    And the event has data as
      | prescriptionCode | ~prescription_code |
    When the user produces the event
    Then wait for "5" seconds
    And get RxpData snapshot
    Then wait for "5" seconds
    And the system produces a new event "RxPDispenseClosed" to topic "rxpdata_prescription_closedresponse" with data
      | prescriptionCode | rxPDataSnapshot1.prescription_by_code[0].prescription_code |
    Then verify the data for the closed Prescription for "RxPDispenseClosed" and "rxPDataSnapshot1.prescription_by_code[0]"
    And verify RxStatus "rxPDataSnapshot1.prescription_by_code[0]"
