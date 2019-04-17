@featureid=RXRPD-50975 @capabilityid=RXRPRP-5078 @RXRPRP-4805 @domain=RxPData @prettify
Feature:Publish Delete Dispense After Sold (Backend)
  As Pharmacy User I want to be able to Delete a dispense after it was sold for some valid reasons
  or events,so that I can take appropriate steps to replace the deleted dispense with appropriate fill

  @jiraid=RXRPD-RXRPD-55446 @acceptance @auto
  Scenario: Validating Published event for Prescription after SOLD (DELIVERED)
    Given dispense with statuses "DELIVERED"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | ~dispense_code |
    When the user produces the event
    And wait for "5" seconds
    And new fields are Added Saved and Updated in RxPData Data Base "rxPDataSnapshot2"
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | ~dispense_code |

  @jiraid=RXRPD-55450 @acceptance @auto
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




