Feature: GetAllProducts API functionality

  Background: Token is generated using login api
    Given User provides login information
    When User sends login api request
    Then User gets back a token in api response

  @FunctionalAPI
  Scenario: GetAllProducts API with no filters
    Given User provides no filter details
    When User sends get all products api reques
    Then "All Products fetched Successfully" message is returned in getall products api response
