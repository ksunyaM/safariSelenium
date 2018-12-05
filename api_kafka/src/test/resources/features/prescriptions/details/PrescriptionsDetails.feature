Feature: System allows a doctor get details of prescriptions
  As a system,
  I allow get details of prescriptions,
  So that Doctor can see which specific drugs prescribed to a patient.

  @RealData
  Scenario Outline: System is displaying details of selected prescription for patient
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor selects draft/issued/expired prescription
    # 3. The doctor checks that information for prescription is correct
    Given Doctor selects patient John Smith
    When Doctor is going to prescription module and select <state> prescription
    Then Doctor checks that sees correct data for selected <state> prescription for patient

    Examples:
    | state   |
    | ISSUED  |
    | EXPIRED |
    | DRAFT   |

  @RealData
  Scenario: System returns code 404 for get details of does not exist prescription
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries select not existing prescription
    # 3. System returns 404 Not Found
    Given Doctor selects patient John Smith
    When Doctor tries get details of prescription with does not exist ID
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System returns code 404 for get details of deleted draft prescription
    # Before:
    # - Patient John Smith is existing in system and has deleted draft prescription
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries select deleted draft prescription
    # 3. System returns 404 Not Found
    Given Doctor selects patient John Smith
    When Doctor tries get details of deleted draft prescriptions
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System returns code 404 for get details of all prescriptions
    # Before:
    # - Patient John Smith is existing in system and prescriptions
    # 1. The doctor is logged in system
    # 2. The doctor tries get all prescriptions
    # 3. System returns 404 Not Found
    Given Doctor is logged in system
    When Doctor tries get details of all prescriptions
    Then System informs doctor about error: 404 Not Found

  ######################################################################################################################
  # TODO - to discussions - currently should be get by patients service when get personal data for patient (using KAFKA)
  @Ignore
  Scenario: System is displaying all prescriptions for patient
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor going to prescriptions module
    # 3. The doctor checks that sees all prescription for patient
    Given Doctor selects patient John Smith
    When Doctor is going to prescription module to get prescription for John Smith
    Then Doctor checks that sees all prescriptions for selected patient

  @Ignore
  Scenario: System cannot get prescriptions for patient - system returns timeout for respond
    # TODO: error handling is moved to the next sprints
    # Before:
    # - Patient John Smith is existing in system with draft prescription
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries get information about selected prescriptions
    # 3. System informs the doctor about error 504 Gateway timeout
    Given Doctor selects patient John Smith
    When Doctor tries get information about selected prescription with timeout
    Then System informs doctor about error: 504 Gateway Timeout
  ######################################################################################################################

  ######################################################################################################################
  # TODO for future (decided by Product Owner)
  # get all prescriptions issued by doctor
  # get all prescriptions from system (for Administrator)
  # get prescriptions by state (e.g. only drafts, only issued, only expired - for patient)
