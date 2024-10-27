
Feature: Forgot Password Page error handling

  @ErrorHandling
  Scenario: Empty form
    Given User landed on ECommerece page and forgot password page
    When User submits empty form
    Then "*Email is required" "*Password is required" "*Confirm Password is required" Errors are displayed
    
  @ErrorHandling
  Scenario Outline: Invalid email
    Given User landed on ECommerece page and forgot password page
    When User provied invalid <email> <password> <confirmpassword> and submits the form
    Then "*Enter Valid Email" email error message is displayed
    Examples: 
      |      email              | password         |  confirmpassword |
      | s123  | 123456  | 123456 |	
      
      
  @ErrorHandling
  Scenario Outline: Password mismatch
    Given User landed on ECommerece page and forgot password page
    When User provied <email> and different passwords <password> <confirmpassword> and submits the form
    Then "Password and Confirm Password must match with each other." password error message is displayed
    Examples: 
      |      email              | password         |  confirmpassword |
      | s1234@gmail.com  | 123456  | 12345de6 |    
      
  @ErrorHandling
  Scenario Outline: User Not found
    Given User landed on ECommerece page and forgot password page
    When User provied not registered email <email> <password> <confirmpassword> and submits the form
    Then "User Not found." user error message is displayed
    Examples: 
      |      email              | password         |  confirmpassword |
      | s123vreveg4@gmail.com  | 123456  | 123456 | 
      
  @ErrorHandling
  Scenario Outline: API intercept failed
    Given User landed on ECommerece page and forgot password page
    When API is down and User provied <email> and <password> <confirmpassword>  and submits the form
    Then "Unknown error occured" api error message is displayed
    Examples: 
      |      email              | password         |  confirmpassword |
      | s1234@gmail.com  | 123456  | 123456 |
      
  @ErrorHandling
  Scenario Outline: API Payload changedInvalid email 
    Given User landed on ECommerece page and forgot password page
    When User provied correct <email> <password> <confirmpassword> and submits the form
    Examples: 
      |      email              | password         |  confirmpassword |
      | s1234@gmail.com  | 123456  | 123456 |
   