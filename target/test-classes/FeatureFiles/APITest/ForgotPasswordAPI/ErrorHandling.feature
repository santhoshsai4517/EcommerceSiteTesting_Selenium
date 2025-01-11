Feature: Forgot Password API error handling

  @ErrorHandlingAPI
  Scenario Outline: Forgot Password API with incorrect details
    Given User provides <username> <password> <confirmpassword> details
    When User sends forgot password api request
    Then <message> message  and <code> error code is returned

    Examples:
      | username                  | password | confirmpassword | message              | code |
      | sfoi4hg9ufiorhgmail.com   | 123      | 123             | User Not found.      | 404  |
      | santhoshsai4517@gmail.com |          |                 | Password is required | 400  |


  @ErrorHandlingAPI
  Scenario Outline: Forgot Password API with wrong end point
    Given User provides <username> <password> <confirmpassword> details
    When User sends request to wrong end point
    Then "404" error code is returned in response

    Examples:
      | username                | password | confirmpassword |
      | sfoi4hg9ufiorhgmail.com | 123      | 123             |
