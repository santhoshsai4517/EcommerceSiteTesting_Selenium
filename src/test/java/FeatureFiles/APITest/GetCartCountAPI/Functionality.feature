Feature: Get Cart Count functionality

  Background:
    Given The user has valid credentials Get Cart Count
    When The user sends a login API request Get Cart Count
    Then The user should receive a valid authentication token Get Cart Count
    When The user sends a request to get all products using the authentication token Get Cart Count
    Then The user should receive a list of product IDs Get Cart Count

  @FunctionalAPI
  Scenario: Retrieve cart count for user with no products in the cart
    When User send request to get cart count with userId
    Then User should receive "No Product in Cart" message

  @FunctionalAPI
  Scenario: Retrieve cart count for user with products in the cart
    When User adds 2 products to cart
    Then User should receive 2 product count in response
