Feature: Create order api functionality

  Background:
    Given The user has valid credentials create order
    When The user sends a login API request create order
    Then The user should receive a valid authentication token create order
    When The user sends a request to get all products using the authentication token create order
    Then The user should receive a list of product IDs create order

  @FunctionalAPI
  Scenario: User sends create order api with product details
    When User sends create order api request with product details
    Then "Order Placed Successfully" message is sent along with order ids in create order api response