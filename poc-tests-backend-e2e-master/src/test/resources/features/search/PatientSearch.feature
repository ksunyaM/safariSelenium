# In MVP Search service enables searching through patients by patient name, it will later be extended.
Feature: Testing Search service
  As a Doctor
  I want to be able to search patients
  So that I can check information about patients

  @StubForGetSearchPatient
  Scenario Outline: Response Header Data Should Be Correct
    When A Doctor log in to the main page
    Then Response Header Data for <tagName> Should Be <value>

    Examples:
      | tagName           | value                       |
      | Content-Type      | application/json            |
      | Content-Encoding  | gzip                        |
      | Vary              | Accept-Encoding, User-Agent |
      | Transfer-Encoding | chunked                     |

#WPP-89 As a Doctor I want to be able to search patients by name
#WPP-139 poc-search should request poc-patient for patients and asynchronously receive results ???
  @StubForGetSearchPatientWithParam
  Scenario: A Doctor wants to Search patient by name
    When A Doctor looks for patients by name beginning with Rob and type as a patient
    Then A Doctor receives all patients with name Rob, Rob*ert, Rob*erta, Rob*in

  @StubForGetSearchPatientInfo
  Scenario Outline: A Doctor got information about patient and check mandatory fields
    Given A Doctor gets information about patient
    When A Doctor receives information about patient(s)
    Then A Doctor checks data <tagName> and <value> are correct

    Examples:
      | tagName     | value                   |
      | id          | PA:123                  |
      | name        | Rob Brzeczyszczykiewicz |
      | dateOfBirth | 12.08.1880              |

  @StubForGetSearchPatientDoesNotExist
  Scenario: A Doctor searches for a name that does not produce any result
    When A Doctor looks for the patients by name Sherlok and type patient which does not exist in the system
    Then A Doctor receives blank results

    #WPP-140 poc-search should timeout request when poc-patient does not send the response with patients ??? connection issue
  @StubForGetSearchPatient
  Scenario: A Doctor wait more than 45 milliseconds to get info about patient
    When A Doctor looks for the patients by name
    Then A Doctor doesn't receive a response during 45 milliseconds from the server with the list of patients

  @StubForGetSearchPatient
  Scenario: Matches on Url Path And Multiple Query Parameters
    When: A Doctor looks a client with a specific parameters
    Then: A Doctor receives a response with these parameters

####### Real Data #######

  @RealDataJ
  Scenario: RD A Doctor wants to Search patient by name
    When RD a Doctor looks for patients by name beginning with Jan and type as a patient
    Then RD a Doctor receives all patients with name Jan, Jan*ert, Jan*erta, Jan*in

  @RealDataJ
  Scenario Outline: RD a Doctor got information about patient 'Jan Kowalski' and check mandatory fields
    Given RD a Doctor gets information about patient - Jan Kowalski
    When RD a Doctor receives information about patient(s)
    Then RD a Doctor checks data <tagName> and <value> are correct

    Examples:
      | tagName     | value        |
      | id          | PW:0         |
      | name        | Jan Kowalski |
      | dateOfBirth | 01.09.1990   |

  @RealDataJ
  Scenario: RD a Doctor searches for a name that does not produce any result
    When RD a Doctor looks for the patients by name Sherlok and type patient which does not exist in the system
    Then RD a Doctor receives blank results

   #Scenario: Empty value

  #Scenario: Numbers

  #Scenario: Date

  #Scenario: Name & Type pairwise

  #Scenario: SQL, JS injections

  #Scenario: Sorting/Ordering

  #Scenario: Boundary values

  #Scenario: Register Entry e.g.: Sherlok, SHERLOK, sherlok

  #Scenario: Different language

  #Scenario: Double name & surname
