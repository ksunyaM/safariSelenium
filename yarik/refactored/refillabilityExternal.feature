@featureid=RXRPRP-3242 @capabilityid=RXRPRP-3240 @jiraid=RXRPD-31169 @domain=RxProcessing @api @prettify @TeamStark
Feature: Refillability External

  @acceptance @jiraid=RXRPD-53271 @auto
  Scenario: refillabilityExternal - Product Excluded rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct1 |
      | refillableIndicator   | b::false         |
    Then wait for "10" seconds
    When user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode | autoTestProduct1 |
    Then the response status code is "200"
    And verify that "c.refillable = false"
    And verify that "c.nonRefillableReason = PRODUCT_EXCLUDED"

  @acceptance @jiraid=RXRPD-46300 @auto @TeamStark
  Scenario Outline: externalrefillability positive
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59514    |
      | locationType   | Pharmacy |
    Then wait for "10" seconds
    When user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.daysSupply              | <daysSupply>        |
      | externalPrescription.quantityRemaining                    | <quantityRemaining> |
      | externalPrescription.direction                            | <direction>         |
      | externalPrescription.rxStatus                             | <rxStatus>          |
      | externalPrescription.lastDispense.actualProductPackCode   | autoTestProduct     |
      | externalPrescription.lastDispense.planCode                | autoTestPlanCode    |
      | dispensingLocation.locationType                           | 59511               |
      | externalPrescription.lastDispense.location.locationNumber | 59514               |
    Then the response status code is "200"
    And the response message is "HTTP/1.1 200 " with data
      | defaultDaysSupply         | externalRefill.externalPrescription.lastDispense.daysSupply        |
      | defaultDispensingQuantity | externalRefill.externalPrescription.lastDispense.quantityDispensed |
      | expandedDirection         | externalRefill.externalPrescription.direction                      |
    And verify that "c.refillable = true"
    Examples:
      | daysSupply | quantityRemaining | direction            | rxStatus |
      | random::2  | 10                | every day one capcul | ACTIVE   |


  @acceptance @jiraid=RXRPD-47507 @auto @TeamStark
  Scenario Outline:externalrefillability Negative
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    Then wait for "10" seconds
    When user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode | <actual app code>    |
      | externalPrescription.lastDispense.daysSupply            | <daysSupply>         |
      | externalPrescription.quantityRemaining                  | <quantityRemaining>  |
      | externalPrescription.direction                          | <direction>          |
      | externalPrescription.rxStatus                           | <rxStatus>           |
      | externalPrescription.fillExpirationDate                 | <fillExpirationDate> |
      | externalPrescription.lastDispense.actualProductPackCode | autoTestProduct      |
    Then the response message is "HTTP/1.1 200 " with data
      | defaultDaysSupply         | externalRefill.externalPrescription.lastDispense.daysSupply        |
      | defaultDispensingQuantity | externalRefill.externalPrescription.lastDispense.quantityDispensed |
      | expandedDirection         | externalRefill.externalPrescription.direction                      |
    And verify that "c.nonRefillableReason = <reason>"
    And verify that "c.refillable = false"
    Examples:
      | actual app code        | daysSupply | quantityRemaining | direction            | rxStatus | reason                | fillExpirationDate |
      | actualProductPackCode1 | random::2  | 0                 | every day one capcul | ACTIVE   | NO_QUANTITY_REMAINING | 2050-01-01         |
      | actualProductPackCode1 | random::2  | random::2         | random::2            | CLOSED   | PRESCRIPTION_CLOSED   | 2050-01-01         |
      | actualProductPackCode1 | random::2  | random::2         | random::2            | DELETED  | PRESCRIPTION_DELETED  | 2050-01-01         |

  @acceptance @jiraid=RXRPD-50303 @auto
  Scenario Outline: refillabilityExternal - Expired Prescription rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProductExpiredPrescription |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    And user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.fillExpirationDate                 | <fill_expiration_date>             |
      | externalPrescription.lastDispense.actualProductPackCode | autoTestProductExpiredPrescription |
      | externalPrescription.lastDispense.planCode              | autoTestPlanCode                   |
    Then the response status code is "200"
    And verify that "c.refillable = <refillableValue>"
    And verify that "c.nonRefillableReason = <nonRefillableReason>"
    Examples:
      | fill_expiration_date | refillableValue | nonRefillableReason   |
      | 2000-01-01           | false           | PRESCRIPTION_EXPIRED  |
      | date::               | true            | c.nonRefillableReason |
      | 2050-01-01           | true            | c.nonRefillableReason |

  @acceptance @jiraid=RXRPD-50305 @auto
  Scenario Outline: refillabilityExternal - Product Discontinued rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProductDiscontinuedRule |
      | status                | <product_status>                |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    And user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode | autoTestProductDiscontinuedRule |
      | externalPrescription.lastDispense.planCode              | autoTestPlanCode                |
    Then the response status code is "200"
    And verify that "c.refillable = <refillableValue>"
    And verify that "c.nonRefillableReason = <nonRefillableReason>"
    Examples:
      | product_status | refillableValue | nonRefillableReason   |
      | INACTIVE       | false           | PRODUCT_DISCONTINUED  |
      | ACTIVE         | true            | c.nonRefillableReason |

  @acceptance @jiraid=RXRPD-50308 @auto
  Scenario Outline: refillabilityExternal - State Level Controls rule
    Given Product Mock is created with params
      | actualProductPackCode     | autoTestProductStateRule |
      | stateDeaClass[0].deaClass | <stateDeaClass>          |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    And user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode | autoTestProductStateRule |
      | externalPrescription.lastDispense.planCode              | autoTestPlanCode         |
    Then the response status code is "200"
    And verify that "c.refillable = <refillableValue>"
    And verify that "c.nonRefillableReason = <nonRefillableReason>"
    Examples:
      | stateDeaClass | refillableValue | nonRefillableReason   |
      | C2            | false           | CONTROLLED_PRODUCT    |
      | C5            | false           | CONTROLLED_PRODUCT    |
      | NA            | true            | c.nonRefillableReason |

  @acceptance @jiraid=RXRPD-50310 @auto
  Scenario Outline: refillabilityExternal - Valid Prescriber rule
    Given Prescriber Mock is created with params
      | code   | autoValidPrescriberRule |
      | status | <prescriberStatus>      |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    And user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.prescriberCode           | autoValidPrescriberRule    |
      | externalPrescription.validPrescriberIndicator | <validPrescriberIndicator> |
      | externalPrescription.lastDispense.planCode    | autoTestPlanCode           |
    Then the response status code is "200"
    And verify that "c.refillable = <refillableValue>"
    And verify that "c.nonRefillableReason = <nonRefillableReason>"
    Examples:
      | prescriberStatus | refillableValue | nonRefillableReason   | validPrescriberIndicator |
      | INVALID          | false           | INVALID_PRESCRIBER    | false                    |
#      | VALID            | true            | c.nonRefillableReason | true                     |

  @acceptance @jiraid=RXRPD-49601 @auto
  Scenario Outline: refillabilityExternal - External Dispense in Progress rule
    Given Prescriber Mock is created with params
      | code   | autoPrescriberDispenseRule |
      | status | <prescriberStatus>         |
    And Plan Mock is created with params
      | planCode | autoTestPlanCode |
    Then wait for "10" seconds
    And user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.inProcess | <inProcessValue>           |
      | externalPrescription.prescriberCode         | autoPrescriberDispenseRule |
      | externalPrescription.lastDispense.planCode  | autoTestPlanCode           |
    Then the response status code is "200"
    And verify that "c.refillable = <refillableValue>"
    And verify that "c.nonRefillableReason = <nonRefillableReason>"
    Examples:
      | prescriberStatus | inProcessValue | refillableValue | nonRefillableReason           |
      | INVALID          | true           | false           | EXTERNAL_DISPENSE_IN_PROGRESS |
#      | VALID            | false          | true            | c.nonRefillableReason         |

  @acceptance @jiraid=RXRPD-60557 @auto @TeamStark
  Scenario: externalrefillability - Same State rule
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Location Mock is created with params
      | locationNumber | 59511    |
      | locationType   | Pharmacy |
    And Location Mock is created with params
      | locationNumber | 59519    |
      | locationType   | Pharmacy |
      | address.state  | NY       |
    Then wait for "10" seconds
    When user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode   | autoTestProduct     |
      | dispensingLocation.locationType                           | 59511               |
      | externalPrescription.lastDispense.location.locationNumber | 59519               |
    Then the response status code is "200"
    And verify that "c.refillable = false"
    And verify that "c.nonRefillableReason = OUT_OF_STATE_REFILL"