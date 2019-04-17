
Feature: Calculate current day of supply

  @auto
  Scenario Outline: Day of supply validation positive
    When user sends API call "taleh" to "test" with data
      |dispensingQuantity   | <num1> |
      |lastDispensedQuantity| <num2> |
      |lastDaysSupply       | <num3> |
    Then the response status code is "200"
    And verify that "taleh.calculatedDaysSupply == <result>"
    Examples:
    |num1     |num2       |num3       |result   |
    |10       |deleted::  |10         |0        |
    |10       |10         | deleted:: |0        |
    |10       |deleted::  | deleted:: |0        |
    |deleted::|deleted::  | 10        |0        |
    |10       |10         |10         |10       |
    |50       |10         |20         |100      |
    |15       |4          |3          |11.25    |
