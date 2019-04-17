@featureid=RXRPRP-3338 @capabilityid=RXRPRP-3333  @domain=RxPData @prettify
Feature:As a RxP I want to be able to trigger Delete Rx in renewal system  When I want to delete that
  dispense for any internal valid reason  So That current dispense,
  which is not sold, can be deleted from renewal system and also generate explicit delete dispense event
  so that order can consume and update the order line status.

  @jiraid=RXRPD-41873 @acceptance @auto
  Scenario Outline: Validating delete dispense event for Prescription which is In progress status("ENTERED", "FILLED", "PRINTED", "REVIEWED", "READY") to generate explicit delete dispense event so that order can consume and update the order line status
    Given dispense with statuses "<Statuses>"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | ~dispense_code |
    When the user produces the event
    And wait for "5" seconds
    And new fields are Added Saved and Updated in RxPData Data Base "rxPDataSnapshot2"
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | ~dispense_code |
    Then the system produces a new event to topic "orders_dispense_deleterequest" with data
      | dispenseCode | ~dispense_code |
    Examples:
      | Statuses |
      | ENTERED  |
      | PRINTED  |
      | REVIEWED |
      | FILLED   |
      | READY    |