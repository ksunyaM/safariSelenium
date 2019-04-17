@featureid=RXRPRP- @capabilityid=RXRPRP- @jiraid=RXRPD- @domain=RxProcessing @api

Feature: As an RXPData kit
  I want to be able to return prescription
  Using Prescription code provided

  @acceptance @auto @smoke @jiraid=RXRPD-34259
  Scenario: RXPData MS returns prescription for a prescriptionCode
  Given an existing prescription in RXPData database with "TwoDispenses"
  When user sends API call "getPrescription" to "readPrescription" with data
    |~param1|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
  Then the response status code is "200"
  And get RxpData snapshot
  Then verify "prescriptionAPI" returned between "getPrescription" and "rxPDataSnapshot1.prescription_by_code[0]"
  Then verify "dispenseListAPI" returned between "getPrescription" and "rxPDataSnapshot1"


  @acceptance @auto @smoke @jiraid=RXRPD-34261
  Scenario Outline: RXPData MS returns an error response on invalid value/missing prescription for a prescriptionCode in prescription search
  Given user sends API call "getPrescription" to "readPrescription" with data
    |~param1|<prescriptionCode>|
  Then the response status code is "<code>"
    And verify that "C.code == <ErrorCode>"
    And verify that "C.message == <ErrorMessage>"

    Examples:
      |prescriptionCode                    |code|ErrorMessage                             |ErrorCode|
      |6ec772f7-a28f-4a60-b7e6-3f1f032e334f|400 |Prescription resource not found          |RXD-RNF-1000-0000|
      |3f1ff32e384f                        |400 |Prescription Code should be a valid UUID |RXD-VAF-1000-0001|