Feature: Doctor can update details of patient
  As a Doctor
  I want to edit details fo patient
  So that I can have up-to-date information of patients

  @RealData
  Scenario: Existing patient is updated correctly
    Given Doctor opens Patients Management View
    When Doctor selects Patient, changes data and click Save button
    Then Doctor checks Patient is updated

  @RealData
  Scenario: Existing patient cannot be updated - system returns 400 Bad Request
    Given Doctor opens Patients Management View
    When Doctor selects Patient, changes incomplete data and click Save button
    Then System informs doctor about error: 400 Bad Request

  @RealData
  Scenario: Existing patient cannot be updated - patient with the same email already exists
    Given Doctor opens Patients Management View
    When Doctor selects Patient, changes email for the existing email in db, and click Save button
    Then System informs doctor about error: 400 Bad Request

  @RealData
  Scenario: System returns 404 when doctor tries update does not existing patient
    Given Doctor opens Patients Management View
    When Doctor tries update not existing Patient
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System returns 404 when doctor tries update deleted patient
    Given Doctor opens Patients Management View
    When Doctor tries update deleted Patient
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System returns 404 when doctor tries update patient without ID in url
    Given Doctor opens Patients Management View
    When Doctor tries update Patient without ID in url
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario Outline: Existing data for patient is updated correctly
    Given Doctor opens Patients Management View
    When Doctor selects Patient, update <fieldName> using <value> and click Save button
    Then Doctor checks that <fieldName> is updated with new <value>

    Examples:
    | fieldName   | value          |
    | name        | TESTUPDATE     |
    | email       | update@test.pl |
    | gender      | F              |
    | dateOfBirth | 09.09.2000     |
    | country     | USA            |
    | phone       | 999-888-777    |
    | note        | Updated note   |
    | avatar      | zaba           |
