package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;

import java.sql.Date;
import java.time.LocalDate;
import java.text.*;

public class EditTaskDialog extends EditDialog {
    private Task task;
    private JCheckBox completedCheckBox;
    private java.sql.Date dateCompleted;

    public EditTaskDialog(JFrame parent, Task task) {
        super(parent, "task", task.getName(), task.getDescription());
        this.task = task;
        createGui();
    }

    protected void onCreate() {
        fields.add(Box.createRigidArea(new Dimension(0, 10)));

        dateCompleted = task.getDateCompleted();
        completedCheckBox = new JCheckBox("", dateCompleted != null);
        if (dateCompleted == null) {
            dateCompleted = java.sql.Date.valueOf(LocalDate.now());
        }
        updateCompletedCheckbox();
        fields.add(completedCheckBox);
        completedCheckBox.addActionListener(e -> updateCompletedCheckbox());
    }

    protected void onUpdate() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        Date date;

        // Check if the task has changed completion
        boolean completedChanged;
        if (completedCheckBox.isSelected()) {
            completedChanged = task.getDateCompleted() == null;
            date = dateCompleted;
        }
        else {
            completedChanged = task.getDateCompleted() != null;
            date = null;
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
        task.setDateCompleted(date);
    }

    protected void onDelete() {
        DatabaseManager.deleteTask(task.getId());
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
