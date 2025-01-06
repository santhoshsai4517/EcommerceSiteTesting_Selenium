Feature: GetAllProducts API functionality

  Background: Token is generated using login api
    Given User provides login information
    When User sends login api request
    Then User gets back a token in api response

  @FunctionalAPI
  Scenario Outline: GetAllProducts API with filters
    Given User provides <Name> <min> <max> <cat> <subcat> <for> filter details to get products
    When User sends get all products api request
    Then "All Products fetched Successfully" message is returned in get all products api response

    Examples:

      | Name   | min   | max     | cat                           | subcat        | for |
      |        |       |         |                               |               |     |
      | ADIDAS |       |         |                               |               |     |
      |        | 50000 |         |                               |               |     |
      |        |       | 50000   |                               |               |     |
      |        |       |         | fashion,electronics           |               |     |
      |        |       |         |                               | shoes,mobiles |     |
      |        |       |         |                               |               | men |
      | ADIDAS | 10000 | 3000000 | fashion,electronics,household | shoes,mobiles | men |

  @FunctionalAPI
  Scenario: GetAllProducts API with filters and no products
    Given User provides tshirt filter
    When User sends get products api request
    Then "No Products Found" message is returned wiht no products



