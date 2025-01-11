Feature: Delete order error handling

  Background:
    Given The user has valid credentials delete order error handling
    When The user sends a login API request delete order error handling
    Then The user should receive a valid authentication token delete order error handling
    When User sends get orders api request with valid userID delete order error handling
    Then The user should receive order details get orders and "Orders fetched for customer Successfully" message delete order error handling

  @ErrorHandlingAPI
  Scenario: User Sends delete order api request without order id
    When User sends delete order api request with no order id
    Then "404" error code is sent to user in delete order

  @ErrorHandlingAPI
  Scenario: User sends delete order api with wrong end point
    When User send request to delete order with wrong end point
    Then "404" error code is sent to user in delete order

  @ErrorHandlingAPI
  Scenario: user sends delete order api without auth token
    When User send request to delete order with no auth token
    Then "Access denied. No token provided." message is sent to user in delete order api response

  @ErrorHandlingAPI
  Scenario: User sends delete order request deleted order id
    When User send delte order request deleted order id
    Then "Order not found" error message is sent to user in delete order
