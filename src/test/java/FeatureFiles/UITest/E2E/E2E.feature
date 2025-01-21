Feature: End to End

  @E2EUI
  Scenario: User runs a e2e flow from UI side

    Given User landed on ECommerce and navigates to register page
    When User provides all valid details in registration form and submits the form
    Then "Account Created Successfully" account created message is displayed
    When User clicks on login button
    Then User is redirected to login page
    When User Clicks on forgot password link
    Then User is redirected to forgot password page
    When User provides registered email and new password and clicks on submit button
    Then "Password Changed Successfully" message is displayed and user is redirected to login page
    When User provides email and updates password in login form and submits the form
    Then "Login Successfully" message is displayed and products are displayed
    When User clicks on view button on product
    Then Product details are displayed
    When User clicks on continue shopping button
    Then User is redirected to products page
    When User clicks on add to cart button on product
    Then Product is added to cart
    When User clicks on cart icon
    Then Cart page is displayed to user along with cart products
    When User clicks on delete button on cart product
    Then Product is deleted from cart
    When User clicks on checkout button on cart page
    Then User is redirected to checkout page
    When User provides all valid details in checkout form and submits the form
    Then "Order Placed Successfully" message is displayed and order details are displayed
    When User clicks on order history link
    Then User is redirected to order history page
    When User clicks on view order button on order history
    Then Order details are displayed
    When User clicks on delete button on order details
    Then order is deleted and "Orders Deleted Successfully" message is displayed
    When User clicks on logout button
    Then "Logout Successfully" message is displayed and login page is displayed to user

