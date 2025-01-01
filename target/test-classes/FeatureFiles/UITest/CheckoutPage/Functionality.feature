Feature: Checkout Page functionality

  @Functional
  Scenario: Home button
    Given User landed on ECommerece page checkout page
    When User clicks on home button in chekout page
    Then "AUTOMATION" message is displayed and Products page is displayed to user

  @Functional
  Scenario: Orders button
    Given User landed on ECommerece page checkout page
    When User clicks on orders button in chekout page
    Then Orders page is displayed to user

  @Functional
  Scenario: Signout button
    Given User landed on ECommerece page checkout page
    When User clicks on signout button in chekout page
    Then "Logout Successfully" is displayed and login page is displayed to user

  @Functional
  Scenario: Cart button
    Given User landed on ECommerece page checkout page
    When User clicks on cart button in chekout page
    Then "My Cart" message is displayed and cart page is displayed user

  @Functional
  Scenario: Logo
    Given User landed on ECommerece page checkout page
    When User clicks on logo in chekout page
    Then "AUTOMATION" message is displayed and Products page is displayed to user

  @Functional
  Scenario: Product details in chekout page
    Given User landed on ECommerece page checkout page
    Then Verify all product details

  @Functional
  Scenario: Product details in chekout page
    Given User landed on ECommerece page checkout page
    When User clicks on checkout button
    Then Verify all product details in checkout page

  @Functional
  Scenario: Completeing order
    Given User landed on ECommerece page checkout page
    When User fills country details and submits order
    Then Verify that order confirmation page is displayed