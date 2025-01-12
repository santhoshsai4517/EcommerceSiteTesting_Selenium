Feature: Add product error handling

  Background:
    Given The user has valid credentials add product error handling
    When The user sends a login API request add product error handling
    Then The user should receive a valid authentication token add product error handling

  @ErrorHandlingAPI
  Scenario: Add product with no image
    When User send request to add product with no image
    Then "Something Went Wrong" is returned to user in add product response

  @ErrorHandlingAPI
  Scenario: Add product with no body
    When User send request to add product with no body
    Then "Something Went Wrong" is returned to user in add product response

  @ErrorHandlingAPI
  Scenario: Add product with wrong end point
    When User send request to add product with wrong end point
    Then User should receive "404" error code in Add product response

  @ErrorHandlingAPI
  Scenario: Add product with no auth token
    When User send request to add product with no auth token
    Then "Access denied. No token provided." message is sent to user in add product response

  @ErrorHandlingAPI
  Scenario Outline: Add product with no details
    When User send request to add products with no details <field>
    Then <message> message is sent to user in add products

    Examples:
      | field              | message                                                                                                                                                                                                                                                             |
      | productName        | Product validation failed: productName: Product Name is required                                                                                                                                                                                                    |
      | productCategory    | Product validation failed: productCategory: Product Category is required                                                                                                                                                                                            |
      | productSubCategory | Product validation failed: productSubCategory: Product Sub Category is required                                                                                                                                                                                     |
      | productPrice       | Product validation failed: productPrice: Product Price is required                                                                                                                                                                                                  |
      | productDescription | Product validation failed: productDescription: Product Description is required                                                                                                                                                                                      |
      | all                | Product validation failed: productName: Product Name is required, productCategory: Product Category is required, productSubCategory: Product Sub Category is required, productPrice: Product Price is required, productDescription: Product Description is required |