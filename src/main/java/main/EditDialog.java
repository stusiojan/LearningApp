package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class EditDialog extends JDialog implements ActionListener {
    protected JPanel fields;
    protected String subject;
    protected JTextField nameField;
    protected JTextArea descriptionArea;
    protected JButton okButton;
    protected JButton cancelButton;
    protected JButton deleteButton;

    protected abstract void onCreate();
    protected abstract void onUpdate();
    protected abstract void onDelete();

    public EditDialog(JFrame parent, String subject, String name, String description) {
        super(parent, "Edit " + subject, true);
        this.subject = subject;
        nameField = new JTextField(name, 25);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        descriptionArea = new JTextArea(description, 4, 25);
        descriptionArea.setFont(nameField.getFont());
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");
        deleteButton = new JButton("<html><span style='color:red;'>Delete</span>");
        okButton.setMaximumSize(okButton.getPreferredSize());
        cancelButton.setMaximumSize(cancelButton.getPreferredSize());
        deleteButton.setMaximumSize(deleteButton.getPreferredSize());
    }

    void createGui() {  
        fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fields.add(new JLabel("Name"));
        fields.add(nameField);
        fields.add(Box.createRigidArea(new Dimension(0, 10)));
        fields.add(new JLabel("Description"));
        fields.add(descriptionArea);

        onCreate();
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
        buttons.add(Box.createRigidArea(new Dimension(10, 0)));
        buttons.add(deleteButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(fields, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.PAGE_END);

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        deleteButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            dispose();
        }
        else if (e.getSource() == okButton) {
            final int MAX_NAME_LENGTH = 50;
            final int MAX_DESCRIPTION_LENGTH = 200;

            String name = nameField.getText();
            String description = descriptionArea.getText();

            if (name.length() == 0 || name.length() > MAX_NAME_LENGTH) {
                JOptionPane.showMessageDialog(this, "The name must be 1 - " + MAX_NAME_LENGTH + " characters long.");
                return;
            }
            else if (description.length() > MAX_DESCRIPTION_LENGTH) {
                JOptionPane.showMessageDialog(this, "The description must be 0 - " + MAX_DESCRIPTION_LENGTH + " characters long.");
                return;
            }
            onUpdate();
            dispose();
        } 
        else {
            int option = JOptionPane.showConfirmDialog(
                null, "Are you sure that you want to permanently delete this " + subject + "?", "Warning", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                onDelete();
                dispose();
            }
        }
    }
}
