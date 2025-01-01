Feature: Order confirmation Page error handling

  @ErrorHandling
  Scenario: Orders api is failed
    Given User landed on ECommerece page orders page when api is down
    Then Error occurs

  @ErrorHandling
  Scenario: Delete api is failed
    Given User landed on ECommerece page orders page when api is down
    When User clicks on delete button when api is down
    Then "Unknown error occured" Error occurs

  @ErrorHandling
  Scenario: View api is failed
    Given User landed on ECommerece page orders page when api is down
    When User clicks on view button when api is down
    Then "You are not authorize to view this order" is displayed and "Unknown error occured" Error occurs



