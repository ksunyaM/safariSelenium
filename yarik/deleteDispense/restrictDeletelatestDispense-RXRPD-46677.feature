@featureid=RXRPRP-4945 @capabilityid=RXRPRP-3333 @RXRPD-46677 @domain=RxPData @prettify
Feature: As Rxp System I want to be able to restri ct the latest dispense under patient order , in order app,
  So That I can only delete the one is in progress, but not sold and do to allow to delete other one for adjudication reason.

  @jiraid=RXRPD-50441 @acceptance @auto
  Scenario: Delete Latest Dispense from List of Dispenses
    Given an prescription in RXPData database with "FiveDispenses"
    And get the last dispense code for deleting "rxPDataSnapshot0"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | DeleteDispenseLastCode |
    When the user produces the event
    And wait for "5" seconds
    And the system produces a new event to topic "rxpdata_dispense_deleterequest" with data
      | dispenseCode | DeleteDispenseLastCode |
    And Deleter Added Saved and Updated in RxPData Data Base "rxPDataSnapshot1"
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | DeleteDispenseLastCode |

  @jiraid=RXRPD-50442 @acceptance @auto
  Scenario: Delete Old Dispense from List of Dispenses | Dispense Deleted Event is not triggered
    Given an prescription in RXPData database with "FiveDispenses"
    And get the last dispense code for deleting "rxPDataSnapshot0"
    And Find not deleted Dispense "rxPDataSnapshot0"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | NotDeletedDisepsneCode |
    When the user produces the event
    And wait for "5" seconds
    And the system produces a new event to topic "rxpdata_dispense_deleterequest" with data
      | dispenseCode | NotDeletedDisepsneCode |
    And the system does not produce a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | NotDeletedDisepsneCode |
    And the system produces a new event to topic "rxpdata_dispense_deletefail" with data
      | dispenseCode      | NotDeletedDisepsneCode                              |
      | failureReasonCode | RXD-RNF-1100-0002                                   |
      | failureMessage    | dispense code is not last dispense in the database. |
#    Then verify the following messages: "dispense code is not last dispense in the database." in the application log

  @jiraid=RXRPD-50501 @acceptance @auto
  Scenario Outline:  Validate Delete Dispense event which is in SOLD and DELIVERED status
    Given dispense with statuses "<statuses>"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | ~dispense_code |
    When the user produces the event
    And wait for "5" seconds
    And the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | ~dispense_code |
    Examples:
      | statuses  |
      | DELIVERED |

  @jiraid=RXRPD-50502@acceptance @auto
  Scenario: Delete Multiple Dispense from List of Dispenses
    Given an prescription in RXPData database with "FiveDispenses"
    And get the last dispense code for deleting "rxPDataSnapshot0"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | DeleteDispenseLastCode |
    When the user produces the event
    And wait for "5" seconds
    And Deleter Added Saved and Updated in RxPData Data Base "rxPDataSnapshot1"
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | DeleteDispenseLastCode |
    And Find not deleted Dispense "rxPDataSnapshot0"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | NotDeletedDisepsneCode |
    When the user produces the event
    And wait for "5" seconds
    And the system does not produce a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | NotDeletedDisepsneCode |
    And the system produces a new event to topic "rxpdata_dispense_deletefail" with data
      | dispenseCode      | NotDeletedDisepsneCode                              |
      | failureReasonCode | RXD-RNF-1100-0002                                   |
      | failureMessage    | dispense code is not last dispense in the database. |
#    Then verify the following messages: "dispense code is not last dispense in the database." in the application log


  @jiraid=RXRPD-50504 @acceptance @auto
  Scenario: Validate Delete Dispense event | Delete Same Dispense Multiple times
    Given an prescription in RXPData database with "FiveDispenses"
    And get the last dispense code for deleting "rxPDataSnapshot0"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | DeleteDispenseLastCode |
    When the user produces the event
    And wait for "5" seconds
    And Deleter Added Saved and Updated in RxPData Data Base "rxPDataSnapshot1"
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | DeleteDispenseLastCode |
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode                                                                         | DeleteDispenseLastCode |
      | deletedReason["com.wba.rxpdata.dispense.deleterequest.event.avro.DeletedReasonType"] | WRONG_PRESCRIBER       |
    When the user produces the event
    And wait for "5" seconds
    Then the system does not produce a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | DeleteDispenseLastCode |
      | deletedReason | WRONG_PRESCRIBER       |
    And the system produces a new event to topic "rxpdata_dispense_deletefail" with data
      | dispenseCode      | DeleteDispenseLastCode                         |
      | failureReasonCode | RXD-RNP-1000-0000                              |
      | failureMessage    | Dispense with deleted status can't be deleted! |

  @jiraid=RXRPD-50505 @acceptance @auto
  Scenario: Validate Prescription with the dispense code does not exist in the database
    Given  an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | uuid:: |
    When the user produces the event
    And wait for "5" seconds
    And the system does not produce a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | P.dispenseCode |
    And the system produces a new event to topic "rxpdata_dispense_deletefail" with data
      | dispenseCode      | P.dispenseCode                                                     |
      | failureReasonCode | RXD-RNF-1000-0000                                                  |
      | failureMessage    | Prescription with the dispense code does not exist in the database |
#    Then verify the following messages: "Prescription with the dispense code does not exist in the database" in the application log
