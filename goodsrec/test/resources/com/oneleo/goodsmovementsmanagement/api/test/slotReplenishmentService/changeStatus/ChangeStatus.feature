@ChangeStatus

Feature: changeStatus
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - The task status passes from Started to Pending
  
    Given There is at least one task with code "<code>" and status "<status>" 

     When the system selects a replenishment task with code "<code>" status "<changestatus>" and warehouse "<warehouse>"
     
     Then The system passes the replenishment tasks with code "<code>"  and statusfinal "<statusfinal>" from Started to Pending
  
    Examples: 
      |  code         | status| warehouse|changestatus|statusfinal|
      |  test1        | Open  | 3701     |Started     |Pending    |
      