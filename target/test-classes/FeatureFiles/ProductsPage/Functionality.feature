
Feature: Products page functionality

  @Functional
  Scenario: Products page functionality
    Given I landed on ECommerece page products page 
    Then "AUTOMATION" logo message is displayed
    
	@Functional
  Scenario: Cart button
    Given I landed on ECommerece page products page
    When User clciks on cart button 
    Then "My Cart" message is displayed and cart page is displayed	
      
	@Functional
  Scenario: Home button
    Given I landed on ECommerece page products page
    When User clciks on home button 
    Then "AUTOMATION" message is displayed and products page is displayed	
    
	@Functional
  Scenario: Orders button
    Given I landed on ECommerece page products page
    When User clciks on orders button 
    Then orders page is displayed

	@Functional
  Scenario: SignOut
    Given I landed on ECommerece page products page
    When User clciks on signout button 
    Then "Logout Successfully" message is displayed and login page is displayed
    
	@Functional
	 Scenario: Add products to cart
    Given I landed on ECommerece page products page
    When User clciks on add cart button on a product 
    Then "Product Added To Cart" message is displayed and product is added to cart
   
 	@Functional
	 Scenario: Search filter
    Given I landed on ECommerece page products page
    When User searches for a product
    
  @Functional
	 Scenario: Min max price filter
    Given I landed on ECommerece page products page
    When User filters using min max price
 
   @Functional
	 Scenario: Categories filter
    Given I landed on ECommerece page products page
    When User filters using categories
    
 