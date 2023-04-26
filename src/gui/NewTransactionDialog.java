package gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

import model.*;
import controller.Cache;
import controller.Controller;
import controller.LogicError;
import database.DatabaseError;

public class NewTransactionDialog extends JDialog {
    private JPanel contentPane;
    private JButton addButton;
    private JButton cancelButton;
    private JCheckBox keepOpenCheckBox;
    private JTabbedPane tabbedPane;
    private JTextField expenseInfoField;
    private JComboBox<Account> expenseAccountBox;
    private JLabel expenseCurrencyLabel;
    private JComboBox<Category> expenseCategoryBox;
    private JLabel incomeCurrencyLabel;
    private JTextField incomeMonthField;
    private JTextField incomeDayField;
    private JTextField incomeYearField;
    private JTextField expenseMonthField;
    private JTextField expenseDayField;
    private JTextField expenseYearField;
    private JTextField incomeInfoField;
    private JComboBox<Account> incomeAccountBox;
    private JComboBox<Category> incomeCategoryBox;
    private JLabel transferOutCurrencyLabel;
    private JLabel transferInCurrencyLabel;
    private JComboBox<Account> transferFromAccountBox;
    private JComboBox<Account> transferToAccountBox;
    private JComboBox<Category> transferCategoryBox;
    private JTextField transferInfoField;
    private JTextField transferMonthField;
    private JTextField transferDayField;
    private JTextField transferYearField;
    private JTextField expenseAmountField;
    private JTextField incomeAmountField;
    private JTextField transferOutAmountField;
    private JTextField transferInAmountField;

    private final Controller controller;
    private final Cache cache;

    public NewTransactionDialog(Controller controller, Cache cache) {
        this.controller = controller;
        this.cache = cache;
        initForms();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        incomeAccountBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Account a = (Account)incomeAccountBox.getSelectedItem();
                if (a == null) {
                    return;
                }
                try {
                    Currency c = cache.getCurrency(a.getCurrencyId());
                    incomeCurrencyLabel.setText(c.getCode());
                } catch (DatabaseError e) {
                    System.out.println("Failed to load currency: " + e.getMessage());
                }
            }
        });
        expenseAccountBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Account a = (Account)expenseAccountBox.getSelectedItem();
                if (a == null) {
                    return;
                }
                try {
                    Currency c = cache.getCurrency(a.getCurrencyId());
                    expenseCurrencyLabel.setText(c.getCode());
                } catch (DatabaseError e) {
                    System.out.println("Failed to load currency: " + e.getMessage());
                }
            }
        });
        transferFromAccountBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Account a = (Account)transferFromAccountBox.getSelectedItem();
                if (a == null) {
                    return;
                }
                try {
                    Currency c = cache.getCurrency(a.getCurrencyId());
                    transferOutCurrencyLabel.setText(c.getCode());
                } catch (DatabaseError e) {
                    System.out.println("Failed to load currency: " + e.getMessage());
                }
            }
        });
        transferToAccountBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Account a = (Account)transferToAccountBox.getSelectedItem();
                if (a == null) {
                    return;
                }
                try {
                    Currency c = cache.getCurrency(a.getCurrencyId());
                    transferInCurrencyLabel.setText(c.getCode());
                } catch (DatabaseError e) {
                    System.out.println("Failed to load currency: " + e.getMessage());
                }
            }
        });
    }

    private void onAdd() {
        int tab = tabbedPane.getSelectedIndex();
        boolean success = switch (tab) {
            case 0 -> onAddExpense();
            case 1 -> onAddIncome();
            case 2 -> onAddTransfer();
            default -> false;
        };

        if (success) {
            if (keepOpenCheckBox.isSelected()) {
                clearForNext();
            } else {
                dispose();
            }
        }

    }

    private void onCancel() {
        dispose();
    }

    private boolean onAddIncome() {
        Account toAccount = (Account)incomeAccountBox.getSelectedItem();
        if (toAccount == null) {
            JOptionPane.showMessageDialog(this, "No account selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Category category = (Category)incomeCategoryBox.getSelectedItem();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "No category selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date date;
        try {
            int day = Integer.parseInt(incomeDayField.getText());
            int month = Integer.parseInt(incomeMonthField.getText());
            int year = Integer.parseInt(incomeYearField.getText());
            Calendar calendar = Calendar.getInstance();
            //noinspection MagicConstant
            calendar.set(year, month - 1, day);
            date = calendar.getTime();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid date value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Decimal amount;
        try {
            amount = Decimal.parse(incomeAmountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (amount.isNegative()) {
            JOptionPane.showMessageDialog(this, "Amount must be positive", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String info = incomeInfoField.getText();

        try {
            controller.createSimpleTransaction(category, toAccount, amount, date, info);
        } catch (DatabaseError e) {
            JOptionPane.showMessageDialog(this, "Failed to create transaction: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (LogicError e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean onAddExpense() {
        Account fromAccount = (Account)expenseAccountBox.getSelectedItem();
        if (fromAccount == null) {
            JOptionPane.showMessageDialog(this, "No account selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Category category = (Category)expenseCategoryBox.getSelectedItem();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "No category selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date date;
        try {
            int day = Integer.parseInt(expenseDayField.getText());
            int month = Integer.parseInt(expenseMonthField.getText());
            int year = Integer.parseInt(expenseYearField.getText());
            Calendar calendar = Calendar.getInstance();
            //noinspection MagicConstant
            calendar.set(year, month - 1, day);
            date = calendar.getTime();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid date value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Decimal amount;
        try {
            amount = Decimal.parse(expenseAmountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (amount.isPositive()) {
            amount = amount.negate();
        }

        String info = expenseInfoField.getText();

        try {
            controller.createSimpleTransaction(category, fromAccount, amount, date, info);
        } catch (DatabaseError e) {
            JOptionPane.showMessageDialog(this, "Failed to create transaction: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (LogicError e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean onAddTransfer() {
        Account fromAccount = (Account)transferFromAccountBox.getSelectedItem();
        if (fromAccount == null) {
            JOptionPane.showMessageDialog(this, "No from account selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Account toAccount = (Account)transferToAccountBox.getSelectedItem();
        if (toAccount == null) {
            JOptionPane.showMessageDialog(this, "No to account selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Category category = (Category)transferCategoryBox.getSelectedItem();
        if (category == null) {
            JOptionPane.showMessageDialog(this, "No category selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Date date;
        try {
            int day = Integer.parseInt(transferDayField.getText());
            int month = Integer.parseInt(transferMonthField.getText());
            int year = Integer.parseInt(transferYearField.getText());
            Calendar calendar = Calendar.getInstance();
            //noinspection MagicConstant
            calendar.set(year, month - 1, day);
            date = calendar.getTime();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid date value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Decimal outAmount;
        try {
            outAmount = Decimal.parse(transferOutAmountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid out amount value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (outAmount.isPositive()) {
            outAmount = outAmount.negate();
        }

        Decimal inAmount;
        try {
            inAmount = Decimal.parse(transferInAmountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid in amount value: " + e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (inAmount.isNegative()) {
            JOptionPane.showMessageDialog(this, "In amount must be positive", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String info = transferInfoField.getText();

        try {
            controller.createTransferTransaction(category, fromAccount, toAccount, outAmount, inAmount, date, info);
        } catch (DatabaseError e) {
            JOptionPane.showMessageDialog(this, "Failed to create transaction: " + e.getMessage(), "Internal Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (LogicError e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void initForms() {
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        incomeDayField.setText(day);
        incomeMonthField.setText(month);
        incomeYearField.setText(year);
        expenseDayField.setText(day);
        expenseMonthField.setText(month);
        expenseYearField.setText(year);
        transferDayField.setText(day);
        transferMonthField.setText(month);
        transferYearField.setText(year);

        List<Account> accounts;
        try {
            accounts = cache.getAllAccounts();
        } catch (DatabaseError e) {
            accounts = new ArrayList<>();
            System.out.println("Failed loading accounts: " + e.getMessage());
        }
        for (Account a : accounts) {
            incomeAccountBox.addItem(a);
            expenseAccountBox.addItem(a);
            transferFromAccountBox.addItem(a);
            transferToAccountBox.addItem(a);
        }

        List<Category> categories;
        try {
            categories = cache.getAllCategories();
        } catch (DatabaseError e) {
            categories = new ArrayList<>();
            System.out.println("Failed loading categories: " + e.getMessage());
        }
        for (Category c : categories) {
            CategoryType type = c.getType();
            if (type == CategoryType.INCOME || type == CategoryType.INCOME_OR_EXPENSE) {
                incomeCategoryBox.addItem(c);
            }
            if (type == CategoryType.EXPENSE || type == CategoryType.INCOME_OR_EXPENSE) {
                expenseCategoryBox.addItem(c);
            }
            transferCategoryBox.addItem(c);
        }

        // clear combo box selections
        incomeCategoryBox.setSelectedIndex(-1);
        expenseCategoryBox.setSelectedIndex(-1);
        transferCategoryBox.setSelectedIndex(-1);
        incomeAccountBox.setSelectedIndex(-1);
        expenseAccountBox.setSelectedIndex(-1);
        transferFromAccountBox.setSelectedIndex(-1);
        transferToAccountBox.setSelectedIndex(-1);
    }

    private void clearForNext() {
        // clear only the amount, category, and info
        incomeAmountField.setText("0");
        incomeInfoField.setText("");
        incomeCategoryBox.setSelectedIndex(-1);
        expenseAmountField.setText("0");
        expenseInfoField.setText("");
        expenseCategoryBox.setSelectedIndex(-1);
        transferInAmountField.setText("0");
        transferOutAmountField.setText("0");
        transferInfoField.setText("");
        transferCategoryBox.setSelectedIndex(-1);
    }
}
