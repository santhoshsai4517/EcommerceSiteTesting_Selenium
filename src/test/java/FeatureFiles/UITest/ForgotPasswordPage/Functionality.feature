
Feature: Forgot Password Page functionality

  @Functional
  Scenario: Forgot Password Page functionality
    Given User landed on ECommerece page forgot password page
    When User clicks on register link
    Then "Register" register page is displayed
    
  @Functional
  Scenario: Forgot Password Page functionality
    Given User landed on ECommerece page forgot password page
    When User clicks on login link
    Then "We Make Your Shopping Simple" login page is displayed  
    
	@Functional
  Scenario Outline: Forgot Password Page functionality
    Given User landed on ECommerece page forgot password page
    When User provied <email> <password> and <confirmpassword> and submits the form
    Then "Password Changed Successfully" message is displayed and "We Make Your Shopping Simple" login page is displayed

    Examples: 
      |      email              | password         |  confirmpassword |
      | s1234@gmail.com  | 123456  | 123456 |	
