package dtu.library.app;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class represents a book with title, author, and signature, where signature
 * is a unique key used by the librarian to identify the book. Very often it is
 * composed of the first letters of the authors plus the year the book was published.
 *
 * @author Hubert
 */
public class Book {
    private static final int FINE = 100;
    private static final int MAX_NUMBER_OF_DAYS = 4 * 7;
    private String title;
    private String author;
    private String signature;
    private Calendar dueDate;

    public Book(String title, String author, String signature) {
        this.title = title;
        this.author = author;
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isOverdue(Calendar currentDate) {
        assert getDueDate() != null;
        return currentDate.after(getDueDate());
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    private void setDueDate(Calendar dueDate) {
        assert dueDate != null;
        this.dueDate = dueDate;
    }

    public double getFine() {
        return FINE;
    }

    public void setDueDateFromBorrowDate(Calendar borrowDate) {
        setDueDate(new GregorianCalendar());
        getDueDate().setTime(borrowDate.getTime());
        getDueDate().add(Calendar.DAY_OF_YEAR, getMaxBorrowDays());
    }

    private int getMaxBorrowDays() {
        return MAX_NUMBER_OF_DAYS;
    }
}
