@featureid=RXRPRP-3338 @capabilityid=RXRPRP-3333  @jiraid=RXRPD-37440  @domain=RxPData @prettify
Feature:Publish Delete Dispense failed Event if no dispense in progress
  As a RxP I want to be able to trigger Delete Rx in renewal system
  when I want to delete that dispense for any internal valid reason
  So That current dispense, which is in sold or deleted status, can publish deletefailed event with data

  @jiraid=RXRPD-40325 @acceptance @auto
  Scenario: Validate Published Delete Dispense failed Event if Dispense in Delivered (sold)/DELETE status
    Given dispense with statuses "ENTERED"
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | ~dispense_code |
    When the user produces the event
    And wait for "5" seconds
    Then the system produces a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | ~dispense_code |
    And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | ~dispense_code |
    When the user produces the event
    And wait for "5" seconds
    Then the system produces a new event to topic "rxpdata_dispense_deletefail" with data
      | failureMessage    | Dispense with deleted status can't be deleted! |
      | dispenseCode      | ~dispense_code                                 |
      | failureReasonCode | RXD-RNP-1000-0000                              |

  @jiraid=RXRPD-40326 @acceptance @auto
  Scenario: Validate Published Delete Dispense failed Event if Dispense record not found
    Given an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
    And the event has data as
      | dispenseCode | ~invalidDispenseCode |
    When the user produces the event
    And wait for "5" seconds
    Then the system does not produce a new event to topic "rxpdata_dispense_deleteresponse" with data
      | dispense.code | ~invalidDispenseCode |
    Then the system produces a new event to topic "rxpdata_dispense_deletefail" with data
      | failureMessage    | Prescription with the dispense code does not exist in the database |
      | dispenseCode      | ~invalidDispenseCode                                               |
      | failureReasonCode | RXD-RNF-1000-0000                                                  |

