@ignore
Feature: Perform CRUD operations on Item
  I should be able to create, update and remove Item

  Background:
    Given database is empty

  Scenario Outline: Create item
    Given There is Artist "<artistName>"
    And there is Publisher "<publisherName>"
    When I create item with title "<title>" and description "<description>" and price <price> and Medium "<medium>"
    Then http response code is 201
    And response body should contain title "<title>"
    And response body should contain description "<description>"
    And response body should contain price <price>
    And response body should contain Artist name "<artistName>"
    And response body should contain Publisher name "<publisherName>"
    Examples:
      | title                           | description   | price | medium | artistName              | publisherName |
      | The Velvet Underground and Nico | 1967          | 500   | CD     |  The Velvet Underground | Verve         |
      | White Light White Heat          | 1968          | 400   | CD     |  The Velvet Underground | Verve         |
      | The Velvet Underground          | 1969          | 300   | CD     |  The Velvet Underground | Verve         |
      | Loaded                          | 1970          | 200   | CD     |  The Velvet Underground | Verve         |
      | Squeeze                         | 1973          | 100   | CD     |  The Velvet Underground | Verve         |

  Scenario Outline: Read item
    When I get item with title "<title>"
    Then http response code is 200
    And response body should contain title "<title>"
    And response body should contain description "<description>"
    And response body should contain price <price>
    And response body should contain Artist name "<artistName>"
    And response body should contain Publisher name "<publisherName>"
    Examples:
      | title                           | description   | price | artistName             | publisherName |
      | White Light/White Heat          | 1968          | 400   | The Velvet Underground | Verve         |
      | The Velvet Underground          | 1969          | 300   | The Velvet Underground | Verve         |
      | Loaded                          | 1970          | 200   | The Velvet Underground | Verve         |
      | Squeeze                         | 1973          | 100   | The Velvet Underground | Verve         |

  Scenario Outline: Update item
    When I update item title
      | title                           | updatedTitle |
      | The Velvet Underground and Nico | First        |
      | White Light/White Heat          | Second       |
      | The Velvet Underground          | Third        |
      | Loaded                          | Fourth       |
      | Squeeze                         | Fifth        |
    Then http response code is 202
    And item with title "<updatedTitle>" should contain price <price>
    Examples:
      | updatedTitle | price |
      | First        | 500   |
      | Second       | 400   |
      | Third        | 300   |
      | Fourth       | 200   |
      | Fifth        | 100   |

  Scenario Outline: Delete item
    When I delete item with title "<title>"
    Then http response code is 200
    And item with title "<title>" should not be found
    Examples:
      | title        |
      | First        |
      | Second       |
      | Third        |
      | Fourth       |
      | Fifth        |