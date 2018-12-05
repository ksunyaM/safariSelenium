Feature: Doctor can cancel visits

  Scenario: Doctor cannot delete any visit
    Given Doctor logged to his application module page
    When Doctor selects visit to delete
    Then Doctor checks that the visit is not deleted
#  and see error <ERROR_MESSAGE>
#    Examples:
#      | ERROR_MESSAGE |
#      | 404           |
#      | 405           |
# TODO cancellation of visit move to another place
#  Scenario Outline: Doctor can cancel scheduled <visit>
#    Given Doctor logged to his application module page
#    When Doctor selects patient <visit> visit to cancel
#    Then Doctor cancelled visit succesfully
#
#
#    Examples:
#      | visit           |
#      | John Smith      |
#      | Jonathan Walker |
#      | Johnie Bean     |


