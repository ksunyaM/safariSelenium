@GoodsMovements

Feature: replenishmentTaskListOfTasksOrder
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - List of tasks order
                    GBD - GM - Replenishment Task - List of tasks status - Open
                    GBD - GM - Replenishment Task - List of tasks status - Started
                    
  
    Given There are more replenishment tasks in the system with status "<status>"
  
     When the system check that the system search all the replenishment tasks with status "<status>" and warehouse "<warehouse>"
     
     Then The system displays the replenishment tasks with status "<status>"
  
    Examples: 
      
      | status    |warehouse|
      | Open      | 3701    |
      | Pending   | 3701    |
      #|Started    | 3701    |