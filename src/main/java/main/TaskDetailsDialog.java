package main;

import main.lib.DatabaseManager;
import main.lib.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class TaskDetailsDialog extends JDialog {
    private final Task task;
    private final Runnable refreshCallback;
    private final JTextField taskNameField;
    private final JTextField milestoneIdField;
    private final JTextField dateCompletedField;
    private final JCheckBox completeCheckBox;
    private final JButton editButton;
    private final JButton saveButton;

    public TaskDetailsDialog(Task task, Runnable refreshCallback) {
        this.task = task;
//        this.dbManager = dbManager;
        this.refreshCallback = refreshCallback;

        setTitle("Task Details");
        setSize(400, 300);
        setLayout(new BorderLayout());

        taskNameField = new JTextField(task.getName());
        milestoneIdField = new JTextField(String.valueOf(task.getMilestoneId()));
        dateCompletedField = new JTextField(String.valueOf(task.getDateCompleted()));
        dateCompletedField.setEditable(false);

        completeCheckBox = new JCheckBox("Complete");
        completeCheckBox.setSelected(task.getDateCompleted() != null);

        editButton = new JButton("Edit");
        saveButton = new JButton("Save");
        saveButton.setEnabled(false);

        initializeUI();
    }

    private void initializeUI() {
        editButton.addActionListener(e -> enableEditing());

        saveButton.addActionListener(e -> saveTaskDetails());

        JPanel detailsPanel = new JPanel(new GridLayout(6, 2));
        detailsPanel.add(new JLabel("Task Name:"));
        detailsPanel.add(taskNameField);
//        detailsPanel.add(new JLabel("Category ID:"));
//        detailsPanel.add(categoryIdField);
        detailsPanel.add(new JLabel("Milestone ID:"));
        detailsPanel.add(milestoneIdField);
//        detailsPanel.add(new JLabel("Milestone Deadline:"));
//        detailsPanel.add(milestoneDeadlineField);
        detailsPanel.add(new JLabel("Date Completed:"));
        detailsPanel.add(dateCompletedField);
        detailsPanel.add(new JLabel("Mark as Complete:"));
        detailsPanel.add(completeCheckBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);

        add(new JScrollPane(detailsPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void enableEditing() {
        taskNameField.setEditable(true);
//        categoryIdField.setEditable(true);
        milestoneIdField.setEditable(true);
//        milestoneDeadlineField.setEditable(true);
        saveButton.setEnabled(true);
    }

    private void saveTaskDetails() {
//        task.seName(taskNameField.getText());
//        task.setCategoryId(Integer.parseInt(categoryIdField.getText()));
//        task.setMilestoneId(Integer.parseInt(milestoneIdField.getText()));
//        task.setMilestoneDeadline(milestoneDeadlineField.getText());
//        task.setDateCompleted(completeCheckBox.isSelected() ? new java.sql.Date(System.currentTimeMillis()) : null);
//
//        try {
//            dbManager.updateTask(task);
//            dispose();
//            refreshCallback.run();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
    }
}
