package database;

import model.*;

import java.util.Date;
import java.util.List;

public interface Database {
    List<Account> getAllAccounts() throws DatabaseError;

    Account getAccount(int id) throws DatabaseError;

    int createAccount(String name, String institution, int currencyId) throws DatabaseError;

    void updateAccount(int id, String name, String institution) throws DatabaseError;

    void archiveAccount(int id) throws DatabaseError;

    void deleteAccount(int id) throws DatabaseError;

    Decimal getAccountBalance(int accountId) throws DatabaseError;

    int createCategory(String name, CategoryType type, CategoryGroup group) throws DatabaseError;

    void updateCategory(int id, String name, CategoryType type, CategoryGroup group) throws DatabaseError;

    void deleteCategory(int id) throws DatabaseError;

    List<Category> getAllCategories() throws DatabaseError;

    Category getCategory(int id) throws DatabaseError;

    int createCurrency(String code, String symbol) throws DatabaseError;

    void updateCurrency(int id, String code, String symbol) throws DatabaseError;

    void deleteCurrency(int id) throws DatabaseError;

    List<Currency> getAllCurrencies() throws DatabaseError;

    Currency getCurrency(int id) throws DatabaseError;

    int createTransaction(
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info
    ) throws DatabaseError;

    void updateTransaction(
            int id,
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info
    ) throws DatabaseError;

    void deleteTransaction(int id) throws DatabaseError;

    int[] createTransferTransaction(
            int category,
            int fromAccount,
            int toAccount,
            Decimal fromAmount,
            Decimal toAmount,
            Date date,
            String info
    ) throws DatabaseError;

    void deleteTransferTransaction(int id) throws DatabaseError;

    List<Transaction> getTransactionsForAccount(int accountId) throws DatabaseError;

    List<Transaction> getTransactionsForCategory(int accountId) throws DatabaseError;

    Transaction getTransaction(int id) throws DatabaseError;
}
