@Ignore
Feature: System allows the doctor to set his working hours
  As a system,
  I allow the doctor to set his working hours,
  so that patient will be able to see the doctor's availability

  Background: Doctor is created


  Scenario: A new doctor creates the new account and fill his working hours
    Given Doctor creates a new account
    When Doctor can fill his working hours scope
    Then Doctor sees visits list within his scope of working hours

  Scenario: Doctor with account can open patient's form and see details
    Given Doctor logged to his account
    When Doctor sees patients list in proper order within his scope of working hours
    Then Doctor can open the patient's form and see details filled by patient


  Scenario Outline: Doctor wants add <name> of patients to his visits calendar
    Given Doctor logged in to his account
    When Doctor fill the patient data to his visit's calendar
    Then Patients appear on the doctor's scope of time

    Examples:
      | name |
      | Kowalski   |
      | Kierunkowskaz   |


