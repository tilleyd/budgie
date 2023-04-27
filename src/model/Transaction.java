package model;

import java.util.Date;

public class Transaction {
    final private int id;
    final private int accountId;
    final private int categoryId;
    final private Decimal amount;
    final private Date date;
    final private String info;
    final private Integer transferAccountId;
    final private Integer transferTransactionId;

    public Transaction(int id, int accountId, int categoryId, Decimal amount, Date date, String info, Integer transferAccountId, Integer transferTransactionId) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.info = info;
        this.transferAccountId = transferAccountId;
        this.transferTransactionId = transferTransactionId;
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getCategoryId() {
        return categoryId;
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

    public Integer getTransferAccountId() {
        return transferAccountId;
    }

    public Integer getTransferTransactionId() {
        return transferTransactionId;
    }

    public boolean isTransfer() {
        return transferAccountId != null;
    }

    public boolean isIncome() {
        return amount.isPositive();
    }

    public boolean isExpense() {
        return amount.isNegative();
    }
}
