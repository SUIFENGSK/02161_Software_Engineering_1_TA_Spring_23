package dtu.library.acceptance_tests;

import dtu.library.app.Book;
import dtu.library.app.LibraryApp;
import dtu.library.app.OperationNotAllowedException;

import java.util.ArrayList;
import java.util.List;

public class BookHelper {
    LibraryApp libraryApp;

    public BookHelper(LibraryApp libraryApp) {
        this.libraryApp = libraryApp;
    }


    public List<Book> getExampleBooks(int n) {
        List<Book> books = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            books.add(new Book("title" + i, "author" + i, "signature" + i));
        }
        return books;
    }

    public void addBooksToLibrary(List<Book> books) {
        libraryApp.adminLogin("adminadmin");
        for (Book b : books) {
            try {
                libraryApp.addBook(b);
            } catch (OperationNotAllowedException e) {
                e.printStackTrace();
            }
        }
    }
}
