package com.example.cateringapp.dao;

import com.example.cateringapp.db.Database;
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

    private static final String CALL_CREATE_NEW_MENU_VERSION = "{ ? = call CreateNewMenuVersion(?, ?) }";
    private static final String SELECT_FUNCTION_STATUS =
            "SELECT status FROM user_objects WHERE object_type = 'FUNCTION' AND object_name = ?";
    private static final String SELECT_FUNCTION_ERRORS =
            "SELECT line, position, text FROM user_errors WHERE name = ? AND type = 'FUNCTION' ORDER BY sequence";
    private static final String SELECT_MENU_VERSION =
            "SELECT version_id, menu_id, version_number, with_prices, template_id FROM menu_versions WHERE version_id = ?";

    private final MenuVersionCloneWorkflow cloneWorkflow = new MenuVersionCloneWorkflow();
    private final TransactionsDao transactionsDao = new TransactionsDao();

    @Override
    public long createNewMenuVersionDatabase(long menuId, long userId) {
        return createNewMenuVersionDatabase(null, menuId, userId);
    }

    public long createNewMenuVersionDatabase(Database pDb, long menuId, long userId) {
        return transactionsDao.executeInTransaction(pDb, conn -> createNewMenuVersionStoredFunction(conn, menuId, userId));
    }

    @Override
    public long createNewMenuVersionInJava(long menuId, long userId) {
        return createNewMenuVersionInJava(null, menuId, userId);
    }

    public long createNewMenuVersionInJava(Database pDb, long menuId, long userId) {
        return transactionsDao.executeInTransaction(pDb, conn -> cloneWorkflow.createNewMenuVersionJava(conn, menuId, userId));
    }

    @Override
    public MenuVersionDto getMenuVersion(long versionId) {
        return getMenuVersion(null, versionId);
    }

    public MenuVersionDto getMenuVersion(Database pDb, long versionId) {
        return transactionsDao.withDatabase(pDb, db -> {
            try (PreparedStatement stmt = db.createCommand(SELECT_MENU_VERSION)) {
                stmt.setLong(1, versionId);
                logger.debug("Fetching menu version with ID: {}", versionId);

                try (ResultSet rs = db.select(stmt)) {
                    if (rs.next()) {
                        MenuVersionDto dto = mapMenuVersion(rs);
                        logger.debug("Successfully retrieved menu version: {}", dto);
                        return dto;
                    }
                }
            }

            logger.warn("Menu version not found: {}", versionId);
            return null;
        });
    }

    private long createNewMenuVersionStoredFunction(Connection conn, long menuId, long userId) throws SQLException {
        validateStoredFunction(conn);

        try (CallableStatement stmt = conn.prepareCall(CALL_CREATE_NEW_MENU_VERSION)) {
            stmt.registerOutParameter(1, Types.BIGINT);
            stmt.setLong(2, menuId);
            stmt.setLong(3, userId);

            logger.info("Calling CreateNewMenuVersion stored function with menuId={}, userId={}", menuId, userId);
            stmt.execute();

            long newVersionId = stmt.getLong(1);
            if (stmt.wasNull()) {
                throw new SQLException("Stored function returned null");
            }

            logger.info("Successfully created new menu version with ID: {}", newVersionId);
            return newVersionId;
        }
    }

    private void validateStoredFunction(Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FUNCTION_STATUS)) {
            stmt.setString(1, "CREATENEWMENUVERSION");

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Stored function CreateNewMenuVersion was not found in the current schema");
                }

                String status = rs.getString("status");
                if (!"VALID".equalsIgnoreCase(status)) {
                    throw new SQLException(buildInvalidFunctionMessage(conn, status));
                }
            }
        }
    }

    private String buildInvalidFunctionMessage(Connection conn, String status) throws SQLException {
        StringBuilder message = new StringBuilder();
        message.append("Stored function CreateNewMenuVersion is not VALID (status=").append(status).append(")");

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FUNCTION_ERRORS)) {
            stmt.setString(1, "CREATENEWMENUVERSION");

            boolean hasErrors = false;
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (!hasErrors) {
                        message.append(". Oracle compilation errors:");
                        hasErrors = true;
                    }
                    message.append(System.lineSeparator())
                            .append(" - line ")
                            .append(rs.getInt("line"))
                            .append(", pos ")
                            .append(rs.getInt("position"))
                            .append(": ")
                            .append(rs.getString("text"));
                }
            }

            if (!hasErrors) {
                message.append(". Recompile the function using create-function.sql.");
            }
        }

        return message.toString();
    }

    private MenuVersionDto mapMenuVersion(ResultSet rs) throws SQLException {
        MenuVersionDto dto = new MenuVersionDto();
        dto.setVersionId(rs.getLong("version_id"));
        dto.setMenuId(rs.getLong("menu_id"));
        dto.setVersionNumber(rs.getInt("version_number"));
        dto.setWithPrices(rs.getInt("with_prices") == 1);
        Object templateId = rs.getObject("template_id");
        dto.setTemplateId(templateId != null ? ((Number) templateId).longValue() : null);
        return dto;
    }

}
