Feature: GetAllProducts API functionality

  @ErrorHandlingAPI
  Scenario: GetAllProducts API with incorrect end point
    Given User provides filter details
    When User sends request with wrong api end point
    Then "404" error is returned in response

  @ErrorHandlingAPI
  Scenario: GetAllProducts API with no authorization token
    Given User provides filter details without authorization token
    When User sends request with api end point
    Then "Access denied. No token provided." message is returned in response



