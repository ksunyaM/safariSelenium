Feature: As a Rxp Data Kit
  I want to update patient responsibility of dispense
  upon receiving billing completed event

  @acceptance @auto @smoke @jiraid=RXRPD-48041
  Scenario: RXPData kit updates patient responsibility of dispense upon receiving billing completed event with value in patientResponsibility
    Given an existing prescription in RXPData database with "normal"
    When an event "billingCompleted" for topic "finance_billing_complete"
    And the event "billingCompleted" has data as
    |dispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    |rxNumber    |rxPDataSnapshot0.disp_by_prescription_code[0].rx_number    |
    When the user produces the event
    And wait for "5" seconds
    And get RxpData snapshot
    And verify that "billingCompleted.patientResponsibility.['com.wba.finance.billing.complete.event.avro.Currency'].decimalValue == rxPDataSnapshot1.disp_by_prescription_code[0].patient_responsibility"


  @acceptance @auto @jiraid=RXRPD-48042
  Scenario:  RXPData kit does not update patient responsibility of dispense upon receiving billing completed event with no value in patientResponsibility
    Given an existing prescription in RXPData database with "normal"
    When an event "billingCompleted" for topic "finance_billing_complete"
    And the event "billingCompleted" has json "billingCompletedM.json"
    And the event "billingCompleted" has data as
      |dispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |rxNumber    |rxPDataSnapshot0.disp_by_prescription_code[0].rx_number    |
    When the user produces the event
    And wait for "5" seconds
    And verify the following messages: "Cannot proceed further, PatientResponsibility is null!" in the application log
    And get RxpData snapshot
    And verify that "null:: == rxPDataSnapshot1.disp_by_prescription_code[0].patient_responsibility"

  @acceptance @auto @smoke @jiraid=RXRPD-48042
  Scenario:  RXPData kit does not update patient responsibility of dispense upon receiving billing completed event with billing status as BILLING_ERROR
    Given an existing prescription in RXPData database with "normal"
    When an event "billingCompleted" for topic "finance_billing_complete"
    And the event "billingCompleted" has json "billingCompletedM.json"
    And the event "billingCompleted" has data as
      |dispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |rxNumber    |rxPDataSnapshot0.disp_by_prescription_code[0].rx_number    |
      |billingStatus|BILLING_ERROR                                             |
    When the user produces the event
    And wait for "5" seconds
    And verify the following messages: "Cannot proceed further, BillingStatus is not BILLING_SUCCESS!" in the application log
    And get RxpData snapshot
    And verify that "null:: == rxPDataSnapshot1.disp_by_prescription_code[0].patient_responsibility"

