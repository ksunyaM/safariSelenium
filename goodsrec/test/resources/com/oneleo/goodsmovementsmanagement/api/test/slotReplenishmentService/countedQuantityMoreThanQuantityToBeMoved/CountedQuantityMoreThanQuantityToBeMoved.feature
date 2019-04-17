@CountedQuantityMoreThanQuantityToBeMoved

Feature: countedQuantityMoreThanQuantityToBeMoved
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Counted quantity more than quantity to be moved
  
    Given The user is a warehouse checker and there is at least one task in Open or Started "<status>"with code "<code>"

     When the system scans the Stock Unit linked with code "<code>" and warehouse "<warehouse>" to the product present in the task and with the earliest expiry date  and scans another Stock Unit linked to the product in the task. Now the counted quantity is bigger than replenishment order quantity
     
     Then The system accepts that the counted quantity with "<quantity>" is major than replenishment order quantity with "<code>"
  
    Examples: 
      |  code     | status| warehouse|
      |  04486    | Open  | 3701     |
      
      
  #@sanity @basic @system 
  #Scenario Outline: GBD - GM - Replenishment Task - Counted quantity less than quantity to be moved
  #
     #Given The user is a warehouse checker and there is at least one task in Open or Started "<status>" and code "<code>"
#
     #When the system scans the Stock Unit linked with code "<code>" and warehouse "<warehouse>" to the product present in the task and with the earliest expiry date  and scans another Stock Unit linked to the product in the task. Now the counted quantity is less than replenishment order quantity
     #
     #Then The system accepts that the counted quantity is major than replenishment order quantity
    #
    #Examples: 
      #|  code     | status|warehouse|
      #|  04419    | Open  |3701     |