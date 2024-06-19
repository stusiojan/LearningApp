package main;

import main.lib.Category;
import main.lib.DatabaseManager;
import main.lib.Milestone;
import main.lib.Task;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class TaskDetailsDialog extends JDialog {
    private final Task task;
    private final Runnable refreshCallback;
    private final JTextField categoryLabelField;
    private final JTextField milestoneLabelField;
    private final JTextField milestoneDeadlineField;
    private final JTextField taskNameField;
    private final JTextArea taskDescriptionField;
    private final JTextField dateCompletedField;
    private final JCheckBox completeCheckBox;
    private final JButton deleteTaskButton;
    private final JButton editButton;
    private final JButton closeButton;
    private Boolean editMode = false;

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

        deleteTaskButton = new JButton("Delete");
        editButton = new JButton("Edit");
        closeButton = new JButton("Close");

        initializeUI();
    }

    private void initializeUI() {
        deleteTaskButton.addActionListener(e -> deleteTask());
        editButton.addActionListener(e -> changeEditMode());
        closeButton.addActionListener(e -> cancelAction());

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteTaskButton);
        buttonPanel.add(editButton);
        buttonPanel.add(closeButton);

        add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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

    private void changeEditMode() {
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

    private void cancelAction() {
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

    private void reverseChanges() {
        taskNameField.setText(task.getName());
        taskDescriptionField.setText(task.getDescription());
        completeCheckBox.setSelected(task.getDateCompleted() != null);
    }

    private void saveTaskDetails() {
        Task task = new Task(
                this.task.getId(),
                taskNameField.getText(),
                completeCheckBox.isSelected() ? new Date(System.currentTimeMillis()) : null,
                taskDescriptionField.getText(),
                this.task.getMilestoneId()
        );

        DatabaseManager.updateTask(task);
        refreshCallback.run();
    }

    private void deleteTask() {
        DatabaseManager.deleteTask(task.getId());
        refreshCallback.run();
        dispose();
    }
}
