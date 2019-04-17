Feature: Application login
@regression@smoke
Scenario: Home page default login
    Given User us is on NetbBanking landing page
    When User login into application with username "eeee" and password  "12345"
    Then Home page is populated
    And Cards are displayed
#@smoke
# Scenario: Home page default login
#    Given User us is on NetbBanking landing page
#    When User login into application with username "john" and password  "6789"
#    Then Home page is populated
#    And Cards are displayed
#
#@sanity@regression
#  Scenario: Home page default login
#    Given User us is on NetbBanking landing page
#    When User sign up with following details
#    |janny|abcd|444| email@com|
#    |ann|cred|3334| email2@com|
#    Then Home page is populated
#    And Cards are displayed

#  Scenario Outline: Home page default login
#    Given User us is on NetbBanking landing page
#    When User login in application with username <username> and password <password>
#    Then Home page is populated
#
#    Examples:
#      |username|password|
#      |user1|pass1|
#      |ser2|pass2|
#      |user3|pass3|