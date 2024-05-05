package main.lib;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.Function;

/** The "static" class dealing with database connection. */
public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        connect(false);
    }
    public static void connect(boolean forTesting) {
        try {
            String databaseName = forTesting ? Config.getTestDatabaseName() : Config.getDatabaseName();
            connection = DriverManager.getConnection(Config.getConnectionString() + databaseName, Config.getDatabaseUser(), Config.getDatabasePassword());
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed: " + e);
        }
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Disconnect failed: " + e);
        }
    }

    public static <T> T[] selectQuery(String query, Function<ResultSet, T> function, Function<Integer, T[]> arrayMaker) {
        ArrayList<T> list = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                list.add(function.apply(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Select query failed:" + e.getMessage());
        }
        T[] array = arrayMaker.apply(list.size());
        return list.toArray(array);
    }

    public static String getSalt(String login) {
        try {
            CallableStatement cs = connection.prepareCall("{? = CALL get_user(?)}");
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setString(2, login);
            cs.executeUpdate();
            return cs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException("Getting salt failed: " + e);
        }
    }

}
