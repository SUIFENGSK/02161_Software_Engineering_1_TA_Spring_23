package dtu.library.acceptance_tests;

import dtu.library.app.DateServer;
import dtu.library.app.LibraryApp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockDateHolder {

    DateServer dateServer = mock(DateServer.class);

    public MockDateHolder(LibraryApp libraryApp) {
        GregorianCalendar calendar = new GregorianCalendar();
        setDate(calendar);
        libraryApp.setDateServer(dateServer);
    }

    public void setDate(Calendar calendar) {
        Calendar c = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        when(this.dateServer.getDate()).thenReturn(c);
    }

    public void advanceDateByDays(int days) {
        Calendar currentDate = dateServer.getDate();
        // Important: we need to create a new object,
        // otherwise, the old calendar object gets changed,
        // which suddenly changes the date for objects
        // using that old calendar object
        Calendar nextDate = new GregorianCalendar();
        nextDate.setTime(currentDate.getTime());
        nextDate.add(Calendar.DAY_OF_YEAR, days);
        setDate(nextDate);
    }
}
