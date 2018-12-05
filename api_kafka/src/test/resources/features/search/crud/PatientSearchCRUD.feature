Feature: System allows to search patients by name
  As a Doctor
  I want to be able to search patients by name
  So that I can check information about patients

  @RealData
  Scenario Outline: A Doctor wants to search patient by name
    Given Doctor login to system to search patient
    When Doctor types phrase <phrase> and click Search button
    Then Doctor sees patient or patients contains searched phrase <phrase>

    Examples:
    | phrase        |
    | jan           |
    | nowak         |
    | Nowak-Jankiel |

  @RealData
  Scenario: A Doctor wants to search patient by name with different letter case
    Given Doctor login to system to search patient
    When Doctor verified search with phrases: jan, JAN, jAn, jAN, Jan
    Then Doctor sees the same results for each phrase

  @RealData
  Scenario: A Doctor wants to check, if typed not existing user, the results are empty
    Given Doctor login to system to search patient
    When Doctor types phrase userNotExisting and click Search button
    Then Doctor sees empty results

  @RealData
  Scenario: System returns Bad request, if search query has not completed all parameters
    When Developer does not put all parameters in search
    Then System returns error: 400 Bad Request

  @RealData
  Scenario: System not searching in the deleted patient
    Given Doctor login to system to search patient
    When Doctor types phrase deletedUser and click Search button
    Then System does not return deleted user contains deletedUser