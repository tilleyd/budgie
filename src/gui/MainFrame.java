package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {
    private JPanel panel;
    private JTabbedPane tabbedPane;
    private JTable accountsTable;
    private JButton newAccountButton;
    private JButton accountSettingsButton;
    private JTextField textField1;
    private JComboBox balanceCurrencyBox;
    private JComboBox accountsCurrencyBox;
    private JList accountsList;
    private JTable transactionsTable;
    private JButton newTransactionButton;
    private JButton deleteTransactionButton;

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
        newTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NewTransactionDialog dialog = new NewTransactionDialog();
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }

    public JComponent getRoot() {
        return panel;
    }
}
