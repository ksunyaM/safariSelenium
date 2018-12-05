Feature: System allows a doctor update the visit details
  As a Doctor
  I want to update visits details

  Scenario Outline: Selected visit for patient is updated properly with all data
    Given Doctor select patient with visit status <visit>
    When Doctor selects the visit and update all data
    Then Doctor checks that visit is updated

    Examples:
      | visit     |
      | scheduled |
      | performed |
      | cancelled |


  Scenario Outline: Selected visit for patient is updated properly with only selected data
    Given Doctor select patient with <visit> visit
    When Doctor updates selected data <data> with <value>
    Then Doctor checks visit is updated

    Examples:
      | visit     | data      | value         |
      | scheduled | date      | 10.08.2018    |
      | performed | duration  | 30            |
      | cancelled | referrals | laryngologist |


  Scenario Outline: Selected visit for patient is updated properly with <visit> status
    Given Doctor select visit with status <visit>
    When Doctor update state to <status>
    Then Doctor checks visit status is updated properly

#performed status cannot be cancelled or scheduled

    Examples:
      | visit     | status    |
      | scheduled | performed |
      | performed | cancelled |
      | cancelled | scheduled |


