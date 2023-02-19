Feature: Overdue book
  Description: Contains the business rules when a book is overdue
  This is more a feature describing a business rule then a
  use case, but Cucumber scenarios can also be used here as
  these business rules are given and must be read by
  domain experts

  Scenario: Overdue book after 29 days
    Given a user is registered with the library
    And the user has borrowed a book
    Then the user has to pay no fine
    Given 29 days have passed
    Then the user has overdue books
    And the user has to pay a fine of 100 DKK

  Scenario: Not overdue book if books was borrowed less than or equal 28 days
    Given a user is registered with the library
    And the user has borrowed a book
    And 28 days have passed
    Then the user has no overdue books
    And the user has to pay no fine
