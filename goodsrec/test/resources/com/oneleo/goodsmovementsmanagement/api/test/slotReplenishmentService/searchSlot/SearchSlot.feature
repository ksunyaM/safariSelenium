@goodsmovements @sanity @basic
Feature: SearchSlot

@sanity @basic @system @searchBySlot @searchSlot
  Scenario Outline: GBD - GM - Replenishment Task - Search by Slot
  
    Given A Replenishment Task exists matching following criteria with "<code>","<internalCode>","<status>","<warehouseCode>" and "<type>"
      
     When The system searches a Replenishment Task with following criteria with "<status>" and "<warehouseCode>"
      
     Then The System retrives the searched slot with the following "<codeSlot>" and stock "<codeStock>"
  
    Examples: 
  
      | code   | internalCode       | status  | warehouseCode | type                |codeSlot|codeStock     |
      | 00100  | 000000000010070074 | Open    | 3701          | Automatic Generation|L456A25 |00000000108797|
