package model;

public class Account {
    final private int id;
    final private String name;
    final private String institution;
    final private boolean archived;
    final private int currencyId;

    public Account(int id, String name, String institution, boolean archived, int currencyId) {
        this.id = id;
        this.name = name;
        this.institution = institution;
        this.archived = archived;
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

    public boolean isArchived() {
        return archived;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    @Override
    public String toString() {
        return name;
    }
}
