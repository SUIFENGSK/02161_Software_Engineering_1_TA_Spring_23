package dtu.library.acceptance_tests;

import dtu.library.app.Address;
import dtu.library.app.LibraryApp;
import dtu.library.app.OperationNotAllowedException;
import dtu.library.app.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserSteps {
    private LibraryApp libraryApp;
    private ErrorMessageHolder errorMessage;
    private UserHelper helper;
    private Address address;
    private User user;

    public UserSteps(LibraryApp libraryApp, ErrorMessageHolder errorMessage, UserHelper helper) {
        this.libraryApp = libraryApp;
        this.errorMessage = errorMessage;
        this.helper = helper;
    }

    @Given("there is a user with CPR {string}, name {string}, e-mail {string}")
    public void thereIsAUserWithCPRNameEMail(String cpr, String name, String email) {
        helper.setUser(new User(cpr, name, email));
        assertThat(helper.getUser().getCpr(), is(equalTo(cpr)));
        assertThat(helper.getUser().getName(), is(equalTo(name)));
        assertThat(helper.getUser().getEmail(), is(equalTo(email)));

    }

    @Given("the user has address street {string}, post code {int}, and city {string}")
    public void theUserHasAddressStreetPostCodeAndCity(String street, Integer postCode, String city) {
        address = new Address(street, postCode, city);
        assertThat(address.getStreet(), is(equalTo(street)));
        assertThat(address.getPostCode(), is(equalTo(postCode)));
        assertThat(address.getCity(), is(equalTo(city)));
        helper.getUser().setAddress(address);
        assertThat(helper.getUser().getAddress(), is(sameInstance(address)));
    }

    @When("the administrator registers the user")
    public void theAdministratorRegistersTheUser() throws OperationNotAllowedException {
        try {
            libraryApp.registerUser(helper.getUser());
        } catch (OperationNotAllowedException e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the user is a registered user of the library")
    public void theUserIsARegisteredUserOfTheLibrary() {
        User user = libraryApp.getUser(helper.getUser().getCpr());
        assertEquals(user.getCpr(), helper.getUser().getCpr());
        assertEquals(user.getName(), helper.getUser().getName());
        assertEquals(user.getEmail(), helper.getUser().getEmail());
        assertEquals(user.getAddress().getStreet(), helper.getUser().getAddress().getStreet());
        assertEquals(user.getAddress().getPostCode(), helper.getUser().getAddress().getPostCode());
        assertEquals(user.getAddress().getCity(), helper.getUser().getAddress().getCity());
        assertTrue(libraryApp.userIsRegistered(helper.getUser()));
    }

    @Given("a user is registered with the library")
    public void aUserIsRegisteredWithTheLibrary() throws OperationNotAllowedException {
        User user = helper.getUser();
        libraryApp.adminLogin("adminadmin");
        libraryApp.registerUser(user);
        libraryApp.adminLogout();
    }

    @When("the administrator registers the user again")
    public void theAdministratorRegistersTheUserAgain() throws OperationNotAllowedException {
        theAdministratorRegistersTheUser();
    }

    @Then("the user has to pay no fine")
    public void theUserHasToPayNoFine() {
        assertThat(libraryApp.getFineForUser(helper.getUser()), is(equalTo(0.0)));
    }

    @Then("the user has overdue books")
    public void theUserHasOverdueBooks() {
        assertThat(libraryApp.userHasOverdueBooks(helper.getUser()), is(true));
    }

    @Then("the user has to pay a fine of {int} DKK")
    public void theUserHasToPayAFineOfDKK(double fine) {
        assertThat(libraryApp.getFineForUser(helper.getUser()), is(equalTo(fine)));
    }

    @Then("the user has no overdue books")
    public void theUserHasNoOverdueBooks() {
        assertThat(libraryApp.userHasOverdueBooks(helper.getUser()), is(false));
    }

    @When("the administrator unregisters that user")
    public void theAdministratorUnregistersThatUser() throws Exception {
        try {
            libraryApp.unregister(helper.getUser());
        } catch (Exception e) {
            errorMessage.setErrorMessage(e.getMessage());
        }
    }

    @Then("the user is not registered with the library")
    public void theUserIsNotRegisteredWithTheLibrary() throws Exception {
        assertFalse(libraryApp.hasUser(helper.getUser().getCpr()));
    }

    @Then("the user is still registered with the library")
    public void theUserIsStillRegisteredWithTheLibrary() throws Exception {
        assertTrue(libraryApp.hasUser(helper.getUser().getCpr()));
    }

    @Given("a user is not registered with the library")
    public void aUserIsNotRegisteredWithTheLibrary() throws Exception {
        user = helper.getUser();
    }
}
