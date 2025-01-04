Feature: Fetch product details using API

  Background:
    Given The user has valid credentials
    When The user sends a login API request
    Then The user should receive a valid authentication token
    When The user sends a request to get all products using the authentication token
    Then The user should receive a list of product IDs

  @FunctionalAPI
  Scenario: Retrieve details for each product
    When The user sends a request to get product details for each product ID using the authentication token and "Product Details fetched Successfully" message is returned in response
