
Feature: Cart Page functionality

  @Functional
  Scenario: Home button
    Given User landed on ECommerece page cart page
    When User clicks on home button
    Then "AUTOMATION" message is displayed and Products page is displayed

  @Functional
  Scenario: Orders button
    Given User landed on ECommerece page cart page
    When User clicks on orders button
    Then Orders page is displayed

  @Functional
  Scenario: Signout button
    Given User landed on ECommerece page cart page
    When User clicks on signout button
    Then "Logout Successfully" is displayed and login page is displayed

  @Functional
  Scenario: Cart button
    Given User landed on ECommerece page cart page
    When User clicks on cart button
    Then "My Cart" message is displayed and cart page is displayed to user

  @Functional
  Scenario: Logo
    Given User landed on ECommerece page cart page
    When User clicks on logo
    Then "AUTOMATION" message is displayed and Products page is displayed

  @Functional
  Scenario: Continue shopping
    Given User landed on ECommerece page cart page
    When User clicks on continue shopping
    Then "AUTOMATION" message is displayed and Products page is displayed

  @Functional
  Scenario: Cart is cleared when user logs out
    Given User landed on ECommerece page cart page
    When User adds products to cart and clicks on logout
    Then "No Product in Your Cart" toast is displayed and "No Products in Your Cart !" message is displayed

  @Functional
  Scenario: Verify product details and total price
    Given User landed on ECommerece page cart page
    When User adds products to cart
    Then Validate product details and total price

  @Functional
  Scenario: Verify checkout button is working
    Given User landed on ECommerece page cart page
    When User adds products to cart and clicks on checkout
    Then Checkout page is displayed

  @Functional
  Scenario: Verify delete button is working
    Given User landed on ECommerece page cart page
    When User adds products to cart and clicks on delete
    Then product is removed from cart

  @Functional
  Scenario: Verify buy now button is working
    Given User landed on ECommerece page cart page
    When User adds products to cart and clicks on buy now