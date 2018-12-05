Feature: The Administrator can get detailed information about doctors
  As Administrator
  I want to get all Doctors or selected Doctor from the list
  So that I can check the details information about their

  @RealData
  Scenario: The Administrator wants to displayed all doctors existing in the system
    Given The Administrator login to the system
    When The Administrator opens Doctors Management View
    Then The Administrator sees list of all doctors

  @RealData
  Scenario: The Administrator wants to displayed details of selected Doctor 1
    Given The Administrator opens Doctors Management Module
    When The Administrator selects Doctor 1
    Then The Administrator sees all details of Doctor 1

  @RealData
  Scenario: The Administrator cannot displayed information for non existing doctor
    Given The Administrator opens Doctors Management Module
    When The Administrator tries open not existing doctor
    Then System informs administrator about error: 404 Not Found