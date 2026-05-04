CREATE OR REPLACE FUNCTION CreateNewMenuVersion (
    p_menu_id IN NUMBER,
    p_user_id IN NUMBER
) RETURN NUMBER
    IS
    v_new_version_id      NUMBER;
    v_new_version_number  NUMBER;
    v_project_id          NUMBER;
    v_active_version_id   NUMBER;
    v_count               NUMBER;
    v_template_id         NUMBER;
    v_with_prices         NUMBER;
    v_has_mv_seq          NUMBER := 0;
    v_has_sections_seq    NUMBER := 0;
    v_has_mi_seq          NUMBER := 0;
    v_seq_val             NUMBER;
    v_max_id              NUMBER;
BEGIN

    -------------------------------------------------
    -- 1. ZÍSKÁNÍ PROJECT_ID
    -------------------------------------------------
    SELECT project_id
    INTO v_project_id
    FROM menus
    WHERE menu_id = p_menu_id;

    -------------------------------------------------
    -- 2. VALIDACE PŘÍSTUPU
    -------------------------------------------------
    SELECT COUNT(*)
    INTO v_count
    FROM project_collaborators
    WHERE project_id = v_project_id
      AND user_id = p_user_id;

    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'User has no access');
    END IF;

    -------------------------------------------------
    -- 3. AKTIVNÍ VERZE
    -------------------------------------------------
    SELECT active_version_id
    INTO v_active_version_id
    FROM projects
    WHERE project_id = v_project_id;

    IF v_active_version_id IS NULL THEN
        RAISE_APPLICATION_ERROR(-20002, 'No active version');
    END IF;

    -------------------------------------------------
    -- 4. NOVÉ ČÍSLO VERZE
    -------------------------------------------------
    SELECT NVL(MAX(version_number), 0) + 1
    INTO v_new_version_number
    FROM menu_versions
    WHERE menu_id = p_menu_id;

    -------------------------------------------------
    -- 5. VYTVOŘENÍ NOVÉ VERZE
    -------------------------------------------------
    -- Fetch template_id and with_prices from the active version
    SELECT template_id, with_prices
    INTO v_template_id, v_with_prices
    FROM menu_versions
    WHERE version_id = v_active_version_id;

    -- Detect if legacy sequences exist (some schemas use sequences instead of IDENTITY)
    SELECT COUNT(*) INTO v_has_mv_seq FROM user_sequences WHERE sequence_name = 'MENU_VERSIONS_SEQ';
    SELECT COUNT(*) INTO v_has_sections_seq FROM user_sequences WHERE sequence_name = 'SECTIONS_SEQ';
    SELECT COUNT(*) INTO v_has_mi_seq FROM user_sequences WHERE sequence_name = 'MENU_ITEMS_SEQ';

    -- Insert new menu_versions row and capture generated version_id
    IF v_has_mv_seq = 1 THEN
        -- Schema uses sequence; ensure sequence is ahead of current max id
        SELECT NVL(MAX(version_id),0) INTO v_max_id FROM menu_versions;
        v_seq_val := menu_versions_seq.NEXTVAL;
        WHILE v_seq_val <= v_max_id LOOP
            v_seq_val := menu_versions_seq.NEXTVAL;
        END LOOP;
        v_new_version_id := v_seq_val;

        INSERT INTO menu_versions (
            version_id,
            menu_id,
            template_id,
            version_number,
            with_prices,
            created_at
        ) VALUES (
            v_new_version_id,
            p_menu_id,
            v_template_id,
            v_new_version_number,
            v_with_prices,
            SYSTIMESTAMP
        );
    ELSE
        -- Schema uses IDENTITY or default generation
        INSERT INTO menu_versions (
            menu_id,
            template_id,
            version_number,
            with_prices,
            created_at
        ) VALUES (
            p_menu_id,
            v_template_id,
            v_new_version_number,
            v_with_prices,
            SYSTIMESTAMP
        ) RETURNING version_id INTO v_new_version_id;
    END IF;

    -------------------------------------------------
    -- 6. RESET MAPOVACÍ TABULKY
    -------------------------------------------------
    DELETE FROM tmp_section_map where 1=1;

    -------------------------------------------------
    -- 7. KOPÍROVÁNÍ SEKCÍ + MAPOVÁNÍ
    -------------------------------------------------
    FOR rec IN (
        SELECT section_id, name, display_order
        FROM sections
        WHERE version_id = v_active_version_id
        ) LOOP

            DECLARE
                v_new_section_id NUMBER;
            BEGIN
                IF v_has_sections_seq = 1 THEN
                    -- Schema uses sequence; ensure sequence is ahead of current max id
                    SELECT NVL(MAX(section_id),0) INTO v_max_id FROM sections;
                    v_seq_val := sections_seq.NEXTVAL;
                    WHILE v_seq_val <= v_max_id LOOP
                        v_seq_val := sections_seq.NEXTVAL;
                    END LOOP;
                    v_new_section_id := v_seq_val;
                    INSERT INTO sections (
                        section_id,
                        version_id,
                        name,
                        display_order,
                        created_at,
                        updated_at
                    ) VALUES (
                        v_new_section_id,
                        v_new_version_id,
                        rec.name,
                        rec.display_order,
                        SYSTIMESTAMP,
                        SYSTIMESTAMP
                    );
                ELSE
                    -- Schema uses IDENTITY; capture generated id
                    INSERT INTO sections (
                        version_id,
                        name,
                        display_order,
                        created_at,
                        updated_at
                    ) VALUES (
                        v_new_version_id,
                        rec.name,
                        rec.display_order,
                        SYSTIMESTAMP,
                        SYSTIMESTAMP
                    ) RETURNING section_id INTO v_new_section_id;
                END IF;

                INSERT INTO tmp_section_map (old_id, new_id)
                VALUES (rec.section_id, v_new_section_id);

            END;

        END LOOP;

    -------------------------------------------------
    -- 8. KOPÍROVÁNÍ POLOŽEK (SET-BASED)
    -------------------------------------------------
    -- Insert menu items. If legacy sequence exists, include menu_item_id generated by sequence
    IF v_has_mi_seq = 1 THEN
        -- Ensure menu_items_seq is ahead of current max
        SELECT NVL(MAX(menu_item_id),0) INTO v_max_id FROM menu_items;
        v_seq_val := menu_items_seq.NEXTVAL;
        WHILE v_seq_val <= v_max_id LOOP
            v_seq_val := menu_items_seq.NEXTVAL;
        END LOOP;

        INSERT INTO menu_items (
            menu_item_id,
            section_id,
            item_id,
            servings_per_person,
            price_at_version,
            display_order,
            notes,
            created_at,
            updated_at
        )
        SELECT
            menu_items_seq.NEXTVAL,
            m.new_id,
            mi.item_id,
            mi.servings_per_person,
            mi.price_at_version,
            mi.display_order,
            mi.notes,
            SYSTIMESTAMP,
            SYSTIMESTAMP
        FROM menu_items mi
                 JOIN tmp_section_map m
                      ON mi.section_id = m.old_id;
    ELSE
        INSERT INTO menu_items (
            section_id,
            item_id,
            servings_per_person,
            price_at_version,
            display_order,
            notes,
            created_at,
            updated_at
        )
        SELECT
            m.new_id,
            mi.item_id,
            mi.servings_per_person,
            mi.price_at_version,
            mi.display_order,
            mi.notes,
            SYSTIMESTAMP,
            SYSTIMESTAMP
        FROM menu_items mi
                 JOIN tmp_section_map m
                      ON mi.section_id = m.old_id;
    END IF;

    -------------------------------------------------
    -- 9. AKTIVACE NOVÉ VERZE
    -------------------------------------------------
    UPDATE projects
    SET active_version_id = v_new_version_id
    WHERE project_id = v_project_id;

    -------------------------------------------------
    -- 10. RETURN
    -------------------------------------------------
    RETURN v_new_version_id;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20010, 'Menu or project not found');

    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20099, SQLERRM);
END;
/