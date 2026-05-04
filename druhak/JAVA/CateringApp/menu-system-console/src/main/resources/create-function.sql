-- Create the CreateNewMenuVersion stored function
-- This function creates a new menu version by cloning sections and menu items from the active version

CREATE OR REPLACE FUNCTION CreateNewMenuVersion(
    p_menu_id IN menu_versions.menu_id%TYPE,
    p_user_id IN users.user_id%TYPE
) RETURN menu_versions.version_id%TYPE IS
    v_new_version_id menu_versions.version_id%TYPE;
    v_project_id projects.project_id%TYPE;
    v_active_version_id menu_versions.version_id%TYPE;
    v_next_version_number NUMBER;
    v_is_collaborator NUMBER;
    v_template_id templates.template_id%TYPE;
    v_with_prices NUMBER;
    v_old_section_id sections.section_id%TYPE;
    v_new_section_id sections.section_id%TYPE;
    v_old_menu_item_id menu_items.menu_item_id%TYPE;

    -- Cursor to iterate through sections
    CURSOR sections_cur(p_version_id IN menu_versions.version_id%TYPE) IS
        SELECT section_id, name, display_order
        FROM sections
        WHERE version_id = p_version_id
        ORDER BY display_order;

    -- Cursor to iterate through menu items in a section
    CURSOR menu_items_cur(p_section_id IN sections.section_id%TYPE) IS
        SELECT item_id, servings_per_person, price_at_version, display_order, notes
        FROM menu_items
        WHERE section_id = p_section_id
        ORDER BY display_order;

BEGIN
    -- Start transaction
    SAVEPOINT start_version_creation;

    -- Step 1: Get the project ID from the menu
    BEGIN
        SELECT project_id INTO v_project_id
        FROM menus
        WHERE menu_id = p_menu_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Menu not found: ' || p_menu_id);
    END;

    -- Step 2: Check if user is a collaborator for this project
    SELECT COUNT(*)
    INTO v_is_collaborator
    FROM project_collaborators
    WHERE project_id = v_project_id AND user_id = p_user_id;

    IF v_is_collaborator = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'User is not a collaborator for project ' || v_project_id);
    END IF;

    -- Step 3: Get the active version ID from the project
    BEGIN
        SELECT active_version_id INTO v_active_version_id
        FROM projects
        WHERE project_id = v_project_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20003, 'Project not found: ' || v_project_id);
    END;

    -- If no active version, get the latest version for this menu
    IF v_active_version_id IS NULL THEN
        BEGIN
            SELECT version_id
            INTO v_active_version_id
            FROM (SELECT version_id FROM menu_versions
                  WHERE menu_id = p_menu_id
                  ORDER BY version_number DESC)
            WHERE ROWNUM = 1;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20004, 'Menu has no versions to clone');
        END;
    END IF;

    -- Step 4: Calculate the next version number
    BEGIN
        SELECT NVL(MAX(version_number), 0) + 1
        INTO v_next_version_number
        FROM menu_versions
        WHERE menu_id = p_menu_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            v_next_version_number := 1;
    END;

    -- Step 5: Get template and with_prices from active version
    BEGIN
        SELECT template_id, with_prices
        INTO v_template_id, v_with_prices
        FROM menu_versions
        WHERE version_id = v_active_version_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20005, 'Active version not found: ' || v_active_version_id);
    END;

    -- Step 6: Create the new menu version
    INSERT INTO menu_versions (menu_id, template_id, version_number, with_prices, created_at)
    VALUES (p_menu_id, v_template_id, v_next_version_number, v_with_prices, SYSTIMESTAMP)
    RETURNING version_id INTO v_new_version_id;

    -- Step 7: Clone sections from the active version
    FOR section_rec IN sections_cur(v_active_version_id)
    LOOP
        INSERT INTO sections (version_id, name, display_order, created_at, updated_at)
        VALUES (v_new_version_id, section_rec.name, section_rec.display_order, SYSTIMESTAMP, SYSTIMESTAMP)
        RETURNING section_id INTO v_new_section_id;

        -- Clone menu items for this section
        FOR menu_item_rec IN menu_items_cur(section_rec.section_id)
        LOOP
            INSERT INTO menu_items (section_id, item_id, servings_per_person, price_at_version, display_order, notes, created_at, updated_at)
            VALUES (v_new_section_id, menu_item_rec.item_id, menu_item_rec.servings_per_person,
                    menu_item_rec.price_at_version, menu_item_rec.display_order, menu_item_rec.notes,
                    SYSTIMESTAMP, SYSTIMESTAMP);
        END LOOP;
    END LOOP;

    -- Step 8: Update the project's active_version_id
    UPDATE projects
    SET active_version_id = v_new_version_id, updated_at = SYSTIMESTAMP
    WHERE project_id = v_project_id;

    -- Commit changes
    COMMIT;

    -- Return the new version ID
    RETURN v_new_version_id;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK TO start_version_creation;
        RAISE_APPLICATION_ERROR(-20099, 'Error creating new menu version: ' || SQLERRM);
END CreateNewMenuVersion;
/

