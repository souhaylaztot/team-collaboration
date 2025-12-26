package com.propertymanager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    
    static {
        try {
            loadDatabaseConfig();
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ PostgreSQL driver loaded successfully!");
        } catch (Exception e) {
            System.err.println("❌ Failed to load database configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void loadDatabaseConfig() throws IOException {
        Properties props = new Properties();
        
        // Try to load from db.properties file first
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                DB_URL = "jdbc:postgresql://" + props.getProperty("db.host", "localhost") + 
                        ":" + props.getProperty("db.port", "5432") + 
                        "/" + props.getProperty("db.name", "property_manager");
                DB_USER = props.getProperty("db.user", "postgres");
                DB_PASSWORD = props.getProperty("db.password", "4426");
                System.out.println("📁 Database config loaded from db.properties");
                return;
            }
        } catch (Exception e) {
            System.out.println("⚠️ db.properties not found, using default values");
        }
        
        // Fallback to default values matching your .env file
        DB_URL = "jdbc:postgresql://localhost:5432/property_manager";
        DB_USER = "postgres";
        DB_PASSWORD = "4426";
        System.out.println("🔧 Using default database configuration");
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("🔗 Connected to PostgreSQL database");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("🔍 URL: " + DB_URL);
            System.err.println("👤 User: " + DB_USER);
            throw e;
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Database connection test successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    public static void close() {
        // PostgreSQL connections are closed automatically when using try-with-resources
        System.out.println("🔒 Database connections closed");
    }
}