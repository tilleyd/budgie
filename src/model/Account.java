package model;

public class Account {
    final private int id;
    final private String name;
    final private String institution;
    final private Currency currency;

    public Account(int id, String name, String institution, Currency currency) {
        this.id = id;
        this.name = name;
        this.institution = institution;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstitution() {
        return institution;
    }

    public Currency getCurrency() {
        return currency;
    }
}
