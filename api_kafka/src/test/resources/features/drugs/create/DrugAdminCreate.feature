Feature: System allows the drug admin to find proper drugs
  As a drugs admin,
  I want to add the drugs to the drugs base
  so that I can order them

  @RealData
  Scenario Outline: New drug is introduced to the market
    Given Drug admin log in to the drug's module
    When Drug admin add the introduced <drug> with drugCode <drugCode> in <dosageForm> with default dosage <dosage> and <comment>
    Then Drug admin checks the drug created

    Examples:
      | drug       | drugCode        | dosageForm | dosage     | comment       |
      | Penicillin | NDC 077-3105-02 | pill       | 250 mg/3xd | Big pills     |
      | Aspirin    | NDC 777-1111-03 | liquid     | 500 mg/3xd | Yellow liquid |

#
#  Scenario Outline: New drug is created with the code and dosage form with default dosage
#    Given Drug admin log in to the drug's module
#    When Drug admin add the ordered drug with proper dosage form
#    Then  <drugName> is created with code <code> and <form> form with default dosage <dosage>
#
#    Examples:
#      | drugName   | code            | form   | dosage     |
#      | Penicillin | NDC 077-3105-02 | pill   | 250 mg/3xd |
#      | Aspirin    | NDC 777-1111-03 | liquid | 500 mg/3xd |
