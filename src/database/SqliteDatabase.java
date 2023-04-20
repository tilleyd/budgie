package database;

import model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDatabase implements Database {
    String path;
    Connection connection;

    public SqliteDatabase(String databasePath) {
        path = databasePath;
        connection = null;
    }

    public void connect() throws DatabaseError {
        if (connection == null) {
            try {
                String connectionString = "jdbc:sqlite:" + path;
                connection = DriverManager.getConnection(connectionString);
            } catch (SQLException e) {
                throw new DatabaseError(e.getMessage());
            }
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignore) {}
            connection = null;
        }
    }

    public void init() {
        // TODO create tables and such
    }

    @Override
    public List<Account> getAllAccounts() throws DatabaseError {
        assertConnection();

        String sql = "SELECT account_id, name, institution, currency, code, symbol FROM accounts INNER JOIN currencies ON currencies.currency_id = accounts.currency";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);

            ArrayList<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                Currency currency = new Currency(rs.getInt("currency"), rs.getString("code"), rs.getString("symbol"));
                int id = rs.getInt("account_id");
                String name = rs.getString("name");
                String institution = rs.getString("institution");
                accounts.add(new Account(id, name, institution, currency));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public Account getAccount(int id) {
        return null;
    }

    @Override
    public int createAccount(String name, String institution, Currency currency) {
        return 0; // TODO
    }

    @Override
    public void updateAccount(int id, String name, String institution) {
        // TODO
    }

    @Override
    public void archiveAccount(int id) {
        // TODO
    }

    @Override
    public void deleteAccount(int id) {
        // TODO
    }

    @Override
    public Decimal getAccountBalance(int accountId) throws DatabaseError {
        assertConnection();

        String sql = "SELECT sum(amount) AS balance FROM transactions WHERE account = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Decimal(rs.getInt("balance"));
            } else {
                throw new DatabaseError("No result returned for account balance");
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public int createCategory(String name, CategoryType type, CategoryGroup group) {
        return 0; // TODO
    }

    @Override
    public void updateCategory(int id, String name, CategoryType type, CategoryGroup group) {
        // TODO
    }

    @Override
    public void deleteCategory(int id) {
        // TODO only if no transactions use it
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>();
    }

    @Override
    public Category getCategory(int id) {
        return null;
    }

    @Override
    public int createCurrency(String code, String symbol) {
        return 0; // TODO
    }

    @Override
    public void updateCurrency(int id, String code, String symbol) {
        // TODO
    }

    @Override
    public void deleteCurrency(int id) {
        // TODO
    }

    @Override
    public List<Currency> getAllCurrencies() throws DatabaseError {
        assertConnection();

        String sql = "SELECT currency_id, code, symbol FROM currencies";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);

            ArrayList<Currency> currencies = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("currency_id");
                String code = rs.getString("code");
                String symbol = rs.getString("symbol");
                Currency currency = new Currency(id, code, symbol);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public Currency getCurrency(int id) {
        return null;
    }

    @Override
    public int createTransaction(
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info,
            Account transferAccount
    ) {
        return 0; // TODO
    }

    @Override
    public void updateTransaction(
            int id,
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info,
            Account transferAccount
    ) {
        // TODO
    }

    @Override
    public void deleteTransaction(int id) {
        // TODO
    }

    @Override
    public List<Transaction> getTransactionsForAccount(int accountId) throws DatabaseError {
        assertConnection();

        String sql = "SELECT transaction_id, amount, date, info, accounts.name AS transfer_name, categories.name AS category_name FROM transactions " +
                "INNER JOIN categories ON categories.category_id = transactions.category " +
                "LEFT OUTER JOIN accounts ON accounts.account_id = transactions.transfer_account " +
                "WHERE account = ? " +
                "ORDER BY date DESC";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            ArrayList<Transaction> transactions = new ArrayList<>();
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                String categoryName = rs.getString("category_name");
                Decimal amount = new Decimal(rs.getInt("amount"));
                Date date;
                try {
                    date = isoFormat.parse(rs.getString("date"));
                } catch (ParseException e) {
                    throw new DatabaseError("Failed to parse date");
                }
                String info = rs.getString("info");
                String transferAccountName = rs.getString("transfer_name");
                Transaction transaction = new Transaction(id, categoryName, amount, date, info, transferAccountName);
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public Transaction getTransaction(int id) {
        return null;
    }

    private void assertConnection() throws DatabaseError {
        if (connection == null) {
            throw new DatabaseError("No database connection");
        }
    }
}
