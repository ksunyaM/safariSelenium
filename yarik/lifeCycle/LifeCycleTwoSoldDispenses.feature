Feature: Lifecycle flow for Prescription with different Status of Dispense  as Sold


  @lifeCycle @jiraid=RXRPD-50145
  Scenario:  RXPData kit Transfer Rx and Dispense Status change and Delete and close Rx Integrated
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And updating the event "newPrescriptionDispense" with the dispenseType data "normal"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    When an event "billingCompleted" for topic "finance_billing_complete"
    And the event "billingCompleted" has data as
      |dispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
      |rxNumber    |rxPDataSnapshot0.disp_by_prescription_code[0].rx_number    |
    When the user produces the event
    And wait for "5" seconds
    And get RxpData snapshot
    And verify that "billingCompleted.patientResponsibility.['com.wba.finance.billing.complete.event.avro.Currency'].decimalValue == rxPDataSnapshot1.disp_by_prescription_code[0].patient_responsibility"
    And an event "printCompleted" for topic "rxassembly_leaflet_print"
    And the event has data as
      |dispenseCode|rxPDataSnapshot1.disp_by_prescription_code[0].dispense_code|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "PRINTED == rxPDataSnapshot2.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot2" for "PRINTED"
    And an event "assemblyCompleted" for topic "rxassembly_complete"
    And the event has data as
      |dispenseCode|rxPDataSnapshot1.disp_by_prescription_code[0].dispense_code |
      |rxNumber    |rxPDataSnapshot2.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot2.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "FILLED == rxPDataSnapshot3.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot3" for "FILLED"
    And an event "finalValidation" for topic "rxfinalvalidation_complete"
    And the event has data as
      |dispenseCode|rxPDataSnapshot1.disp_by_prescription_code[0].dispense_code|
      |rxNumber    |rxPDataSnapshot3.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot3.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "READY == rxPDataSnapshot4.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot4" for "READY"
    Given an event "OrderLineDelivered" for topic "orders_orderline_deliverynotify"
    And the event "OrderLineDelivered" has data as
      |dispenseCode|rxPDataSnapshot4.disp_by_prescription_code[0].dispense_code|
      |soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber|random::5|
      |soldDateTime            |timestamp::                     |
    When the user produces the event
    And wait for "5" seconds
    Then get RxpData snapshot
    And verify delivered dispense for "Cassandra" for "rxPDataSnapshot5"
    And the system produces a new event "RxPDispenseDelivered" to topic "rxpdata_dispense_deliverresponse" with data
      |dispenseCode|OrderLineDelivered.dispenseCode|
    And verify delivered dispense for "NotifyEvent" for "rxPDataSnapshot5"
    And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
    And the event "refillRequest" has data as
      |prescriptionCode|rxPDataSnapshot5.prescription_by_code[0].prescription_code|
      |lastDispenseCode|rxPDataSnapshot5.disp_by_prescription_code[0].dispense_code|
    And update the event "refillRequest" with the "same" location
    When the user produces the event
    Then wait for "5" seconds
    And wait till log value "createDispenseEventRequested: Create Dispense Event - Dispense Persisted , proceeding to publish notification"
    Then verify new "normal" refill has been created with data
      |snapshot|rxPDataSnapshot6|
      |event   |refillRequest   |
      |dispLoc | same      |
      |prescriber  | Prescriber |
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot6" for "REVIEWED"
    When an event "billingCompleted" for topic "finance_billing_complete"
    And the event "billingCompleted" has data as
      |dispenseCode|refillDispenseCode|
      |rxNumber    |rxNumberRefill |
    When the user produces the event
    And wait for "5" seconds
    And get RxpData snapshot
    And verify that "billingCompleted.patientResponsibility.['com.wba.finance.billing.complete.event.avro.Currency'].decimalValue == rxPDataSnapshot7.disp_by_prescription_code[0].patient_responsibility"
    And an event "printCompleted" for topic "rxassembly_leaflet_print"
    And the event has data as
      |dispenseCode|refillDispenseCode|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot7.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify the status is correctly updated in "rxPDataSnapshot8,PRINTED"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot8" for "PRINTED,dispCode"
    And an event "assemblyCompleted" for topic "rxassembly_complete"
    And the event has data as
      |dispenseCode|refillDispenseCode |
      |rxNumber    |rxNumberRefill|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot8.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify the status is correctly updated in "rxPDataSnapshot9,FILLED"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot9" for "FILLED,dispCode"
    And an event "finalValidation" for topic "rxfinalvalidation_complete"
    And the event has data as
      |dispenseCode|refillDispenseCode|
      |rxNumber    |rxNumberRefill|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot9.disp_by_prescription_code[0].prescription_code" given Prescription
    And verify the status is correctly updated in "rxPDataSnapshot10,READY"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot10" for "READY,dispCode"
    Given an event "OrderLineDelivered" for topic "orders_orderline_deliverynotify"
    And the event "OrderLineDelivered" has data as
      |dispenseCode|refillDispenseCode|
      |soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber|random::5|
      |soldDateTime            |timestamp::                     |
    When the user produces the event
    And wait for "5" seconds
    Then get RxpData snapshot
    And verify delivered dispense for "Cassandra" for "rxPDataSnapshot11"
    And the system produces a new event "RxPDispenseDelivered1" to topic "rxpdata_dispense_deliverresponse" with data
      |dispenseCode|refillDispenseCode|
    And verify delivered dispense for "NotifyEvent" for "rxPDataSnapshot11,RxPDispenseDelivered1"
