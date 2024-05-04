package main.lib;

import java.sql.*;
import main.lib.Config;

/** The "static" class dealing with database connection. */
public class DatabaseManager {
    private static Connection connection;

    public static void connect() throws SQLException {
        connect(false);
    }
    public static void connect(boolean forTesting) throws SQLException {
        String databaseName = forTesting ? Config.getTestDatabaseName() : Config.getDatabaseName();

        try {
            connection = DriverManager.getConnection(Config.getConnectionString() + databaseName, Config.getDatabaseUser(), Config.getDatabasePassword());
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
