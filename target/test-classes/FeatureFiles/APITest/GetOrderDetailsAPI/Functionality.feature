Feature: Get order details functionality

  Background:
    Given The user has valid credentials get order details functionality
    When The user sends a login API request get order details functionality
    Then The user should receive a valid authentication token get order details functionality
    When The user sends a request to get all orders using the authentication token get order details functionality
    Then The user should receive a list of orders get order details functionality

  @FunctionalAPI
  Scenario: Get order details for all orders
    When The user sends a request to get order details for all orders
    Then "Orders fetched for customer Successfully" message is returned and same order is returned