Feature: Administrator can create a new user with a role - Doctor, hereinafter referred to as "Doctor"
  As Administrator
  I want to create a new Doctor
  To increase the list of Doctors in the system

  @RealData
  Scenario Outline: A new Doctor is created properly
    Given The Administrator logs into the Doctor module and checks existing doctors
    When The Administrator creates a new Doctor Jan Hippocrates
    Then The Administrator checks that the Doctor <tagName> <value> is created
    Examples:
      | tagName | value             |
      | name    | Jan               |
      | surname | Hippocrates       |
      | email   | jkowalski@e2e.com |

  @RealData
  Scenario: A new Doctor is created with the correct list of specializations
    When The administrator checks that the created Doctor has a list of specializations
    Then The specialization list contains the correct specializations Gynecologist and orthopedist

  @RealData
  Scenario: Verify the Location header in the response to tell the client the URI of the newly created resource
    When The Administrator creates a new Doctor Jan Hippocrates
    Then Response Header for Location Should contains the created ID

  @RealData
  Scenario: A new Doctor cannot be created because api is not found
    When The Administrator is creating a new Doctor with wrong Rest API address
    Then The Administrator checks that the Doctor is not created, due to an error: 404 - Not Found

#  @RealData
#  Scenario: A new Doctor cannot be created because api is not found (Internal Server Error)
#    When The Administrator is creating a new Doctor with internal server error
#    Then The Administrator checks that the Doctor is not created, due to an error: Internal Server Error

#  @Ignore
#  Scenario: A new Doctor cannot be created, because he already exists
#    Given The Administrator logs into the Doctor module and checks existing doctors
#    When The Administrator creates a Doctor that already exists in the system
#    Then The Administrator checks that the Doctor is not created, due to an error: User already exists.(400 ---- the same email !!!)
#
#  @RealDataRR
#  Scenario: Failed to create a new Doctor due to system failure
#    Given The Administrator logs into the Doctor module
#    When The Administrator creates a new Doctor
#    Then The Administrator checks that the Doctor is not created, due to an error: Failed. User not created.
#
# @RealData
#  Scenario: Check for timeout functionality. Timeout values should be configurable. Check application behavior after operation timeout.
#    When The Administrator creates a new Doctor Jan Hippocrates
#    Then Timeout values should be less than 150