
Feature: Cart Page functionality

  @Functional
  Scenario: Home button
    Given User landed on ECommerece page cart page
    When User clicks on home button
    Then "Automation" message is displayed and Products page is displayed
