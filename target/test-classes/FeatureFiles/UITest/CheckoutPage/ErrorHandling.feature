Feature: Checkout Page functionality

  @ErrorHandling
  Scenario: No country details
    Given User landed on ECommerce page checkout page
    When User clicks on submit button without country details
    Then "Please Enter Full Shipping Information" message is displayed and order is not submitted

  @ErrorHandling
  Scenario: API is failed
    Given User landed on ECommerce page checkout page
    When User clicks on submit button and api is failed
    Then "Unknown error occured" unknwon message is displayed and order is not submitted
