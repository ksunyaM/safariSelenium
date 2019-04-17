@featureid=RXRPRP-  @capabilityid=RXRPRP-  @jiraid=RXRPD-  @domain=RxProcessing @api

Feature: As an RXPData kit
  I want to be able to return Dispense
  Using Prescription and Dispense code provided

  @acceptance @auto @smoke @jiraid=RXRPD-35905
  Scenario: As an RXPData kit I should be able to return valid dispense object for getDispense api call
  Given an existing prescription in RXPData database with "allFields"
  When user sends API call "readDispense" to "readDispense" with data
  |~param1|rxPDataSnapshot0.prescription_by_code[0].prescription_code|
  |~param2|rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code|
  And the response status code is "200"
  Then verify that "rxPDataSnapshot0.prescription_by_code[0].prescription_code == C.prescriptionCode"
  Then verify that "rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code == C.code"
  Then verify "dispenseAPI" returned between "readDispense" and "rxPDataSnapshot0.disp_by_prescription_code[0]"



  @acceptance @auto @smoke @jiraid=RXRPD-34254
  Scenario Outline: As an RXPData kit I should be able to return valid error response for getDispense api call with missing/invalid values
    Given user sends API call to "readDispense" with data
      |~param1|<prescription_code>|
      |~param2|<dispense_code>    |
    And the response status code is "<statusCode>"
#    And the response body contains "<response>"
    And verify that "C.code == <ErrorCode>"
    And verify that "C.message == <ErrorMessage>"
  Examples:
  |prescription_code                   |dispense_code                       |statusCode|ErrorMessage                            |ErrorCode|
  |dd65caad-cec3-4f54-84bb-02688ea81964|123132                              |400       |Dispense Code should be a valid UUID    |RXD-VAF-1100-0001|
  |1232134123213141233                 |101146bb-ca2b-427b-841f-1b7ac9c00922|400       |Prescription Code should be a valid UUID|RXD-VAF-1000-0001|
  |dd65caad-cec3-4f54-84bb-02688ea81964|101146bb-ca2b-427b-841f-1b7ac9c00922|400       |Prescription/Dispense resource not found|RXD-RNF-1100-0000|


