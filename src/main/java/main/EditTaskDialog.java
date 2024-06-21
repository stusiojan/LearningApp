package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDate;
import java.text.*;

public class EditTaskDialog extends JDialog implements ActionListener {
    private Task task;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JCheckBox completedCheckBox;
    private java.sql.Date dateCompleted;
    private JButton okButton;
    private JButton cancelButton;

    public EditTaskDialog(JFrame parent, Task task) {
        super(parent, "Edit task", true);
        this.task = task;
        nameField = new JTextField(task.getName(), 25);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        descriptionArea = new JTextArea(task.getDescription(), 4, 25);
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

        dateCompleted = task.getDateCompleted();
        completedCheckBox = new JCheckBox("", dateCompleted != null);
        if (dateCompleted == null) {
            dateCompleted = java.sql.Date.valueOf(LocalDate.now());
        }
        updateCompletedCheckbox();
        fields.add(completedCheckBox);
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

        completedCheckBox.addActionListener(this);
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

            if (name.length() == 0 || name.length() > Task.MAX_NAME_LENGTH) {
                JOptionPane.showMessageDialog(this, "The name must be 1 - " + Task.MAX_NAME_LENGTH + " characters long.");
                return;
            }
            else if (description.length() > Task.MAX_DESCRIPTION_LENGTH) {
                JOptionPane.showMessageDialog(this, "The description must be 0 - " + Task.MAX_DESCRIPTION_LENGTH + " characters long.");
                return;
            }

            // Check if the task has changed completion
            boolean completedChanged = false;
            if (completedCheckBox.isSelected()) {
                completedChanged = task.getDateCompleted() == null;
                task.setDateCompleted(dateCompleted);
            }
            else {
                completedChanged = task.getDateCompleted() != null;
                task.setDateCompleted(null);
            }

            boolean changed = completedChanged;
            changed = changed || !name.equals(task.getName());
            changed = changed || !description.equals(task.getDescription());
            if (changed) {
                task.setName(name);
                task.setDescription(description);
                DatabaseManager.updateTask(task);
            }
            if (completedChanged) {
                DatabaseManager.switchTaskDone(task.getId());
            }

            dispose();
        }
        else if (e.getSource() == completedCheckBox) {
            updateCompletedCheckbox();
        }
    }

    private void updateCompletedCheckbox() {
        String date = "N/A";
        if (completedCheckBox.isSelected()) {
            DateFormat dateFmt = new SimpleDateFormat("dd.MM.yyyy");
            date = dateFmt.format(dateCompleted);
        }
        completedCheckBox.setText(String.format("Completed: %s", date));
    }

}
