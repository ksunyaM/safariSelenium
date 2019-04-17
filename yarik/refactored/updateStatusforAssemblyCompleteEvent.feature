@featureid=RXRPRP-3234 @capabilityid=RXRPRP-3240 @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-32693

Feature: As a Rxp Data Kit
  I want to update status of dispense
  upon receiving assembly completed event

  @acceptance @auto @smoke @jiraid=RXRPD-34271
  Scenario Outline: Rxp Data updates status of dispense from PRINTED to FILLED upon receiving assembly completed event
    Given dispense with statuses "<dispenseStatus>"
    And an event "assemblyCompleted" for topic "rxassembly_complete"
    And save the DispenseCode "dispCode,PRINTED" to be changed in DataStorage  "rxPDataSnapshot1"
    And the event has data as
      |dispenseCode|dispCode|
      |rxNumber    |rxPDataSnapshot1.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify the status is correctly updated in "<dataSnapshot>,FILLED"
    And verify the Dispense codes are updated for other tables for "<dataSnapshot>" for "FILLED,dispCode"
  Examples:
      |   dispenseStatus        | dataSnapshot       |
      |PRINTED                  | rxPDataSnapshot2   |
      |PRINTED,PRINTED          | rxPDataSnapshot3   |
      |PRINTED,PRINTED,FILLED   | rxPDataSnapshot4   |


  @acceptance @auto @smoke @jiraid=RXRPD-34272
  Scenario: Rxp Data does not update status of dispense upon receiving assembly completed event when status is not PRINTED
    Given dispense with statuses "REVIEWED"
    And an event "assemblyCompleted" for topic "rxassembly_complete"
    And the event has data as
      |dispenseCode|rxPDataSnapshot1.disp_by_prescription_code[0].dispense_code|
      |rxNumber    |rxPDataSnapshot1.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And load the snapshot for the "rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify that "FILLED != rxPDataSnapshot1.disp_by_prescription_code[0].dispense_status"
    Then verify the following messages: "Invalid dispense status update request in Assembly" in the application log










