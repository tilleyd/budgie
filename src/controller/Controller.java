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

    Account createAccount(String name, String institution, Currency currency) throws DatabaseError {
        int id = db.createAccount(name, institution, currency);
        return new Account(id, name, institution, currency.getId());
    }

    void updateAccount(int id, String name, String institution) throws DatabaseError {
        db.updateAccount(id, name, institution);
    }

    void archiveAccount(int id) throws DatabaseError {
        db.archiveAccount(id);
    }

    void deleteAccount(int id) throws DatabaseError {
        // TODO delete all transactions from account
        // TODO remove references from transfer transactions
        throw new DatabaseError("deleteAccount not implemented yet");
        // db.deleteAccount(id);
    }

    Category createCategory(String name, CategoryType type, CategoryGroup group) throws DatabaseError {
        int id = db.createCategory(name, type, group);
        return new Category(id, name, type, group);
    }

    void updateCategory(int id, String name, CategoryType type, CategoryGroup group) throws DatabaseError {
        db.updateCategory(id, name, type, group);
    }

    void deleteCategory(int id) throws DatabaseError {
        // TODO check if any transactions use it first
        db.deleteCategory(id);
    }

    Currency createCurrency(String code, String symbol) throws DatabaseError {
        int id = db.createCurrency(code, symbol);
        return new Currency(id, code, symbol);
    }

    void updateCurrency(int id, String code, String symbol) throws DatabaseError {
        db.updateCurrency(id, code, symbol);
    }

    void deleteCurrency(int id) throws DatabaseError {
        // TODO check if any accounts use it first
        db.deleteCurrency(id);
    }

    Transaction createSimpleTransaction(
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info
    ) throws LogicError, DatabaseError {
        if (amount.isZero()) {
            throw new LogicError("Zero amount transactions are not allowed");
        }
        int id = db.createTransaction(category, account, amount, date, info);
        return new Transaction(id, category.getId(), amount, date, info, null, null);
    }

    Transaction[] createTransferTransaction(
            Category category,
            Account from,
            Account to,
            Decimal fromAmount,
            Decimal toAmount,
            Date date,
            String info
    ) throws LogicError, DatabaseError {
        if (from == to) {
            throw new LogicError("Cannot create transfer into same account");
        }
        if (from.getCurrencyId() == to.getCurrencyId() && !fromAmount.equals(toAmount)) {
            throw new LogicError("Transfers of same currency must have an equal amount");
        }
        if (fromAmount.isZero() || toAmount.isZero()) {
            throw new LogicError("Zero amount transfers are not allowed");
        }
        if (fromAmount.isPositive() || toAmount.isNegative()) {
            throw new LogicError("From amount must be negative and to amount must be positive");
        }

        int[] ids = db.createTransferTransaction(category.getId(), from.getId(), to.getId(), fromAmount, toAmount, date, info);
        int fromId = ids[0];
        int toId = ids[1];
        Transaction fromTransaction = new Transaction(fromId, category.getId(), fromAmount, date, info, to.getId(), toId);
        Transaction toTransaction = new Transaction(toId, category.getId(), toAmount, date, info, from.getId(), fromId);
        return new Transaction[]{fromTransaction, toTransaction};
    }

    void updateTransaction(
            int id,
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info
    ) throws DatabaseError {
        db.updateTransaction(id, category, account, amount, date, info);
    }

    void deleteTransaction(int id) throws DatabaseError {
        db.deleteTransaction(id);
    }
}