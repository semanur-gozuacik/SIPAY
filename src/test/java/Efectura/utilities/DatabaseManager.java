package Efectura.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager(String dbUrl, String username, String password) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, username, password);
    }

    public static synchronized Connection getConnection(String dbUrl, String username, String password) {
        try {
            if (instance == null || instance.connection == null || instance.connection.isClosed()) {
                instance = new DatabaseManager(dbUrl, username, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error", e);
        }

        return instance.connection;
    }
}