package main.lib;

import javax.swing.*;
import java.awt.*;

public abstract class DetailsDialog extends JDialog {
    protected final JButton deleteButton;
    protected final JButton editButton;
    protected final JButton closeButton;
    protected Boolean editMode = false;

    public DetailsDialog() {
        setTitle("Details");
        setSize(400, 300);
        setLayout(new BorderLayout());

        deleteButton = new JButton("Delete");
        editButton = new JButton("Edit");
        closeButton = new JButton("Close");

        deleteButton.addActionListener(e -> deleteAction());
        editButton.addActionListener(e -> editAction());
        closeButton.addActionListener(e -> closeAction());

        buttonLayout();
    }

    protected void buttonLayout() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    protected abstract void deleteAction();
    protected abstract void editAction();
    protected abstract void closeAction();
}
