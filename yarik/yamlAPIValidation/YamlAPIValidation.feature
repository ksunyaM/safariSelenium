Feature: Verify if all the RxpData API response and Yaml are in sync

  @acceptance @auto
  Scenario: API for getting Dispense for params PrescriptionCode and DispenseCode
    Given an existing prescription in RXPData database with "allFields"
    When user sends API call "getDispense" to "readDispense" with data
      | ~param1 | rxPDataSnapshot0.prescription_by_code[0].prescription_code  |
      | ~param2 | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
    And verify by yaml - "getDispense"


  @acceptance @auto
  Scenario: RXPData MS returns a list of dispenses for a prescriptionCode
    Given an existing prescription in RXPData database with "TwoDispenses"
    When user sends API call "getDispenseListbyPrescriptionCode" to "getDispenseListbyPrescriptionCode" with data
      | ~param1 | rxPDataSnapshot0.prescription_by_code[0].prescription_code |
    And verify by yaml - "getDispenseListbyPrescriptionCode"


  @acceptance @auto
  Scenario: RXPData MS returns prescription for a prescriptionCode
    Given an existing prescription in RXPData database with "TwoDispenses"
    When user sends API call "getPrescription" to "readPrescription" with data
      | ~param1 | rxPDataSnapshot0.prescription_by_code[0].prescription_code |
    And verify by yaml - "readPrescription"


  @acceptance @auto
  Scenario: RXPData returns a list of dispenses with prescription info on successfully providing dispense code
    Given an existing prescription in RXPData database with "allFields"
    When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
      | rxNumberFilter        | null::                                                      |
      | patientDispenseFilter | null::                                                      |
      | dispenseCode          | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
    And verify by yaml - "searchPrescriptionList"

  @acceptance @auto
  Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Location Number, Location Type and Rx Number
    Given an existing prescription in RXPData database with "TwoDispenses"
    When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
      | patientDispenseFilter                  | null::                                                        |
      | dispenseCode                           | null::                                                        |
      | rxNumberFilter.location.locationNumber | rxPDataSnapshot0.prescription_by_rx_number[0].location_number |
      | rxNumberFilter.location.locationType   | rxPDataSnapshot0.prescription_by_rx_number[0].location_type   |
      | rxNumberFilter.rxNumber                | rxPDataSnapshot0.prescription_by_rx_number[0].rx_number       |
    And verify by yaml - "searchPrescriptionList"


  @acceptance @auto
  Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Patient code and status
    Given an existing prescription in RXPData database with "TwoPrescription"
    When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
      | rxNumberFilter                       | null::                                                    |
      | dispenseCode                         | null::                                                    |
      | patientDispenseFilter.patientCode    | rxPDataSnapshot0.prescription_by_pat_code[0].patient_code |
      | patientDispenseFilter.dispenseStatus | READY                                                     |
    And verify by yaml - "searchPrescriptionList"


  @acceptance @auto
  Scenario: RXPData returns a list of dispenses with prescription info on successfully providing Patient code
    Given an existing prescription in RXPData database with "TwoPrescription"
    When user sends API call "searchPrescriptionList" to "searchPrescriptionList" with data
      | rxNumberFilter                       | null::                                                    |
      | dispenseCode                         | null::                                                    |
      | patientDispenseFilter.patientCode    | rxPDataSnapshot0.prescription_by_pat_code[0].patient_code |
      | patientDispenseFilter.dispenseStatus | null::                                                    |
    And verify by yaml - "searchPrescriptionList"


