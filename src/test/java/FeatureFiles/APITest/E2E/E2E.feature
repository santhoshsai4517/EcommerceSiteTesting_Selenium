Feature: End to End

  @E2EAPI
  Scenario: User runs a e2e flow

    When User sends register request with all valid details
    Then "Registered Successfully" message is returned in register response
    When User sends forgot password request with all valid details
    Then "Password Changed Successfully" message is returned to user in forgot password api response
    When user sends login api request with valid details
    Then "Login Successfully" message is returned along with token and user ID
    When User sends get all products api request without any filters
    Then "All Products fetched Successfully" message is returned along with product list in get all products api response
    When User sends get product details request with valid product id
    Then "Product Details fetched Successfully" message is returned along with product details in get product by id api response
