package dtu.library.acceptance_tests;

import dtu.library.app.Address;
import dtu.library.app.User;

public class UserHelper {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        if (user == null) {
            user = exampleUser();
        }
        return user;
    }

    private User exampleUser() {
        User user = new User("231171-3879", "Freddie E. Messina", "FreddieEMessina@armyspy.com");
        Address address = new Address("Øksendrupvej 68", 1321, "København K");
        user.setAddress(address);
        return user;
    }
}
