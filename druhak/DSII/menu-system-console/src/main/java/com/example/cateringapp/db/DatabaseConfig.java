package com.example.cateringapp.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database configuration and connection management.
 * Loads configuration from db.properties file.
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private static final String CONFIG_FILE = "db.properties";
    private static String url;
    private static String username;
    private static String password;

    static {
        loadConfiguration();
    }

    /**
     * Loads database configuration from properties file.
     */
    private static void loadConfiguration() {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.error("Configuration file {} not found", CONFIG_FILE);
                throw new RuntimeException("db.properties file not found on classpath");
            }

            Properties props = new Properties();
            props.load(input);

            url = props.getProperty("db.url", "jdbc:oracle:thin:@bayer.cs.vsb.cz:1521:oracle");
            username = props.getProperty("db.username", "ZAM0074");
            password = props.getProperty("db.password", "osVSOwCvA6yO96Ao");

            logger.info("Database configuration loaded successfully");
            logger.debug("Database URL: {}", url);
        } catch (IOException e) {
            logger.error("Failed to load database configuration: {}", e.getMessage());
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    /**
     * Gets a connection to the Oracle database.
     *
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            logger.debug("Successfully obtained database connection");
            return conn;
        } catch (SQLException e) {
            logger.error("Failed to obtain database connection: {}", e.getMessage());
            throw e;
        }
    }
}

