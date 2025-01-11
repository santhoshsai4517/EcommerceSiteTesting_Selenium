Feature: Fetch cart product details using API

  Background:
    Given The user has valid credentials get cart product details
    When The user sends a login API request get cart product details
    Then The user should receive a valid authentication token get cart product details
    When The user sends a request to get all products using the authentication token get cart product details
    Then The user should receive a list of product IDs get cart product details

  @FunctionalAPI
  Scenario: Retrieve products in cart for user with no cart products
    When The user sends a request to get cart products
    Then "No Product in Cart" message is returned in api response

  @FunctionalAPI
  Scenario: Retrieve products in cart for user with cart products
    When User adds products to cart using add to cart api
    When Get cart product details api response contains same products
    Then User should receive a list of product details get cart product details and "Cart Data Found" message