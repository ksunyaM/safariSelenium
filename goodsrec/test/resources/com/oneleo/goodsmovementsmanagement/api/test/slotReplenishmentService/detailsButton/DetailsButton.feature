@GoodsMovements

Feature: detailsButton
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Details button
  
    Given There is at least one RT with code "<code>" in Open status "<status>" in the system
  
     When the system check the button details for the replenishment task with code "<code>", status "<status>" and warehouse "<warehouse>"
     
     Then The system opens the UI with information about the "<tasknumber>"
  
    Examples: 
      |  code       | status  |warehouse|
      |  00100      | Open    | 3701    |