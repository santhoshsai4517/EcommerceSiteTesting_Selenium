Feature: Register API error handling

  @ErrorHandlingAPI
  Scenario Outline:User provided invalid email address
    Given User provides <email> invalid email address
    When User sends register api request
    Then <error> error is returned in register api response

    Examples:

      | email | error              |
      |       | Email is required! |
      | 123   | Enter Valid Email! |

  @ErrorHandlingAPI
  Scenario:User provided invalid email address
    Given User provides invalid email address
    When User sends api request
    Then Error "User already exisits with this Email Id!"  is returned in register api response


  @ErrorHandlingAPI
  Scenario Outline:User provided invalid phone number
    Given User provides <mobile> invalid phone number
    When User sends register api request
    Then <error> error is returned in register api response

    Examples:

      | mobile     | error                               |
      |            | Phone Number is required!           |
      | 123456d9vg | Phone Number can be only in digit   |
      | 123        | Phone Number must be 10 Digit long! |
      | 123123425  | Phone Number must be 10 Digit long! |

  @ErrorHandlingAPI
  Scenario Outline:User provided invalid password
    Given User provides <password> invalid password
    When User sends register api request
    Then <error> error is returned in register api response

    Examples:

      | password   | error                                                          |
      |            | Password is required!                                          |
      | 12345678   | Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small |
      | 123        | Password must be 8 Character Long!                             |
      | 12345678a  | Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small |
      | 12345678A  | Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small |
      | 12345678_  | Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small |
      | 12345678a_ | Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small |
      | 12345678A_ | Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small |

  @ErrorHandlingAPI
  Scenario Outline:User provided some empty information
    Given User provides <field> as empty
    When User sends register api request
    Then <error> error is returned in register api response

    Examples:

      | field | error                   |
      | fn    | First Name is required! |
      | ln    | Last Name is required!  |

  @ErrorHandlingAPI
  Scenario: Register API with incorrect end point
    Given User provides all correct details
    When User sends request with wrong register api end point
    Then "404" error is returned in register api reesponse
