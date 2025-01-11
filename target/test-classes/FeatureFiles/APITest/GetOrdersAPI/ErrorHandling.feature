Feature: Get orders error handling

  Background:
    Given The user has valid credentials get orders error handling
    When The user sends a login API request get orders error handling
    Then The user should receive a valid authentication token get orders error handling

  @ErrorHandlingAPI
  Scenario: User Sends get orders api request without user id
    When User sends get orders api request with no userID
    Then User should receive "404" error code get orders error handling

  @ErrorHandlingAPI
  Scenario: Retrieve orders details with wrong end point
    When User send request to get orders with wrong end point
    Then User should receive "404" error code get orders error handling

  @ErrorHandlingAPI
  Scenario: Retrieve orders details with no auth token
    When User send request to get orders with no auth token
    Then "Access denied. No token provided." message is sent to user in get orders api response

  @ErrorHandlingAPI
  Scenario: Retrieve orders with wrong user id
    When User send request to get orders api with wrong userId
    Then "No Data Found" error message is sent to user in get orders

