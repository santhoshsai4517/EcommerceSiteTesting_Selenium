Feature: Fetch product details using API

  Background:
    Given user has valid credentials
    When user sends a login API request
    Then user should receive a valid authentication token
    When user sends a request to get all products using the authentication token
    Then user should receive a list of product IDs

  @ErrorHandlingAPI
  Scenario: No auth token is sent in api request
    When User does not send auth token in api request
    Then "Access denied. No token provided." message is returned in response to user

  @ErrorHandlingAPI
  Scenario: No product ID is sent in api request
    When User does not send productID in api request
    Then "404" code is returned in response to user

  @ErrorHandlingAPI
  Scenario: User sends product id that does not exists
    When User sends product id that does not exists
    Then "Product not found" error message is returned in response to user

  @ErrorHandlingAPI
  Scenario: User sends request to wrong end point
    When User sends product id to wrong endpoint
    Then "404" code is returned in response to user
