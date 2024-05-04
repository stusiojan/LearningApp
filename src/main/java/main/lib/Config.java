package main.lib;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/** Loads and stores data from config.properties. */
public class Config {
    private static final String connectionString;
    private static final String databaseName;
    private static final String databasePassword;
    private static final String databaseUser;
    private static final String testDatabaseName;

    static {
        String configPath = System.getProperty("user.dir");
        if (configPath.endsWith("/target") || configPath.endsWith("\\target")) {
            // cut /target if the program is run from jar
            configPath = configPath.substring(0, configPath.length() - 7);
        }
        configPath += "/config.properties";

        Properties configFile = new Properties();
        try {
            configFile.load(new FileInputStream(configPath));
        } catch (IOException e) {
            throw new RuntimeException("No config.properties", e);
        }

        connectionString = configFile.getProperty("connectionString");
        databaseName = configFile.getProperty("databaseName");
        databasePassword = configFile.getProperty("databasePassword");
        databaseUser = configFile.getProperty("databaseUser");
        testDatabaseName = configFile.getProperty("testDatabaseName");
    }

    public static String getConnectionString() {
        return connectionString;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static String getDatabasePassword() {
        return databasePassword;
    }

    public static String getDatabaseUser() {
        return databaseUser;
    }

    public static String getTestDatabaseName() {
        return testDatabaseName;
    }
}
