Feature: System allows a doctor get details of drugs
  As a system,
  I allow get details of drugs,
  So that Doctor can see which specific drugs and the dosage can he apply for te patient

  Scenario: System is displaying particular drug for prescription
    Given Doctor is going to the prescription's module
    When Doctor selects the drug from the list
    Then Drug is selected properly

  Scenario: System is displaying details of selected drug for patient
    Given Doctor is going to prescription module
    When Doctor selects the drug from the list
    Then Doctor checks that sees correct data for selected drug for patient

  Scenario: System returns empty drugs list when there's no particular drug
    Given Doctor is going to prescription module
    When Doctor select the drug that not have been existed on the list
    Then Doctor checks the empty list

  Scenario: System not allowed for get details of not exists drug
    Given Doctor selects drug
    When Doctor tries get details for non-existent drug
    Then System display graceful information with not found error
#    System return code 404 with <ERROR_MESSAGE>

  Scenario: System cannot get drug for prescriptions because the system is not responding
#    timeout case
    Given Doctor select drug
    When Doctor tries get details from drug module to get proper drug
    Then System display graceful information with timeout error
#    System return code 504 with <ERROR_MESSAGE>

