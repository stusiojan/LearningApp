package main.lib;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    private final int id;
    private final String name;
    private final String description; // nullable

    public Category(ResultSet row) {
        try {
            id = row.getInt("cat_id");
            name = row.getString("cat_name");
            description = row.getString("cat_description");
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
}