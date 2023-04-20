package model;

import java.util.Date;

public class Transaction {
    final private int id;
    final private String categoryName;
    final private Decimal amount;
    final private Date date;
    final private String info;
    final private String transferAccountName;

    public Transaction(int id, String categoryName, Decimal amount, Date date, String info, String transferAccountName) {
        this.id = id;
        this.categoryName = categoryName;
        this.amount = amount;
        this.date = date;
        this.info = info;
        this.transferAccountName = transferAccountName;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Decimal getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public String getTransferAccountName() {
        return transferAccountName;
    }

    public boolean isTransfer() {
        return transferAccountName != null;
    }

    public boolean isIncome() {
        return amount.isPositive();
    }

    public boolean isExpense() {
        return amount.isNegative();
    }
}
