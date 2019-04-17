@CheckScannedStockUnitFailure
Feature: CheckScannedStockUnitFailure
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Check scanned Stock Unit - Failure
    Given There is at least one task with code "<code>" and status "<status>" 
     When the system selects a replenishment task with code "<code>" status "<status>" and warehouse "<warehouse>" and stockgroup "<stockgroup>"   
     Then The system displays a blocking message
    Examples: 
      |  code  | status   | warehouse|stockgroup    |
      |  00100 | Open     | 3701     |00000000046681|
      

