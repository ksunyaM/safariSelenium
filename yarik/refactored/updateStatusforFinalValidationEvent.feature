@featureid=RXRPRP-3234 @capabilityid=RXRPRP-3240 @process=RxpData @api @domain=RxProcessing @jiraid=RXRPD-32695

Feature: As a Rxp Data Kit
  I want to update status of dispense
  upon receiving final validation event

  @acceptance @auto @smoke @jiraid=RXRPD-34273
  Scenario Outline: Rxp Data updates status of dispense from FILLED to READY upon receiving assembly completed event
    Given dispense with statuses "<dispenseStatus>"
    And an event "finalValidation" for topic "rxfinalvalidation_complete"
    And save the DispenseCode "dispCode,FILLED" to be changed in DataStorage  "rxPDataSnapshot1"
    And the event has data as
      |dispenseCode|dispCode|
      |rxNumber    |rxPDataSnapshot1.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify the status is correctly updated in "<dataSnapshot>,READY"
    And verify the Dispense codes are updated for other tables for "<dataSnapshot>" for "READY,dispCode"
  Examples:
  |   dispenseStatus      |  dataSnapshot    |
  |FILLED                 | rxPDataSnapshot2 |
  |FILLED,FILLED          | rxPDataSnapshot3 |
  |FILLED,FILLED,READY    | rxPDataSnapshot4 |


  @acceptance @auto @smoke @jiraid=RXRPD-34274
  Scenario: Rxp Data does not update status of dispense upon receiving final validation event when status is not FILLED
    Given dispense with statuses "REVIEWED"
    And an event "finalValidation" for topic "rxfinalvalidation_complete"
    And the schema name is "FinalValidationCompleted-value"
    And the event has data as
      |dispenseCode|rxPDataSnapshot1.disp_by_prescription_code[0].dispense_code|
      |rxNumber    |rxPDataSnapshot1.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And load the snapshot for the "rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify that "READY != rxPDataSnapshot2.disp_by_prescription_code[0].dispense_status"
    Then verify the following messages: "Invalid dispense status update request in Final validation" in the application log
