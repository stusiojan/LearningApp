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

    private static <T> T[] selectQuery(String query, Function<ResultSet, T> function, Function<Integer, T[]> arrayMaker) {
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

    public static Category[] getCategories() {
        return selectQuery("SELECT * FROM categories", Category::new, Category[]::new);
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

    public static String getHash(String login) {
        String sql = "SELECT hash FROM users WHERE login = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getString("hash");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add new user: " + e);
        }
    }

    public static boolean hasUser(String login) {
        return !getSalt(login).equals("EMPTY");     // FIXME(?): Could be done better
    }

    public static void addUser(String login, String hash, String salt) {
        String sql = "INSERT INTO users (login, hash, salt) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.setString(2, hash);
            statement.setString(3, salt);
            statement.executeUpdate();
            statement.close();
        }  catch (SQLException e) {
            throw new RuntimeException("Failed to add new user: " + e);
        }
    }

    public static void deleteUser(String login) {
        String sql = "DELETE FROM users WHERE login = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            statement.executeUpdate();
            statement.close();
        }   catch (SQLException e) {
            throw new RuntimeException("Failed to delete user: " + e);
        }
    }
}
