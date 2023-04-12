package model;

import java.util.Date;

public class Transaction {
    final private int id;
    final private Category category;
    final private Account account;
    final private Decimal amount;
    final private Date date;
    final private Transaction balancingTransaction;

    public Transaction(int id, Category category, Account account, Decimal amount, Date date, Transaction balancingTransaction) {
        this.id = id;
        this.category = category;
        this.account = account;
        this.amount = amount;
        this.date = date;
        this.balancingTransaction = balancingTransaction;
    }

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Account getAccount() {
        return account;
    }

    public Decimal getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public Transaction getBalancingTransaction() {
        return balancingTransaction;
    }

    public boolean isTransfer() {
        return balancingTransaction != null;
    }

    public boolean isIncome() {
        return amount.isPositive();
    }

    public boolean isExpense() {
        return amount.isNegative();
    }
}
