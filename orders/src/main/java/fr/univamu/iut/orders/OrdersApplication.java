package fr.univamu.iut.orders;

import fr.univamu.iut.orders.database.DatabaseManager;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class OrdersApplication extends Application {
    
    /**
     * Initialize DatabaseManager when the application starts.
     * This ensures the singleton is created early and validates the database connection.
     */
    public OrdersApplication() {
        super();
        initializeDatabaseManager();
    }
    
    private void initializeDatabaseManager() {
        try {
            DatabaseManager dbManager = DatabaseManager.getInstance();
            System.out.println("DatabaseManager initialized successfully");
            System.out.println("JDBC URL: " + dbManager.getJdbcUrl());
            
            // Test the connection
            if (dbManager.testConnection()) {
                System.out.println("Database connection: SUCCESS");
            } else {
                System.err.println("Database connection: FAILED");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize DatabaseManager: " + e.getMessage());
        }
    }
}