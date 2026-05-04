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
     */
    long createNewMenuVersion(long menuId, long userId);

    /**
     * Creates a new menu version using the Java/JDBC implementation.
     *
     * @param menuId Menu ID
     * @param userId User ID
     * @return Version ID of the newly created menu version
     */
    long createNewMenuVersionInJava(long menuId, long userId);

    /**
     * Retrieves a menu version by its ID.
     *
     * @param versionId Version ID
     * @return MenuVersionDto or null if not found
     */
    MenuVersionDto getMenuVersion(long versionId);
}

