package com.example.cateringapp.dao;

/**
 * Shared schema identifiers used by the menu clone DAOs.
 */
final class MenuCloneSchemaIds {
    static final String TABLE_MENU_VERSIONS = "MENU_VERSIONS";
    static final String TABLE_SECTIONS = "SECTIONS";
    static final String TABLE_MENU_ITEMS = "MENU_ITEMS";

    static final String COLUMN_VERSION_ID = "VERSION_ID";
    static final String COLUMN_SECTION_ID = "SECTION_ID";
    static final String COLUMN_MENU_ITEM_ID = "MENU_ITEM_ID";

    static final String SEQUENCE_MENU_VERSIONS = "MENU_VERSIONS_SEQ";
    static final String SEQUENCE_SECTIONS = "SECTIONS_SEQ";
    static final String SEQUENCE_MENU_ITEMS = "MENU_ITEMS_SEQ";

    static final String KEY_MENU_VERSION_ID = "menu_versions.version_id";
    static final String KEY_SECTION_ID = "sections.section_id";

    private MenuCloneSchemaIds() {
    }
}

