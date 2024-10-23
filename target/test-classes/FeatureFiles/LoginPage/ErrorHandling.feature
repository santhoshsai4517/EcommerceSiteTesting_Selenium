
Feature: Login error handling
    
@ErrorHandling
  Scenario Outline: Login page functionality
    Given I landed on ECommerece page and logging in 
    When Logged in with wrong username <username> and password <password>
    Then "Incorrect email or password." error message is displayed

    Examples: 
      |      username              | password         |
      | santhoshsai4517@gmail.com  | 151Fa04124@457   |
      
