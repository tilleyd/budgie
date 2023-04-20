package database;

import model.*;

import java.util.Date;
import java.util.List;

public interface Database {
    List<Account> getAllAccounts() throws DatabaseError;

    Account getAccount(int id);

    int createAccount(String name, String institution, Currency currency);

    void updateAccount(int id, String name, String institution);

    void archiveAccount(int id);

    void deleteAccount(int id);

    Decimal getAccountBalance(int accountId) throws DatabaseError;

    int createCategory(String name, CategoryType type, CategoryGroup group);

    void updateCategory(int id, String name, CategoryType type, CategoryGroup group);

    void deleteCategory(int id);

    List<Category> getAllCategories();

    Category getCategory(int id);

    int createCurrency(String code, String symbol);

    void updateCurrency(int id, String code, String symbol);

    void deleteCurrency(int id);

    List<Currency> getAllCurrencies() throws DatabaseError;

    Currency getCurrency(int id);

    int createTransaction(
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info,
            Account transferAccount
    );

    void updateTransaction(
            int id,
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info,
            Account transferAccount
    );

    void deleteTransaction(int id);

    List<Transaction> getTransactionsForAccount(int accountId) throws DatabaseError;

    Transaction getTransaction(int id);
}
