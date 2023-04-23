package gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

import model.*;

public class AccountSettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField nameField;
    private JTextField institutionField;
    private JButton archiveButton;
    private JButton deleteButton;
    private JTextField currencyField;

    private String closeOperation = "CANCEL";

    public AccountSettingsDialog(Account account, String currencyCode) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveButton);

        nameField.setText(account.getName());
        institutionField.setText(account.getInstitution());
        currencyField.setText(currencyCode);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDelete();
            }
        });

        archiveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onArchive();
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

    public String getCloseOperation() {
        return closeOperation;
    }

    public String getName() {
        return nameField.getText();
    }

    public String getInstitution() {
        return institutionField.getText();
    }

    private void onSave() {
        closeOperation = "SAVE";
        // TODO validate form
        dispose();
    }

    private void onCancel() {
        closeOperation = "CANCEL";
        dispose();
    }

    private void onDelete() {
        // TODO confirm
        closeOperation = "DELETE";
        dispose();
    }

    private void onArchive() {
        // TODO confirm
        closeOperation = "ARCHIVE";
        dispose();
    }
}
