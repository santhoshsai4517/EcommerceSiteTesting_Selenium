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
    When User sends get cart count api request with user id
    Then User should receive "No Product in Cart" message in get cart count api response
    When User sends add product to cart request with valid product id
    Then "Product Added To Cart" message is returned in add to cart api response
    When User sends get cart count api request with user id with products in cart
    Then User should receive product count in response
    When User ends Get cart product details api request
    Then User should receive a list of product details get cart product details and "Cart Data Found" message in get cart details api response
    When user sends a request to add product with all valid details
    Then "Product Added Successfully" message is returned along with product id in add product response
    When User sends get product details request with new product id
    Then "Product Details fetched Successfully" message is returned along with product details in get product by id api response
    When User sends add product to cart request with new product id
    Then "Product Added To Cart" message is returned in add to cart api response
    When User ends Get cart product details api request
    When User sends create order api request with product details from cart
    Then "Order Placed Successfully" message is sent to user along with order ids in create order api response
    When User sends get orders api request with valid userID EtoE
    Then user should receive order details get orders and "Orders fetched for customer Successfully" message
    When User sends valid order id to delete in delete order api
    Then User receives "Orders Deleted Successfully" message in delete order api response EtoE
    When User sends valid product id to delete product api
    Then User receives "Product Deleted Successfully" message in delete product api response EtoE