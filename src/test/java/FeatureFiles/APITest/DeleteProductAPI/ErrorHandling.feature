Feature: Delete product error handling

  Background:
    Given The user has valid credentials delete product error handling
    When The user sends a login API request delete product error handling
    Then The user should receive a valid authentication token delete product error handling
    When User sends get products api request with valid userID delete product error handling
    Then The user should receive order details get products delete product error handling

  @ErrorHandlingAPI
  Scenario: User Sends delete product api request without product id
    When User sends delete product api request with no order id
    Then "404" error code is sent to user in delete product

  @ErrorHandlingAPI
  Scenario: User sends delete product api with wrong end point
    When User send request to delete product with wrong end point
    Then "404" error code is sent to user in delete product

  @ErrorHandlingAPI
  Scenario: user sends delete product api without auth token
    When User send request to delete product with no auth token
    Then "Access denied. No token provided." message is sent to user in delete product api response

  @ErrorHandlingAPI
  Scenario: User sends delete product request with deleted product
    When User send delete product request with deleted product id
    Then "Product not foundd" error message is sent to user in delete product
