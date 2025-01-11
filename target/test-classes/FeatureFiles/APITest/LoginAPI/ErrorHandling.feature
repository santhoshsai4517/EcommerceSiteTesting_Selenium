Feature: Login API error handling

  @ErrorHandlingAPI
  Scenario Outline: Login API with incorrect login credentials
    Given User provides <username> <password> login credentials
    When User sends request
    Then "Incorrect email or password." message is returned

    Examples:
      | username                  | password       |
      | santhoshsai4517@gmail.com | 151Fa04124@457 |


  @ErrorHandlingAPI
  Scenario Outline: Login API with incorrect end point
    Given User provides <username> <password> login credentials
    When User sends request with wrong end point
    Then "404" error is returned

    Examples:
      | username                  | password       |
      | santhoshsai4517@gmail.com | 151Fa04124@457 |

  @ErrorHandlingAPI
  Scenario Outline: Login API with no email
    Given User provides <username> <password> login credentials
    When User sends request
    Then "400" error is returned and "Email is required" message is returned in body

    Examples:
      | username | password       |
      |          | 151Fa04124@457 |

  @ErrorHandlingAPI
  Scenario Outline: Login API with no password
    Given User provides <username> <password> login credentials
    When User sends request
    Then "400" error is returned and "Password is required" message is returned in body

    Examples:
      | username                  | password |
      | santhoshsai4517@gmail.com |          |
