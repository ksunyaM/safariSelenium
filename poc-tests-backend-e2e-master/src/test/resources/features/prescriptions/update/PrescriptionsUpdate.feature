Feature: System allows a doctor update only draft prescriptions
  As a system,
  I allow editing only a draft prescription,
  So that Doctor can add new drugs and diseases to the prescription.

  @RealData
  Scenario: Selected draft prescription for patient is updated properly with all data
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor select draft prescription, edit data and click Save as Draft button
    # 3. The doctor checks that prescription is saved correctly with a edited data
    Given Doctor selects patient John Smith
    When Doctor selects draft prescription and updates data from UI
    Then Doctor checks that prescription is updated

  @RealData
  Scenario Outline: Selected draft prescription for patient is updated properly with only selected data
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor select draft prescription, edit data and click Save as Draft button
    # 3. The doctor checks that prescription is saved correctly with a selected edited field
    Given Doctor selects patient John Smith
    When Doctor selects draft prescription and updates selected data <fieldName> with <value>
    Then Doctor checks that <fieldName> in prescription is updated with correct <value>

    Examples:
      | fieldName     | value       |
      | diseaseId     | DI:1        |
      | validityDate  | 1548979199  |
      | note          | update note |

  @RealData
  Scenario: Selected draft prescription for patient is updated to issued state
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor select draft prescription and click Save as Issued button
    # 3. The doctor checks that prescription is saved correctly with ISSUED state
    Given Doctor selects patient John Smith
    When Doctor selects draft prescription and update state to ISSUED
    Then Doctor checks that state in prescription is updated with correct ISSUED

  @RealData
  Scenario Outline: System not allowed to update issued and expired prescriptions
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor select ISSUED/EXPIRED prescription, edit data and click Save as Issued button
    # 3. System returns 405 Not Allowed
    Given Doctor selects patient John Smith
    When Doctor tries edit prescription with <state> state
    Then System informs doctor about error: <statusCode> <error>

    Examples:
      | state   | statusCode  | error     |
      | ISSUED  | 403         | Forbidden |
      | EXPIRED | 403         | Forbidden |

  @RealData
  Scenario: System not allowed for update does not exist prescription
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries edit non-existing prescription
    # 3. System returns 404 Not found
    Given Doctor selects patient John Smith
    When Doctor tries edit prescriptions with does not exist ID
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: Incomplete Draft prescription cannot be updated to Issued - doctor sees correct error information
    # Before:
    # - Patient John Smith is existing in system and has draft prescription
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor update data without disease and click Save an Issued button
    # 3. System informs the doctor about error 400 Bad Request
    # TODO: (TO CHANGE AFTER IMPLEMENT ERROR HANDLING ??)
    Given Doctor selects patient John Smith
    When Doctor tries edit incomplete the existing draft prescription for patient
    Then System informs doctor about error: 400 Bad Request

  @RealData
  Scenario: System not allowed to update deleted draft prescriptions
    # Before:
    # - Patient John Smith is existing in system and has deleted draft
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries update deleted draft prescription
    # 3. System returns 404 Not Found
    Given Doctor selects patient John Smith
    When Doctor tries edit deleted draft prescription
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System not allowed for update prescription without ID
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries edit prescription without ID
    # 3. System returns 404 Not found
    Given Doctor selects patient John Smith
    When Doctor tries edit prescriptions without ID
    Then System informs doctor about error: 404 Not Found

  ######################################################################################################################
  @Ignore
  Scenario: Draft prescription cannot be updated - system returns timeout for respond
    # TODO: error handling is moved to the next sprints
    # Before:
    # - Patient John Smith is existing in system with draft prescription
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor update data in the prescription and click Save as Issued button
    # 3. System informs the doctor about error 504 Gateway timeout
    Given Doctor selects patient John Smith
    When Doctor update the draft prescription for patient with timeout
    Then System informs doctor about error: 504 Gateway Timeout