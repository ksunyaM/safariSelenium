Feature: System allows a doctor delete only draft prescriptions
  As a system,
  I allow deleting prescription,
  So that prescription draft can't be issued.

  @RealData
  Scenario Outline: Selected draft prescription for patient is deleted properly, but issued and expired prescriptions cannot be deleted
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor select draft, issued or expired prescription and click Removed icon
    # 3. The doctor checks that prescription is removed for drafts, and not removed for issued and expired
    Given Doctor selects patient John Smith
    When Doctor selects <state> prescription and click Remove button
    Then System informs doctor about error: <statusCode> <error>

    Examples:
    | state   | statusCode | error      |
    | DRAFT   | 204        | No Content |
    | ISSUED  | 403        | Forbidden  |
    | EXPIRED | 403        | Forbidden  |

  @RealData
  Scenario: System not allowed for delete does not exist prescription
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries remove not existing prescription
    # 3. System returns 204 No Content
    Given Doctor selects patient John Smith
    When Doctor tries remove of prescription with not existing ID
    Then System informs doctor about error: 204 No Content
    # TODO: after new requirement for CRUD, should be 204 No Content, currently is 404

  @RealData
  Scenario: System not allowed for delete all prescription and returns 404
    # Before:
    # - Patient John Smith is existing in system and has draft, issued and expired prescriptions
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries remove all prescriptions
    # 3. System returns 404 Not Found
    Given Doctor selects patient John Smith
    When Doctor tries remove all prescriptions
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System not allowed for delete deleted prescription and returns 204
    # Before:
    # - Patient John Smith is existing in system and has deleted prescription
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries remove deleted prescription
    # 3. System returns 204 No Content
    Given Doctor selects patient John Smith
    When Doctor tries remove deleted prescription
    Then System informs doctor about error: 204 No Content
    # TODO: after new requirement for CRUD, should be 204 No Content, currently is 403

  ######################################################################################################################
  @Ignore
  Scenario: Draft prescription cannot be deleted - system returns timeout for respond
    # TODO: error handling is moved to the next sprints
    # Before:
    # - Patient John Smith is existing in system with draft prescription
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor tries delete draft prescription
    # 3. System informs the doctor about error 504 Gateway Timeout
    Given Doctor selects patient John Smith
    When Doctor tries delete selected prescription with timeout
    Then System informs doctor about error: 504 Gateway Timeout
  ######################################################################################################################
