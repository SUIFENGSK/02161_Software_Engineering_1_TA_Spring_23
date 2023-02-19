package dtu.library.acceptance_tests;

import dtu.library.app.Book;
import dtu.library.app.Medium;
import dtu.library.app.LibraryApp;
import dtu.library.app.OperationNotAllowedException;

import java.util.ArrayList;
import java.util.List;

public class BookHelper {
    LibraryApp libraryApp;

    public BookHelper(LibraryApp libraryApp) {
        this.libraryApp = libraryApp;
    }


    public List<Medium> getExampleBooks(int n) {
        List<Medium> media = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            media.add(new Book("title" + i, "author" + i, "signature" + i));
        }
        return media;
    }

    public void addBooksToLibrary(List<Medium> media) {
        libraryApp.adminLogin("adminadmin");
        for (Medium b : media) {
            try {
                libraryApp.addMedium(b);
            } catch (OperationNotAllowedException e) {
                e.printStackTrace();
            }
        }
    }
}
