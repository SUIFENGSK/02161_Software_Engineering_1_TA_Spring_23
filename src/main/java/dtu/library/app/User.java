package dtu.library.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class User {
    private final String cpr;
    private final String name;
    private final String email;
    private Address address;
    private final List<Medium> borrowedMedia = new ArrayList<>();

    private double fine = 0d;

    private boolean hasFine = false;

    public User(String cpr, String name, String email) {
        this.cpr = cpr;
        this.name = name;
        this.email = email;
    }

    public String getCpr() {
        return cpr;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void borrowBook(Medium medium, Calendar borrowDate) throws Exception {
        canBorrow(borrowDate);
        medium.setDueDateFromBorrowDate(borrowDate);
        borrowedMedia.add(medium);
    }

    public boolean hasBorrowed(Medium medium) {
        return borrowedMedia.contains(medium);
    }

    public void returnBook(Medium medium) throws TooManyMediaException {
        if (!borrowedMedia.contains(medium)) {
            throw new TooManyMediaException("book is not borrowed by the user");
        }
        borrowedMedia.remove(medium);
    }

    public double getFine(Calendar currentDate) {
        if (!hasFine) {
            double fineValue = borrowedMedia.stream()
                    .filter(b -> b.isOverdue(currentDate))
                    .mapToDouble(Medium::getFine)
                    .sum();
            if (fineValue == 0) {
                fine = 0;
                hasFine = false;
            } else {
                fine = fineValue;
                hasFine = true;
            }
        }
        return hasFine ? fine : 0;
    }

    public boolean hasOverdueMedia(Calendar date) {
        return borrowedMedia.stream().anyMatch(b -> b.isOverdue(date));
    }

    public void sendEmailReminder(EmailServer emailServer, Calendar currentDate) {
        long numberOfOverdueMedia = borrowedMedia.stream()
                .filter(b -> b.isOverdue(currentDate))
                .count();
        emailServer.sendEmail(email,
                "Overdue book(s)/CD(s)",
                String.format("You have %s overdue book(s)/CD(s)", numberOfOverdueMedia));
    }

    public void payFine(int money) {
        if (fine == money) {
            fine = 0;
            hasFine = false;
        } else {
            fine = fine - money;
            hasFine = true;
        }
    }

    public void canBorrow(Calendar borrowDate) throws Exception {
        if (borrowedMedia.size() >= 10) {
            throw new TooManyMediaException("Can't borrow more than 10 books");
        }
        if (hasOverdueMedia(borrowDate)) {
            throw new OperationNotAllowedException("Can't borrow book/CD if user has overdue books/CDs");
        }
        if (hasFine) {
            throw new OperationNotAllowedException("Can't borrow book/CD if user has outstanding fines");
        }
    }

    public List<Medium> getBorrowedMedia() {
        return Collections.unmodifiableList(borrowedMedia);
    }
}
