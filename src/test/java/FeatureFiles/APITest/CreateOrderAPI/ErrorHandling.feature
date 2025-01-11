Feature: Create order api functionality

  Background:
    Given The user has valid credentials create order error handling
    When The user sends a login API request create order error handling
    Then The user should receive a valid authentication token create order error handling
    When The user sends a request to get all products using the authentication token create order error handling
    Then The user should receive a list of product IDs create order error handling

  @ErrorHandlingAPI
  Scenario:Create order API with no body
    When User send request to create order with no body
    Then "Something Went Wrong" message is snet to user in create order response

  @ErrorHandlingAPI
  Scenario: Create order with wrong end point
    When User send request to create order with wrong end point
    Then User should receive "404" error code in create order response

  @ErrorHandlingAPI
  Scenario: Create order with no auth token
    When User send create order with no auth token
    Then "Access denied. No token provided." message is sent to user in create order response
