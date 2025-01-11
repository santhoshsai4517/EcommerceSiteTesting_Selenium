Feature: Delete order api functionality

  Background:
    Given The user has valid credentials delete order
    When The user sends a login API request delete order
    Then The user should receive a valid authentication token delete order
    When User sends get orders api request with valid userID delete order
    Then The user should receive order details get orders and "Orders fetched for customer Successfully" message delete order

  @FunctionalAPI
  Scenario: User sends order id to delete order
    When User sends valid order id to delete
    Then User receives "Orders Deleted Successfully" message in delete order api response


