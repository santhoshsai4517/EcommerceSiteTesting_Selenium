Feature: Login functionality

  @Functional
  Scenario: Login page functionality
    Given I landed on ECommerece page
    Then "We Make Your Shopping Simple" message is displayed

  @Functional
  Scenario: Login page functionality
    Given I landed on ECommerece page
    When I Click on Register button
    Then Page is redirected to register page and  "Register" text is displayed

  @Functional
  Scenario: Login page functionality
    Given I landed on ECommerece page
    When I Click on Register link
    Then Page is redirected to register page and  "Register" text is displayed

  @Functional
  Scenario: Login page functionality
    Given I landed on ECommerece page
    When I Click on Forgot password link
    Then Page is redirected to forgot password page and  "Enter New Password" text is displayed

  @Functional
  Scenario Outline: Login page functionality
    Given I landed on ECommerece page
    When Logged in with username <username> and password <password>
    Then "Login Successfully" message is displayed and "AUTOMATION" title is visible

    Examples:
      | username                  | password        |
      | santhoshsai4517@gmail.com | 151Fa04124@4517 |
      
