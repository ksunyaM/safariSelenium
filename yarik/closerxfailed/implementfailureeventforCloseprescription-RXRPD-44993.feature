@jiraid=RXRPD-44993 @featureid=RXRPRP-4791 @capabilityid=RXRPRP-4433  @domain=RxPData @prettify
Feature:As a pharmacy user, I want to  able to trigger a notification to close Rx failed in RxMS
  when the Rx is imported to renewal system and current dispense is in progress  So that RxMS can close the respective Rx

  @jiraid=RXRPD-46170  @acceptance @auto
  Scenario: Validate close prescription failed for invalid prescription
    Given an existing prescription in RXPData database with RxStatus "ACTIVE"
    And an event for topic "rxpdata_prescription_closerequest"
    And the event has data as random prescriptionCode
    When the user produces the event
    Then wait for "5" seconds
    And the system produces a new event "RxPPrescriptionCloseFailed" to topic "rxpdata_prescription_closefail" with data
      | prescriptionCode | P.prescriptionCode |
    And validate close Prescription Reasons "RxPPrescriptionCloseFailed"

  @jiraid=RXRPD-44409 @acceptance @auto
  Scenario: Validate Close rx for close (Inactive) prescription
    Given an existing prescription in RXPData database with RxStatus "CLOSED"
    And an event for topic "rxpdata_prescription_closerequest"
    And the event has data as
      | prescriptionCode        | rxPDataSnapshot0.prescription_by_code[0].prescription_code |
    Then wait for "5" seconds
    When the user produces the event
    Then wait for "5" seconds
    And the system produces a new event "RxPPrescriptionCloseFailed" to topic "rxpdata_prescription_closefail" with data
      | prescriptionCode              | rxPDataSnapshot0.prescription_by_code[0].prescription_code |
    And  validate close Prescription Reasons for "RxPPrescriptionCloseFailed"
