package dtu.library.acceptance_tests;

import dtu.library.app.LibraryApp;
import dtu.library.app.OperationNotAllowedException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.Mockito.verify;

public class EMailSteps {
    LibraryApp libraryApp;
    UserHelper helper;
    MockEmailServerHolder emailServerHolder;
    ErrorMessageHolder errorMessageHolder;

    public EMailSteps(LibraryApp libraryApp, UserHelper helper, MockEmailServerHolder emailServerHolder, ErrorMessageHolder errorMessageHolder) {
        this.libraryApp = libraryApp;
        this.helper = helper;
        this.emailServerHolder = emailServerHolder;
        this.errorMessageHolder = errorMessageHolder;
    }

    @When("the administrator sends a reminder e-mail")
    public void theAdministratorSendsAReminderEMail() {
        try {
            libraryApp.sendReminder();
        } catch (OperationNotAllowedException e) {
            errorMessageHolder.setErrorMessage(e.getMessage());
        }
    }

    @Then("then the user receives an email with subject {string} and text {string}")
    public void thenTheUserReceivesAnEmailWithSubjectAndText(String subject, String text) {
        verify(emailServerHolder.getMockEmailServer()).sendEmail(helper.getUser().getEmail(), subject, text);
    }
}
