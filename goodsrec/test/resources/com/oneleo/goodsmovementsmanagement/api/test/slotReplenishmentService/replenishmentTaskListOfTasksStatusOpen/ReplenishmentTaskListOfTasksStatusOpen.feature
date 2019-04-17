@GoodsMovements

Feature: replenishmentTaskListOfTasksStatusOpen
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - List of tasks status - Open
  
    Given There are more replenishment tasks in the system with code "<status>"
  
     When the system check that the system search all the replenishment tasks with Open "<status>" and warehouse "<warehouse>"
     
     Then The system displays the replenishment tasks in Open "<status>" and warehouse "<warehouse>"
  
    Examples: 
    | status|warehouse|
    | Open  |3701     |



