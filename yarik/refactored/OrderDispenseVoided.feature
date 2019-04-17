
Feature: As an RXPData kit I want to consume OrderLineVoided event and revert the status to ready

Scenario: RXPData consumes OrderLineVoided event and updates dispense to READY
  Given dispense with statuses "DELIVERED"
  Given an event "OrderLineVoided" for topic "orders_orderline_voidresponse"
  And the event "OrderLineVoided" has data as
  | dispenseCode  | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
  When the user produces the event
  And wait for "5" seconds
  Then get RxpData snapshot
  And verify order voided is updated in "rxPDataSnapshot2.disp_by_prescription_code[0]"
  And the system produces a new event "RxPDispenseVoided" to topic "rxpdata_dispense_voidresponse" with data
    | dispenseCode  | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
  And verify order voided for "RxPDispenseVoided" is notified in "rxPDataSnapshot2.disp_by_prescription_code[0]"
  And verify the Dispense codes are updated for other tables for "rxPDataSnapshot2" for "READY"




Scenario Outline: RXPData consumes OrderLineVoided event and does not update for other status
  Given dispense with statuses "<status>"
  Given an event "OrderLineVoided" for topic "orders_orderline_voidresponse"
  And the event "OrderLineVoided" has data as
    | dispenseCode  | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
  When the user produces the event
  And wait for "5" seconds
  Then get RxpData snapshot
  And verify that "rxPDataSnapshot2.disp_by_prescription_code[0].dispense_status == <status>"
  And the system does not produce a new event to topic "rxpdata_dispense_voided" with data
    | dispenseCode  | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |

  Examples:
  | status    |
#  | REVIEWED  |
  | PRINTED   |
#  | FILLED    |
#  | READY     |
#  | DELETED   |

