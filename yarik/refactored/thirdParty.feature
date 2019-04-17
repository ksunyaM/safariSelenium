Feature: Refill Validation Rule
  Do Not allow scripts for third party vendors (PBMs) that require certified system


  @acceptance @jiraid=RXRPD-36536 @auto @TeamNeptune
  Scenario Outline: externalrefillability positive
    Given user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.daysSupply            | <daysSupply>            |
      | externalPrescription.lastDispense.planCode              | <planCode>              |
      | externalPrescription.lastDispense.actualProductPackCode | <actualProductPackCode> |
      | externalPrescription.lastDispense.inProcess             | false                   |
      | externalPrescription.quantityRemaining                  | <quantityRemaining>     |
      | externalPrescription.direction                          | <direction>             |
      | externalPrescription.rxStatus                           | <rxStatus>              |
      | externalPrescription.validPrescriberIndicator           | true                    |
      | externalPrescription.fillExpirationDate                 | <fillExpirationDate>    |
    Given User prepare the "RxPlan" mock with params
      | planCode      | Testabc      |
      | certifiedPlan | NOT_REQUIRED |
    And update plan "RxPlan" response mock json with Data
    And update plan "RxPlan" response mock json with certified plan Data
      | certifiedPlan | NOT_REQUIRED |
      | certifiedPlan | YES          |
      | certifiedPlan | NO           |

    Given User prepare the "Prescriber" mock with params
      | prescriberCode        | PRESCRIBER_CODE          |
      | prescriberLocationCde | PRESCRIBER_LOCATION_CODE |
    Given User prepare the "Product" mock with params
      | code | 6c990dcb-e906-4974-9b54-77bd43ef2c66 |

    And Upload all Mock to server
    Then the response message is "HTTP/1.1 200 " with data
      | defaultDaysSupply         | externalRefill.externalPrescription.lastDispense.daysSupply        |
      | defaultDispensingQuantity | externalRefill.externalPrescription.lastDispense.quantityDispensed |
    And verify that "c.refillable == <expected>"

    Examples:
      | daysSupply | quantityRemaining | direction            | rxStatus | planCode | actualProductPackCode                | fillExpirationDate | expected |
      | random::2  | random::2         | every day one capcul | ACTIVE   | ILBB     | 6c990dcb-e906-4974-9b54-77bd43ef2c66 | 2018-12-31         | true     |
      | random::2  | random::2         | every day one capcul | ACTIVE   | ILBB     | 6c990dcb-e906-4974-9b54-77bd43ef2c66 | 2018-12-31         | true     |
      | random::2  | random::2         | every day one capcul | ACTIVE   | ILBB     | 6c990dcb-e906-4974-9b54-77bd43ef2c66 | 2018-12-29         | false    |

