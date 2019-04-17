@PickLocationIdentified


Feature: pickLocationIdentified
@sanity @basic @system 
  Scenario Outline: GBD - GM - Replenishment Task - Pick Location identified
  
    Given The user is a warehouse checker and there is at least one task in Open or Started "<status>"with code "<code>"

     When the system scans the Stock Unit linked with code "<status>" and warehouse "<warehouse>" to the product present in the task and with the earliest expiry date  and scans another Stock Unit linked to the product in the task. Now the counted quantity is bigger than replenishment order quantity
     
     Then The system accepts that the counted quantity with "<quantity>" is major than replenishment order quantity with "<code>"
  
    Examples: 
      |  code     | status| quantity|warehouse|
      |  04486    | Open  | 280     | 3701    |
