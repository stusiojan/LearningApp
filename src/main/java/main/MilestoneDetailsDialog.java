package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class MilestoneDetailsDialog extends DetailsDialog {
    private final Milestone milestone;
    private final Runnable refreshCallback;
    private final JTextField milestoneNameField;
    private final JTextArea milestoneDescriptionField;
    private final JTextField milestoneDateAddedField;
    private final JTextField milestoneDeadlineField;

    public MilestoneDetailsDialog(Milestone milestone, Runnable refreshCallback) {
        this.milestone = milestone;
        this.refreshCallback = refreshCallback;

        setTitle("Milestone Details");


        milestoneNameField = new JTextField(milestone.getName());
        milestoneDescriptionField = new JTextArea(milestone.getDescription());
        milestoneDescriptionField.setRows(5);
        milestoneDescriptionField.setLineWrap(true);
        milestoneDescriptionField.setWrapStyleWord(true);
        milestoneDateAddedField = new JTextField(String.valueOf(milestone.getDateAdded()));
        milestoneDeadlineField = new JTextField(String.valueOf(milestone.getDeadline()));

        contentLayout();
        pack();
    }

    private void contentLayout() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Category category = DatabaseManager.getCategory(milestone.getCategoryId());
        contentPanel.add(
                new JLabel("<html><b>Category: #" + category.getId() + " " + category.getName() + "</b></html>"),
                BorderLayout.NORTH
        );

        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 20, 20));
        detailsPanel.add(new JLabel("Milestone Name:"));
        detailsPanel.add(milestoneNameField);
        detailsPanel.add(new JLabel("Milestone Description:"));
        detailsPanel.add(milestoneDescriptionField);

        JPanel startDate = new JPanel(new GridLayout(1, 2));
        startDate.add(new JLabel("Start Date:"));
        startDate.add(milestoneDateAddedField);
        JPanel endDate = new JPanel(new GridLayout(1, 2));
        endDate.add(new JLabel("Deadline:"));
        endDate.add(milestoneDeadlineField);

        detailsPanel.add(startDate);
        detailsPanel.add(endDate);

        detailsPanel.add(new JLabel("Current Progress (Tasks Done / All Tasks):"));
        JProgressBar progressBar = new JProgressBar() {
            @Override
            public String getString() {
                return getValue() + " / " + getMaximum();
            }

        };
        progressBar.setMaximum(milestone.getTasksAll());
        progressBar.setValue(milestone.getTasksDone());
        progressBar.setStringPainted(true);
        detailsPanel.add(progressBar);

        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        disableEditing();
    }

    private void disableEditing() {
        milestoneNameField.setEditable(false);
        milestoneNameField.setBackground(Color.GRAY);

        milestoneDescriptionField.setEditable(false);
        milestoneDescriptionField.setBackground(Color.GRAY);

        milestoneDateAddedField.setEditable(false);
        milestoneDateAddedField.setBackground(Color.GRAY);

        milestoneDeadlineField.setEditable(false);
        milestoneDeadlineField.setBackground(Color.GRAY);
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
        milestoneNameField.setEditable(true);
        milestoneNameField.setBackground(Color.WHITE);
        milestoneDescriptionField.setEditable(true);
        milestoneDescriptionField.setBackground(Color.WHITE);
        milestoneDateAddedField.setEditable(true);
        milestoneDateAddedField.setBackground(Color.WHITE);
        milestoneDeadlineField.setEditable(true);
        milestoneDeadlineField.setBackground(Color.WHITE);
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

    private void reverseChanges() {
        milestoneNameField.setText(milestone.getName());
        milestoneDescriptionField.setText(milestone.getDescription());
        milestoneDeadlineField.setText(String.valueOf(milestone.getDeadline()));
    }

    private void saveTaskDetails() {
        Milestone milestone;
        milestone = new Milestone(
                this.milestone.getId(),
                milestoneNameField.getText(),
                Date.valueOf(milestoneDateAddedField.getText()),
                Date.valueOf(milestoneDeadlineField.getText()),
                this.milestone.getDateCompleted(),
                milestoneDescriptionField.getText(),
                this.milestone.getTasksAll(),
                this.milestone.getTasksDone(),
                this.milestone.getUserId(),
                this.milestone.getCategoryId()
        );

        DatabaseManager.updateEntireMilestone(milestone);
        refreshCallback.run();
    }

    @Override
    protected void deleteAction() {
        DatabaseManager.deleteTask(milestone.getId());
        refreshCallback.run();
        dispose();
    }
}
