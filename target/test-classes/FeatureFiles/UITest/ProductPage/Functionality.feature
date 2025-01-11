Feature: Product page functionality

  @Functional
  Scenario: Logo button is working
    Given User lands on ECommerece page and product page
    When User clicks on logo button
    Then "AUTOMATION" logo message is displayed and products page is displayed

  @Functional
  Scenario: Cart button
    Given User lands on ECommerece page and product page
    When User clciked on cart button
    Then "My Cart" message is displayed and cart page

  @Functional
  Scenario: Home button
    Given User lands on ECommerece page and product page
    When User clciked on home button
    Then "AUTOMATION" logo message is displayed and products page is displayed

  @Functional
  Scenario: Orders button
    Given User lands on ECommerece page and product page
    When User clciked on orders button
    Then orders page is displayed to user

  @Functional
  Scenario: SignOut
    Given User lands on ECommerece page and product page
    When User clciked on signout button
    Then "Logout Successfully" message is displayed and login page

  @Functional
  Scenario: Continue shopping
    Given User lands on ECommerece page and product page
    When User clciked on continue shopping button
    Then "AUTOMATION" logo message is displayed and products page is displayed

  @Functional
  Scenario: Add to cart
    Given User lands on ECommerece page and product page
    When User clciked on add to cart button
    Then products are added to cart