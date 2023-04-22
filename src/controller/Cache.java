package controller;

import java.util.List;
import java.util.HashMap;

import database.Database;
import database.DatabaseError;
import model.*;

public class Cache {
    final private Database database;
    final private HashMap<Integer, Category> categories;
    final private HashMap<Integer, Currency> currencies;
    final private HashMap<Integer, Account> accounts;

    public Cache(Database db) {
        database = db;
        categories = new HashMap<>();
        currencies = new HashMap<>();
        accounts = new HashMap<>();
    }

    public List<Category> getAllCategories() throws DatabaseError {
        List<Category> fetched = database.getAllCategories();
        categories.clear();
        for (Category c : fetched) {
            categories.put(c.getId(), c);
        }
        return fetched;
    }

    public Category getCategory(int id) throws DatabaseError {
        Category fromCache = categories.get(id);
        if (fromCache != null) {
            return fromCache;
        }

        // try to fetch from database
        Category fromDb = database.getCategory(id);
        if (fromDb != null) {
            categories.put(fromDb.getId(), fromDb);
            return fromDb;
        }

        // does not exist
        return null;
    }

    public List<Currency> getAllCurrencies() throws DatabaseError {
        List<Currency> fetched = database.getAllCurrencies();
        currencies.clear();
        for (Currency c : fetched) {
            currencies.put(c.getId(), c);
        }
        return fetched;
    }

    public Currency getCurrency(int id) throws DatabaseError {
        Currency fromCache = currencies.get(id);
        if (fromCache != null) {
            return fromCache;
        }

        // try to fetch from database
        Currency fromDb = database.getCurrency(id);
        if (fromDb != null) {
            currencies.put(fromDb.getId(), fromDb);
            return fromDb;
        }

        // does not exist
        return null;
    }

    public List<Account> getAllAccounts() throws DatabaseError {
        List<Account> fetched = database.getAllAccounts();
        accounts.clear();
        for (Account a : fetched) {
            accounts.put(a.getId(), a);
        }
        return fetched;
    }

    public Account getAccount(int id) throws DatabaseError {
        Account fromCache = accounts.get(id);
        if (fromCache != null) {
            return fromCache;
        }

        // try to fetch from database
        Account fromDb = database.getAccount(id);
        if (fromDb != null) {
            accounts.put(fromDb.getId(), fromDb);
            return fromDb;
        }

        // does not exist
        return null;
    }
}
