package com.example.cateringapp.dao;

import com.example.cateringapp.dto.MenuVersionDto;

/**
 * Data Access Object interface for MenuVersion operations.
 */
public interface MenuVersionDaoInterface {

    /**
     * Creates a new menu version by calling the CreateNewMenuVersion stored function.
     *
     * @param menuId Menu ID
     * @param userId User ID
     * @return Version ID of the newly created menu version
     * @throws Exception if operation fails
     */
    long createNewMenuVersion(long menuId, long userId) throws Exception;

    /**
     * Retrieves a menu version by its ID.
     *
     * @param versionId Version ID
     * @return MenuVersionDto or null if not found
     * @throws Exception if operation fails
     */
    MenuVersionDto getMenuVersion(long versionId) throws Exception;
}

