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

        String sql = "SELECT account_id, name, institution, currency FROM accounts WHERE archived = FALSE";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);

            ArrayList<Account> accounts = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("account_id");
                String name = rs.getString("name");
                String institution = rs.getString("institution");
                int currencyId = rs.getInt("currency");
                accounts.add(new Account(id, name, institution, false, currencyId));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public Account getAccount(int id) throws DatabaseError {
        assertConnection();

        String sql = "SELECT name, institution, archived, currency FROM accounts WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String institution = rs.getString("institution");
                boolean archived = rs.getBoolean("archived");
                int currencyId = rs.getInt("currency");
                return new Account(id, name, institution, archived, currencyId);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public int createAccount(String name, String institution, int currencyId) throws DatabaseError {
        assertConnection();

        String sql = "INSERT INTO accounts (name, institution, archived, currency) VALUES (?, ?, FALSE, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, institution);
            ps.setInt(3, currencyId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseError("Account creation failed (no rows affected)");
            }

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            } else {
                throw new DatabaseError("Account creation failed (no ID returned)");
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public void updateAccount(int id, String name, String institution) throws DatabaseError {
        assertConnection();

        String sql = "UPDATE accounts SET name = ?, institution = ? WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, institution);
            ps.setInt(3, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseError("Account update failed (no rows affected)");
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public void archiveAccount(int id) throws DatabaseError {
        assertConnection();

        String sql = "UPDATE accounts SET archived = TRUE WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseError("Account archival failed (no rows affected)");
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public void deleteAccount(int id) throws DatabaseError {
        assertConnection();

        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseError("Account deletion failed (no rows affected)");
            }
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
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
    public int createCategory(String name, CategoryType type, CategoryGroup group) throws DatabaseError {
        // TODO
        throw new DatabaseError("createCategory not implemented");
    }

    @Override
    public void updateCategory(int id, String name, CategoryType type, CategoryGroup group) throws DatabaseError {
        // TODO
        throw new DatabaseError("updateCategory not implemented");
    }

    @Override
    public void deleteCategory(int id) throws DatabaseError {
        // TODO only if no transactions use it
        throw new DatabaseError("deleteCategory not implemented");
    }

    @Override
    public List<Category> getAllCategories() throws DatabaseError {
        assertConnection();

        String sql = "SELECT category_id, name, " +
                "category_groups.description AS \"group\", category_types.description AS \"type\" FROM categories " +
                "INNER JOIN category_groups ON category_group_id = \"group\" " +
                "INNER JOIN category_types ON category_type_id = \"type\"";
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);

            ArrayList<Category> categories = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("category_id");
                String name = rs.getString("name");
                CategoryType type = CategoryType.fromString(rs.getString("type"));
                CategoryGroup group = CategoryGroup.fromString(rs.getString("group"));
                Category category = new Category(id, name, type, group);
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public Category getCategory(int id) throws DatabaseError {
        // TODO
        throw new DatabaseError("getCategory not implemented");
    }

    @Override
    public int createCurrency(String code, String symbol) throws DatabaseError {
        // TODO
        throw new DatabaseError("createCurrency not implemented");
    }

    @Override
    public void updateCurrency(int id, String code, String symbol) throws DatabaseError {
        // TODO
        throw new DatabaseError("updateCurrency not implemented");
    }

    @Override
    public void deleteCurrency(int id) throws DatabaseError {
        // TODO
        throw new DatabaseError("deleteCurrency not implemented");
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
    public Currency getCurrency(int id) throws DatabaseError {
        // TODO
        throw new DatabaseError("getCurrency not implemented");
    }

    @Override
    public int createTransaction(
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info
    ) throws DatabaseError {
        // TODO
        throw new DatabaseError("createTransaction not implemented");
    }

    @Override
    public void updateTransaction(
            int id,
            Category category,
            Account account,
            Decimal amount,
            Date date,
            String info
    ) throws DatabaseError {
        // TODO
        throw new DatabaseError("updateTransaction not implemented");
    }

    @Override
    public void deleteTransaction(int id) throws DatabaseError {
        // TODO
        throw new DatabaseError("deleteTransaction not implemented");
    }

    @Override
    public int[] createTransferTransaction(
            int category,
            int fromAccount,
            int toAccount,
            Decimal fromAmount,
            Decimal toAmount,
            Date date,
            String info
    ) throws DatabaseError {
        // TODO
        throw new DatabaseError("createTransferTransaction not implemented");
    }

    @Override
    public void deleteTransferTransaction(int id) throws DatabaseError {
        // TODO
        throw new DatabaseError("deleteTransferTransaction not implemented");
    }

    @Override
    public List<Transaction> getTransactionsForAccount(int accountId) throws DatabaseError {
        assertConnection();

        String sql = "SELECT transaction_id, category, amount, account, date, info, transfer_account, " +
                "transfer_transaction FROM transactions " +
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
                int categoryId = rs.getInt("category");
                Integer transferAccountId = (Integer)rs.getObject("transfer_account");
                Integer transferTransactionId = (Integer)rs.getObject("transfer_transaction");
                Decimal amount = new Decimal(rs.getInt("amount"));
                String info = rs.getString("info");

                Date date;
                try {
                    date = isoFormat.parse(rs.getString("date"));
                } catch (ParseException e) {
                    throw new DatabaseError("Failed to parse date");
                }

                Transaction transaction = new Transaction(id, categoryId, amount, date, info, transferAccountId, transferTransactionId);
                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            throw new DatabaseError(e.getMessage());
        }
    }

    @Override
    public Transaction getTransaction(int id) throws DatabaseError {
        // TODO
        throw new DatabaseError("getTransaction not implemented");
    }

    private void assertConnection() throws DatabaseError {
        if (connection == null) {
            throw new DatabaseError("No database connection");
        }
    }
}
