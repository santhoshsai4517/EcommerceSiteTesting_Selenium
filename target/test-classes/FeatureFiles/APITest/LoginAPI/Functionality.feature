Feature: Login API functionality

  @FunctionalAPI
  Scenario: Login API with correct login credentials
    Given User provides correct login credentials
    When User sends login request
    Then "Login Successfully" message is returned along with token
