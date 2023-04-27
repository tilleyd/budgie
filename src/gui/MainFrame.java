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
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import controller.Cache;
import controller.Controller;
import controller.LogicError;
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
    private JButton editCategoryButton;

    private final Database database;
    private final Controller controller;
    private final Cache cache;

    private List<Account> accounts;
    private List<Currency> currencies;
    private List<Category> categories;
    private List<Transaction> transactions;

    public MainFrame(Database db) {
        database = db;
        cache = new Cache(db);
        controller = new Controller(db);

        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                promptCreateAccount();
            }
        });
        editAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                promptEditAccount();
            }
        });
        addTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                promptAddTransaction();
            }
        });
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int idx = tabbedPane.getSelectedIndex();
                if (idx == 0) {
                    refreshAccountsTable();
                    calculateOverallBalance();
                }
            }
        });
        accountsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                refreshTransactions();
            }
        });
        deleteTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onDeleteTransaction();
            }
        });
        addCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onCreateCategory();
            }
        });
        editCategoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onEditCategory();
            }
        });

        initTableHeaders();

        refreshCurrencies();
        refreshCategories();
        refreshAccounts();
    }

    public JComponent getRoot() {
        return panel;
    }

    private void refreshCurrencies() {
        try {
            currencies = cache.getAllCurrencies();
        } catch (DatabaseError e) {
            currencies = new ArrayList<>();
            System.out.println("Failed to load currencies: " + e.getMessage());
        }

        // refresh currencies table
        DefaultTableModel model = (DefaultTableModel)currenciesTable.getModel();
        model.setRowCount(0);
        for (Currency c : currencies) {
            String code = c.getCode();
            String symbol = c.getSymbol();
            model.addRow(new Object[]{code, symbol});
        }

        // refresh dashboard currency box
        accountsCurrencyBox.removeAllItems();
        accountsCurrencyBox.addItem("Default");
        for (Currency c : currencies) {
            accountsCurrencyBox.addItem(c.getCode());
        }
    }

    private void refreshCategories() {
        try {
            categories = cache.getAllCategories();

            // update categories table
            DefaultTableModel model = (DefaultTableModel)categoriesTable.getModel();
            model.setRowCount(0);
            for (Category c : categories) {
                String name = c.getName();
                String group = c.getGroup().toString();
                String type = c.getType().toString();
                model.addRow(new Object[]{name, group, type});
            }
        } catch (DatabaseError e) {
            System.out.println("Failed to load categories: " + e.getMessage());
        }
    }

    private void refreshAccounts() {
        try {
            accounts = cache.getAllAccounts();
        } catch (DatabaseError e) {
            accounts = new ArrayList<>();
            System.out.println("Failed to load accounts: " + e.getMessage());
        }

        refreshAccountsTable();
        calculateOverallBalance();
        balanceField.setText("Unimplemented");
    }

    private void refreshTransactions() {
        int selectedIdx = accountsList.getSelectedIndex();
        if (selectedIdx >= 0) {
            populateTransactionsForAccount(accounts.get(selectedIdx));
        } else {
            populateTransactionsForAccount(null);
        }
    }

    private void refreshAccountsTable() {
        // clear the table
        DefaultTableModel tableModel = (DefaultTableModel)accountsTable.getModel();
        tableModel.setRowCount(0);

        for (Account a : accounts) {
            // add to the accounts table
            String name = a.getName();
            String balance = "0.00";
            try {
                Decimal balanceAmount = database.getAccountBalance(a.getId());
                Currency currency = cache.getCurrency(a.getCurrencyId());
                balance = currency.getSymbol() + " " + balanceAmount.toString();
            } catch (DatabaseError e) {
                System.out.println("Failed to fetch account balance: " + e.getMessage());
            }
            tableModel.addRow(new Object[]{name, balance});

            // add to the transactions accounts list
            DefaultListModel<String> listModel = new DefaultListModel<>();
            accountsList.removeAll();
            accountsList.setSelectedIndex(-1);
            for (Account account : accounts) {
                listModel.addElement(account.getName());
            }
            accountsList.setModel(listModel);
        }
    }

    private void calculateOverallBalance() {
        // TODO
    }

    private void initTableHeaders() {
        // dashboard accounts table
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsTable.setDefaultEditor(Object.class, null);
        DefaultTableModel model = (DefaultTableModel)accountsTable.getModel();
        model.addColumn("Account");
        model.addColumn("Balance");

        // transactions table
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionsTable.setDefaultEditor(Object.class, null);
        model = (DefaultTableModel)transactionsTable.getModel();
        model.addColumn("Date");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("From/To");
        model.addColumn("Info");

        // categories table
        categoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesTable.setDefaultEditor(Object.class, null);
        model = (DefaultTableModel)categoriesTable.getModel();
        model.addColumn("Category");
        model.addColumn("Group");
        model.addColumn("Type");

        // currencies table
        currenciesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        currenciesTable.setDefaultEditor(Object.class, null);
        model = (DefaultTableModel)currenciesTable.getModel();
        model.addColumn("Code");
        model.addColumn("Symbol");
    }

    private void populateTransactionsForAccount(Account account) {
        DefaultTableModel model = (DefaultTableModel)transactionsTable.getModel();
        model.setRowCount(0);
        if (account == null) {
            transactions = null;
            return;
        }

        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy E");
        try {
            Currency currency = cache.getCurrency(account.getCurrencyId());
            transactions = database.getTransactionsForAccount(account.getId());
            for (Transaction t : transactions) {
                String date = fmt.format(t.getDate());
                String amount = currency.getSymbol() + " " + t.getAmount().toString();
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

    private void promptEditAccount() {
        int selectedIdx = accountsTable.getSelectedRow();
        if (selectedIdx < 0) {
            return;
        }

        Account selectedAccount = accounts.get(selectedIdx);
        String currencyCode = "";
        try {
            currencyCode = cache.getCurrency(selectedAccount.getCurrencyId()).getCode();
        } catch (DatabaseError ignore) {}

        AccountSettingsDialog dialog = new AccountSettingsDialog(selectedAccount, currencyCode);
        dialog.pack();
        dialog.setLocationRelativeTo(editAccountButton);
        dialog.setVisible(true);

        String closeOperation = dialog.getCloseOperation();
        switch (closeOperation) {
            case "SAVE" -> {
                try {
                    String name = dialog.getName();
                    String institution = dialog.getInstitution();
                    controller.updateAccount(selectedAccount.getId(), name, institution);
                    refreshAccounts();
                } catch (DatabaseError e) {
                    JOptionPane.showMessageDialog(getRoot(), e.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
            case "DELETE" -> {
                try {
                    controller.deleteAccount(selectedAccount.getId());
                    refreshAccounts();
                } catch (LogicError | DatabaseError e) {
                    JOptionPane.showMessageDialog(getRoot(), e.getMessage(), "Delete Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
            case "ARCHIVE" -> {
                try {
                    controller.archiveAccount(selectedAccount.getId());
                    refreshAccounts();
                } catch (DatabaseError e) {
                    JOptionPane.showMessageDialog(getRoot(), e.getMessage(), "Archive Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void promptCreateAccount() {
        NewAccountDialog dialog = new NewAccountDialog(currencies);
        dialog.pack();
        dialog.setLocationRelativeTo(newAccountButton);
        dialog.setVisible(true);

        String closeOperation = dialog.getCloseOperation();
        if (closeOperation.equals("CREATE")) {
            try {
                String name = dialog.getName();
                String institution = dialog.getInstitution();
                Currency currency = currencies.get(dialog.getCurrencyIndex());
                controller.createAccount(name, institution, currency);
                refreshAccounts();
            } catch (DatabaseError e) {
                JOptionPane.showMessageDialog(getRoot(), e.getMessage(), "Create Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void promptAddTransaction() {
        NewTransactionDialog dialog = new NewTransactionDialog(controller, cache);
        dialog.pack();
        dialog.setLocationRelativeTo(addTransactionButton);
        dialog.setVisible(true);

        refreshTransactions();
    }

    private void onDeleteTransaction() {
        int selectedIdx = transactionsTable.getSelectedRow();
        if (selectedIdx < 0 || transactions == null) {
            return;
        }

        Transaction transaction = transactions.get(selectedIdx);

        String message = "Delete transaction with value " + transaction.getAmount().toString() + "?";
        if (transaction.getTransferTransactionId() != null) {
            message = message + " This will also delete the transaction from the transferred account.";
        }
        int confirmation = JOptionPane.showConfirmDialog(getRoot(), message, "Delete Transaction", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteTransaction(transaction);
                refreshTransactions();
            } catch (DatabaseError e) {
                JOptionPane.showMessageDialog(getRoot(), "Deletion failed: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCreateCategory() {
        NewCategoryDialog dialog = new NewCategoryDialog();
        dialog.pack();
        dialog.setLocationRelativeTo(addCategoryButton);
        dialog.setVisible(true);

        String closeOperation = dialog.getCloseOperation();
        if (closeOperation.equals("CREATE")) {
            try {
                String name = dialog.getCategoryName();
                CategoryType type = dialog.getCategoryType();
                CategoryGroup group = dialog.getCategoryGroup();
                controller.createCategory(name, type, group);
                refreshCategories();
            } catch (DatabaseError e) {
                JOptionPane.showMessageDialog(getRoot(), e.getMessage(), "Create Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEditCategory() {
        int selectedIdx = categoriesTable.getSelectedRow();
        if (selectedIdx < 0) {
            return;
        }

        Category selected = categories.get(selectedIdx);
        EditCategoryDialog dialog = new EditCategoryDialog(selected);
        dialog.pack();
        dialog.setLocationRelativeTo(editCategoryButton);
        dialog.setVisible(true);

        String closeOperation = dialog.getCloseOperation();
        switch (closeOperation) {
            case "DELETE" -> {
                try {
                    controller.deleteCategory(selected.getId());
                    refreshCategories();
                } catch (DatabaseError e) {
                    JOptionPane.showMessageDialog(getRoot(), "Deletion failed: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
                } catch (LogicError e) {
                    JOptionPane.showMessageDialog(getRoot(), e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
            case "SAVE" -> {
                String name = dialog.getCategoryName();
                CategoryType type = dialog.getCategoryType();
                CategoryGroup group = dialog.getCategoryGroup();
                try {
                    controller.updateCategory(selected.getId(), name, type, group);
                    refreshCategories();
                } catch (DatabaseError e) {
                    JOptionPane.showMessageDialog(getRoot(), "Update failed: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
