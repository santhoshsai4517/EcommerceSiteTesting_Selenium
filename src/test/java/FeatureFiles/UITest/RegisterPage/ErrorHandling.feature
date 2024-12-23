
Feature: Register page error handling

  @ErrorHandling
  Scenario: User submits empty registration form
    Given User landed on ECommerece registration page
    When User submits empty registration form
    Then "*First Name is required" "*Email is required" "*Phone Number is required" "*Password is required" "Confirm Password is required" "*Please check above checkbox" message is displayed

  @ErrorHandling
  Scenario Outline: User provides inalid email and invalid email error is displayed
    Given User landed on ECommerece registration page
    When Incorrect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs
    Then "*Enter Valid Email" invalid email error message is displayed

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | vteg | 1234567890 | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 |	true		 |

  @ErrorHandling
  Scenario Outline: User provides mismatch password and password mismatch error is displayed
    Given User landed on ECommerece registration page
    When Incorrect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs
    Then "Password and Confirm Password must match with each other." password mismatch error message is displayed

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | santhoshsai4517@gmail.com | 1234567890 | Student    | Male   | 151Fa0124@4517 | 151Fa04124@4517 |	true		 |
      
  @ErrorHandling
  Scenario Outline: User provides mobile number with chars and mobile error is displayed
    Given User landed on ECommerece registration page
    When Incorrect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs
    Then "*only numbers is allowed" mobile error message is displayed

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | santhoshsai4517@gmail.com | 123456789w | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 |	true		 |
      
	@ErrorHandling
  Scenario Outline: User provides mobile number with less than 10 digits and mobile error is displayed
    Given User landed on ECommerece registration page
    When Incorrect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs
    Then "*Phone Number must be 10 digit" 1o digit mobile error message is displayed

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | santhoshsai4517@gmail.com | 123456789 | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 |	true		 |
      
  @ErrorHandling
  Scenario Outline: User provides registered email and registerd user is displayed
    Given User landed on ECommerece registration page
    When Incorrect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs
    Then "User already exisits with this Email Id!" USER EXIST error message is displayed

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | santhoshsai4517@gmail.com | 1234567890 | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 |	true		 |
      
      
	@ErrorHandling
  Scenario Outline: User provides password length less than 8 and passowrd error is displayed
    Given User landed on ECommerece registration page
    When Incorrect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs
    Then <error> password length error message is displayed

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck | error |
      | santhosh | sai   | sarteljnsai4517@gmail.com | 1234567890 | Student    | Male   | 123456789 | 123456789 |	true		 |  Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small | 
      | santhosh | sai   | sarteljnsai4517@gmail.com | 1234567890 | Student    | Male   | 123 | 123 |	true		 |  Password must be 8 Character Long! |
      
	@ErrorHandling
  Scenario Outline: User provides correct details and api is failed and error is displayed
    Given User landed on ECommerece registration page
    When API Intercepted and failed currect details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs


    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | sarteljnsai4517@gmail.com | 1234567890 | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 |	true		 | 
      
	@ErrorHandling
  Scenario Outline: User provides registered email and registerd user is displayed
    Given User landed on ECommerece registration page
    When API is intercepted and payload is modified and correct details <fName> <lName> <email> <mobile> <occupation> <gender> <password> <confirmPassword> <ageCheck> and error occurs

    Examples: 
      | fName    | lName | email           | mobile     | occupation | gender | password        | confirmPassword | ageCheck |
      | santhosh | sai   | santhoshcergsai4517@gmail.com | 1234567890 | Student    | Male   | 151Fa04124@4517 | 151Fa04124@4517 |	true		 |
              