Feature: Return Book
  Descriptions: The user returns a book
  Actors: User

  Scenario: A user returns a borrowed book
    Given a user is registered with the library
    And that the user has borrowed a book
    When the user returns the book
    Then the book is not borrowed by the user

  Scenario: A user returns a book he hasn't borrowed
    Given a user is registered with the library
    And that the user not borrowed the book
    When the user returns the book
    Then the book is not borrowed by the user
    And the error message "book is not borrowed by the user" is given