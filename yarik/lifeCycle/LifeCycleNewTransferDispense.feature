Feature: Lifecycle flow for newly transferred prescription with different statuses of dispense


  @lifeCycle
  Scenario Outline:  New prescription transferred and dispense is made to go from reviewed to ready.
    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE     |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE|
    When update the "Prescriber" response Json with Data
    And Upload all Mock to server
    Given an event "newPrescriptionDispense" for topic "rxpdata_prescription_external_transferin"
    And the event "newPrescriptionDispense" has json "<json>"
    And updating the event "newPrescriptionDispense" with the dispenseType data "<dispenseType>"
    And update the event with the PrescriberDetails for Event "newPrescriptionDispense"
    And new Rx Number for new Prescription is generated
    When the user produces the event
    And wait till log value "transferRxEventRequested: Transfer Rx Event - Rx & Dispense Persisted , No Notification published"
    And Load the Persist Data of "PrescriptionByExternalRxNumber" into the Event "prescriptionData" with parameters "newPrescriptionDispense"
    And load the snapshot for the "prescriptionData.prescription_code" given Prescription
    And an event "printCompleted" for topic "rxassembly_leaflet_print"
    And save the DispenseCode "dispCode,REVIEWED" to be changed in DataStorage  "rxPDataSnapshot0"
    And the event has data as
      |dispenseCode|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "PRINTED == rxPDataSnapshot1.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot1" for "PRINTED"
    And an event "assemblyCompleted" for topic "rxassembly_complete"
    And the event has data as
      |dispenseCode|dispCode |
      |rxNumber    |rxPDataSnapshot0.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "FILLED == rxPDataSnapshot2.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot2" for "FILLED"
    And an event "finalValidation" for topic "rxfinalvalidation_complete"
    And the event has data as
      |dispenseCode|dispCode|
      |rxNumber    |rxPDataSnapshot1.disp_by_prescription_code[0].rx_number|
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "READY == rxPDataSnapshot3.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot3" for "READY"
    When an event "OrderLineDelivered" for topic "orders_orderline_deliverynotify"
    And the event "OrderLineDelivered" has data as
      |dispenseCode                                                                                   |rxPDataSnapshot3.disp_by_prescription_code[0].dispense_code|
      |soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber|random::5|
      |soldDateTime                                                                                   |timestamp::                     |
    When the user produces the event
    And wait for "5" seconds
    And load the snapshot for the "rxPDataSnapshot0.disp_by_prescription_code[0].prescription_code" given Prescription
    Then verify that "DELIVERED == rxPDataSnapshot4.disp_by_prescription_code[0].dispense_status"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot4" for "DELIVERED"
    Examples:
      |dispenseType                | json                                         |
      |normal                      | transferNewPrescriptionDispense_nonM.json    |
      |inadequateRemainingQuantity | transferNewPrescriptionDispense_nonM.json    |
      |unlimitedRefill             | transferNewPrescriptionDispense_nonM.json    |
      |normal                      | transferNewPrescriptionDispense.json         |
      |inadequateRemainingQuantity | transferNewPrescriptionDispense.json         |
      |unlimitedRefill             | transferNewPrescriptionDispense.json         |