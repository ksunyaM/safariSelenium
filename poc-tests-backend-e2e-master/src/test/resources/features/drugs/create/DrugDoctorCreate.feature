Feature: System allows the doctor/drug admin to find proper drugs
  As a doctor/drug admin
  I want to prescribe proper drugs for the patient
  so that I can see the drugs from the list

  @RealData
  Scenario Outline: A doctor select proper drugs with <dosage> and <form> from the list
    Given Doctor select a new prescription
    When Doctor fill in <drug> with <drugCode> where dosage form <dosageForm> and dosage <dosage> and doctor comment <comment>
    Then Drugs saved in prescriptions
    Examples:
      | drug       | drugCode        | dosageForm | dosage     | comment       |
      | Penicillin | NDC 077-3105-02 | pill       | 250 mg/3xd | Big pills     |
      | Aspirin    | NDC 777-1111-03 | liquid     | 500 mg/3xd | Yellow liquid |



