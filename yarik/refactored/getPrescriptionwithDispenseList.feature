@featureid=RXRPRP-  @capabilityid=RXRPRP- @jiraid=RXRPD- @domain=RxProcessing @api
Feature: As a RXPData kit
  I want to be able to provide dispenseList using
  Location Number, Location Type and Rx Number or by Patient code and status

  @acceptance @auto @smoke @jiraid=RXRPD-53474
Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Location Number, Location Type and Rx Number or by Patient code and status or dispense code
  Given an existing prescription in RXPData database with "allFields"
  When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
  |rxNumberFilter|null::|
  |patientDispenseFilter|null::|
  |dispenseCode         |rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
  Then the response status code is "200"
  Then verify "prescriptionAPI" returned between "searchPrescriptionList.prescriptions[0]" and "rxPDataSnapshot0.prescription_by_code[0]"
  Then verify "dispenseAPI" returned between "searchPrescriptionList.prescriptions[0].dispenses[0]" and "rxPDataSnapshot0.disp_by_prescription_code[0]"

  @acceptance @auto @smoke @jiraid=RXRPD-53475
Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Location Number, Location Type and Rx Number
   Given an existing prescription in RXPData database with "TwoDispenses"
   When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
   |patientDispenseFilter|null::|
   |dispenseCode         |null::|
   |rxNumberFilter.location.locationNumber|rxPDataSnapshot0.prescription_by_rx_number[0].location_number|
   |rxNumberFilter.location.locationType|rxPDataSnapshot0.prescription_by_rx_number[0].location_type|
   |rxNumberFilter.rxNumber|rxPDataSnapshot0.prescription_by_rx_number[0].rx_number|
   Then the response status code is "200"
   Then verify "prescriptionAPI" returned between "searchPrescriptionList.prescriptions[0]" and "rxPDataSnapshot0.prescription_by_code[0]"
   Then verify "dispenseListAPI" returned between "searchPrescriptionList.prescriptions[0]" and "rxPDataSnapshot0"

  @acceptance @auto @smoke @jiraid=RXRPD-53523
  Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Patient code and status
   Given an existing prescription in RXPData database with "TwoPrescription"
   When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
   |rxNumberFilter|null::|
   |dispenseCode         |null::|
   |patientDispenseFilter.patientCode|rxPDataSnapshot0.prescription_by_pat_code[0].patient_code|
   |patientDispenseFilter.dispenseStatus|READY|
  Then the response status code is "200"
  Then verify "prescriptionListAPI" returned between "searchPrescriptionList" and "READY"

  @acceptance @auto @smoke @jiraid=RXRPD-53528
  Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Patient code
    Given an existing prescription in RXPData database with "TwoPrescription"
    When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
      |rxNumberFilter|null::|
      |dispenseCode         |null::|
      |patientDispenseFilter.patientCode|rxPDataSnapshot0.prescription_by_pat_code[0].patient_code|
      |patientDispenseFilter.dispenseStatus|null::|
    Then the response status code is "200"
    Then verify "prescriptionListAPI" returned between "searchPrescriptionList" and "null::"



  @acceptance @auto @smoke  @jiraid=RXRPD-53529
Scenario: RXPData returns an error on incorrect dispense code
  When user sends API call to "searchPrescriptionList" with data
  |rxNumberFilter|null::|
  |patientDispenseFilter|null::|
  |dispenseCode         |2132133|
  Then the response status code is "400"
  And verify that "C.code == RXD-VAF-1100-0001"
  And verify that "C.message == Dispense Code should be a valid UUID"

  @acceptance @auto @smoke @jiraid=RXRPD-53548
Scenario: RXPData returns an error when given multiple filters together
  When user sends API call to "searchPrescriptionList" with data
    |rxNumberFilter|null::|
    |dispenseCode         |6ec772f7-a28f-4a60-b7e6-3f1f032e334f|
  Then the response status code is "400"
  And verify that "C.code == RXD-VAF-1000-0002"
  And verify that "C.message == Invalid Prescription Search Payload Encountered"

  @acceptance @auto @smoke @jiraid=RXRPD-53548
Scenario: RXPData returns an error when given multiple filters together
  When user sends API call to "searchPrescriptionList" with data
  |patientDispenseFilter|null::|
  |dispenseCode         |6ec772f7-a28f-4a60-b7e6-3f1f032e334f|
  Then the response status code is "400"
  And verify that "C.code == RXD-VAF-1000-0002"
  And verify that "C.message == Invalid Prescription Search Payload Encountered"

  @acceptance @auto @smoke @jiraid=RXRPD-53550
Scenario: RXPData returns an error when given multiple filters together
  When user sends API call to "searchPrescriptionList" with data
  |dispenseCode         |null::|
  Then the response status code is "400"
  And verify that "C.code == RXD-VAF-1000-0002"
  And verify that "C.message == Invalid Prescription Search Payload Encountered"

  @acceptance @auto @smoke @jiraid=RXRPD-53551
Scenario: RXPData returns an error when given multiple filters together
  When user sends API call to "searchPrescriptionList"
  Then the response status code is "400"
  And verify that "C.code == RXD-VAF-1000-0002"
  And verify that "C.message == Invalid Prescription Search Payload Encountered"