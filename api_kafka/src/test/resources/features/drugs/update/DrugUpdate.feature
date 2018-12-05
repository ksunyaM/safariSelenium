Feature: System allows a the system admin to update the drug details
  As a System admin
  I want to update drug details

  Scenario Outline: Selected drug is updated properly with all data
    Given System admin selects drug <drugCode>
    When System admin updates all data <name> and <dosageForm>
    Then System admin checks that drug is updated

    Examples:
      | drugCode        | name       | dosageForm    |
      | NDC 077-3105-02 | Penicillin | pills         |
      | NDC 077-3105-03 | Aspirin    | liquid        |
      | NDC 077-3105-04 | Antibiotic | suppositories |


  Scenario Outline: Selected drug is updated properly with only selected data
    Given System admin selects <drugCode> drug
    When System admin updates selected data <name> with <dosageForm>
    Then System admin checks drug is updated

    Examples:
      | drugCode        | name       | dosageForm    |
      | NDC 077-3105-02 | Penicillin | pills         |
      | NDC 077-3105-03 | Aspirin    | liquid        |
      | NDC 077-3105-04 | Antibiotic | suppositories |


  Scenario Outline: Selected drug is updated properly with changed data
    Given System admin selects <drugCode> drug with <name> and with <dosageForm>
    When System admin updates fields <newCode> and <newName> and <newDosageForm>
    Then System admin checks drug is updated

    Examples:
      | drugCode        | newCode         | name       | newName    | dosageForm    | newDosageForm |
      | NDC 077-3105-02 | NDC 077-3105-03 | Penicillin | Aspirin    | pills         | liquid        |
      | NDC 077-3105-03 | NDC 077-3105-04 | Aspirin    | Antibiotic | liquid        | suppositories |
      | NDC 077-3105-04 | NDC 077-3105-01 | Antibiotic | Penicillin | suppositories | pills         |

#penicillin penicillinPlus czy zupdatuje poprawnie