@domain=RXPData @featureid=RXRPD-46412 @featureid=RXRPD-46411 @featureid=RXRPD-46678 @featureid=RXRPD-46769 @capabilityid=RXRPRP-4818
Feature: As an RXPData kit I want to consume OrderLineDelivered event and update dispense record based on Info provided

  @auto @smoke @jiraid=RXRPD-50870
  Scenario: RXPData consumes OrderLineDelivered event and updates dispense
    Given dispense with statuses "READY"
    Given an event "OrderLineDelivered" for topic "orders_orderline_deliverynotify"
    And the event "OrderLineDelivered" has data as
      | dispenseCode                                                                                    | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
      | soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber | random::5                                                   |
      | soldDateTime                                                                                    | timestamp::                                                 |
    When the user produces the event
    And wait for "5" seconds
    Then get RxpData snapshot
    And verify delivered dispense for "Cassandra" for "rxPDataSnapshot2"
    And the system produces a new event "RxPDispenseDelivered" to topic "rxpdata_dispense_deliverresponse" with data
      | dispenseCode | OrderLineDelivered.dispenseCode |
    And verify delivered dispense for "NotifyEvent" for "rxPDataSnapshot2"


  @auto @acceptance @jiraid=RXRPD-50873
  Scenario: RXPData consumes OrderLineDelivered event and updates dispense when no sold by information is present
    Given dispense with statuses "READY"
    Given an event "OrderLineDelivered" for topic "orders_orderline_deliverynotify"
    And the event "OrderLineDelivered" has data as
      | dispenseCode | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
      | soldBy       | null::                                                      |
      | soldDateTime | timestamp::                                                 |
    When the user produces the event
    And wait for "5" seconds
    Then get RxpData snapshot
    And verify delivered dispense for "Cassandra" for "rxPDataSnapshot2"
    And verify the Dispense codes are updated for other tables for "rxPDataSnapshot2" for "DELIVERED"
    And the system produces a new event "RxPDispenseDelivered" to topic "rxpdata_dispense_deliverresponse" with data
      | dispenseCode | OrderLineDelivered.dispenseCode |
    And verify delivered dispense for "NotifyEvent" for "rxPDataSnapshot2"

  @auto @smoke @jiraid=RXRPD-50871
  Scenario Outline: RXPData does not consume OrderLineDelivered event and update dispense when dispense is not in "READY" stats
    Given dispense with statuses "<status>"
    Given an event "OrderLineDelivered" for topic "orders_orderline_deliverynotify"
    And the event "OrderLineDelivered" has data as
      | dispenseCode                                                                                    | rxPDataSnapshot0.disp_by_prescription_code[0].dispense_code |
      | soldBy.['com.wba.order.orderline.delivery.notify.event.avro.EmployeeIdentifier'].employeeNumber | random::5                                                   |
      | soldDateTime                                                                                    | timestamp::                                                 |
    When the user produces the event
    And wait for "5" seconds
    Then get RxpData snapshot
    And verify that "rxPDataSnapshot2.disp_by_prescription_code[0].dispense_status == <status>"
    And the system does not produce a new event to topic "rxpdata_dispense_deliverresponse" with data
      | dispenseCode | OrderLineDelivered.dispenseCode |

    Examples:
      | status    |
      | REVIEWED  |
      | PRINTED   |
      | FILLED    |
      | DELETED   |
      | DELIVERED |
