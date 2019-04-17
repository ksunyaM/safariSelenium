@featureid=RXRPRP-3228 @featureid=RXRPRP-4628 @capabilityid=RXRPRP-2912 @domain=RxProcessing @api @prettify @TeamStark
Feature: Refillability Internal

  @acceptance @jiraid=RXRPD-47414 @auto @TeamStark1
  Scenario: refillabilityInternal - Refillable prescirption
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59514    |
      | locationType   | Pharmacy |
    Given a prescription with "1" dispenses in RXPData database
      | quantity_remaining                           | 10                 |
      | spec_fill_expiration_date                    | 2050-01-01         |
      | spec_location_number                         | 59511              |
      | location_number                              | 59511              |
      | spec_prescriber_location_code                | 59511              |
      | disp_by_prescription_code[0].days_supply     | 5                  |
      | spec_prescribed_actual_product_pack_code     | autoTestProduct    |
      | actual_product_pack_code                     | autoTestProduct    |
      | coupon_actual_product_pack_code              | autoTestProduct    |
      | spec_prescriber_code                         | autoTestPrescriber |
      | plan_code                                    | autoTestPlanCode   |
      | disp_by_prescription_code[0].dispense_status | DELIVERED          |
      | prescription_by_pat_code[0].dispense_status  | DELIVERED          |
#      | disp_by_prescription_code[0].actual_product_pack_code |   |
    Then wait for "10" seconds
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
      | dispensingLocation.locationNumber                      | 59514              |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply | d::5           |
      | c.internalRxRefillabilityResponseList[0].refillable        | true           |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode  | ~dispenseCode0 |

  @acceptance @jiraid=RXRPD-47414 @auto @TeamStark1
  Scenario: refillabilityInternal - Refillable prescirption 2
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59514    |
      | locationType   | Pharmacy |
    Given a prescription with "2" dispenses in RXPData database
      | quantity_remaining                           | 10                 |
      | spec_fill_expiration_date                    | 2050-01-01         |
      | spec_location_number                         | 59511              |
      | location_number                              | 59511              |
      | spec_prescriber_location_code                | 59511              |
      | disp_by_prescription_code[0].days_supply     | 5                  |
      | spec_prescribed_actual_product_pack_code     | autoTestProduct    |
      | actual_product_pack_code                     | autoTestProduct    |
      | coupon_actual_product_pack_code              | autoTestProduct    |
      | spec_prescriber_code                         | autoTestPrescriber |
      | plan_code                                    | autoTestPlanCode   |
      | disp_by_prescription_code[0].dispense_status | DELETED            |
      | prescription_by_pat_code[0].dispense_status  | DELETED            |
      | disp_by_prescription_code[1].dispense_status | DELIVERED          |
      | prescription_by_pat_code[1].dispense_status  | DELIVERED          |
    Then wait for "10" seconds
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode1     |
      | dispensingLocation.locationNumber                      | 59514              |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply | d::5           |
      | c.internalRxRefillabilityResponseList[0].refillable        | true           |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode  | ~dispenseCode1 |

  @acceptance @jiraid=RXRPD-53272 @auto
  Scenario: refillabilityInternal - Deleted Dispense order 1
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    Given a prescription with "5" dispenses in RXPData database
      | disp_by_prescription_code[0].dispense_status    | DELETED            |
      | disp_by_prescription_code[1].dispense_status    | DELETED            |
      | disp_by_prescription_code[2].dispense_status    | DELIVERED          |
      | prescription_by_pat_code[2].dispense_status     | DELIVERED          |
      | quantity_remaining                              | 100                |
      | spec_original_quantity_prescribed               | 70                 |
      | disp_by_prescription_code[2].quantity_dispensed | 60                 |
      | disp_by_prescription_code[3].quantity_dispensed | 60                 |
      | disp_by_prescription_code[2].days_supply        | 10                 |
      | disp_by_prescription_code[3].days_supply        | 20                 |
      | spec_prescribed_actual_product_pack_code        | autoTestProduct    |
      | actual_product_pack_code                        | autoTestProduct    |
      | coupon_actual_product_pack_code                 | autoTestProduct    |
      | spec_prescriber_code                            | autoTestPrescriber |
      | plan_code                                       | autoTestPlanCode   |
      | spec_fill_expiration_date                       | 2050-01-01         |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode2     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].refillable                | true           |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode          | ~dispenseCode2 |
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply         | d::10          |
      | c.internalRxRefillabilityResponseList[0].defaultDispensingQuantity | d::60          |

  @acceptance @jiraid=RXRPD-53272 @auto
  Scenario: refillabilityInternal - Deleted Dispense order 2
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    Given a prescription with "5" dispenses in RXPData database
      | disp_by_prescription_code[0].dispense_status    | DELETED            |
      | prescription_by_pat_code[0].dispense_status     | DELETED            |
      | quantity_remaining                              | 100                |
      | disp_by_prescription_code[0].quantity_dispensed | 60                 |
      | disp_by_prescription_code[1].quantity_dispensed | 60                 |
      | disp_by_prescription_code[0].days_supply        | 10                 |
      | disp_by_prescription_code[1].days_supply        | 20                 |
      | spec_prescribed_actual_product_pack_code        | autoTestProduct    |
      | actual_product_pack_code                        | autoTestProduct    |
      | coupon_actual_product_pack_code                 | autoTestProduct    |
      | spec_prescriber_code                            | autoTestPrescriber |
      | plan_code                                       | autoTestPlanCode   |
      | spec_fill_expiration_date                       | 2050-01-01         |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].refillable                | false                |
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason       | PRESCRIPTION_DELETED |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode          | ~dispenseCode0       |
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply         | d::10                |
      | c.internalRxRefillabilityResponseList[0].defaultDispensingQuantity | d::60                |


  @acceptance @jiraid=RXRPD-53270 @auto
  Scenario: refillabilityInternal - Product Excluded rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct1 |
      | refillableIndicator   | b::false         |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    Then wait for "10" seconds
    Given a prescription with "5" dispenses in RXPData database
      | spec_fill_expiration_date                | 2050-01-01         |
      | spec_location_number                     | 59511              |
      | location_number                          | 59511              |
      | spec_prescriber_location_code            | 59511              |
      | spec_prescribed_actual_product_pack_code | autoTestProduct1   |
      | actual_product_pack_code                 | autoTestProduct1   |
      | coupon_actual_product_pack_code          | autoTestProduct1   |
      | spec_prescriber_code                     | autoTestPrescriber |
      | spec_fill_expiration_date                | 2050-01-01         |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].refillable          | false            |
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | PRODUCT_EXCLUDED |

  @acceptance @jiraid=RXRPD-47413 @auto @TeamStark
  Scenario Outline: refillabilityInternal - Default Quantity when 2 Dispenses have the same quantity
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    Given a prescription with "2" dispenses in RXPData database
      | quantity_remaining                              | <quantityRemaining> |
      | spec_original_quantity_prescribed               | 70                  |
      | disp_by_prescription_code[0].quantity_dispensed | 60                  |
      | disp_by_prescription_code[1].quantity_dispensed | 60                  |
      | disp_by_prescription_code[0].days_supply        | 10                  |
      | disp_by_prescription_code[1].days_supply        | 20                  |
      | spec_prescribed_actual_product_pack_code        | autoTestProduct     |
      | actual_product_pack_code                        | autoTestProduct     |
      | coupon_actual_product_pack_code                 | autoTestProduct     |
      | spec_prescriber_code                            | autoTestPrescriber  |
      | spec_fill_expiration_date                       | 2050-01-01          |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply         | d::10                |
      | c.internalRxRefillabilityResponseList[0].defaultDispensingQuantity | d::<defaultQuantity> |
  #    | c.internalRxRefillabilityResponseList[0].refillable                | true  |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode          | ~dispenseCode0       |
    Examples:
      | quantityRemaining | defaultQuantity |
      | 100               | 60              |
      | 50                | 50              |

  @acceptance @jiraid=RXRPD-47414 @auto @TeamStark1
  Scenario Outline: refillabilityInternal - Default Quantity when Previous Dispense Qty < Prescribed Qty
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    Given a prescription with "2" dispenses in RXPData database
      | quantity_remaining                              | <quantityRemaining> |
      | spec_original_quantity_prescribed               | 70                  |
      | disp_by_prescription_code[0].quantity_dispensed | 60                  |
      | disp_by_prescription_code[1].quantity_dispensed | 40                  |
      | disp_by_prescription_code[0].days_supply        | 10                  |
      | disp_by_prescription_code[1].days_supply        | 20                  |
      | actual_product_pack_code                        | autoTestProduct     |
      | coupon_actual_product_pack_code                 | autoTestProduct     |
      | spec_prescriber_code                            | autoTestPrescriber  |
      | plan_code                                       | autoTestPlanCode    |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply         | d::10                |
      | c.internalRxRefillabilityResponseList[0].defaultDispensingQuantity | d::<defaultQuantity> |
#      | c.internalRxRefillabilityResponseList[0].refillable                | true  |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode          | ~dispenseCode0       |
    Examples:
      | quantityRemaining | defaultQuantity |
      | 100               | 70              |
      | 50                | 50              |

  @acceptance @jiraid=RXRPD-47416 @auto @TeamStark1
  Scenario Outline: refillabilityInternal - Default Quantity when Previous Dispense Qty > Prescribed Qty
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    Given a prescription with "2" dispenses in RXPData database
      | quantity_remaining                              | <quantityRemaining> |
      | spec_original_quantity_prescribed               | 60                  |
      | disp_by_prescription_code[0].quantity_dispensed | 70                  |
      | disp_by_prescription_code[1].quantity_dispensed | 40                  |
      | disp_by_prescription_code[0].days_supply        | 10                  |
      | disp_by_prescription_code[1].days_supply        | 20                  |
      | actual_product_pack_code                        | autoTestProduct     |
      | coupon_actual_product_pack_code                 | autoTestProduct     |
      | spec_prescriber_code                            | autoTestPrescriber  |
      | plan_code                                       | autoTestPlanCode    |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].defaultDaysSupply         | d::10                |
      | c.internalRxRefillabilityResponseList[0].defaultDispensingQuantity | d::<defaultQuantity> |
#      | c.internalRxRefillabilityResponseList[0].refillable                | true  |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode          | ~dispenseCode0       |
    Examples:
      | quantityRemaining | defaultQuantity |
      | 100               | 70              |
      | 50                | 50              |

  @acceptance @jiraid=RXRPD-47410 @auto @TeamStark1
  Scenario Outline: refillabilityInternal - Not refillable prescirption because of Closed or Deleted
    Given a prescription with "1" dispenses in RXPData database
      | quantity_remaining | 10         |
      | rx_status          | <rxStatus> |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                 |
    Examples:
      | rxStatus | nonRefillableReason  |
      | CLOSED   | PRESCRIPTION_CLOSED  |
      | DELETED  | PRESCRIPTION_DELETED |

  @acceptance @jiraid=RXRPD-47411 @auto @TeamStark1
  Scenario: refillabilityInternal - Not refillable prescirption because of quantity_remaining is 0
    Given Prescriber Mock is created with params
      | code | autoTestPrescriber |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | quantity_remaining            | 0                  |
      | spec_fill_expiration_date     | 2050-01-01         |
      | spec_location_number          | 59511              |
      | location_number               | 59511              |
      | spec_prescriber_location_code | 59511              |
      | spec_prescriber_code          | autoTestPrescriber |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | NO_QUANTITY_REMAINING |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                 |

  @acceptance @jiraid=RXRPD-50302 @auto
  Scenario Outline: refillabilityInternal - Expired Prescription rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                       | <fill_expiration_date> |
      | quantity_remaining                              | 100                    |
      | spec_original_quantity_prescribed               | 70                     |
      | disp_by_prescription_code[0].quantity_dispensed | 60                     |
      | disp_by_prescription_code[0].days_supply        | 10                     |
      | spec_prescribed_actual_product_pack_code        | autoTestProduct        |
      | actual_product_pack_code                        | autoTestProduct        |
      | coupon_actual_product_pack_code                 | autoTestProduct        |
      | spec_prescriber_code                            | autoTestPrescriber     |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | <refillableValue>     |
    Examples:
      | fill_expiration_date | refillableValue | nonRefillableReason                                          |
      | 2000-01-01           | false           | PRESCRIPTION_EXPIRED                                         |
#      | date::               | true            | c.internalRxRefillabilityResponseList[0].nonRefillableReason |

  @acceptance @jiraid=RXRPD-50304 @auto
  Scenario Outline: refillabilityInternal - Product Discontinued rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct2 |
      | status                | <product_status> |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                                    | 2050-01-01         |
      | spec_location_number                                         | 59511              |
      | location_number                                              | 59511              |
      | spec_prescriber_location_code                                | 59511              |
      | spec_prescribed_actual_product_pack_code                     | autoTestProduct2   |
      | disp_by_prescription_code[0].actual_product_pack_code        | autoTestProduct2   |
      | disp_by_prescription_code[0].coupon_actual_product_pack_code | autoTestProduct2   |
      | spec_prescriber_code                                         | autoTestPrescriber |
      | disp_by_prescription_code[0].dispense_status | DELIVERED          |
      | prescription_by_pat_code[0].dispense_status  | DELIVERED          |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | <refillableValue>     |
    Examples:
      | refillableValue | nonRefillableReason  | product_status |
      | false           | PRODUCT_DISCONTINUED | INACTIVE       |
#      | true            | c.internalRxRefillabilityResponseList[0].nonRefillableReason|ACTIVE|

  @acceptance @jiraid=RXRPD-50306 @auto
  Scenario Outline: refillabilityInternal - State Level Controls rule
    Given Product Mock is created with params
      | actualProductPackCode     | autoTestProduct |
      | stateDeaClass[0].deaClass | <stateDeaClass> |
      | stateDeaClass[0].state    | IL              |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                                    | 2050-01-01         |
      | spec_location_number                                         | 59511              |
      | location_number                                              | 59511              |
      | spec_prescriber_location_code                                | 59511              |
      | spec_prescribed_actual_product_pack_code                     | autoTestProduct    |
      | disp_by_prescription_code[0].actual_product_pack_code        | autoTestProduct    |
      | disp_by_prescription_code[0].coupon_actual_product_pack_code | autoTestProduct    |
      | spec_prescriber_code                                         | autoTestPrescriber |
      | disp_by_prescription_code[0].dispense_status | DELIVERED          |
      | prescription_by_pat_code[0].dispense_status  | DELIVERED          |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | <refillableValue>     |
    Examples:
      | stateDeaClass | refillableValue | nonRefillableReason                                          |
      | C2            | false           | CONTROLLED_PRODUCT                                           |
      | C5            | false           | CONTROLLED_PRODUCT                                           |
      | NA            | true            | c.internalRxRefillabilityResponseList[0].nonRefillableReason |

  @acceptance @jiraid=RXRPD-50309 @auto
  Scenario Outline: refillabilityInternal - Valid Prescriber rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code   | autoTestPrescriber1 |
      | status | <prescriberStatus>  |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                                    | 2050-01-01          |
      | spec_location_number                                         | 59511               |
      | location_number                                              | 59511               |
      | spec_prescriber_location_code                                | 59511               |
      | precriberCode                                                | autoTestPrescriber1 |
      | spec_prescribed_actual_product_pack_code                     | autoTestProduct     |
      | disp_by_prescription_code[0].actual_product_pack_code        | autoTestProduct     |
      | disp_by_prescription_code[0].coupon_actual_product_pack_code | autoTestProduct     |
      | spec_prescriber_code                                         | autoTestPrescriber1 |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | <refillableValue>     |
    Examples:
      | prescriberStatus | refillableValue | nonRefillableReason |
      | INVALID          | false           | INVALID_PRESCRIBER  |
#      | VALID            | true            | c.internalRxRefillabilityResponseList[0].nonRefillableReason|

  @acceptance @jiraid=RXRPD-365361 @auto @TeamNeptune
  Scenario Outline: refillabilityInternal - Not refillable prescirption because of Government Plan
    Given a prescription with "1" dispenses in RXPData database
      | quantity_remaining | 10         |
      | rx_status          | <rxStatus> |
    Given User prepare the "RxPlan" mock with params
      | planCode | Testabc |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                 |
    Examples:
      | rxStatus | nonRefillableReason  |
      | CLOSED   | PRESCRIPTION_CLOSED  |
      | DELETED  | PRESCRIPTION_DELETED |

  @acceptance @jiraid=RXRPD-365361 @auto @TeamNeptune
  Scenario Outline: refillabilityInternal - Not refillable prescirption because of Government Plan
    Given a prescription with "1" dispenses in RXPData database
      | quantity_remaining | 10         |
      | rx_status          | <rxStatus> |
    And update plan "RxPlan" response mock json with Data
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
    Given User prepare the "RxPlan" mock with params
      | planCode | Testabc |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | <nonRefillableReason> |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                 |
    Examples:
      | rxStatus | nonRefillableReason  |
      | CLOSED   | PRESCRIPTION_CLOSED  |
      | DELETED  | PRESCRIPTION_DELETED |

  @acceptance @jiraid=RXRPD-60556 @auto @TeamStark
  Scenario: refillabilityInternal - Same State rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59524    |
      | locationType   | Pharmacy |
      | address.state  | NY       |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                | 2050-01-01         |
      | spec_location_number                     | 59511              |
      | location_number                          | 59511              |
      | spec_prescriber_location_code            | 59511              |
      | spec_prescribed_actual_product_pack_code | autoTestProduct    |
      | actual_product_pack_code                 | autoTestProduct    |
      | coupon_actual_product_pack_code          | autoTestProduct    |
      | spec_prescriber_code                     | autoTestPrescriber |
      | disp_by_prescription_code[0].dispense_status | DELIVERED          |
      | prescription_by_pat_code[0].dispense_status  | DELIVERED          |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
      | dispensingLocation.locationNumber                      | 59524              |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].refillable          | false               |
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | OUT_OF_STATE_REFILL |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode    | ~dispenseCode0      |

  @acceptance @jiraid=RXRPD-62169 @auto @TeamStark1
  Scenario: refillabilityInternal - Multiple Active Dispenses 1
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59514    |
      | locationType   | Pharmacy |
    Given a prescription with "2" dispenses in RXPData database
      | quantity_remaining                           | 10                 |
      | spec_fill_expiration_date                    | 2050-01-01         |
      | spec_location_number                         | 59511              |
      | location_number                              | 59511              |
      | spec_prescriber_location_code                | 59511              |
      | disp_by_prescription_code[0].days_supply     | 5                  |
      | spec_prescribed_actual_product_pack_code     | autoTestProduct    |
      | actual_product_pack_code                     | autoTestProduct    |
      | coupon_actual_product_pack_code              | autoTestProduct    |
      | spec_prescriber_code                         | autoTestPrescriber |
      | plan_code                                    | autoTestPlanCode   |
      | disp_by_prescription_code[0].dispense_status | ENTERED            |
      | disp_by_prescription_code[1].dispense_status | PRINTED            |
      | prescription_by_pat_code[0].dispense_status  | ENTERED            |
      | prescription_by_pat_code[1].dispense_status  | PRINTED            |
    Then wait for "10" seconds
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
      | dispensingLocation.locationNumber                      | 59514              |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].refillable          | false                         |
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | EXTERNAL_DISPENSE_IN_PROGRESS |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode    | ~dispenseCode0                |

  @acceptance @jiraid=RXRPD-62171 @auto @TeamStark1
  Scenario: refillabilityInternal - Multiple Active Dispenses 2
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59514    |
      | locationType   | Pharmacy |
    Given a prescription with "5" dispenses in RXPData database
      | quantity_remaining                           | 10                 |
      | spec_fill_expiration_date                    | 2050-01-01         |
      | spec_location_number                         | 59511              |
      | location_number                              | 59511              |
      | spec_prescriber_location_code                | 59511              |
      | disp_by_prescription_code[0].days_supply     | 5                  |
      | spec_prescribed_actual_product_pack_code     | autoTestProduct    |
      | actual_product_pack_code                     | autoTestProduct    |
      | coupon_actual_product_pack_code              | autoTestProduct    |
      | spec_prescriber_code                         | autoTestPrescriber |
      | plan_code                                    | autoTestPlanCode   |
      | disp_by_prescription_code[0].dispense_status | DELETED            |
      | disp_by_prescription_code[1].dispense_status | DELETED            |
      | disp_by_prescription_code[2].dispense_status | FILLED             |
      | prescription_by_pat_code[0].dispense_status  | DELETED            |
      | prescription_by_pat_code[1].dispense_status  | DELETED            |
      | prescription_by_pat_code[2].dispense_status  | FILLED             |
    Then wait for "10" seconds
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode2     |
      | dispensingLocation.locationNumber                      | 59514              |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].refillable          | false                         |
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | EXTERNAL_DISPENSE_IN_PROGRESS |
      | c.internalRxRefillabilityResponseList[0].lastDispenseCode    | ~dispenseCode2                |