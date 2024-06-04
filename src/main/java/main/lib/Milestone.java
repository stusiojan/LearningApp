package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Milestone {
    private final int id;
    private final String name;
    private final Date dateAdded;
    private final Date deadline;
    private final Date dateCompleted;
    private final String description;
    private final int tasksAll;
    private final int tasksDone;
    private final int userId;
    private final int categoryId;
    
    public Milestone(ResultSet row) {
        try {
            id = row.getInt("mil_id");
            name = row.getString("mil_name");
            dateAdded = row.getDate("date_added");
            deadline = row.getDate("deadline");
            dateCompleted = row.getDate("mil_completed");
            description = row.getString("mil_description");
            tasksAll = row.getInt("mil_tasks_all");
            tasksDone = row.getInt("mil_tasks_done");
            userId = row.getInt("user_id");
            categoryId = row.getInt("cat_id");
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Date getDateCompleted() {
        return dateCompleted;
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

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
