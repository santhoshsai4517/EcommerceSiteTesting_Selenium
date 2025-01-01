Feature: Forgot Password API functionality

  @FunctionalAPI
  Scenario: Forgot Password API with correct details
    Given User provides correct details
    When User sends forgot password request
    Then "Password Changed Successfully" message is returned to user
