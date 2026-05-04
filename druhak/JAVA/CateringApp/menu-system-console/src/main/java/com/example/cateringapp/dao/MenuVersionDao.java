package com.example.cateringapp.dao;

import com.example.cateringapp.db.DatabaseConfig;
import com.example.cateringapp.dto.MenuVersionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Implementation of MenuVersionDaoInterface using plain JDBC.
 */
public class MenuVersionDao implements MenuVersionDaoInterface {
    private static final Logger logger = LoggerFactory.getLogger(MenuVersionDao.class);

    // SQL Queries
    private static final String CALL_CREATE_NEW_MENU_VERSION = "{ ? = call CreateNewMenuVersion(?, ?) }";
    private static final String SELECT_MENU_VERSION = "SELECT version_id, menu_id, version_number, with_prices, template_id " +
                                                       "FROM menu_versions WHERE version_id = ?";

    /**
     * Creates a new menu version by calling the Oracle stored function CreateNewMenuVersion.
     *
     * @param menuId Menu ID
     * @param userId User ID
     * @return Version ID of the newly created menu version
     * @throws Exception if operation fails
     */
    @Override
    public long createNewMenuVersion(long menuId, long userId) throws Exception {
        Connection conn = null;
        CallableStatement stmt = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareCall(CALL_CREATE_NEW_MENU_VERSION);

            // Register output parameter (return value)
            stmt.registerOutParameter(1, Types.BIGINT);

            // Set input parameters
            stmt.setLong(2, menuId);
            stmt.setLong(3, userId);

            logger.info("Calling CreateNewMenuVersion stored function with menuId={}, userId={}", menuId, userId);

            // Execute the function
            stmt.execute();

            // Get the return value
            long newVersionId = stmt.getLong(1);

            if (stmt.wasNull()) {
                logger.error("CreateNewMenuVersion returned NULL");
                throw new RuntimeException("Stored function returned null");
            }

            logger.info("Successfully created new menu version with ID: {}", newVersionId);
            return newVersionId;

        } catch (SQLException e) {
            logger.error("Database error calling CreateNewMenuVersion: {}", e.getMessage());
            throw new RuntimeException("Failed to create new menu version: " + e.getMessage(), e);
        } finally {
            closeResources(stmt, conn);
        }
    }

    /**
     * Retrieves a menu version by its ID.
     *
     * @param versionId Version ID
     * @return MenuVersionDto or null if not found
     * @throws Exception if operation fails
     */
    @Override
    public MenuVersionDto getMenuVersion(long versionId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(SELECT_MENU_VERSION);
            stmt.setLong(1, versionId);

            logger.debug("Fetching menu version with ID: {}", versionId);

            rs = stmt.executeQuery();

            if (rs.next()) {
                MenuVersionDto dto = new MenuVersionDto();
                dto.setVersionId(rs.getLong("version_id"));
                dto.setMenuId(rs.getLong("menu_id"));
                dto.setVersionNumber(rs.getInt("version_number"));
                dto.setWithPrices(rs.getInt("with_prices") == 1);
                dto.setTemplateId(rs.getObject("template_id") != null ? rs.getLong("template_id") : null);

                logger.debug("Successfully retrieved menu version: {}", dto);
                return dto;
            }

            logger.warn("Menu version not found: {}", versionId);
            return null;

        } catch (SQLException e) {
            logger.error("Database error retrieving menu version: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve menu version: " + e.getMessage(), e);
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    /**
     * Closes database resources safely.
     *
     * @param rs ResultSet to close
     * @param stmt Statement to close
     * @param conn Connection to close
     */
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("Error closing ResultSet: {}", e.getMessage());
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Error closing Statement: {}", e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Error closing Connection: {}", e.getMessage());
            }
        }
    }

    /**
     * Closes database resources safely.
     *
     * @param stmt CallableStatement to close
     * @param conn Connection to close
     */
    private void closeResources(CallableStatement stmt, Connection conn) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Error closing Statement: {}", e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Error closing Connection: {}", e.getMessage());
            }
        }
    }
}

