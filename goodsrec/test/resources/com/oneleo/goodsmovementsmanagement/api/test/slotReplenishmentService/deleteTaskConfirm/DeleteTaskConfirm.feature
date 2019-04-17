@GoodsMovements

Feature: deleteTaskConfirm
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Delete Task - Confirm
  
    Given There is at least one task with "<code>" in Open or Started "<status>"in the system
  
     When the system delete the replenishment task with code "<code>", status "<status>" and warehouse "<warehouse>"
     
     Then The system displays a confirm deletion message
     
     And  The user confirms the action and the system delete phisically the task with check on db for the delete task with code "<code>"
  
    Examples: 
      |  code       | status|warehouse|
      |  test1      | Open  | 3701    |
