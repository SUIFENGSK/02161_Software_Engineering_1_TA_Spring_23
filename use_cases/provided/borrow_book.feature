Feature: Borrow Book
  Description: The user borrows a book
  Actors: User

  Scenario: User borrows book
    Given a book with signature "Beck99" is in the library
    And a user is registered with the library
    When the user borrows the book
    Then the book is borrowed by the user

  Scenario: User borrows book more than 10 books
    Given a user is registered with the library
    And the user has borrowed 10 books
    Given a book with signature "Beck99" is in the library
    When the user borrows the book
    Then the book is not borrowed by the user
    And the error message "Can't borrow more than 10 books" is given

  Scenario: User cannot borrow books if he has overdue books
    Given a book with signature "Beck99" is in the library
    And a book with signature "Rose11" is in the library
    And a user is registered with the library
    When the user borrows the book with signature "Beck99"
    And 29 days have passed
    And the user borrows the book with signature "Rose11"
    Then the book with signature "Rose11" is not borrowed by the user
    And the error message "Can't borrow book/CD if user has overdue books/CDs" is given

  Scenario: User cannot borrow books if he has outstanding fines
    Given a book with signature "Beck99" is in the library
    And a book with signature "Rose11" is in the library
    And a user is registered with the library
    When the user borrows the book with signature "Beck99"
    And 29 days have passed
    Then the user has to pay a fine of 100 DKK
    When the user returns the book with signature "Beck99"
    Then the user has to pay a fine of 100 DKK
    When the user borrows the book with signature "Rose11"
    Then the book with signature "Rose11" is not borrowed by the user
    And the error message "Can't borrow book/CD if user has outstanding fines" is given