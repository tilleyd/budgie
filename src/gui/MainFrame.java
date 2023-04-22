package gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.Cache;
import database.Database;
import database.DatabaseError;
import model.*;

public class MainFrame {
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JTable accountsTable;
    private JButton newAccountButton;
    private JButton editAccountButton;
    private JTextField balanceField;
    private JComboBox<String> accountsCurrencyBox;
    private JList<String> accountsList;
    private JTable transactionsTable;
    private JButton addTransactionButton;
    private JButton deleteTransactionButton;
    private JTable currenciesTable;
    private JButton addCurrencyButton;
    private JButton deleteCurrencyButton;
    private JComboBox<String> localCurrencyBox;
    private JTable categoriesTable;
    private JButton addCategoryButton;
    private JButton deleteCategoryButton;

    private final Database database;
    private final Cache cache;

    private List<Account> accounts;
    private List<Currency> currencies;

    public MainFrame(Database db) {
        database = db;
        cache = new Cache(db);

        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NewAccountDialog dialog = new NewAccountDialog();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        editAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AccountSettingsDialog dialog = new AccountSettingsDialog();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        addTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NewTransactionDialog dialog = new NewTransactionDialog();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int idx = tabbedPane.getSelectedIndex();
                if (idx == 0) {
                    refreshDashboardBalances();
                }
            }
        });
        accountsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                populateTransactionsForAccount(accounts.get(accountsList.getSelectedIndex()));
            }
        });

        initTableHeaders();
        loadCurrencies();
        loadAccounts();

        initDashboardTab();
        initCategoriesTab();
        initCurrenciesTab();
        // NOTE: load transactions last to improve cache hits with categories
        initTransactionsTab();

        refreshDashboardBalances();
    }

    public JComponent getRoot() {
        return panel;
    }

    private void refreshDashboardBalances() {
        dashboardRefreshTable();
        dashboardCalculateBalance();
    }

    private void initDashboardTab() {
        accountsCurrencyBox.removeAllItems();
        accountsCurrencyBox.addItem("Default");

        // populate the currency selection box
        for (Currency c : currencies) {
            accountsCurrencyBox.addItem(c.getCode());
        }
    }

    private void initTransactionsTab() {
        DefaultListModel<String> model = new DefaultListModel<>();
        try {
            List<Account> accounts = cache.getAllAccounts();
            accountsList.removeAll();
            for (Account account : accounts) {
                model.addElement(account.getName());
            }
            accountsList.setModel(model);
            accountsList.setSelectedIndex(0);
        } catch (DatabaseError e) {
            System.out.println("Failed to load accounts: " + e.getMessage());
        }
    }

    private void initCategoriesTab() {
        try {
            List<Category> categories = cache.getAllCategories();
            DefaultTableModel model = (DefaultTableModel)categoriesTable.getModel();
            model.setRowCount(0);
            for (Category c : categories) {
                String name = c.getName();
                String type = c.getType().toFriendlyString();
                String group = c.getGroup().toFriendlyString();
                model.addRow(new Object[]{name, type, group});
            }
        } catch (DatabaseError e) {
            System.out.println("Failed to load categories: " + e.getMessage());
        }
    }

    private void initCurrenciesTab() {
        try {
            List<Currency> currencies = cache.getAllCurrencies();
            DefaultTableModel model = (DefaultTableModel)currenciesTable.getModel();
            model.setRowCount(0);
            for (Currency c : currencies) {
                String code = c.getCode();
                String symbol = c.getSymbol();
                model.addRow(new Object[]{code, symbol});
            }
        } catch (DatabaseError e) {
            System.out.println("Failed to load currencies: " + e.getMessage());
        }
    }

    private void initTableHeaders() {
        // dashboard accounts table
        DefaultTableModel model = (DefaultTableModel)accountsTable.getModel();
        model.addColumn("Account");
        model.addColumn("Balance");

        // transactions table
        model = (DefaultTableModel)transactionsTable.getModel();
        model.addColumn("Date");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("From/To");
        model.addColumn("Info");

        // categories table
        model = (DefaultTableModel)categoriesTable.getModel();
        model.addColumn("Category");
        model.addColumn("Type");
        model.addColumn("Group");

        // currencies table
        model = (DefaultTableModel)currenciesTable.getModel();
        model.addColumn("Code");
        model.addColumn("Symbol");
    }

    private void loadCurrencies() {
        try {
            currencies = cache.getAllCurrencies();
        } catch (DatabaseError e) {
            System.out.println("Failed to load currencies: " + e.getMessage());
        }
    }

    private void loadAccounts() {
        try {
            accounts = cache.getAllAccounts();
        } catch (DatabaseError e) {
            System.out.println("Failed to load accounts: " + e.getMessage());
        }
    }

    private void dashboardRefreshTable() {
        DefaultTableModel model = (DefaultTableModel)accountsTable.getModel();
        model.setRowCount(0);
        for (Account a : accounts) {
            String name = a.getName();
            String balanceAmount = "0.00";
            try {
                balanceAmount = database.getAccountBalance(a.getId()).toString();
            } catch (DatabaseError e) {
                System.out.println("Failed to fetch account balance: " + e.getMessage());
            }
            String balance = a.getCurrency().getSymbol() + " " + balanceAmount;
            model.addRow(new Object[]{name, balance});
        }
    }

    private void dashboardCalculateBalance() {
        // TODO
        balanceField.setText("Unimplemented");
    }

    private void populateTransactionsForAccount(Account account) {
        DefaultTableModel model = (DefaultTableModel)transactionsTable.getModel();
        model.setRowCount(0);
        try {
            List<Transaction> transactions = database.getTransactionsForAccount(account.getId());
            for (Transaction t : transactions) {
                String date = t.getDate().toString();  // TODO use local format
                String amount = t.getAmount().toString();
                String category = cache.getCategory(t.getCategoryId()).getName();
                Integer transferAccountId = t.getTransferAccountId();

                String transferAccount;
                if (transferAccountId == null) {
                    transferAccount = "";
                } else {
                    transferAccount = cache.getAccount(transferAccountId).getName();
                }
                String info = t.getInfo();
                model.addRow(new Object[]{date, amount, category, transferAccount, info});
            }
        } catch (DatabaseError e) {
            System.out.println("Failed to load transactions: " + e.getMessage());
        }
    }
}
