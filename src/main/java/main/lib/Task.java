package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Task {
    private final int id;
    private final String name;
    private final Date dateCompleted;
    private final String description;
    private final int milestoneId;

    public Task(ResultSet row) {
        try {
            id = row.getInt("task_id");
            name = row.getString("task_name");
            dateCompleted = row.getDate("task_completed");
            description = row.getString("task_description");
            milestoneId = row.getInt("mil_id");
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

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public String getDescription() {
        return description;
    }

    public int getMilestoneId() {
        return milestoneId;
    }
}
