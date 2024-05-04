package main.lib;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Function;

/** The "static" class dealing with database connection. */
public class DatabaseManager {
    private static Connection connection;

    public static void connect() throws SQLException {
        connect(false);
    }
    public static void connect(boolean forTesting) throws SQLException {
        String databaseName = forTesting ? Config.getTestDatabaseName() : Config.getDatabaseName();
        connection = DriverManager.getConnection(Config.getConnectionString() + databaseName, Config.getDatabaseUser(), Config.getDatabasePassword());
    }

    public static void disconnect() throws SQLException{
        connection.close();
    }

    public static <T> T[] selectQuery(String query, Function<ResultSet, T> function, Function<Integer, T[]> arrayMaker) {
        ArrayList<T> list = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                list.add(function.apply(result));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Query failed:" + ex.getMessage());
        }
        T[] array = arrayMaker.apply(list.size());
        return list.toArray(array);
    }

}
