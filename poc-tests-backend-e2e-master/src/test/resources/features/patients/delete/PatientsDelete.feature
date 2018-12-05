Feature: Doctor can delete patient
  As a Doctor
  I want to delete patient
  So that I can decrease patients list

  @RealData
  Scenario: Selected patient is deleted properly
    Given Doctor opens Patients Management View
    When Doctor selects undeleted Patient and click Delete button
    Then Doctor checks Patient is deleted and cannot get his details information

  @RealData
  Scenario: Doctor cannot delete deleted patient and system returns code 204
    Given Doctor opens Patients Management View
    When Doctor tries delete deleted Patient
    Then System informs doctor about error: 204 No Content

  @RealData
  Scenario: Doctor cannot delete not existing patient - system returns code 204
    Given Doctor opens Patients Management View
    When Doctor tries delete not existing Patient
    Then System informs doctor about error: 204 No Content

  @RealData
  Scenario: System returns code 404 when doctor or admin tries remove all patients
    Given Doctor opens Patients Management View
    When Doctor tries delete all patients
    Then System informs doctor about error: 404 Not Found
