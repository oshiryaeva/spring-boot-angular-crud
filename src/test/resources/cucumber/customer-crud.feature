Feature: Perform CRUD operations on Customer
  I should be able to create, update and remove Customer

  Scenario Outline: Create customer
    When I create customer with first name "<firstName>" and last name "<lastName>" and email "<email>"
    Then http response code should be 201
    Examples:
      | firstName  | lastName   | email              |
      | Thomas     | Chatterton | thomas@chatter.ton |
      | Hannibal   | Barca      | hann@ib.al         |
      | Démosthène | Agrafiotis | demo@sthe.ne       |
      | Friedrich  | Nietzsche  | nie@tzsc.he        |

  Scenario Outline: Read customer
    When I get customer with email "<email>"
    Then http response code should be 200
    And http response body should contain first name "<firstName>"
    And http response body should contain last name "<lastName>"
    Examples:
      | firstName  | lastName   | email              |
      | Thomas     | Chatterton | thomas@chatter.ton |
      | Hannibal   | Barca      | hann@ib.al         |
      | Démosthène | Agrafiotis | demo@sthe.ne       |
      | Friedrich  | Nietzsche  | nie@tzsc.he        |

  Scenario Outline: Update customer
    When I update customer's email
      | firstName  | lastName   | email              | newEmail              |
      | Thomas     | Chatterton | thomas@chatter.ton | new@thomas.chatterton |
      | Hannibal   | Barca      | hann@ib.al         | new@hanni.bal         |
      | Démosthène | Agrafiotis | demo@sthe.ne       | new@demosthe.ne       |
      | Friedrich  | Nietzsche  | nie@tzsc.he        | new@nietzsc.he        |
    Then http response code should be 202
    And customer with email "<newEmail>" should contain first name "<firstName>"
    And customer with email "<newEmail>" should contain last name "<lastName>"
    Examples:
      | firstName  | lastName   | newEmail              |
      | Thomas     | Chatterton | new@thomas.chatterton |
      | Hannibal   | Barca      | new@hanni.bal         |
      | Démosthène | Agrafiotis | new@demosthe.ne       |
      | Friedrich  | Nietzsche  | new@nietzsc.he        |

  Scenario Outline: Delete customer
    When I delete customer with email "<email>"
    Then http response code should be 200
    And customer with email "<email>" should not be found
    Examples:
      | email                 |
      | new@thomas.chatterton |
      | new@hanni.bal         |
      | new@demosthe.ne       |
      | new@nietzsc.he        |