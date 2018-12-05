Feature: Doctor can get details about visit
  As a Doctor
  I want to get all visits from the list
  So that I can manage my visits

  @StubForPostVisit
  Scenario Outline: Doctor wants to check information about <visit>
    Given Doctor log in to his account
    When Doctor gets information about visit's details
    Then Doctor checks and confirm the <visit> status

    Examples:
      | visit     |
      | scheduled |
      | performed |
      | cancelled |

  @Ignore
  Scenario Outline: Doctor can check the visits details
    Given Doctor opened his calendar with visits details
    When Doctor opens referrals
    Then Doctor checks that all referrals exists <type>

    Examples:
      | type                |
      | all                 |
      | without referral    |
      | with a few referral |
