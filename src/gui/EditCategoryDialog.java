package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.*;

public class EditCategoryDialog extends JDialog {
    private JPanel contentPane;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JComboBox<CategoryGroup> groupBox;
    private JComboBox<CategoryType> typeBox;

    private String closeOperation = "CANCEL";
    final private Category category;

    public EditCategoryDialog(Category category) {
        this.category = category;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(saveButton);

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

        nameField.setText(category.getName());
        groupBox.setSelectedItem(category.getGroup());
        typeBox.setSelectedItem(category.getType());

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDelete();
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

    private void onSave() {
        closeOperation = "SAVE";
        dispose();
    }

    private void onDelete() {
        String message = "Delete category '" + category.getName() + "'?";
        int confirmation = JOptionPane.showConfirmDialog(this, message, "Delete Transaction", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            closeOperation = "DELETE";
            dispose();
        }
    }

    private void onCancel() {
        closeOperation = "CANCEL";
        dispose();
    }
}
