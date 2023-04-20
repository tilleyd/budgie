package controller;

import database.Database;
import database.DatabaseError;
import model.*;

import java.util.Date;
import java.util.List;

public class Controller {
    final private Database db;

    public Controller(Database db) {
        this.db = db;
    }

    Account createAccount(String name, String institution, Currency currency) {
        int id = db.createAccount(name, institution, currency);
        return new Account(id, name, institution, currency);
    }

    void updateAccount(int id, String name, String institution) {
        db.updateAccount(id, name, institution);
    }

    void archiveAccount(int id) {
        db.archiveAccount(id);
    }

    void deleteAccount(int id) throws DatabaseError {
        // delete all transactions first
        List<Transaction> transactions = db.getTransactionsForAccount(id);
        for (Transaction t : transactions) {
            deleteTransaction(t.getId());
        }
        db.deleteAccount(id);
    }

    Category createCategory(String name, CategoryType type, CategoryGroup group) {
        int id = db.createCategory(name, type, group);
        return new Category(id, name, type, group);
    }

    void updateCategory(int id, String name, CategoryType type, CategoryGroup group) {
        db.updateCategory(id, name, type, group);
    }

    void deleteCategory(int id) {
        // TODO check if any transactions use it first
        db.deleteCategory(id);
    }

    Currency createCurrency(String code, String symbol) {
        int id = db.createCurrency(code, symbol);
        return new Currency(id, code, symbol);
    }

    void updateCurrency(int id, String code, String symbol) {
        db.updateCurrency(id, code, symbol);
    }

    void deleteCurrency(int id) {
        // TODO check if any accounts use it first
        db.deleteCurrency(id);
    }

    Transaction createTransaction(
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info,
            Account transferAccount
    ) throws LogicError {
        if (transferAccount != null) {
            // prohibit transfers into same account
            if (transferAccount.equals(account)) {
                throw new LogicError("Cannot create a transfer into same account");
            }
        }

        int id = db.createTransaction(category, account, amount, date, info, transferAccount);
        return new Transaction(id, category.getName(), amount, date, info, transferAccount == null ? null : transferAccount.getName());
    }

    void updateTransaction(
            int id,
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info,
            Account transferAccount
    ) {
        db.updateTransaction(id, category, account, amount, date, info, transferAccount);
    }

    void deleteTransaction(int id) {
        db.deleteTransaction(id);
    }
}
