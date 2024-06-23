package main;

import main.lib.*;

import javax.swing.*;
import java.awt.*;


public class EditCategoryDialog extends EditDialog {
    private Category category;

    public EditCategoryDialog(JFrame parent, Category category) {
        super(parent, "category", category.getName(), category.getDescription());
        this.category = category;
        createGui();
    }

    protected void onCreate() {
        fields.add(Box.createRigidArea(new Dimension(0, 10)));

        var info = String.format("%d out of %d task(s) have been completed.", 
            category.getTasksDone(), 
            category.getTasksAll());
        fields.add(new JLabel(info));
    }

    protected void onUpdate() {
        String name = nameField.getText();
        String description = descriptionArea.getText();

        boolean changed = false;
        changed = changed || !name.equals(category.getName());
        changed = changed || !description.equals(category.getDescription());
        if (changed) {
            category.setName(name);
            category.setDescription(description);
            DatabaseManager.updateCategory(category);
        }
    }

    protected void onDelete() {
        DatabaseManager.deleteCategory(category.getId());
    }
}
