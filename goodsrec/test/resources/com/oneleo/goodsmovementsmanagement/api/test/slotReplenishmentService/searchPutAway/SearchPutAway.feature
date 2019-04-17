@SearchPutAway

Feature: searchByStock
@sanity @basic @system 
  Scenario Outline:  GBD - GM - Replenishment Task - Search by Stock Unit
  
     Given There are more replenishment tasks with code "<code>" in the system with stock "<stock>"   
     When  The system search for replenishment tasks with slot "<slot>" warehouse "<warehouse>" and "<status>"
     Then  the system performs the search with stock as search criteria "<stock>"
  
    Examples: 
      | code      | stock         |warehouse|status|slot|
      | TESTALFO  | 00000000000088|3701     |Open  |QN0010|