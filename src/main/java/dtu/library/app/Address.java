package dtu.library.app;

public class Address {
    private final String street;
    private final int postCode;
    private final String city;

    public Address(String street, int postCode, String city) {
        this.street = street;
        this.postCode = postCode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public int getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }
}
