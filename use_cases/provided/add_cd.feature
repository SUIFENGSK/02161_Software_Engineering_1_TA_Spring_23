Feature: Add Cd
  Description: A cd is added to the library
  Actors: Administrator

  Scenario: Add a cd successfully
    Given that the administrator is logged in
    And there is a cd with title "Extreme Programming", author "Kent Beck", and signature "Beck99"
    And the cd is not in the library
    When the cd is added to the library
    Then the cd with title "Extreme Programming", author "Kent Beck", and signature "Beck99" is contained in the library

