Feature: Delete product api functionality

  Background:
    Given The user has valid credentials delete product
    When The user sends a login API request delete product
    Then The user should receive a valid authentication token delete product
    When User sends get products api request with valid userID delete product
    Then The user should receive order details get products delete product

  @FunctionalAPI
  Scenario: User sends product id to delete product
    When User sends valid product id to delete
    Then User receives "Product Deleted Successfully" message in delete product api response


