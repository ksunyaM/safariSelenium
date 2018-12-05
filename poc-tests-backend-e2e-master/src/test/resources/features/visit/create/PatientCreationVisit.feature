Feature: System allows particular patient to visit the available doctor
  As a system,
  I allow creating a new visit,
  so that new visit will be saved in the database.

  @StubForVisitService
  Scenario: Patient creates a new visit
    Given Patient creates a new account and sees form to fill in to set up a new visit
    When Patient fill in form to set up a new visit
    Then New visit is created

##  @Ignore
#  Scenario Outline: Patient has own account and creates new visit to available doctor
#    Given Patient logged to his account and fill in form to set up the visit scope
#    When Patient chooses available date and doctors propositions appear according to place of <location>
#    Then Patient selects date and available doctor and confirm the visit date
#
#    Examples:
#      | location      |
#      | Krakow        |
#      | Warsaw        |
#      | Ciechocinek   |
#      | Krynica Zdroj |


#  @Ignore
  @FakeData
  Scenario Outline: Patient hasn't/has own account and creates new visit to available doctor
    Given Patient <accountOwnership> and fill in form to set up the visit scope
    When Patient chooses available date and doctors propositions appear according to place of <location>
    Then Patient selects date and available doctor and confirm the visit date

    Examples:
      | location      | accountOwnership          |
      | Krakow        | create account and log in |
      | Warsaw        | log in to account         |
      | Ciechocinek   |                           |
      | Krynica Zdroj |                           |

  @Ignore
  Scenario Outline: Patient doesn't fill in each form field
    Given Patient creates a new account
    When Patient try to leave empty fields <field>
    Then Patient cannot confirm his form

    Examples:
      | field               |
      | Name                |
      | Last Name           |
      | Date of birth       |
      | Identity number     |
      | Disease description |

