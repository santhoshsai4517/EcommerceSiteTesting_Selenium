Feature: Fetch product details using API

  Background:
    Given The user has valid credentials add to cart
    When The user sends a login API request add to cart
    Then The user should receive a valid authentication token add to cart
    When The user sends a request to get all products using the authentication token add to cart
    Then The user should receive a list of product IDs add to cart

  @FunctionalAPI
  Scenario: Add product to cart successfully
    When The user sends a request to add product to cart for each product ID using the authentication token
    Then "Product Added To Cart" message is returned in add to cart response
