Feature: Doctor can create new patients
  As a Doctor
  I want to add new patient to the list
  So that I can stored details information about patients

  @RealData
  Scenario: New patient is created properly
    Given Doctor opens Patients Management View
    When Doctor creates the new patient
    Then Doctor checks the patient is created

#  @Ignore("Validations weren't implemented");
#  @RealData
#  Scenario: New patient cannot be created - doctor sees correct error information
#    Given Doctor opens Patients Management View
#    When Doctor creates incomplete the new patient
#    Then System informs doctor about error: 400 Bad Request
#
#  @Ignore("Validations weren't implemented");
#  @RealData
#  Scenario: New patient cannot be created - patient with the same email already exists
#    Given Doctor opens Patients Management View
#    When Doctor creates the new patient with the same email
#    Then System informs doctor about error: 400 Bad Request
#
#  @RealData
#  Scenario: New patient is created when the patient with the same email is deleted
#    Given Doctor opens Patients Management View
#    When Doctor creates the new patient with the same email what was used for deleted patient
#    Then Doctor checks the patient is created
