package main;

import main.lib.*;
import java.util.*;
import java.text.*;

import javax.swing.*;
import javax.swing.text.DateFormatter;

import java.awt.*;

public class EditMilestoneDialog extends EditDialog {
    private Milestone milestone;
    private JSpinner deadlineSpinner;

    public EditMilestoneDialog(JFrame parent, Milestone milestone) {
        super(parent, "milestone", milestone.getName(), milestone.getDescription());
        this.milestone = milestone;

        var deadlineDateModel = new SpinnerDateModel(milestone.getDeadline(), null, null, Calendar.DAY_OF_WEEK);
        deadlineSpinner = new JSpinner(deadlineDateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(deadlineSpinner, "dd.MM.yyyy");
        DateFormatter formatter = (DateFormatter)dateEditor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        deadlineSpinner.setEditor(dateEditor);

        createGui();
    }

    public void onCreate() {
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
    }

    public void onUpdate() {
        String name = nameField.getText();
        String description = descriptionArea.getText();

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
    }

    public void onDelete() {
        DatabaseManager.deleteMilestone(milestone.getId());
    }
}
