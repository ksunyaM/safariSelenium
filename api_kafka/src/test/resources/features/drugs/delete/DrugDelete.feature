Feature: Drug admin cannot delete any drug has been already saved in prescription
As a drug admin
  I want to delete not assigned drugs and not delete assigned drugs
  so that I cannot see not assigned deleted drug and I can see assigned drug


  Scenario: Drug admin deletes drug
    Given Drug admin logged to his application module page
    When Drug admin selects/create drug to delete
    Then The drug is delete from the system
    And Check drug exist in system

#  @Ignore
#  Scenario: Deleted drug cannot be deleted once again
#    Given Drug admin selects deleted drug
#    When Drug admin tries to delete deleted drug
#    Then System informs admin gracefully about error 404 Not Found

  Scenario: System cannot delete all drugs
    Given Drug admin logged to his application module page
    When Drug admin tries to delete all drugs
    Then System informs admin gracefully about error 405 method not allowed

#  @Ignore
#  Scenario: Drug cannot be deleted because the system doesn't respond
#    Given Drug admin logged to his application drug management module
#    When Drug admin tries to delete drug
#    Then System informs admin gracefully about error 504 Gateway Timeout








