Feature: Get Cart Count functionality

  Background:
    Given The user has valid credentials Get Cart Count
    When The user sends a login API request Get Cart Count
    Then The user should receive a valid authentication token Get Cart Count

  @FunctionalAPI
  Scenario: Retrieve cart count for user with no products in the cart
    When User send request to get cart count with userId
    Then User should receive "No Product in Cart" message
