package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Task {
    private final int id;
    private String name;
    private Date dateCompleted;
    private String description;
    private final int milestoneId;

    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 200;

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

    public Task(int id, String name, Date dateCompleted, String description, int milestoneId) {
        this.id = id;
        this.name = name;
        this.dateCompleted = dateCompleted;
        this.description = description;
        this.milestoneId = milestoneId;
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

    public void setName(String name) {
        this.name = name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public void setDescription(String description) {
        this.description = description.length() > MAX_DESCRIPTION_LENGTH
                ? description.substring(0, MAX_DESCRIPTION_LENGTH) : description;
    }

    public String dateCompletedToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(this.dateCompleted);
    }
}
