Feature: Order Page functionality

  @Functional
  Scenario: Home button
    Given User landed on ECommerece page order page
    When User clicks on home button in order page
    Then "AUTOMATION" message is displayed and user is redirected Products page from order page

  @Functional
  Scenario: Orders button
    Given User landed on ECommerece page order page
    When User clicks on orders button in order page
    Then user is redirected to Orders page is displayed from order page

  @Functional
  Scenario: Signout button
    Given User landed on ECommerece page order page
    When User clicks on signout button in order page
    Then "Logout Successfully" is displayed and user is redirected to login page from order page

  @Functional
  Scenario: Cart button
    Given User landed on ECommerece page order page
    When User clicks on cart button in order page
    Then "My Cart" message is displayed and user is redirected to cart page from order page

  @Functional
  Scenario: Logo
    Given User landed on ECommerece page order page
    When User clicks on logo in order page
    Then "AUTOMATION" message is displayed and user is redirected Products page from order page



