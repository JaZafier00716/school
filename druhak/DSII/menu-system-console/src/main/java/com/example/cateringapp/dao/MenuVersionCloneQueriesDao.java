package com.example.cateringapp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Query-focused DAO used by the Java menu version clone workflow.
 */
final class MenuVersionCloneQueriesDao {
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
    private static final String SELECT_VERSION_BELONGS_TO_MENU =
            "SELECT COUNT(*) FROM menu_versions WHERE version_id = ? AND menu_id = ?";
    private static final String SELECT_IDENTITY_COLUMNS =
            "SELECT COUNT(*) FROM user_tab_identity_cols WHERE table_name = ? AND column_name = ?";
    private static final String SELECT_SEQUENCE_EXISTS =
            "SELECT COUNT(*) FROM user_sequences WHERE sequence_name = ?";
    private static final String SELECT_MAX_VERSION_ID = "SELECT NVL(MAX(version_id), 0) FROM menu_versions";
    private static final String SELECT_MAX_SECTION_ID = "SELECT NVL(MAX(section_id), 0) FROM sections";
    private static final String SELECT_MAX_MENU_ITEM_ID = "SELECT NVL(MAX(menu_item_id), 0) FROM menu_items";
    private static final String UPDATE_PROJECT_ACTIVE_VERSION =
            "UPDATE projects SET active_version_id = ? WHERE project_id = ?";

    long getProjectId(Connection conn, long menuId) throws SQLException {
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

    boolean isCollaborator(Connection conn, long projectId, long userId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_COLLABORATOR_COUNT)) {
            stmt.setLong(1, projectId);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    Long getActiveVersionId(Connection conn, long projectId) throws SQLException {
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

    long resolveSourceVersionId(Connection conn, long menuId, Long activeVersionId) throws SQLException {
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

    int getNextVersionNumber(Connection conn, long menuId) throws SQLException {
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

    VersionSnapshotRow getVersionSnapshot(Connection conn, long versionId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_VERSION_TEMPLATE_AND_PRICES)) {
            stmt.setLong(1, versionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Object templateId = rs.getObject("template_id");
                    boolean withPrices = rs.getInt("with_prices") == 1;
                    Long resolvedTemplateId = templateId != null ? ((Number) templateId).longValue() : null;
                    return new VersionSnapshotRow(resolvedTemplateId, withPrices);
                }
            }
        }
        throw new SQLException("Active version not found: " + versionId);
    }

    List<SectionRowData> getSectionsForVersion(Connection conn, long versionId) throws SQLException {
        List<SectionRowData> sections = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_SECTIONS_BY_VERSION)) {
            stmt.setLong(1, versionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sections.add(new SectionRowData(
                            rs.getLong("section_id"),
                            rs.getString("name"),
                            rs.getInt("display_order")
                    ));
                }
            }
        }
        return sections;
    }

    List<MenuItemRowData> getMenuItemsForSection(Connection conn, long sectionId) throws SQLException {
        List<MenuItemRowData> menuItems = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_MENU_ITEMS_BY_SECTION)) {
            stmt.setLong(1, sectionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    menuItems.add(new MenuItemRowData(
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

    boolean isIdentityColumn(Connection conn, String tableName, String columnName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_IDENTITY_COLUMNS)) {
            stmt.setString(1, tableName);
            stmt.setString(2, columnName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    boolean sequenceExists(Connection conn, String sequenceName) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_SEQUENCE_EXISTS)) {
            stmt.setString(1, sequenceName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    long getMaxVersionId(Connection conn) throws SQLException {
        return readMaxId(conn, SELECT_MAX_VERSION_ID);
    }

    long getMaxSectionId(Connection conn) throws SQLException {
        return readMaxId(conn, SELECT_MAX_SECTION_ID);
    }

    long getMaxMenuItemId(Connection conn) throws SQLException {
        return readMaxId(conn, SELECT_MAX_MENU_ITEM_ID);
    }

    void updateProjectActiveVersion(Connection conn, long projectId, long newVersionId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_PROJECT_ACTIVE_VERSION)) {
            stmt.setLong(1, newVersionId);
            stmt.setLong(2, projectId);
            stmt.executeUpdate();
        }
    }

    private boolean versionBelongsToMenu(Connection conn, long versionId, long menuId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_VERSION_BELONGS_TO_MENU)) {
            stmt.setLong(1, versionId);
            stmt.setLong(2, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private long readMaxId(Connection conn, String sql) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    record VersionSnapshotRow(Long templateId, boolean withPrices) {
    }

    record SectionRowData(long sectionId, String name, int displayOrder) {
    }

    record MenuItemRowData(long itemId, BigDecimal servingsPerPerson, BigDecimal priceAtVersion,
                           int displayOrder, String notes) {
    }
}

