
Feature: Products page error handling

  @ErrorHandling
  Scenario: Products page error handling no products are displayed due to api failure
    Given I landed on ECommerece page products page after login
    Then No products are displayed
    
  @ErrorHandling
  Scenario: Products page error handling no products are displayed due to filters
    Given I landed on ECommerece page products page after login
    When User applies tshirt filter
    Then "No Products Found" error toast message is displayed
    
  @ErrorHandling
  Scenario: Products page error handling add to cart api is failed
    Given I landed on ECommerece page products page after login
    When User clicks on add to cart button
    Then Product is not added to cart
   