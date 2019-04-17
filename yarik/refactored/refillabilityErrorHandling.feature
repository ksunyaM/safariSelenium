@featureid=RXRPRP-3848 @capabilityid=RXRPRP-3848 @jiraid=RXRPD-54373 @jiraid=RXRPD-36867 @domain=RxProcessing @api @TeamStark
Feature: Refillability Error Handling

  @acceptance @jiraid=RXRPD-55027 @auto
  Scenario: refillabilityExternalErrorHandling - Product Mock fails
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct11 |
      | statusCode            | 502               |
    Then wait for "10" seconds
    When user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode | autoTestProduct11 |
    Then the response status code is "200"
    And verify that "c.refillable = false"
    And verify that "c.nonRefillableReason = INFRASTRUCTURE_ISSUE"
    And verify the following messages: "Error in WebApplicationException HTTP 502 Bad Gateway" in the application log

  @acceptance @jiraid=RXRPD-55028 @auto
  Scenario: refillabilityExternalErrorHandling - Location Mock fails
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Location Mock is created with params
      | locationNumber | 59522    |
      | locationType   | Pharmacy |
      | statusCode     | 502      |
    And Location Mock is created with params
      | locationNumber | 59523    |
      | locationType   | Pharmacy |
      | statusCode     | 503      |
    Then wait for "10" seconds
    When user sends API call "externalRefill" to "rxrefillability_ExternalAPI" with data
      | externalPrescription.lastDispense.actualProductPackCode   | autoTestProduct |
      | dispensingLocation.locationType                           | 59522           |
      | externalPrescription.lastDispense.location.locationNumber | 59523           |
    Then the response status code is "200"
    And verify that "c.refillable = false"
    And verify that "c.nonRefillableReason = INFRASTRUCTURE_ISSUE"
    And verify the following messages: "Error in WebApplicationException HTTP 503 Service Unavailable" in the application log

  @acceptance @jiraid=RXRPD-55024 @auto
  Scenario: refillabilityInternalErrorHandling - Product Mock fails
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProductMockFails |
      | statusCode            | 502                      |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                                    | 2050-01-01               |
      | spec_location_number                                         | 59511                    |
      | location_number                                              | 59511                    |
      | spec_prescriber_location_code                                | 59511                    |
      | precriberCode                                                | autoTestPrescriber       |
      | spec_prescribed_actual_product_pack_code                     | autoTestProductMockFails |
      | disp_by_prescription_code[0].actual_product_pack_code        | autoTestProductMockFails |
      | disp_by_prescription_code[0].coupon_actual_product_pack_code | autoTestProductMockFails |
      | spec_prescriber_code                                         | autoTestPrescriber       |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | INFRASTRUCTURE_ISSUE |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                |
    And verify the following messages: "Error in WebApplicationException HTTP 502 Bad Gateway" in the application log

  @acceptance @jiraid=RXRPD-55025 @auto
  Scenario: refillabilityInternalErrorHandling - Prescriber Mock fails
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code       | aut0PrescriberMockFails |
      | statusCode | 503                     |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                                    | 2050-01-01              |
      | spec_location_number                                         | 59511                   |
      | location_number                                              | 59511                   |
      | spec_prescriber_location_code                                | 59511                   |
      | precriberCode                                                | aut0PrescriberMockFails |
      | spec_prescribed_actual_product_pack_code                     | autoTestProduct         |
      | disp_by_prescription_code[0].actual_product_pack_code        | autoTestProduct         |
      | disp_by_prescription_code[0].coupon_actual_product_pack_code | autoTestProduct         |
      | spec_prescriber_code                                         | aut0PrescriberMockFails |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | INFRASTRUCTURE_ISSUE |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                |
    And verify the following messages: "Error in WebApplicationException HTTP 503 Service Unavailable" in the application log

  @acceptance @jiraid=RXRPD-55026 @auto
  Scenario: refillabilityInternalErrorHandling - Location Mock fails
    Given Product Mock is created with params
      | actualProductPackCode | autoTestProduct |
    And Prescriber Mock is created with params
      | code | autoTestPrescriber |
    And Location Mock is created with params
      | locationNumber | 59522    |
      | locationType   | Pharmacy |
      | statusCode     | 502      |
    And Location Mock is created with params
      | locationNumber | 59523    |
      | locationType   | Pharmacy |
      | statusCode     | 503      |
    Then wait for "10" seconds
    Given a prescription with "1" dispenses in RXPData database
      | spec_fill_expiration_date                                    | 2050-01-01         |
      | spec_location_number                                         | 59522              |
      | location_number                                              | 59522              |
      | spec_prescriber_location_code                                | 59522              |
      | precriberCode                                                | autoTestPrescriber |
      | spec_prescribed_actual_product_pack_code                     | autoTestProduct    |
      | disp_by_prescription_code[0].actual_product_pack_code        | autoTestProduct    |
      | disp_by_prescription_code[0].coupon_actual_product_pack_code | autoTestProduct    |
      | spec_prescriber_code                                         | autoTestPrescriber |
    When user sends API call "refillabilityInternal" to "refillability-internal" with data
      | internalRxRefillabilityRequestList[0].prescriptionCode | ~prescription_code |
      | internalRxRefillabilityRequestList[0].lastDispenseCode | ~dispenseCode0     |
      | dispensingLocation.locationNumber                      | 59523              |
    Then the response status code is "200"
    And Consumed Event "refillabilityInternal" has values
      | c.internalRxRefillabilityResponseList[0].nonRefillableReason | INFRASTRUCTURE_ISSUE |
      | c.internalRxRefillabilityResponseList[0].refillable          | false                |
#    And verify the following messages: "Error in WebApplicationException HTTP 503 Service Unavailable" in the application log