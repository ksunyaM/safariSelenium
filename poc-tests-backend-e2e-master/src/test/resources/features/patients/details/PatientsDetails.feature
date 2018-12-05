Feature: Doctor can get details about patients
  As a Doctor
  I want to get all patients or selected patient from the list
  So that I can verify my patients and check their details

  @RealData
  Scenario: Doctor get details of patient, who has not prescription yet
    Given Doctor opens Patients Management View
    When Doctor selects patient without prescriptions
    Then Doctor checks that patient exists and has not prescriptions

  @RealData
  Scenario: Doctor get details of patient, who has not prescription yet
    Given Doctor opens Patients Management View
    When Doctor selects patient with prescriptions
    Then Doctor checks that patient exists and has prescriptions

  @RealData
  Scenario: System returns 404 when doctor tries get details of does not exist patient
    Given Doctor opens Patients Management View
    When Doctor tries get details of does not exist patient
    Then System informs doctor about error: 404 Not Found

  @RealData
  Scenario: System returns 404 when doctor tries get details of deleted patient
    Given Doctor opens Patients Management View
    When Doctor tries get details of deleted patient
    Then System informs doctor about error: 404 Not Found
