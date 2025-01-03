Feature: Register API functionality

  @FunctionalAPI
  Scenario: Register API by providing all valid details
    Given User provides correct detials
    When User sends register request
    Then "Registered Successfully" message is returned in register api response
