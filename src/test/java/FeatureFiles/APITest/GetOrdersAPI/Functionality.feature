Feature: Fetch orders details using API

  Background:
    Given The user has valid credentials get orders
    When The user sends a login API request get orders
    Then The user should receive a valid authentication token get orders

  @FunctionalAPI
  Scenario: Retrieve order details for user
    When User sends get orders api request with valid userID
    Then The user should receive order details get orders and "Orders fetched for customer Successfully" message

  @FunctionalAPI
  Scenario: Retrieve order details for user with no orders
    When User with no orders sends get orders api request with valid userID
    Then The user should receive no order details get orders and "No Orders" message


