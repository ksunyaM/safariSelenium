@featureid=RXRPRP-3338 @capabilityid=RXRPRP-3333 @RXRPD-34878 @domain=RxPData @prettify
Feature:Publish Delete Dispense Not Sold in RxMS( Internal Events)
  As a RxP I want to be able to trigger Delete Rx in renewal system
  when I want to delete that dispense for any internal valid reason
  So That current dispense, which is not sold, can be deleted from renewal system.

  @jiraid=RXRPD-37170 @acceptance @auto
  Scenario Outline: Validating Published event for Prescription which is In progress status ("ENTERED", "FILLED", "PRINTED", "REVIEWED", "READY")
    Given dispense with statuses "<Statuses>"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode  | ~dispense_code |
    When the user produces the event
    And wait for "5" seconds
    And new fields are Added Saved and Updated in RxPData Data Base "rxPDataSnapshot2"
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | ~dispense_code |
    Examples:
      | Statuses |
      | PRINTED  |
      | REVIEWED |
      | FILLED   |
      | READY    |