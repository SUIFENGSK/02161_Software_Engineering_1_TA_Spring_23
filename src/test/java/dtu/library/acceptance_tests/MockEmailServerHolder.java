package dtu.library.acceptance_tests;

import dtu.library.app.EmailServer;
import dtu.library.app.LibraryApp;

import static org.mockito.Mockito.mock;

public class MockEmailServerHolder {
    private final EmailServer mockEmailServer = mock(EmailServer.class);

    public MockEmailServerHolder(LibraryApp libraryApp) {
        libraryApp.setEmailServer(mockEmailServer);
    }

    public EmailServer getMockEmailServer() {
        return mockEmailServer;
    }

}
