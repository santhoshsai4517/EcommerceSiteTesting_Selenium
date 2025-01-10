Feature: Get order details error handling

  Background:
    Given The user has valid credentials get order details error handling
    When The user sends a login API request get order details error handling
    Then The user should receive a valid authentication token get order details error handling
    When The user sends a request to get all orders using the authentication token get order details error handling
    Then The user should receive a list of orders get order details error handling

  @ErrorHandlingAPI
  Scenario: User Sends get order details api request without order id
    When User sends get order details api request with no order id
    Then "Order not found" error message is sent to user in get order details

  @ErrorHandlingAPI
  Scenario: Retrieve order details with wrong end point
    When User send request to get order details with wrong end point
    Then User should receive "404" error code get order details error handling

  @ErrorHandlingAPI
  Scenario: Retrieve order details with no auth token
    When User send request to get order details with no auth token
    Then "Access denied. No token provided." message is sent to user in get order details api response

