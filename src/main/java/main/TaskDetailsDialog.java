package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class TaskDetailsDialog extends DetailsDialog {
    private final Task task;
    private final Runnable refreshCallback;
    private final JTextField categoryLabelField;
    private final JTextField milestoneLabelField;
    private final JTextField milestoneDeadlineField;
    private final JTextField taskNameField;
    private final JTextArea taskDescriptionField;
    private final JTextField dateCompletedField;
    private final JCheckBox completeCheckBox;
    public TaskDetailsDialog(Task task, Runnable refreshCallback) {
        this.task = task;
        this.refreshCallback = refreshCallback;

        setTitle("Task Details");
        setSize(400, 300);
        setLayout(new BorderLayout());
        Milestone milestone = DatabaseManager.getMilestone(task.getMilestoneId());
        Category category = DatabaseManager.getCategory(milestone.getCategoryId());

        taskNameField = new JTextField(task.getName());
        taskDescriptionField = new JTextArea(task.getDescription());
        taskDescriptionField.setRows(5);
        taskDescriptionField.setLineWrap(true);
        taskDescriptionField.setWrapStyleWord(true);
        milestoneLabelField = new JTextField("#" + milestone.getId() + " " + milestone.getName());
        milestoneDeadlineField = new JTextField(String.valueOf(milestone.getDeadline()));
        categoryLabelField = new JTextField("#" + category.getId() + " " + category.getName());
        dateCompletedField = new JTextField(String.valueOf(task.getDateCompleted()));
        dateCompletedField.setEditable(false);

        completeCheckBox = new JCheckBox("Complete");
        completeCheckBox.setSelected(task.getDateCompleted() != null);

        contentLayout();
        buttonLayout();
    }

    private void contentLayout() {
        JPanel categoryPanel = new JPanel(new GridLayout(1, 2));
        categoryPanel.add(new JLabel("Category:"));
        categoryPanel.add(categoryLabelField);
        JPanel milestonePanel = new JPanel(new GridLayout(1, 2));
        milestonePanel.add(new JLabel("Milestone:"));
        milestonePanel.add(milestoneLabelField);

        JPanel detailsPanel = new JPanel(new GridLayout(5, 2));
        detailsPanel.add(categoryPanel);
        detailsPanel.add(milestonePanel);
        detailsPanel.add(new JLabel("Task Name:"));
        detailsPanel.add(taskNameField);
        detailsPanel.add(new JLabel("Task Description:"));
        detailsPanel.add(taskDescriptionField);
        if(task.getDateCompleted() != null){
            detailsPanel.add(new JLabel("Date Completed:"));
            detailsPanel.add(dateCompletedField);

        } else {
            detailsPanel.add(new JLabel("Milestone Deadline:"));
            detailsPanel.add(milestoneDeadlineField);
        }

        detailsPanel.add(new JLabel("Mark as Complete:"));
        detailsPanel.add(completeCheckBox);

        add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        disableEditing();
    }

    private void disableEditing() {
        categoryLabelField.setEditable(false);
        categoryLabelField.setBackground(Color.GRAY);

        milestoneLabelField.setEditable(false);
        milestoneLabelField.setBackground(Color.GRAY);

        taskNameField.setEditable(false);
        taskNameField.setBackground(Color.GRAY);

        taskDescriptionField.setEditable(false);
        taskDescriptionField.setBackground(Color.GRAY);

        dateCompletedField.setEditable(false);
        dateCompletedField.setBackground(Color.GRAY);

        milestoneDeadlineField.setEditable(false);
        milestoneDeadlineField.setBackground(Color.GRAY);

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

        DatabaseManager.updateTask(task);
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
