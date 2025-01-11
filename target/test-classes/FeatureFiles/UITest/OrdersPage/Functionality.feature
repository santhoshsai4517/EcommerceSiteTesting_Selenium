Feature: Order confirmation Page functionality

  @Functional
  Scenario: Home button
    Given User landed on ECommerece page orders page
    When User clicks on home button in orders page
    Then "AUTOMATION" message is displayed and user is redirected Products page from orders page

  @Functional
  Scenario: Orders button
    Given User landed on ECommerece page orders page
    When User clicks on orders button in orders page
    Then user is redirected to Orders page is displayed from orders page

  @Functional
  Scenario: Signout button
    Given User landed on ECommerece page orders page
    When User clicks on signout button in orders page
    Then "Logout Successfully" is displayed and user is redirected to login page from orders page

  @Functional
  Scenario: Cart button
    Given User landed on ECommerece page orders page
    When User clicks on cart button in orders page
    Then "My Cart" message is displayed and user is redirected to cart page from orders page

  @Functional
  Scenario: Logo
    Given User landed on ECommerece page orders page
    When User clicks on logo in orders page
    Then "AUTOMATION" message is displayed and user is redirected Products page from orders page

  @Functional
  Scenario: Go back to shopping
    Given User landed on ECommerece page orders page
    When User clicks on go back to shopping button in orders page
    Then "AUTOMATION" message is displayed and user is redirected Products page from orders page

  @Functional
  Scenario: Go back to cart
    Given User landed on ECommerece page orders page
    When User clicks on go back to cart button in orders page
    Then "My Cart" message is displayed and user is redirected to cart page from orders page

  @Functional
  Scenario: No orders
    Given User landed on ECommerece page orders page with no orders
    Then If no orders then "You have No Orders to show at this time." message is displayed

  @Functional
  Scenario: Orders are displayed
    Given User landed on ECommerece page orders page
    Then Orders are displayed

  @Functional
  Scenario: Delete order
    Given User landed on ECommerece page orders page
    When User clicks on delete button

  @Functional
  Scenario: View order
    Given User landed on ECommerece page orders page
    When User clicks on view button

