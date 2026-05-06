package com.example.cateringapp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the Java-based menu version cloning workflow.
 */
final class MenuVersionCloneWorkflow {
    private static final Logger logger = LoggerFactory.getLogger(MenuVersionCloneWorkflow.class);

    private final MenuVersionCloneQueriesDao queriesDao = new MenuVersionCloneQueriesDao();
    private final MenuVersionCloneInsertDao insertDao = new MenuVersionCloneInsertDao();

    long createNewMenuVersionJava(Connection conn, long menuId, long userId) throws SQLException {
        logger.info("Creating new menu version in Java for menuId={}, userId={}", menuId, userId);

        long projectId = queriesDao.getProjectId(conn, menuId);
        if (!queriesDao.isCollaborator(conn, projectId, userId)) {
            throw new SQLException("User is not a collaborator for project " + projectId);
        }

        Long activeVersionId = queriesDao.getActiveVersionId(conn, projectId);
        long sourceVersionId = queriesDao.resolveSourceVersionId(conn, menuId, activeVersionId);

        MenuVersionCloneQueriesDao.VersionSnapshotRow sourceVersion = queriesDao.getVersionSnapshot(conn, sourceVersionId);
        int nextVersionNumber = queriesDao.getNextVersionNumber(conn, menuId);

        boolean menuVersionIdentity = queriesDao.isIdentityColumn(conn,
                MenuCloneSchemaIds.TABLE_MENU_VERSIONS, MenuCloneSchemaIds.COLUMN_VERSION_ID);
        boolean sectionsIdentity = queriesDao.isIdentityColumn(conn,
                MenuCloneSchemaIds.TABLE_SECTIONS, MenuCloneSchemaIds.COLUMN_SECTION_ID);
        boolean menuItemIdentity = queriesDao.isIdentityColumn(conn,
                MenuCloneSchemaIds.TABLE_MENU_ITEMS, MenuCloneSchemaIds.COLUMN_MENU_ITEM_ID);
        boolean menuVersionSequence = queriesDao.sequenceExists(conn, MenuCloneSchemaIds.SEQUENCE_MENU_VERSIONS);
        boolean sectionsSequence = queriesDao.sequenceExists(conn, MenuCloneSchemaIds.SEQUENCE_SECTIONS);
        boolean menuItemSequence = queriesDao.sequenceExists(conn, MenuCloneSchemaIds.SEQUENCE_MENU_ITEMS);

        long newVersionId = insertDao.insertMenuVersion(conn, queriesDao, menuId, sourceVersion.templateId(),
                nextVersionNumber, sourceVersion.withPrices(), menuVersionIdentity, menuVersionSequence);

        var sections = queriesDao.getSectionsForVersion(conn, sourceVersionId);
        Map<Long, Long> sectionIdMapping = new HashMap<>();

        for (MenuVersionCloneQueriesDao.SectionRowData section : sections) {
            long newSectionId = insertDao.insertSection(conn, queriesDao, newVersionId, section, sectionsIdentity, sectionsSequence);
            sectionIdMapping.put(section.sectionId(), newSectionId);
        }

        for (MenuVersionCloneQueriesDao.SectionRowData section : sections) {
            long targetSectionId = sectionIdMapping.get(section.sectionId());
            for (MenuVersionCloneQueriesDao.MenuItemRowData menuItem : queriesDao.getMenuItemsForSection(conn, section.sectionId())) {
                insertDao.insertMenuItem(conn, queriesDao, targetSectionId, menuItem, menuItemIdentity, menuItemSequence);
            }
        }

        queriesDao.updateProjectActiveVersion(conn, projectId, newVersionId);

        logger.info("Successfully created new menu version in Java with ID: {}", newVersionId);
        return newVersionId;
    }
}
