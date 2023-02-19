package dtu.library.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LibraryApp {

    public boolean adminLoggedIn = false;
    public List<Medium> media = new ArrayList<>();
    public List<Cd> cds = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    private DateServer dateServer = new DateServer();
    private EmailServer emailServer = new EmailServer();


    public void addMedium(Medium medium) throws OperationNotAllowedException {
        if (adminLoggedIn) {
            media.add(medium);
        } else {
            throw new OperationNotAllowedException("Administrator login required");
        }
    }

    public boolean containsMediaWithSignature(String signature) {
        // return books.stream().anyMatch(book -> book.getSignature().equals(signature));
        for (Medium medium : media) {
            if (medium.getSignature().equals(signature)) {
                return true;
            }
        }
        return false;
    }

    public boolean adminLoggedIn() {
        return adminLoggedIn;
    }

    public boolean adminLogin(String password) {
        if (password.equals("adminadmin")) {
            adminLoggedIn = true;
            return true;
        }
        return false;
    }

    public void adminLogout() {
        adminLoggedIn = false;
    }

    public List<Medium> search(String searchText) {
        // return books.stream().filter(book -> book.getSignature().contains(searchText)).collect(Collectors.toList());
        List<Medium> result = new ArrayList<>();
        for (Medium medium : media) {
            if (medium.getSignature().contains(searchText) || medium.getTitle().contains(searchText) || medium.getAuthor().contains(searchText)) {
                result.add(medium);
            }
        }
        return result;
    }

    public void registerUser(User user) throws OperationNotAllowedException {
        if (!adminLoggedIn()) {
            throw new OperationNotAllowedException("Administrator login required");
        }
        if (userIsRegistered(user)) {
            throw new OperationNotAllowedException("User is already registered");
        }
        users.add(user);
    }

    public User getUser(String cpr) {
        for (User user : users) {
            if (user.getCpr().equals(cpr)) {
                return user;
            }
        }
        return null;
    }

    public boolean userIsRegistered(User user) {
        return getUser(user.getCpr()) != null;
    }

    public void borrowMedium(String cpr, String signature) throws Exception {
        User user = getUser(cpr);
        Medium medium = getMediumFromSignature(signature);
        user.borrowBook(medium, dateServer.getDate());
    }

    private Medium getMediumFromSignature(String signature) {
        for (Medium medium : media) {
            if (medium.getSignature().equals(signature)) {
                return medium;
            }
        }
        return null;
    }

    public boolean userHasBorrowedMedium(String cpr, String signature) {
        User user = getUser(cpr);
        Medium medium = getMediumFromSignature(signature);
        return user.hasBorrowed(medium);
    }

    public void returnMedium(String cpr, String signature) throws TooManyMediaException {
        User user = getUser(cpr);
        Medium medium = getMediumFromSignature(signature);
        user.returnBook(medium);
    }

    public double getFineForUser(User user) {
        return user.getFine(dateServer.getDate());
    }

    public void setDateServer(DateServer dateServer) {
        this.dateServer = dateServer;
    }

    public boolean userHasOverdueMedia(User user) {
        return user.hasOverdueMedia(dateServer.getDate());
    }

    public void setEmailServer(EmailServer mockEmailServer) {
        this.emailServer = mockEmailServer;
    }

    public void sendReminder() throws OperationNotAllowedException {
        checkAdministratorLoggedIn();
        Calendar currentDate = dateServer.getDate();
        users.stream()
                .filter(u -> u.hasOverdueMedia(currentDate))
                .forEach(u -> {
                    u.sendEmailReminder(emailServer, currentDate);
                });
    }

    private void checkAdministratorLoggedIn() throws OperationNotAllowedException {
        if (!adminLoggedIn()) {
            throw new OperationNotAllowedException("Administrator login required");
        }
    }

    public void payFine(User user, int money) {
        user.payFine(money);
    }

    public boolean canBorrow(User user) {
        try {
            user.canBorrow(dateServer.getDate());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void unregister(User user) throws Exception {
        checkAdministratorLoggedIn();
        if (!users.contains(user)) {
            throw new Exception("User not registered");
        }
        if (!user.getBorrowedMedia().isEmpty()) {
            throw new Exception("Can't unregister user: user has still borrowed books/CDs");
        }
        if (user.getFine(dateServer.getDate()) > 0) {
            throw new Exception("Can't unregister user: user has still fines to pay");
        }
        users.remove(user);
    }

    public boolean hasUser(String cpr) {
        return getUser(cpr) != null;
    }
}
