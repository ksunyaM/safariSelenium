@CheckScannedStockUnit
Feature: checkScannedStockUnit
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Check scanned Stock Unit
  
    Given There is at least one task with code "<code>" and status "<status>" 

     When the system selects a replenishment task with code "<code>" status "<status>" and warehouse "<warehouse>"
     
     Then The system passes the replenishment tasks with code "<code>" and accepts and displays in the CountedQty field the stockUnit PBOH quantity
  
    Examples: 
      |  code  | status   | warehouse|
      |  00100 | Open     | 3701     |
      
