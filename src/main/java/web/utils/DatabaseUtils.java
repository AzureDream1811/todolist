package web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtils {
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
        Properties props = new Properties();
        try (InputStream input = DatabaseUtils.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                throw new RuntimeException("database.properties not found in classpath");
            }

            props.load(input);

            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");

            if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
                throw new RuntimeException("Missing required database configuration in database.properties");
            }


        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection successful!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database. Please check your connection settings:");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            throw e;
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Connection test failed", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Connection test: " + testConnection());
    }
}
