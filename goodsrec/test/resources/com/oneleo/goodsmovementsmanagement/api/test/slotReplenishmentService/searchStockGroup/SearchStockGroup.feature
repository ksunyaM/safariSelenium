@goodsmovements @sanity @basic
Feature: SearchStockGroup

@sanity @basic @system @searchBySlot @searchSlot
  Scenario Outline: GBD - GM - Replenishment Task - Search by Stock Unit
  
    Given A Replenishment Task exists matching following criteria with "<code>","<internalCode>","<status>","<warehouseCode>" and "<type>"
      
     When The system searches a Replenishment Task with following criteria with "<status>" and "<warehouseCode>"
      
     Then The System retrives the searched stock with the code "<codeStock>" and slot "<codeSlot>"
  
    Examples: 
  
      | code   | internalCode       | status  | warehouseCode | type                |codeStock      |codeSlot|
      | 00100  | 000000000010070074 | Open    | 3701          | Automatic Generation|00000000108797 |L456A25 |
