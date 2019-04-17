@GoodsMovements

Feature: deleteTaskNotConfirm
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Delete Task - NotConfirm
  
    Given There is at least one task with "<code>" in Open or Started "<status>"in the system
  
     When the system delete the replenishment task with code "<code>", status "<status>" and warehouse "<warehouse>"
     
     Then The system displays a confirm deletion message. The user doesn't confirm the action and the system doesn't delete the task with "<code>"     
  
    Examples: 
      |  code       | status|warehouse|
      |  test1      | Open  | 3701    |
