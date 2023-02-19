package dtu.library.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LibraryApp {

    public boolean adminLoggedIn = false;
    public List<Book> books = new ArrayList<>();
    public List<Cd> cds = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    private DateServer dateServer = new DateServer();
    private EmailServer emailServer = new EmailServer();


    public void addBook(Book book) throws OperationNotAllowedException {
        if (adminLoggedIn) {
            books.add(book);
        } else {
            throw new OperationNotAllowedException("Administrator login required");
        }
    }

    public boolean containsBookWithSignature(String signature) {
        // return books.stream().anyMatch(book -> book.getSignature().equals(signature));
        for (Book book : books) {
            if (book.getSignature().equals(signature)) {
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

    public List<Book> search(String searchText) {
        // return books.stream().filter(book -> book.getSignature().contains(searchText)).collect(Collectors.toList());
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getSignature().contains(searchText) || book.getTitle().contains(searchText) || book.getAuthor().contains(searchText)) {
                result.add(book);
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

    public void borrowBook(String cpr, String signature) throws Exception {
        User user = getUser(cpr);
        Book book = getBookFromSignature(signature);
        user.borrowBook(book, dateServer.getDate());
    }

    private Book getBookFromSignature(String signature) {
        for (Book book : books) {
            if (book.getSignature().equals(signature)) {
                return book;
            }
        }
        return null;
    }

    public boolean userHasBorrowedBook(String cpr, String signature) {
        User user = getUser(cpr);
        Book book = getBookFromSignature(signature);
        return user.hasBorrowed(book);
    }

    public void returnBook(String cpr, String signature) throws TooManyMediaException {
        User user = getUser(cpr);
        Book book = getBookFromSignature(signature);
        user.returnBook(book);
    }

    public double getFineForUser(User user) {
        return user.getFine(dateServer.getDate());
    }

    public void setDateServer(DateServer dateServer) {
        this.dateServer = dateServer;
    }

    public boolean userHasOverdueBooks(User user) {
        return user.hasOverdueBooks(dateServer.getDate());
    }

    public void setEmailServer(EmailServer mockEmailServer) {
        this.emailServer = mockEmailServer;
    }

    public void sendReminder() throws OperationNotAllowedException {
        checkAdministratorLoggedIn();
        Calendar currentDate = dateServer.getDate();
        users.stream()
                .filter(u -> u.hasOverdueBooks(currentDate))
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
        if (!user.getBorrowedBooks().isEmpty()) {
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

    public boolean containsCdWithSignature(String signature) {
        for (Cd cd : cds) {
            if (cd.getSignature().equals(signature)) {
                return true;
            }
        }
        return false;

    }

    public void addCd(Cd cd) throws OperationNotAllowedException {
        if (adminLoggedIn) {
            cds.add(cd);
        } else {
            throw new OperationNotAllowedException("Administrator login required");
        }
    }
}
