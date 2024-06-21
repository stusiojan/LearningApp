package main;

import main.lib.*;
import java.util.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.DateFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditMilestoneDialog extends JDialog implements ActionListener {
    private Milestone milestone;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton okButton;
    private JButton cancelButton;
    private JSpinner deadlineSpinner;

    public EditMilestoneDialog(JFrame parent, Milestone milestone) {
        super(parent, "Edit milestone", true);
        this.milestone = milestone;
        nameField = new JTextField(milestone.getName(), 25);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        descriptionArea = new JTextArea(milestone.getDescription(), 4, 25);
        descriptionArea.setFont(nameField.getFont());
        okButton = new JButton("Ok");
        cancelButton = new JButton("Cancel");


        var deadlineDateModel = new SpinnerDateModel(milestone.getDeadline(), null, null, Calendar.DAY_OF_WEEK);
        deadlineSpinner = new JSpinner(deadlineDateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(deadlineSpinner, "dd.MM.yyyy");
        DateFormatter formatter = (DateFormatter)dateEditor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        deadlineSpinner.setEditor(dateEditor);

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fields.add(new JLabel("Name"));
        fields.add(nameField);
        fields.add(Box.createRigidArea(new Dimension(0, 10)));
        fields.add(new JLabel("Description"));
        fields.add(descriptionArea);
        fields.add(Box.createRigidArea(new Dimension(0, 10)));
        fields.add(new JLabel("Deadline"));
        fields.add(deadlineSpinner);
        fields.add(Box.createRigidArea(new Dimension(0, 10)));

        // Show information
        DateFormat dateFmt = new SimpleDateFormat("dd.MM.yyyy");
        String dateAdded = dateFmt.format(milestone.getDateAdded());
        String dateCompleted = "N/A";
        if (milestone.getDateCompleted() != null)
            dateCompleted = dateFmt.format(milestone.getDateCompleted());

        fields.add(new JLabel(String.format("<html><b>Added:</b> %s</html>", dateAdded)));
        fields.add(new JLabel(String.format("<html><b>Completed:</b> %s</html>", dateCompleted)));
        var info = String.format("%d out of %d task(s) have been completed.", 
            milestone.getTasksDone(), 
            milestone.getTasksAll());
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

            if (name.length() == 0 || name.length() > Milestone.MAX_NAME_LENGTH) {
                JOptionPane.showMessageDialog(this, "The name must be 1 - " + Milestone.MAX_NAME_LENGTH + " characters long.");
                return;
            }
            else if (description.length() > Milestone.MAX_DESCRIPTION_LENGTH) {
                JOptionPane.showMessageDialog(this, "The description must be 0 - " + Milestone.MAX_DESCRIPTION_LENGTH + " characters long.");
                return;
            }

            var deadlineDateModel = (SpinnerDateModel)deadlineSpinner.getModel();
            var deadline = new java.sql.Date(deadlineDateModel.getDate().getTime());

            boolean changed = false;
            changed = changed || !name.equals(milestone.getName());
            changed = changed || !description.equals(milestone.getDescription());
            changed = changed || !deadline.equals(milestone.getDeadline());

            if (changed) {
                milestone.setName(name);
                milestone.setDescription(description);
                milestone.setDeadline(deadline);
                DatabaseManager.updateMilestone(milestone);
            }
            dispose();
        }
    }
}
