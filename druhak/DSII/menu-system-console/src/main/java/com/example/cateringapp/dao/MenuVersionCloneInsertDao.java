package com.example.cateringapp.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/**
 * Insert-focused DAO used by the Java menu version clone workflow.
 */
final class MenuVersionCloneInsertDao {
    private static final String SELECT_NEXTVAL_TEMPLATE = "SELECT %s.NEXTVAL FROM dual";

    long insertMenuVersion(Connection conn, MenuVersionCloneQueriesDao queriesDao, long menuId, Long templateId,
                           int versionNumber, boolean withPrices, boolean identity, boolean sequence)
            throws SQLException {
        if (identity) {
            String sql = "INSERT INTO menu_versions (menu_id, template_id, version_number, with_prices, created_at) VALUES (?, ?, ?, ?, SYSTIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, menuId);
                setNullableLong(stmt, 2, templateId);
                stmt.setInt(3, versionNumber);
                stmt.setInt(4, withPrices ? 1 : 0);
                return executeAndReturnGeneratedKey(stmt, MenuCloneSchemaIds.KEY_MENU_VERSION_ID);
            }
        }

        if (sequence) {
            long id = nextSequenceValueAboveMax(conn, MenuCloneSchemaIds.SEQUENCE_MENU_VERSIONS, queriesDao.getMaxVersionId(conn));
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

    long insertSection(Connection conn, MenuVersionCloneQueriesDao queriesDao, long versionId,
                       MenuVersionCloneQueriesDao.SectionRowData section, boolean identity, boolean sequence)
            throws SQLException {
        if (identity) {
            String sql = "INSERT INTO sections (version_id, name, display_order, created_at, updated_at) VALUES (?, ?, ?, SYSTIMESTAMP, SYSTIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, versionId);
                stmt.setString(2, section.name());
                stmt.setInt(3, section.displayOrder());
                return executeAndReturnGeneratedKey(stmt, MenuCloneSchemaIds.KEY_SECTION_ID);
            }
        }

        if (sequence) {
            long id = nextSequenceValueAboveMax(conn, MenuCloneSchemaIds.SEQUENCE_SECTIONS, queriesDao.getMaxSectionId(conn));
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

    void insertMenuItem(Connection conn, MenuVersionCloneQueriesDao queriesDao, long sectionId,
                        MenuVersionCloneQueriesDao.MenuItemRowData menuItem, boolean identity, boolean sequence)
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
            long id = nextSequenceValueAboveMax(conn, MenuCloneSchemaIds.SEQUENCE_MENU_ITEMS, queriesDao.getMaxMenuItemId(conn));
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
            }
            return;
        }

        throw new SQLException("No ID generation strategy available for menu_items.menu_item_id");
    }

    private long nextSequenceValueAboveMax(Connection conn, String sequenceName, long maxId) throws SQLException {
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
}

