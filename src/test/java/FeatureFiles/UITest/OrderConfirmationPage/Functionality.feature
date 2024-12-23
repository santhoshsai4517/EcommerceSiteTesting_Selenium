Feature: Order confirmation Page functionality

  @Functional
  Scenario: Home button
    Given User landed on ECommerece page order confirmation page
    When User clicks on home button in order confirmation page
    Then "AUTOMATION" message is displayed and user is redirected Products page

  @Functional
  Scenario: Orders button
    Given User landed on ECommerece page order confirmation page
    When User clicks on orders button in order confirmation page
    Then user is redirected to Orders page is displayed

  @Functional
  Scenario: Signout button
    Given User landed on ECommerece page order confirmation page
    When User clicks on signout button in order confirmation page
    Then "Logout Successfully" is displayed and user is redirected to login page

  @Functional
  Scenario: Cart button
    Given User landed on ECommerece page order confirmation page
    When User clicks on cart button in order confirmation page
    Then "My Cart" message is displayed and user is redirected to cart page

  @Functional
  Scenario: Logo
    Given User landed on ECommerece page order confirmation page
    When User clicks on logo in order confirmation page
    Then "AUTOMATION" message is displayed and user is redirected Products page

  @Functional
  Scenario: Orders link
    Given User landed on ECommerece page order confirmation page
    When User clicks on orders link in order confirmation page
    Then user is redirected to Orders page is displayed

  @Functional
  Scenario: Product details, order id, url
    Given User landed on ECommerece page order confirmation page
    Then Validate product details,order id,url

  @FunctionalFileDownload
  Scenario: Download buttons
    Given User landed on ECommerece page order confirmation page
    When User clicks on download buttons in order confirmation page