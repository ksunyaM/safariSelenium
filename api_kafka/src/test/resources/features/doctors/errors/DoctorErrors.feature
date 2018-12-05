# Errors
# When a request goes wrong, the server should provide a helpful response to the client, so that the client can understand and potentially fix the error.

Feature: The Administrator can check error handling for response
  As Administrator
  I want to check an error handling
  To be sure that the system works properly

  @StubForEmptyResponse
  Scenario: Request for an empty response
    When The Administrator requests for empty response
    Then The Administrator receives an exception: NoHttpResponseException

  @StubForRandomDataResponse
  Scenario: Request for a random response data
    When The Administrator requests for random-data response
    Then The Administrator receives an exception: ClientProtocolException

  @StubForTimeoutResponse
  Scenario: Request for a timeout response
    When The Administrator requests a response with timeout
    Then The Administrator receives an exception: SocketTimeoutException

  @StubForRetryResponse
  Scenario: The Service is retrying to get a response
    When The Administrator requests the Doctors and receives 503 for response
    Then The Administrator is retrying to get a response with code 200

  @StubForTimeoutPostResponse
  Scenario: Request to get a timeout for post response
    When The Administrator requests for post response with timeout
    Then The Administrator receives an exception: SocketTimeoutException

#Scenario: 400 Bad Request: Failure due to a client-side problem. For example, missing mandatory header, poorly formed JSON, or failed (business) validation like a negative number in a quantity field.

#Scenario: 500 Internal Server Error: Failure due to a server-side problem. For example, the server cannot connect to the database (often used in the a catch-all exception block).

#Scenario: In the response body, provide a description of the error to help the client. For example, “Mandatory field Name and Surname are missing”.

#Scenario: Return multiple errors in one response IF the application has lots of validations