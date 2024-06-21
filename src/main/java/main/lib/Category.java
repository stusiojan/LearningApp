package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    private final int id;
    private String name;
    private String description; // nullable
    private final int tasksAll;
    private final int tasksDone;

    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    public Category(ResultSet row) {
        try {
            id = row.getInt("cat_id");
            name = row.getString("cat_name");
            description = row.getString("cat_description");
            tasksAll = row.getInt("cat_tasks_all");
            tasksDone = row.getInt("cat_tasks_done");
        } catch (SQLException ex) {
            throw new RuntimeException("Creating object from database row failed: " + ex.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTasksAll() {
        return tasksAll;
    }

    public int getTasksDone() {
        return tasksDone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}