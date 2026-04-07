package fr.univamu.iut.orders.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseManager handles MariaDB database connections.
 * This is a singleton class that provides global access to database connections.
 * Usage:
 *   DatabaseManager dbManager = DatabaseManager.getInstance();
 *   Connection conn = dbManager.getConnection();
 *   // Use connection...
 *   dbManager.closeConnection(conn);
 */
public class DatabaseManager {
    
    private static DatabaseManager instance;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    
    /**
     * Private constructor to enforce singleton pattern.
     * Loads database configuration from .env file.
     */
    private DatabaseManager() {
        Dotenv dotenv = Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();
        
        String host = dotenv.get("DATABASE_URL");
        String port = dotenv.get("DATABASE_PORT");
        String database = dotenv.get("DATABASE_NAME");
        this.username = dotenv.get("DATABASE_USERNAME");
        this.password = dotenv.get("DATABASE_PASSWORD");
        
        // Build JDBC connection string
        this.jdbcUrl = String.format("jdbc:mariadb://%s:%s/%s", host, port, database);
        
        // Load MariaDB driver
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MariaDB JDBC Driver not found", e);
        }
    }
    
    /**
     * Gets the singleton instance of DatabaseManager.
     * Initializes the instance on first call.
     * 
     * @return the DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Gets a new database connection.
     * 
     * @return a Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
    
    /**
     * Closes the given database connection.
     * 
     * @param connection the connection to close
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Tests the database connection.
     * 
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets the JDBC URL (for debugging).
     * 
     * @return the JDBC connection URL
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }
}
