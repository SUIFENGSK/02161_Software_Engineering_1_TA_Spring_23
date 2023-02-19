package dtu.library.app;

public class Cd extends Medium {
    public Cd(String title, String author, String signature) {
        super(title, author, signature);
    }

    @Override
    protected int getMaxBorrowDays() {
        return 0;
    }

    @Override
    double getFine() {
        return 0;
    }

    @Override
    public String getTypeName() {
        return null;
    }
}
