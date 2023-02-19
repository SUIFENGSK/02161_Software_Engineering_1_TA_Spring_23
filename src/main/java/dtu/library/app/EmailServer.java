package dtu.library.app;

// The e-mail server has no code coverage, as the tests only use the mock object
// and not the real EmailServer class
public class EmailServer {
    public void sendEmail(String email, String subject, String text) {
        System.out.format("sendEmail(%s,%s,%s)\n", email, subject, text);
    }
}
