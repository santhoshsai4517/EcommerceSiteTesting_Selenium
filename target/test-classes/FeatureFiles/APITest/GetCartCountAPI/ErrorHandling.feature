Feature: Get Cart Count error handling

  Background:
    Given The user has valid credentials Get Cart Count error handling
    When The user sends a login API request Get Cart Count error handling
    Then The user should receive a valid authentication token Get Cart Count error handling

  @ErrorHandlingAPI
  Scenario: Retrieve cart count with no user id
    When User send request to get cart count with no userId
    Then User should receive "404" error code

  @ErrorHandlingAPI
  Scenario: Retrieve cart count with wrong end point
    When User send request to get cart count with wrong end point
    Then User should receive "404" error code

  @ErrorHandlingAPI
  Scenario: Retrieve cart count with no auth token
    When User send request to get cart count with no auth token
    Then "Access denied. No token provided." message is sent to user

  @ErrorHandlingAPI
  Scenario: Retrieve cart count with wrong user id
    When User send request to get cart count with wrong userId
    Then "No Data Found" error message is sent to user
