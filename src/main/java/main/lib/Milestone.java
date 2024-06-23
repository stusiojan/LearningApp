package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Milestone {
    private final int id;
    private String name;
    private final Date dateAdded;
    private Date deadline;
    private final Date dateCompleted;
    private String description;
    private final int tasksAll;
    private final int tasksDone;
    private final int userId;
    private final int categoryId;
    
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 200;
    
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

    public Milestone(int id, String name, Date dateAdded, Date deadline, Date dateCompleted, String description, int tasksAll, int tasksDone, int userId, int categoryId) {
        this.id = id;
        this.name = name;
        this.dateAdded = dateAdded;
        this.deadline = deadline;
        this.dateCompleted = dateCompleted;
        this.description = description;
        this.tasksAll = tasksAll;
        this.tasksDone = tasksDone;
        this.userId = userId;
        this.categoryId = categoryId;
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

    public void setName(String name) {
        this.name = name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description.length() > MAX_DESCRIPTION_LENGTH
                ? description.substring(0, MAX_DESCRIPTION_LENGTH) : description;
    }
}
