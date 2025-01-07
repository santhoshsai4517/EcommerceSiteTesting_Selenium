Feature: Fetch cart product details using API

  Background:
    Given user has valid credentials get cart product details
    When user sends a login API request get cart product details
    Then user should receive a valid authentication token get cart product details

  @ErrorHandlingAPI
  Scenario: No auth token is sent in get cart products api request
    When User does not send auth token in get cart products api request
    Then "Access denied. No token provided." message is returned in get cart products api response to user

  @ErrorHandlingAPI
  Scenario: User sends request to wrong end point
    When User sends get cart products to wrong endpoint
    Then "404" code is returned in get cart products api response to user

  @ErrorHandlingAPI
  Scenario: Retrieve cart products with wrong user id
    When User send request to get cart products with wrong userId
    Then "No Data Found" error message is sent to user in get cart products api response

  @ErrorHandlingAPI
  Scenario: Retrieve cart products with no user id
    When User send request to get cart products with no userId
    Then "404" code is returned in get cart products api response to user
