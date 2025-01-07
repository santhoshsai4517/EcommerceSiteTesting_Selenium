Feature: Add to cart error handling

  Background:
    Given The user has valid credentials add to cart error handling
    When The user sends a login API request add to cart error handling
    Then The user should receive a valid authentication token add to cart error handling
    When The user sends a request to get all products using the authentication token add to cart error handling
    Then The user should receive a list of product IDs add to cart error handling

  @ErrorHandlingAPI
  Scenario: Add to cart with no user id
    When User send request to add to cart with no userId
    Then User should receive "400" error code and "Not Authorized!" error message

  @ErrorHandlingAPI
  Scenario: Add to cart with wrong end point
    When User send request to add to cart with wrong end point
    Then User should receive "404" error code in Add to cart response

  @ErrorHandlingAPI
  Scenario: Add to cart with no auth token
    When User send request to add to cart with no auth token
    Then "Access denied. No token provided." message is sent to user in add to cart response

  @ErrorHandlingAPI
  Scenario: Add to cart with wrong user id
    When User send request to add to cart with wrong userId
    Then User should receive "400" error code and "Not Authorized!" error message
