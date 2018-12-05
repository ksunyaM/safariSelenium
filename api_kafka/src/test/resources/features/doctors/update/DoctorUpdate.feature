Feature: System allows an Administrator update information about doctor
  As Administrator
  I want to update information about doctor
  So that I have up-to-date information about doctor

  @RealData
  Scenario: The Administrator wants to update all information about doctor in one request
    Given The Administrator opens Doctors Management View
    When The Administrator selects Doctor and update all data
    Then The Administrator sees all updated details of Doctor

  @RealData
  Scenario Outline: The Administrator wants to update only selected information
    Given The Administrator opens Doctors Management View
    When The Administrator selects Doctor and update only <fieldName> with <value>
    Then The Administrator sees updated <fieldName> with new <value>
    Examples:
      | fieldName | value                 |
      | name      | Upd_Doctors_Name      |
      | surname   | Upd_Doctors_Surname   |
      | email     | Upd_testTest@mail.com |

  @RealData
  Scenario: System not allowed for update does not exist doctor
    Given The Administrator opens Doctors Management View
    When The Administrator tries edit doctor with not existing ID
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: The system does not allow updating the information of a doctor who has already been deleted
    Given The Administrator selects and deletes the Doctor
    When The administrator is trying to update the information of the doctor who is deleted
    Then System informs doctor about error: 404 Not Found

    # TODO: 400 bad request when update email and another doctor with the same email is existing in system