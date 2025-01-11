Feature: Add product api functionality

  Background:
    Given The user has valid credentials add product
    When The user sends a login API request add product
    Then The user should receive a valid authentication token add product


  @FunctionalAPI
  Scenario: User provides all valid details
    When The user sends a request to add product with all valid details
    Then "Product Added Successfully" message is returned along with product id
