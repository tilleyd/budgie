package gui;

import javax.swing.*;
import java.awt.event.*;

public class NewTransactionDialog extends JDialog {
    private JPanel contentPane;
    private JButton addButton;
    private JButton cancelButton;
    private JCheckBox keepOpenCheckBox;
    private JTabbedPane tabbedPane;
    private JTextField expenseInfoField;
    private JComboBox expenseAccountBox;
    private JSpinner expenseAmount;
    private JLabel expenseCurrencyLabel;
    private JComboBox expenseCategoryBox;
    private JLabel incomeCurrencyLabel;
    private JTextField incomeMonthField;
    private JTextField incomeDayField;
    private JTextField incomeYearField;
    private JTextField expenseMonthField;
    private JTextField expenseDayField;
    private JTextField expenseYearField;
    private JTextField incomeInfoField;
    private JComboBox incomeAccountBox;
    private JSpinner incomeAmountSpinner;
    private JComboBox incomeCategoryBox;
    private JLabel transferOutCurrencyLabel;
    private JLabel transferInCurrencyLabel;
    private JCheckBox checkBox1;
    private JSpinner transferInAmountSpinner;
    private JSpinner transferOutAmountSpinner;
    private JComboBox transferFromAccountBox;
    private JComboBox transferToAccountBox;
    private JComboBox transferCategoryBox;
    private JTextField transferInfoField;
    private JTextField transferMonthField;
    private JTextField transferDayFIeld;
    private JTextField transferYearField;

    public NewTransactionDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
