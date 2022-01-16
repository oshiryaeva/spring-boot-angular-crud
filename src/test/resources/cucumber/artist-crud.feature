Feature: Perform CRUD operations on Artist
  I should be able to create, update and remove Artist

  Scenario Outline: Create artist
    When I create artist with name "<name>"
    Then the response code should be 201
    Examples:
      | name              |
      | Аквариум          |
      | Lee Scratch Perry |
      | Левша-Пацан       |
      | Рома ВПР          |

  Scenario Outline: Read artist
    When I get artist with name "<name>"
    Then the response code should be 200
    And the response body should contain name "<name>"
    Examples:
    Examples:
      | name              |
      | Аквариум          |
      | Lee Scratch Perry |
      | Левша-Пацан       |
      | Рома ВПР          |

  Scenario: Update artist
    When I update artist's name
      | name              | newName           |
      | Аквариум          | Аквариум in Dub   |
      | Lee Scratch Perry | Lee Perry         |
      | Левша-Пацан       | Левша             |
      | Рома ВПР          | Рома В.П.Р.       |
    Then the response code should be 202

  Scenario Outline: Delete artist
    When I delete artist with name "<name>"
    Then the response code should be 200
    And artist with name "<name>" should not be found
    Examples:
      | name              |
      | Аквариум in Dub   |
      | Lee Perry         |
      | Левша             |
      | Рома В.П.Р.       |