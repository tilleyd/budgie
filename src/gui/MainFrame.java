package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton addButton;
    private JButton deleteButton;
    private JComboBox localCurrencyBox;
    private JTable categoriesTable;
    private JButton addButton1;
    private JButton deleteButton1;

    public MainFrame() {
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

        initAccountsTableHeaders();
        loadCurrencies();
        loadAccounts();
        calculateBalance();
    }

    public JComponent getRoot() {
        return panel;
    }

    private void initAccountsTableHeaders() {
        DefaultTableModel model = (DefaultTableModel)accountsTable.getModel();
        model.addColumn("Account");
        model.addColumn("Type");
        model.addColumn("Balance");
    }

    private void loadCurrencies() {
        String[] currencies = {"ZAR", "CAD"};
        accountsCurrencyBox.addItem("Default");
        for (String currency : currencies) {
            accountsCurrencyBox.addItem(currency);
        }
    }

    private void loadAccounts() {
        // TODO
        DefaultTableModel model = (DefaultTableModel)accountsTable.getModel();
        model.addRow(new Object[]{"SB Checking", "Checking", "R 1000.00"});
    }

    private void calculateBalance() {
        // TODO
        balanceField.setText("R 1000.00");
    }
}
