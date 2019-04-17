Feature: Lifecycle flow for Prescription with different Status of Dispense and delete the dispense


@lifeCycle @jiraid=RXRPD-50145 
Scenario Outline:  RXPData kit Transfer Rx and Dispense Status change and Delete and close Rx Integrated
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
  And load the snapshot for the "rxPDataSnapshot1.disp_by_prescription_code[0].prescription_code" given Prescription
  Then verify that "READY == rxPDataSnapshot3.disp_by_prescription_code[0].dispense_status"
  And verify the Dispense codes are updated for other tables for "rxPDataSnapshot3" for "READY"
  And an event "dispenseDelete" for topic "rxpdata_dispense_deleterequest"
  And the event has data as
    | dispenseCode |rxPDataSnapshot1.disp_by_prescription_code[0].dispense_code |
  When the user produces the event
  Then wait for "5" seconds
  And new fields are Added Saved and Updated in RxPData Data Base "rxPDataSnapshot4"
  #And verify the Dispense codes are updated for other tables for "rxPDataSnapshot4" for "DELETED"
  And an event for topic "rxpdata_prescription_closerequest"
  And the event has data as
    | prescriptionCode | ~prescription_code |
  When the user produces the event
  Then wait for "5" seconds
  And get RxpData snapshot
  And verify RxStatus "rxPDataSnapshot5.prescription_by_code[0]"
  And an event "refillRequest" for topic "rxpdata_create_refill_dispense"
  And the event "refillRequest" has data as
    |prescriptionCode|rxPDataSnapshot5.prescription_by_code[0].prescription_code|
    |lastDispenseCode|rxPDataSnapshot5.disp_by_prescription_code[0].dispense_code|
  When the user produces the event
  Then wait for "5" seconds
  Then verify if new record has been created

Examples:
  |dispenseType                | json                                         |
  |normal                      | transferNewPrescriptionDispense_nonM.json    |
  |inadequateRemainingQuantity | transferNewPrescriptionDispense_nonM.json    |
  |unlimitedRefill             | transferNewPrescriptionDispense_nonM.json    |
  |normal                      | transferNewPrescriptionDispense.json         |
  |inadequateRemainingQuantity | transferNewPrescriptionDispense.json         |
  |unlimitedRefill             | transferNewPrescriptionDispense.json         |