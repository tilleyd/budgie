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

import database.Database;
import database.DatabaseError;
import model.*;

public class MainFrame {
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JTable accountsTable;
    private JButton newAccountButton;
    private JButton accountSettingsButton;
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

    private List<Account> accounts;
    private List<Currency> currencies;
    private List<Category> categories;

    public MainFrame(Database db) {
        database = db;
        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NewAccountDialog dialog = new NewAccountDialog();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        accountSettingsButton.addActionListener(new ActionListener() {
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
        initTransactionsTab();
        initCategoriesTab();
        initCurrenciesTab();

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
            List<Account> accounts = database.getAllAccounts();
            accountsList.removeAll();
            for (Account account : accounts) {
                model.addElement(account.getName());
            }
            accountsList.setModel(model);
            accountsList.setSelectedIndex(0);
        } catch (DatabaseError e) {
            System.out.println(e.getMessage());
        }
    }

    private void initCategoriesTab() {
        // TODO
    }

    private void initCurrenciesTab() {
        // TODO
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
        // TODO

        // currencies table
        // TODO
    }

    private void loadCurrencies() {
        try {
            currencies = database.getAllCurrencies();
        } catch (DatabaseError e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadAccounts() {
        try {
            accounts = database.getAllAccounts();
        } catch (DatabaseError e) {
            System.out.println(e.getMessage());
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
                System.out.println(e.getMessage());
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
                String date = t.getDate().toString();
                String amount = t.getAmount().toString();
                String category = t.getCategoryName();
                String transferAccount = t.getTransferAccountName();
                if (transferAccount == null) {
                    transferAccount = "";
                }
                String info = t.getInfo();
                model.addRow(new Object[]{date, amount, category, transferAccount, info});
            }
        } catch (DatabaseError e) {
            System.out.println(e.getMessage());
        }
    }
}
