package com.example.cateringapp.dao;

import com.example.cateringapp.db.DatabaseConfig;
import com.example.cateringapp.dto.MenuVersionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String SELECT_MENU_PROJECT_ID = "SELECT project_id FROM menus WHERE menu_id = ?";
    private static final String SELECT_COLLABORATOR_COUNT =
            "SELECT COUNT(*) FROM project_collaborators WHERE project_id = ? AND user_id = ?";
    private static final String SELECT_PROJECT_ACTIVE_VERSION =
            "SELECT active_version_id FROM projects WHERE project_id = ?";
    private static final String SELECT_LATEST_VERSION_ID =
            "SELECT version_id FROM (SELECT version_id FROM menu_versions WHERE menu_id = ? ORDER BY version_number DESC) WHERE ROWNUM = 1";
    private static final String SELECT_MAX_VERSION_NUMBER =
            "SELECT NVL(MAX(version_number), 0) FROM menu_versions WHERE menu_id = ?";
    private static final String SELECT_VERSION_TEMPLATE_AND_PRICES =
            "SELECT template_id, with_prices FROM menu_versions WHERE version_id = ?";
    private static final String SELECT_SECTIONS_BY_VERSION =
            "SELECT section_id, name, display_order FROM sections WHERE version_id = ? ORDER BY display_order";
    private static final String SELECT_MENU_ITEMS_BY_SECTION =
            "SELECT item_id, servings_per_person, price_at_version, display_order, notes FROM menu_items WHERE section_id = ? ORDER BY display_order";
    private static final String UPDATE_PROJECT_ACTIVE_VERSION =
            "UPDATE projects SET active_version_id = ? WHERE project_id = ?";
    private static final String SELECT_IDENTITY_COLUMNS =
            "SELECT COUNT(*) FROM user_tab_identity_cols WHERE table_name = ? AND column_name = ?";
    private static final String SELECT_SEQUENCE_EXISTS =
            "SELECT COUNT(*) FROM user_sequences WHERE sequence_name = ?";
    private static final String SELECT_MAX_VERSION_ID = "SELECT NVL(MAX(version_id), 0) FROM menu_versions";
    private static final String SELECT_MAX_SECTION_ID = "SELECT NVL(MAX(section_id), 0) FROM sections";
    private static final String SELECT_MAX_MENU_ITEM_ID = "SELECT NVL(MAX(menu_item_id), 0) FROM menu_items";
    private static final String SELECT_NEXTVAL_TEMPLATE = "SELECT %s.NEXTVAL FROM dual";

    @Override
    public long createNewMenuVersion(long menuId, long userId) {
        return executeInTransaction(conn -> createNewMenuVersionStoredFunction(conn, menuId, userId));
    }

    @Override
    public long createNewMenuVersionInJava(long menuId, long userId) {
        return executeInTransaction(conn -> createNewMenuVersionJava(conn, menuId, userId));
    }

    @Override
    public MenuVersionDto getMenuVersion(long versionId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_VERSION)) {
            stmt.setLong(1, versionId);

            logger.debug("Fetching menu version with ID: {}", versionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MenuVersionDto dto = new MenuVersionDto();
                    dto.setVersionId(rs.getLong("version_id"));
                    dto.setMenuId(rs.getLong("menu_id"));
                    dto.setVersionNumber(rs.getInt("version_number"));
                    dto.setWithPrices(rs.getInt("with_prices") == 1);
                    Object templateId = rs.getObject("template_id");
                    dto.setTemplateId(templateId != null ? ((Number) templateId).longValue() : null);

                    logger.debug("Successfully retrieved menu version: {}", dto);
                    return dto;
                }
            }

            logger.warn("Menu version not found: {}", versionId);
            return null;
        } catch (SQLException e) {
            logger.error("Database error retrieving menu version: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve menu version: " + e.getMessage(), e);
        }
    }

    private long executeInTransaction(SqlFunction<Long> action) {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            long result = action.apply(conn);
            conn.commit();
            return result;
        } catch (SQLException e) {
            rollbackQuietly(conn);
            logger.error("Database error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        } catch (RuntimeException e) {
            rollbackQuietly(conn);
            throw e;
        } finally {
            closeConnectionQuietly(conn);
        }
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

    private long createNewMenuVersionJava(Connection conn, long menuId, long userId) throws SQLException {
        logger.info("Creating new menu version in Java for menuId={}, userId={}", menuId, userId);

        long projectId = getProjectId(conn, menuId);
        if (!isCollaborator(conn, projectId, userId)) {
            throw new SQLException("User is not a collaborator for project " + projectId);
        }

        Long activeVersionId = getActiveVersionId(conn, projectId);
        long sourceVersionId = resolveSourceVersionId(conn, menuId, activeVersionId);

        VersionSnapshot sourceVersion = getVersionSnapshot(conn, sourceVersionId);
        int nextVersionNumber = getNextVersionNumber(conn, menuId);

        boolean menuVersionIdentity = isIdentityColumn(conn, "MENU_VERSIONS", "VERSION_ID");
        boolean sectionsIdentity = isIdentityColumn(conn, "SECTIONS", "SECTION_ID");
        boolean menuItemIdentity = isIdentityColumn(conn, "MENU_ITEMS", "MENU_ITEM_ID");
        boolean menuVersionSequence = sequenceExists(conn, "MENU_VERSIONS_SEQ");
        boolean sectionsSequence = sequenceExists(conn, "SECTIONS_SEQ");
        boolean menuItemSequence = sequenceExists(conn, "MENU_ITEMS_SEQ");

        long newVersionId = insertMenuVersion(conn, menuId, sourceVersion.templateId(), nextVersionNumber,
                sourceVersion.withPrices(), menuVersionIdentity, menuVersionSequence);

        List<SectionRow> sections = getSectionsForVersion(conn, sourceVersionId);
        Map<Long, Long> sectionIdMapping = new HashMap<>();

        for (SectionRow section : sections) {
            long newSectionId = insertSection(conn, newVersionId, section, sectionsIdentity, sectionsSequence);
            sectionIdMapping.put(section.sectionId(), newSectionId);
        }

        for (SectionRow section : sections) {
            long targetSectionId = sectionIdMapping.get(section.sectionId());
            for (MenuItemRow menuItem : getMenuItemsForSection(conn, section.sectionId())) {
                insertMenuItem(conn, targetSectionId, menuItem, menuItemIdentity, menuItemSequence);
            }
        }

        updateProjectActiveVersion(conn, projectId, newVersionId);

        logger.info("Successfully created new menu version in Java with ID: {}", newVersionId);
        return newVersionId;
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
        List<String> errors = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_FUNCTION_ERRORS)) {
            stmt.setString(1, "CREATENEWMENUVERSION");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    errors.add("line " + rs.getInt("line") + ", pos " + rs.getInt("position") + ": " + rs.getString("text"));
                }
            }
        }

        StringBuilder message = new StringBuilder();
        message.append("Stored function CreateNewMenuVersion is not VALID (status=").append(status).append(")");

        if (!errors.isEmpty()) {
            message.append(". Oracle compilation errors:");
            for (String error : errors) {
                message.append(System.lineSeparator()).append(" - ").append(error);
            }
        } else {
            message.append(". Recompile the function using create-function.sql.");
        }

        return message.toString();
    }

    private long getProjectId(Connection conn, long menuId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_PROJECT_ID)) {
            stmt.setLong(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Menu not found: " + menuId);
    }

    private boolean isCollaborator(Connection conn, long projectId, long userId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_COLLABORATOR_COUNT)) {
            stmt.setLong(1, projectId);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Long getActiveVersionId(Connection conn, long projectId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_PROJECT_ACTIVE_VERSION)) {
            stmt.setLong(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object value = rs.getObject(1);
                    return value != null ? ((Number) value).longValue() : null;
                }
            }
        }

        return null;
    }

    private long resolveSourceVersionId(Connection conn, long menuId, Long activeVersionId) throws SQLException {
        if (activeVersionId != null && versionBelongsToMenu(conn, activeVersionId, menuId)) {
            return activeVersionId;
        }

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_LATEST_VERSION_ID)) {
            stmt.setLong(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Menu has no versions to clone");
    }

    private boolean versionBelongsToMenu(Connection conn, long versionId, long menuId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM menu_versions WHERE version_id = ? AND menu_id = ?")) {
            stmt.setLong(1, versionId);
            stmt.setLong(2, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private int getNextVersionNumber(Connection conn, long menuId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_MAX_VERSION_NUMBER)) {
            stmt.setLong(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }
            }
        }

        return 1;
    }

    private VersionSnapshot getVersionSnapshot(Connection conn, long versionId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_VERSION_TEMPLATE_AND_PRICES)) {
            stmt.setLong(1, versionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object templateId = rs.getObject("template_id");
                    boolean withPrices = rs.getInt("with_prices") == 1;
                    return new VersionSnapshot(templateId != null ? ((Number) templateId).longValue() : null, withPrices);
                }
            }
        }

        throw new SQLException("Active version not found: " + versionId);
    }

    private List<SectionRow> getSectionsForVersion(Connection conn, long versionId) throws SQLException {
        List<SectionRow> sections = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_SECTIONS_BY_VERSION)) {
            stmt.setLong(1, versionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sections.add(new SectionRow(
                            rs.getLong("section_id"),
                            rs.getString("name"),
                            rs.getInt("display_order")
                    ));
                }
            }
        }

        return sections;
    }

    private List<MenuItemRow> getMenuItemsForSection(Connection conn, long sectionId) throws SQLException {
        List<MenuItemRow> menuItems = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_ITEMS_BY_SECTION)) {
            stmt.setLong(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(new MenuItemRow(
                            rs.getLong("item_id"),
                            rs.getBigDecimal("servings_per_person"),
                            rs.getBigDecimal("price_at_version"),
                            rs.getInt("display_order"),
                            rs.getString("notes")
                    ));
                }
            }
        }

        return menuItems;
    }

    private long insertMenuVersion(Connection conn, long menuId, Long templateId, int versionNumber,
                                   boolean withPrices, boolean identity, boolean sequence) throws SQLException {
        if (identity) {
            String sql = "INSERT INTO menu_versions (menu_id, template_id, version_number, with_prices, created_at) VALUES (?, ?, ?, ?, SYSTIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, menuId);
                setNullableLong(stmt, 2, templateId);
                stmt.setInt(3, versionNumber);
                stmt.setInt(4, withPrices ? 1 : 0);
                return executeAndReturnGeneratedKey(stmt, "menu_versions.version_id");
            }
        }

        if (sequence) {
            long id = nextSequenceValueAboveMax(conn, "MENU_VERSIONS_SEQ", SELECT_MAX_VERSION_ID);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO menu_versions (version_id, menu_id, template_id, version_number, with_prices, created_at) VALUES (?, ?, ?, ?, ?, SYSTIMESTAMP)")) {
                stmt.setLong(1, id);
                stmt.setLong(2, menuId);
                setNullableLong(stmt, 3, templateId);
                stmt.setInt(4, versionNumber);
                stmt.setInt(5, withPrices ? 1 : 0);
                stmt.executeUpdate();
                return id;
            }
        }

        throw new SQLException("No ID generation strategy available for menu_versions.version_id");
    }

    private long insertSection(Connection conn, long versionId, SectionRow section, boolean identity, boolean sequence)
            throws SQLException {
        if (identity) {
            String sql = "INSERT INTO sections (version_id, name, display_order, created_at, updated_at) VALUES (?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, versionId);
                stmt.setString(2, section.name());
                stmt.setInt(3, section.displayOrder());
                return executeAndReturnGeneratedKey(stmt, "sections.section_id");
            }
        }

        if (sequence) {
            long id = nextSequenceValueAboveMax(conn, "SECTIONS_SEQ", SELECT_MAX_SECTION_ID);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO sections (section_id, version_id, name, display_order, created_at, updated_at) VALUES (?, ?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)")) {
                stmt.setLong(1, id);
                stmt.setLong(2, versionId);
                stmt.setString(3, section.name());
                stmt.setInt(4, section.displayOrder());
                stmt.executeUpdate();
                return id;
            }
        }

        throw new SQLException("No ID generation strategy available for sections.section_id");
    }

    private void insertMenuItem(Connection conn, long sectionId, MenuItemRow menuItem, boolean identity, boolean sequence)
            throws SQLException {
        if (identity) {
            String sql = "INSERT INTO menu_items (section_id, item_id, servings_per_person, price_at_version, display_order, notes, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, sectionId);
                stmt.setLong(2, menuItem.itemId());
                setNullableBigDecimal(stmt, 3, menuItem.servingsPerPerson());
                setNullableBigDecimal(stmt, 4, menuItem.priceAtVersion());
                stmt.setInt(5, menuItem.displayOrder());
                stmt.setString(6, menuItem.notes());
                stmt.executeUpdate();
            }
            return;
        }

        if (sequence) {
            long id = nextSequenceValueAboveMax(conn, "MENU_ITEMS_SEQ", SELECT_MAX_MENU_ITEM_ID);
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO menu_items (menu_item_id, section_id, item_id, servings_per_person, price_at_version, display_order, notes, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)")) {
                stmt.setLong(1, id);
                stmt.setLong(2, sectionId);
                stmt.setLong(3, menuItem.itemId());
                setNullableBigDecimal(stmt, 4, menuItem.servingsPerPerson());
                setNullableBigDecimal(stmt, 5, menuItem.priceAtVersion());
                stmt.setInt(6, menuItem.displayOrder());
                stmt.setString(7, menuItem.notes());
                stmt.executeUpdate();
                return;
            }
        }

        throw new SQLException("No ID generation strategy available for menu_items.menu_item_id");
    }

    private boolean isIdentityColumn(Connection conn, String tableName, String columnName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_IDENTITY_COLUMNS)) {
            stmt.setString(1, tableName);
            stmt.setString(2, columnName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean sequenceExists(Connection conn, String sequenceName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_SEQUENCE_EXISTS)) {
            stmt.setString(1, sequenceName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private long nextSequenceValueAboveMax(Connection conn, String sequenceName, String maxSql) throws SQLException {
        long maxId = 0;
        try (PreparedStatement stmt = conn.prepareStatement(maxSql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                maxId = rs.getLong(1);
            }
        }

        long nextValue = getNextSequenceValue(conn, sequenceName);
        while (nextValue <= maxId) {
            nextValue = getNextSequenceValue(conn, sequenceName);
        }

        return nextValue;
    }

    private long getNextSequenceValue(Connection conn, String sequenceName) throws SQLException {
        String sql = String.format(SELECT_NEXTVAL_TEMPLATE, sequenceName);
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        throw new SQLException("Unable to obtain NEXTVAL for sequence " + sequenceName);
    }

    private long executeAndReturnGeneratedKey(PreparedStatement stmt, String keyName) throws SQLException {
        int affected = stmt.executeUpdate();
        if (affected != 1) {
            throw new SQLException("Unexpected affected row count for " + keyName + ": " + affected);
        }

        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                return keys.getLong(1);
            }
        }

        throw new SQLException("No generated key returned for " + keyName);
    }

    private void updateProjectActiveVersion(Connection conn, long projectId, long newVersionId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_ACTIVE_VERSION)) {
            stmt.setLong(1, newVersionId);
            stmt.setLong(2, projectId);
            stmt.executeUpdate();
        }
    }

    private void setNullableLong(PreparedStatement stmt, int index, Long value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.BIGINT);
        } else {
            stmt.setLong(index, value);
        }
    }

    private void setNullableBigDecimal(PreparedStatement stmt, int index, BigDecimal value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, Types.DECIMAL);
        } else {
            stmt.setBigDecimal(index, value);
        }
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                logger.warn("Error rolling back transaction: {}", e.getMessage());
            }
        }
    }

    private void closeConnectionQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.warn("Error closing Connection: {}", e.getMessage());
            }
        }
    }

    private interface SqlFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    private record VersionSnapshot(Long templateId, boolean withPrices) {
    }

    private record SectionRow(long sectionId, String name, int displayOrder) {
    }

    private record MenuItemRow(long itemId, BigDecimal servingsPerPerson, BigDecimal priceAtVersion,
                               int displayOrder, String notes) {
    }
}
