@ReplenishmentTaskSearchBySlot

Feature: searchBySlot
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Search by Slot
  
     Given There are more replenishment tasks in the system with slot "<code>"   
     When  The system search for replenishment tasks with "<code>" , status "<status>" and "<warehouse>"
     Then  the system performs the search with slot with "<slot>" 
  
    Examples: 
      | code  | slot|
      | 04278 | QN0010|
      
      
