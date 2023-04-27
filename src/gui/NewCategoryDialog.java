package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.*;

public class NewCategoryDialog extends JDialog {
    private JPanel contentPane;
    private JButton createButton;
    private JButton cancelButton;
    private JTextField nameField;
    private JComboBox<CategoryGroup> groupBox;
    private JComboBox<CategoryType> typeBox;

    private String closeOperation = "CANCEL";

    public NewCategoryDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(createButton);

        // populate boxes
        groupBox.addItem(CategoryGroup.DAY_TO_DAY);
        groupBox.addItem(CategoryGroup.RECURRING);
        groupBox.addItem(CategoryGroup.EXCEPTION);
        groupBox.addItem(CategoryGroup.INVESTMENT);
        groupBox.addItem(CategoryGroup.DEBT);
        groupBox.addItem(CategoryGroup.INTEREST);
        typeBox.addItem(CategoryType.INCOME_OR_EXPENSE);
        typeBox.addItem(CategoryType.INCOME);
        typeBox.addItem(CategoryType.EXPENSE);

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

    public String getCategoryName() {
        return nameField.getText();
    }

    public CategoryType getCategoryType() {
        return (CategoryType)typeBox.getSelectedItem();
    }

    public CategoryGroup getCategoryGroup() {
        return (CategoryGroup)groupBox.getSelectedItem();
    }

    private void onCreate() {
        closeOperation = "CREATE";
        dispose();
    }

    private void onCancel() {
        closeOperation = "CANCEL";
        dispose();
    }
}
