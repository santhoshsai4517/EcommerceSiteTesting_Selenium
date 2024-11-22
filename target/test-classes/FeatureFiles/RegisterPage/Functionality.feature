Feature: Register page functionality

  @Functional
  Scenario: User clicks on login page link in register page
    Given User landed on ECommerece register page
    When User click on login link
    Then "We Make Your Shopping Simple" login message is displayed

  @Functional
  Scenario Outline: User provides all correct details and user is registered successfully
    Given User landed on ECommerece register page
    When User provied <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and submits form with correct details
    Then "Account Created Successfully" accoun created message is displayed

    Examples:
      | fName    | lName | email                    | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | qewgrtheoffgrf@gmail.com | 1234567890 | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 | true     |
      
