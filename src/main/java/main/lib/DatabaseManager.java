package main.lib;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Function;
import java.util.Optional;
import java.time.LocalDate;

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

    private static <T> T[] selectQuery(PreparedStatement statement, Function<ResultSet, T> function, Function<Integer, T[]> arrayMaker) {
        ArrayList<T> list = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                list.add(function.apply(result));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Select query failed:" + e.getMessage());
        }
        T[] array = arrayMaker.apply(list.size());
        return list.toArray(array);
    }

    private static <T> T[] selectQuery(String query, Function<ResultSet, T> function, Function<Integer, T[]> arrayMaker) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return selectQuery(statement, function, arrayMaker);
        } catch (SQLException e) {
            throw new RuntimeException("Select query failed:" + e.getMessage());
        }
    }

    private static <T> T selectById(String query, Function<ResultSet, T> function, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next())
                return function.apply(result);
            else
                throw new SQLException("Failed to retrieve by id.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve: " + e);
        }
    }

    private static void deleteById(String sql, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        }  catch (SQLException e) {
            throw new RuntimeException("Failed to delete: " + e);
        }
    }

    public static Category[] getCategories() {
        return selectQuery("SELECT * FROM categories", Category::new, Category[]::new);
    }

    public static Optional<User> getUser(String login) {
        String sql = "SELECT user_id, login, hash, salt FROM users WHERE login = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet result = statement.executeQuery();
            if (result.next())
                return Optional.of(new User(result));
            else
                return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve user data: " + e);
        }
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

    public static Milestone[] getMilestones(int categoryId, int userId) {
        String sql = "SELECT * FROM milestones WHERE user_id = ? AND cat_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, categoryId);
            return selectQuery(statement, Milestone::new, Milestone[]::new);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get milestones" + e);
        }
    }

    public static Task[] getTasks(int milestoneId) {
        String sql = "SELECT * FROM tasks WHERE mil_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, milestoneId);
            return selectQuery(statement, Task::new, Task[]::new);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get tasks" + e);
        }
    }

    public static Category getCategory(int categoryId) {
        return selectById("SELECT * FROM categories WHERE cat_id = ?", Category::new, categoryId);
    }

    public static Milestone getMilestone(int milestoneId) {
        return selectById("SELECT * FROM milestones WHERE mil_id = ?", Milestone::new, milestoneId);
    }

    public static Task getTask(int taskId) {
        return selectById("SELECT * FROM tasks WHERE task_id = ?", Task::new, taskId);
    }

    public static int addCategory(String categoryName) {
        String sql = "INSERT INTO categories (cat_name, cat_description) VALUES (?, ?)";
        try {
            String returns[] = { "cat_id" };
            PreparedStatement statement = connection.prepareStatement(sql, returns);
            statement.setString(1, categoryName);
            statement.setString(2, "");
            statement.executeUpdate();
            
            int id;
            ResultSet result = statement.getGeneratedKeys();
            if (result.next())
                id = result.getInt(1);
            else
                throw new SQLException("Failed to obtain category id.");
            statement.close();
            return id;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add new category: " + e);
        }
    }

    public static int addMilestone(String milestoneName, int userId, int categoryId) {
        Date dateAdded = Date.valueOf(LocalDate.now());
        Date deadline = Date.valueOf(LocalDate.now().plusWeeks(1));

        String sql = "INSERT INTO milestones (mil_name, date_added, deadline, mil_description, user_id, cat_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            String returns[] = { "mil_id" };
            PreparedStatement statement = connection.prepareStatement(sql, returns);
            statement.setString(1, milestoneName);
            statement.setDate(2, dateAdded);
            statement.setDate(3, deadline);
            statement.setString(4, "");      
            statement.setInt(5, userId);
            statement.setInt(6, categoryId);     
            statement.executeUpdate();

            int id;
            ResultSet result = statement.getGeneratedKeys();
            if (result.next())
                id = result.getInt(1);
            else
                throw new SQLException("Failed to obtain category id.");
            statement.close();
            return id;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add new milestone: " + e);
        }
    }

    public static int addTask(String taskName, int milestoneId) {
        String sql = "INSERT INTO tasks (task_name, task_description, mil_id) VALUES (?, ?, ?)";
        try {
            String returns[] = { "task_id" };
            PreparedStatement statement = connection.prepareStatement(sql, returns);
            statement.setString(1, taskName);
            statement.setString(2, "");
            statement.setInt(3, milestoneId);
            statement.executeUpdate();
            
            // FIXME: code duplication.
            int id;
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
            } else {
                throw new SQLException("Failed to obtain category id.");
            }
            statement.close();
            return id;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add new task: " + e);
        }
    }

    public static void deleteCategory(int categoryId) {
        deleteById("DELETE FROM categories WHERE cat_id = ?", categoryId);
    }

    public static void deleteMilestone(int milestoneId) {
        deleteById("DELETE FROM milestones WHERE mil_id = ?", milestoneId);
    }

    public static void deleteTask(int taskId) {
        deleteById("DELETE FROM tasks WHERE task_id = ?",taskId);
    }
}
