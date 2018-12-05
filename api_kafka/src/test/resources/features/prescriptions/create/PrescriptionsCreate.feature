Feature: System allows a doctor create new prescriptions
  As a system,
  I allow creating a new prescription,
  So that Doctor can prescribe specific drugs to a patient.

  @RealData
  Scenario: New prescription is created as draft
    # Before:
    # - Patient John Smith is existing in system
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor fill data in the prescription and click Save as Draft button
    # 3. The doctor checks that prescription is saved correctly as draft
    Given Doctor selects patient John Smith
    When Doctor creates the new prescription for patient as DRAFT
    Then Doctor checks that data of prescription are saved correctly as DRAFT

  @RealData
  Scenario: New prescription is created as issued
    # Before:
    # - Patient John Smith is existing in system
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor fill data in the prescription and click Save as Issued button
    # 3. The doctor checks that prescription is saved correctly as issued
    Given Doctor selects patient John Smith
    When Doctor creates the new prescription for patient as ISSUED
    Then Doctor checks that data of prescription are saved correctly as ISSUED

  @RealData
  Scenario: New issued prescription cannot be created - doctor sees correct error information
    # Before:
    # - Patient John Smith is existing in system
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor fill data without disease and click Save an Issued button
    # 3. System informs the doctor about error 400 Bad Request
    Given Doctor selects patient John Smith
    When Doctor creates incomplete the new prescription for patient
    Then System informs doctor about error: 400 Bad Request
    # TODO: 400 is not implemented yet in CRUD

  @Ignore
  Scenario: New prescription cannot be created - system returns timeout for response
    # TODO: error handling is moved to the next sprints
      # Before:
    # - Patient John Smith is existing in system
    # 1. The doctor is logged in system and select details about John Smith
    # 2. The doctor fill data in the prescription and click Save as Issued button
    # 3. System informs the doctor about error 504 Gateway timeout
    Given Doctor selects patient John Smith
    When Doctor creates the new prescription for patient with timeout
    Then System informs doctor about error: 504 Gateway Timeout
