package dtu.library.acceptance_tests;


import dtu.library.app.Cd;
import dtu.library.app.LibraryApp;
import dtu.library.app.OperationNotAllowedException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CdSteps {
    private LibraryApp libraryApp;
    private ErrorMessageHolder errorMessageHolder;
    private Cd cd;
    private List<Cd> cds;
    private UserHelper helper;

    public CdSteps(LibraryApp libraryApp, ErrorMessageHolder errorMessageHolder, UserHelper helper, MockDateHolder dateHolder, BookHelper bookHelper) {
        this.libraryApp = libraryApp;
        this.errorMessageHolder = errorMessageHolder;
        this.helper = helper;
    }

    @Given("there is a cd with title {string}, author {string}, and signature {string}")
    public void thereIsACdWithTitleAuthorAndSignature(String title, String author, String signature) {
        cd = new Cd(title, author, signature);
    }

    @Given("the cd is not in the library")
    public void theCdIsNotInTheLibrary() {
        assertFalse(libraryApp.containsCdWithSignature(cd.getSignature()));
    }

    @When("the cd is added to the library")
    public void theCdIsAddedToTheLibrary() {
        try {
            libraryApp.addCd(cd);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the cd with title {string}, author {string}, and signature {string} is contained in the library")
    public void theCdWithTitleAuthorAndSignatureIsContainedInTheLibrary(String title, String author, String signature) {
        assertTrue(libraryApp.containsCdWithSignature(signature));
    }
}
