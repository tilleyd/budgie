package gui;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

import model.*;

public class NewAccountDialog extends JDialog {
    private JPanel contentPane;
    private JButton createButton;
    private JButton cancelButton;
    private JTextField nameField;
    private JTextField institutionField;
    private JComboBox<String> currencyBox;

    private String closeOperation = "CANCEL";

    public NewAccountDialog(List<Currency> currencies) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButton);

        for (Currency c : currencies) {
            currencyBox.addItem(c.getCode());
        }

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreate();
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

    public String getCloseOperation() {
        return closeOperation;
    }

    public String getName() {
        return nameField.getText();
    }

    public String getInstitution() {
        return institutionField.getText();
    }

    public int getCurrencyIndex() {
        return currencyBox.getSelectedIndex();
    }

    private void onCreate() {
        if (getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Account name is missing", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (getCurrencyIndex() < 0) {
            JOptionPane.showMessageDialog(this, "No currency selected", "Validation Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        closeOperation = "CREATE";
        dispose();
    }

    private void onCancel() {
        closeOperation = "CANCEL";
        dispose();
    }
}
