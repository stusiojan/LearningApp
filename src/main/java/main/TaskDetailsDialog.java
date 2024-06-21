package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class TaskDetailsDialog extends DetailsDialog {
    private final Task task;
    private final Runnable refreshCallback;
    private final JTextField taskNameField;
    private final JTextArea taskDescriptionField;
    private final JTextField dateCompletedField;
    private final JCheckBox completeCheckBox;
    public TaskDetailsDialog(Task task, Runnable refreshCallback) {
        this.task = task;
        this.refreshCallback = refreshCallback;

        setTitle("Task Details");

        taskNameField = new JTextField(task.getName());
        taskDescriptionField = new JTextArea(task.getDescription());
        taskDescriptionField.setRows(5);
        taskDescriptionField.setLineWrap(true);
        taskDescriptionField.setWrapStyleWord(true);
        dateCompletedField = new JTextField(String.valueOf(task.getDateCompleted()));
        dateCompletedField.setEditable(false);

        completeCheckBox = new JCheckBox("Complete");
        completeCheckBox.setSelected(task.getDateCompleted() != null);

        contentLayout();
        pack();
    }

    private void contentLayout() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        Milestone milestone = DatabaseManager.getMilestone(task.getMilestoneId());
        Category category = DatabaseManager.getCategory(milestone.getCategoryId());
        headerPanel.add(
                new JLabel("<html><b>Category: #" + category.getId() + " " + category.getName() + "</b></html>")
        );
        headerPanel.add(
                new JLabel("<html><b>Milestone: #" + milestone.getId() + " " + milestone.getName() + "</b></html>")
        );
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        detailsPanel.add(new JLabel("Task Name:"));
        detailsPanel.add(taskNameField);
        detailsPanel.add(new JLabel("Task Description:"));
        detailsPanel.add(taskDescriptionField);
        if(task.getDateCompleted() != null){
            detailsPanel.add(new JLabel("Date Completed:"));
            detailsPanel.add(dateCompletedField);

        } else {
            detailsPanel.add(new JLabel("Milestone Deadline:"));
            detailsPanel.add(new JLabel(String.valueOf(milestone.getDeadline())));
        }

        detailsPanel.add(new JLabel("Mark as Complete:"));
        detailsPanel.add(completeCheckBox);

        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        disableEditing();
    }

    private void disableEditing() {
        taskNameField.setEditable(false);
        taskNameField.setBackground(Color.GRAY);

        taskDescriptionField.setEditable(false);
        taskDescriptionField.setBackground(Color.GRAY);

        dateCompletedField.setEditable(false);
        dateCompletedField.setBackground(Color.GRAY);

        completeCheckBox.setEnabled(false);
    }

    private void reverseChanges() {
        taskNameField.setText(task.getName());
        taskDescriptionField.setText(task.getDescription());
        completeCheckBox.setSelected(task.getDateCompleted() != null);
    }

    private void saveTaskDetails() {
        Task task;
        task = new Task(
                this.task.getId(),
                taskNameField.getText(),
                completeCheckBox.isSelected() ? new Date(System.currentTimeMillis()) : null,
                taskDescriptionField.getText(),
                this.task.getMilestoneId()
        );

        DatabaseManager.updateEntireTask(task);
        if (completeCheckBox.isSelected() && this.task.getDateCompleted() == null) {
            DatabaseManager.switchTaskDone(task.getId());
        }
        refreshCallback.run();
    }

    @Override
    protected void editAction() {
        if (editMode) {
            editMode = false;
            editButton.setText("Edit");
            closeButton.setText("Close");
            saveTaskDetails();
            disableEditing();
            return;
        }
        editMode = true;
        editButton.setText("Save");
        closeButton.setText("Cancel");
        taskNameField.setEditable(true);
        taskNameField.setBackground(Color.WHITE);
        taskDescriptionField.setEditable(true);
        taskDescriptionField.setBackground(Color.WHITE);
        if(task.getDateCompleted() == null)
            completeCheckBox.setEnabled(true);
    }

    @Override
    protected void closeAction() {
        if (editMode) {
            editMode = false;
            editButton.setText("Edit");
            closeButton.setText("Close");
            reverseChanges();
            disableEditing();
            return;
        }
        dispose();
    }

    @Override
    protected void deleteAction() {
        DatabaseManager.deleteTask(task.getId());
        refreshCallback.run();
        dispose();
    }
}
