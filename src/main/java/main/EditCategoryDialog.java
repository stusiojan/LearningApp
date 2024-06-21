package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditCategoryDialog extends JDialog implements ActionListener {
    private Category category;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton okButton;
    private JButton cancelButton;

    public EditCategoryDialog(JFrame parent, Category category) {
        super(parent, "Edit category", true);
        this.category = category;
        nameField = new JTextField(category.getName(), 25);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        descriptionArea = new JTextArea(category.getDescription(), 4, 25);
        descriptionArea.setFont(nameField.getFont());
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fields.add(new JLabel("Name"));
        fields.add(nameField);
        fields.add(Box.createRigidArea(new Dimension(0, 10)));
        fields.add(new JLabel("Description"));
        fields.add(descriptionArea);
        fields.add(Box.createRigidArea(new Dimension(0, 10)));

        var info = String.format("%d out of %d task(s) have been completed.", 
            category.getTasksDone(), 
            category.getTasksAll());
        fields.add(new JLabel(info));
        for (Component c : fields.getComponents()) {
            ((JComponent)c).setAlignmentX(LEFT_ALIGNMENT);
        }

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttons.add(Box.createHorizontalGlue());
        buttons.add(okButton);
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        buttons.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(fields, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.PAGE_END);

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        }
        else if (e.getSource() == okButton) {
            String name = nameField.getText();
            String description = descriptionArea.getText();

            if (name.length() == 0 || name.length() > Category.MAX_NAME_LENGTH) {
                JOptionPane.showMessageDialog(this, "The name must be 1 - " + Category.MAX_NAME_LENGTH + " characters long.");
                return;
            }
            else if (description.length() > Category.MAX_DESCRIPTION_LENGTH) {
                JOptionPane.showMessageDialog(this, "The description must be 0 - " + Category.MAX_DESCRIPTION_LENGTH + " characters long.");
                return;
            }

            boolean changed = false;
            changed = changed || !name.equals(category.getName());
            changed = changed || !description.equals(category.getDescription());

            if (changed) {
                category.setName(name);
                category.setDescription(description);
                DatabaseManager.updateCategory(category);
            }
            dispose();
        }
    }
}
