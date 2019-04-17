@ReplenishmentTaskTargetLocationUpdatedManually


Feature: updateManually
@sanity @basic @system 
  Scenario Outline:  GBD - GM - Replenishment Task - Target Location updated manually
  
     Given There is at least one task in "<Open>" status with code "<code>"
     When  the system displays a warning message and indicates that the max quantity should be: Replenishment Qty-Qty in the slot
     Then  The system displays the warning message with the correct quantity to work
  
    Examples: 
      | code      | status |
      | 03776     | Open   |



