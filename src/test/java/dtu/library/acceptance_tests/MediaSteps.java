package dtu.library.acceptance_tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dtu.library.app.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MediaSteps {

    private LibraryApp libraryApp;
    private ErrorMessageHolder errorMessageHolder;
    private Medium medium;
    private List<Medium> media;
    private UserHelper helper;
    private MockDateHolder dateHolder;
    private BookHelper bookHelper;

    /*
     * Note that the constructor is apparently never called, but there are no null
     * pointer exceptions regarding that libraryApp is not set. When creating the
     * BookSteps object, the Cucumber libraries are using that constructor with an
     * object of class LibraryApp as the default.
     *
     * This also holds for all other step classes that have a similar constructor.
     * In this case, the <b>same</b> object of class LibraryApp is used as an
     * argument. This provides an easy way of sharing the same object, in this case
     * the object of class LibraryApp and the errorMessage Holder, among all step classes.
     *
     * This principle is called <em>dependency injection</em>. More information can
     * be found in the "Cucumber for Java" book available online from the DTU Library.
     */
    public MediaSteps(LibraryApp libraryApp, ErrorMessageHolder errorMessageHolder, UserHelper helper, MockDateHolder dateHolder, BookHelper bookHelper) {
        this.libraryApp = libraryApp;
        this.errorMessageHolder = errorMessageHolder;
        this.helper = helper;
        this.dateHolder = dateHolder;
        this.bookHelper = bookHelper;
    }

    @Given("there is a book with title {string}, author {string}, and signature {string}")
    public void thereIsABookWithTitleAuthorAndSignature(String title, String author, String signature) throws Exception {
        medium = new Book(title, author, signature);
    }

    @Given("the book is not in the library")
    public void theBookIsNotInTheLibrary() {
        assertFalse(libraryApp.containsMediaWithSignature(medium.getSignature()));
    }


    @Given("these books are contained in the library")
    public void theseBooksAreContainedInTheLibrary(List<List<String>> books) throws Exception {
        for (List<String> bookInfo : books) {
            libraryApp.addMedium(new Book(bookInfo.get(0), bookInfo.get(1), bookInfo.get(2)));
        }
    }

    @When("the book is added to the library")
    public void bookIsAddedToTheLibrary() {
        try {
            libraryApp.addMedium(medium);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the book with title {string}, author {string}, and signature {string} is contained in the library")
    public void theBookWithTitleAuthorAndSignatureIsContainedInTheLibrary(String title, String author, String signature)
            throws Exception {
        assertTrue(libraryApp.containsMediaWithSignature(signature));
    }

    @Then("the error message {string} is given")
    public void theErrorMessageIsGiven(String errorMessage) throws Exception {
        assertEquals(errorMessage, this.errorMessageHolder.getErrorMessage());
    }

    @Given("the library has a book with title {string}, author {string}, and signature {string}")
    public void theLibraryHasABookWithTitleAuthorAndSignature(String title, String author, String signature)
            throws Exception {
        Medium medium = new Book(title, author, signature);
        libraryApp.addMedium(medium);
    }

    @When("the user searches for the text {string}")
    public void theUserSearchesForTheText(String searchText) throws Exception {
        media = libraryApp.search(searchText);
    }

    @Then("the book with signature {string} is found")
    public void theBookWithSignatureIsFound(String signature) throws Exception {
        assertEquals(1, media.size());
        assertEquals(signature, media.get(0).getSignature());
    }

    @Then("no books are found")
    public void noBooksAreFound() throws Exception {
        assertTrue(media.isEmpty());
    }

    @Then("the books with signatures {string} and {string} are found")
    public void theBooksWithSignaturesAndAreFound(String signature1, String signature2) throws Exception {
        assertEquals(2, media.size());
        Medium medium1 = media.get(0);
        Medium medium2 = media.get(1);
        assertTrue((medium1.getSignature().equals(signature1) && medium2.getSignature().equals(signature2))
                || (medium1.getSignature().equals(signature2) && medium2.getSignature().equals(signature1)));
    }

    @Given("a book with signature {string} is in the library")
    public void aBookWithSignatureIsInTheLibrary(String signature) throws OperationNotAllowedException {
        medium = new Book("title", "author", signature);
        addBookToLibrary(medium);
    }

    private void addBookToLibrary(Medium medium) throws OperationNotAllowedException {
        libraryApp.adminLogin("adminadmin");
        libraryApp.addMedium(medium);
    }

    @When("the user borrows the book")
    public void theUserBorrowsTheBook() {
        try {
            libraryApp.borrowMedium(helper.getUser().getCpr(), medium.getSignature());
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the book is borrowed by the user")
    public void theBookIsBorrowedByTheUser() {
        assertTrue(libraryApp.userHasBorrowedMedium(helper.getUser().getCpr(), medium.getSignature()));
    }

    @Given("the user has borrowed {int} books")
    public void theUserHasBorrowedBooks(Integer int1) throws Exception {
        List<Medium> exampleMedia = getExampleBooks(10);
        addBooksToLibrary(exampleMedia);
        for (Medium b : exampleMedia) {
            libraryApp.borrowMedium(helper.getUser().getCpr(), b.getSignature());
        }
    }

    private void addBooksToLibrary(List<Medium> exampleMedia) {
        libraryApp.adminLogin("adminadmin");
        for (Medium b : exampleMedia) {
            try {
                addBookToLibrary(b);
            } catch (OperationNotAllowedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Medium> getExampleBooks(int n) {
        List<Medium> media = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            media.add(new Book("title" + i, "author" + i, "signature" + i));
        }
        return media;
    }

    @Then("the book is not borrowed by the user")
    public void theBookIsNotBorrowedByTheUser() {
        assertFalse(libraryApp.userHasBorrowedMedium(helper.getUser().getCpr(), medium.getSignature()));
    }

    @Given("that the user has borrowed a book")
    public void thatTheUserHasBorrowedABook() throws Exception {
        medium = new Book("title", "author", "signature");
        addBookToLibrary(medium);
        libraryApp.borrowMedium(helper.getUser().getCpr(), medium.getSignature());
    }

    @When("the user returns the book")
    public void theUserReturnsTheBook() {
        try {
            libraryApp.returnMedium(helper.getUser().getCpr(), medium.getSignature());
        } catch (TooManyMediaException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Given("that the user not borrowed the book")
    public void thatTheUserNotBorrowedTheBook() throws OperationNotAllowedException {
        medium = new Book("title", "author", "signature");
        addBookToLibrary(medium);
    }

    @Given("the user has borrowed a book")
    public void theUserHasBorrowedABook() throws Exception {
        thatTheUserHasBorrowedABook();
    }

    @Given("there is a user with one overdue book")
    public void thereIsAUserWithOneOverdueBook() throws Exception {
        libraryApp.registerUser(helper.getUser());
        thatTheUserHasBorrowedABook();
        dateHolder.advanceDateByDays(29);
        assertThat(libraryApp.userHasOverdueMedia(helper.getUser()), is(true));
    }

    @When("the user borrows the book with signature {string}")
    public void theUserBorrowsTheBookWithSignature(String signature) throws Exception {
        try {
            libraryApp.borrowMedium(helper.getUser().getCpr(), signature);
        } catch (Exception e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the book with signature {string} is not borrowed by the user")
    public void theBookWithSignatureIsNotBorrowedByTheUser(String signature) throws Exception {
        assertThat(libraryApp.userHasBorrowedMedium(helper.getUser().getCpr(), signature), is(false));
    }

    @When("the user returns the book with signature {string}")
    public void theUserReturnsTheBookWithSignature(String string) {
        try {
            libraryApp.returnMedium(helper.getUser().getCpr(), string);
        } catch (TooManyMediaException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Given("a user has an overdue book")
    public void aUserHasAnOverdueBook() throws Exception {
        User user = helper.getUser();
        libraryApp.adminLogin("adminadmin");
        libraryApp.registerUser(user);
        libraryApp.adminLogout();
        List<Medium> media = bookHelper.getExampleBooks(1);
        bookHelper.addBooksToLibrary(media);
        medium = media.get(0);
        libraryApp.borrowMedium(user.getCpr(), medium.getSignature());
        dateHolder.advanceDateByDays(29);
        assertThat(libraryApp.userHasBorrowedMedium(user.getCpr(), medium.getSignature()), is(true));
    }

    @When("the user pays {int} DKK")
    public void theUserPaysDKK(int money) throws Exception {
        libraryApp.payFine(helper.getUser(), money);
    }

    @Then("the user can borrow books again")
    public void theUserCanBorrowBooksAgain() throws Exception {
        assertThat(libraryApp.canBorrow(helper.getUser()), is(true));
    }

    @Given("the user has another overdue book")
    public void theUserHasAnotherOverdueBook() throws Exception {
        List<Medium> media = new ArrayList<>();
        media.add(new Book("title", "author", "signature"));
        bookHelper.addBooksToLibrary(media);
        medium = media.get(0);
        libraryApp.borrowMedium(helper.getUser().getCpr(), medium.getSignature());
        dateHolder.advanceDateByDays(29);
        assertThat(libraryApp.userHasOverdueMedia(helper.getUser()), is(true));
    }

    @Then("the user cannot borrow books")
    public void theUserCannotBorrowBooks() throws Exception {
        assertThat(libraryApp.canBorrow(helper.getUser()), is(false));
    }

    @Given("the user has to pay a fine")
    public void theUserHasToPayAFine() throws Exception {
        medium = new Book("title", "author", "signature");
        bookHelper.addBooksToLibrary(Collections.singletonList(medium));
        libraryApp.borrowMedium(helper.getUser().getCpr(), medium.getSignature());
        dateHolder.advanceDateByDays(29);
        assertTrue(libraryApp.getFineForUser(helper.getUser()) > 0);
        libraryApp.returnMedium(helper.getUser().getCpr(), medium.getSignature());
    }

    @Given("there is a cd with title {string}, author {string}, and signature {string}")
    public void thereIsACdWithTitleAuthorAndSignature(String title, String author, String signature) {
        medium = new Cd(title, author, signature);
    }

    @Given("the cd is not in the library")
    public void theCdIsNotInTheLibrary() {
        assertFalse(libraryApp.containsMediaWithSignature(medium.getSignature()));
    }

    @When("the cd is added to the library")
    public void theCdIsAddedToTheLibrary() {
        try {
            libraryApp.addMedium(medium);
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("the cd with title {string}, author {string}, and signature {string} is contained in the library")
    public void theCdWithTitleAuthorAndSignatureIsContainedInTheLibrary(String title, String author, String signature) {
        assertTrue(libraryApp.containsMediaWithSignature(signature));
    }

    @Given("the user has borrowed a CD")
    public void theUserHasBorrowedACD() throws Exception {
        medium = new Cd("title", "author", "signature");
        libraryApp.adminLogin("adminadmin");
        libraryApp.addMedium(medium);
        libraryApp.borrowMedium(helper.getUser().getCpr(), medium.getSignature());
    }

    @Then("the user has overdue CDs")
    public void theUserHasOverdueCDs() {
        assertTrue(libraryApp.userHasOverdueMedia(helper.getUser()));
    }
}