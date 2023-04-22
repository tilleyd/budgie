package model;

public class Account {
    final private int id;
    final private String name;
    final private String institution;
    final private int currencyId;

    public Account(int id, String name, String institution, int currencyId) {
        this.id = id;
        this.name = name;
        this.institution = institution;
        this.currencyId = currencyId;
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

    public int getCurrencyId() {
        return currencyId;
    }
}
