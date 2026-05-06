package com.example.cateringapp.db;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC helper inspired by the lecture template.
 * Keeps a connection instance and provides convenience methods for DAOs.
 */
@Slf4j
public class Database implements AutoCloseable {

    @Getter
    private Connection connection;
    private boolean transactionActive;

    public boolean connect(String connectionString) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(connectionString);
                log.debug("Database connection opened via provided connection string");
            }
            return true;
        } catch (SQLException e) {
            log.error("Failed to connect using provided connection string: {}", e.getMessage());
            return false;
        }
    }

    public boolean connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConfig.getConnection();
                log.debug("Database connection opened via DatabaseConfig");
            }
            return true;
        } catch (SQLException e) {
            log.error("Failed to connect using DatabaseConfig: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
        Connection currentConnection = connection;
        connection = null;
        transactionActive = false;

        if (currentConnection == null) {
            return;
        }

        try {
            if (!currentConnection.isClosed()) {
                currentConnection.close();
                log.debug("Database connection closed");
            }
        } catch (SQLException e) {
            log.warn("Error closing database connection: {}", e.getMessage());
        }
    }

    public void beginTransaction() throws SQLException {
        ensureConnected();
        if (!transactionActive) {
            connection.setAutoCommit(false);
            transactionActive = true;
        }
    }

    public void endTransaction() throws SQLException {
        ensureConnected();
        if (transactionActive) {
            connection.commit();
            connection.setAutoCommit(true);
            transactionActive = false;
        }
    }

    public void rollback() throws SQLException {
        ensureConnected();
        if (transactionActive) {
            connection.rollback();
            connection.setAutoCommit(true);
            transactionActive = false;
        }
    }

    public ResultSet select(PreparedStatement command) throws SQLException {
        return command.executeQuery();
    }

    public int executeNonQuery(PreparedStatement command) throws SQLException {
        return command.executeUpdate();
    }

    public int executeScalar(PreparedStatement command) throws SQLException {
        try (ResultSet rs = command.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public PreparedStatement createCommand(String sql) throws SQLException {
        ensureConnected();
        // In JDBC, transaction scope is bound to the connection.
        return connection.prepareStatement(sql);
    }

    public static Database connect(Database existingDb) {
        if (existingDb == null) {
            Database db = new Database();
            if (!db.connect()) {
                throw new IllegalStateException("Failed to open database connection");
            }
            return db;
        }
        return existingDb;
    }

    public static void close(Database originalDb, Database dbToClose) {
        if (originalDb == null && dbToClose != null) {
            dbToClose.close();
        }
    }

    private void ensureConnected() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new SQLException("Database connection is not open");
        }
    }
}
