package model;

import java.util.List;

public class Account {
    final private int id;
    final private String name;
    final private String institution;
    final private Currency currency;
    final private List<Transaction> transactions;

    public Account(int id, String name, String institution, Currency currency, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.institution = institution;
        this.currency = currency;
        this.transactions = transactions;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
