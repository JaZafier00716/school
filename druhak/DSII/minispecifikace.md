# Minispecifikace funkce `CreateNewMenuVersionFc()`

Funkce vytvoří novou verzi menu na základě aktuálně aktivní verze.  
Součástí operace je kopírování sekcí a položek menu, aby vznikl nezávislý snapshot.

---

## Vstupní parametry
- `p_menu_id`: Identifikátor menu, pro které se má vytvořit nová verze.
- `p_user_id`: Identifikátor uživatele, který operaci provádí (pro audit a oprávnění).

## Výstupní hodnoty
- `v_new_version_id`: Identifikátor nově vytvořené verze menu.

---

## Použité tabulky
- `projects`
- `menus`
- `menu_versions`
- `sections`
- `menu_items`
- `project_collaborators`

---

## Popis transakce

Funkce je implementována jako atomická databázová transakce splňující ACID vlastnosti.  
Zajišťuje vytvoření nové verze menu jako snapshotu aktuální verze.

1. **Získání projektu a validace přístupu**  
   Načte se `project_id` z tabulky `menus` dle `p_menu_id`.  
   Následně je ověřeno, že uživatel (`p_user_id`) má přístup k projektu v tabulce `project_collaborators`.  
   V opačném případě je vyvolána chyba.

```sql
    SELECT project_id
    INTO v_project_id
    FROM menus
    WHERE menu_id = p_menu_id;

    IF NOT EXISTS (
        SELECT 1
        FROM project_collaborators
        WHERE project_id = v_project_id
          AND user_id = p_user_id
    ) THEN
        RAISE ERROR 'User has no access to this project';
    END IF;
```

2. **Získání aktuální verze menu**  
   Načte se `active_version_id` z tabulky `projects` dle `project_id`.  
   Je ověřeno, že aktivní verze existuje.

```sql
   SELECT active_version_id
    INTO v_active_version_id
    FROM projects
    WHERE project_id = v_project_id;

    IF v_active_version_id IS NULL THEN
        RAISE ERROR 'No active version set';
    END IF;

    -- ověření existence verze
    IF NOT EXISTS (
        SELECT 1
        FROM menu_versions
        WHERE version_id = v_active_version_id
    ) THEN
        RAISE ERROR 'Active version does not exist';
    END IF;
```

3. **Výpočet čísla nové verze**  
   Z tabulky `menu_versions` se zjistí `MAX(version_number) + 1`.

```sql
   SELECT MAX(version_number) + 1
    INTO v_new_version_number
    FROM menu_versions
    WHERE menu_id = p_menu_id;

    IF v_new_version_number IS NULL THEN
        v_new_version_number := 1;
    END IF;
```

4. **Vytvoření nové verze menu**  
   Vytvoří se nový záznam v tabulce `menu_versions` na základě aktivní verze.

```sql
    INSERT INTO menu_versions (
        menu_id,
        template_id,
        version_number,
        with_prices,
        created_at
    )
    SELECT
        mv.menu_id,
        mv.template_id,
        v_new_version_number,
        mv.with_prices,
        CURRENT_TIMESTAMP
    FROM menu_versions mv
    WHERE mv.version_id = v_active_version_id
    RETURNING version_id INTO v_new_version_id;
```

5. **Kopírování sekcí**  
   Pro každou sekci z aktuální verze se vytvoří nový záznam v tabulce `sections`.  
   Mezi starými a novými sekcemi je vytvořeno mapování: old_section_id -> new_section_id.

```sql
    FOR EACH section IN (
        SELECT section_id, name, display_order
        FROM sections
        WHERE version_id = v_active_version_id
    ) LOOP

        DECLARE v_new_section_id NUMBER;

        INSERT INTO sections (
            version_id,
            name,
            display_order,
            created_at
        )
        VALUES (
            v_new_version_id,
            section.name,
            section.display_order,
            CURRENT_TIMESTAMP
        )
        RETURNING section_id INTO v_new_section_id;

        -- mapování old → new
        v_section_map[section.section_id] := v_new_section_id;

    END LOOP;
```

6. **Kopírování položek menu**  
   Pro každou sekci se zkopírují položky z tabulky `menu_items`.  
   Při kopírování se využívá mapování sekcí, aby položky odkazovaly na nové sekce.

```sql
   FOR EACH item_rec IN (
        SELECT section_id, item_id, servings_per_person,
               price_at_version, display_order, notes
        FROM menu_items
        WHERE section_id IN (
            SELECT section_id
            FROM sections
            WHERE version_id = v_active_version_id
        )
    ) LOOP

        INSERT INTO menu_items (
            section_id,
            item_id,
            servings_per_person,
            price_at_version,
            display_order,
            notes,
            created_at
        )
        VALUES (
            v_section_map[item_rec.section_id],
            item_rec.item_id,
            item_rec.servings_per_person,
            item_rec.price_at_version,
            item_rec.display_order,
            item_rec.notes,
            CURRENT_TIMESTAMP
        );

    END LOOP;
```

7. **(Volitelné) Nastavení nové verze jako aktivní**  
   V závislosti na návrhu systému může být nová verze nastavena jako aktivní aktualizací `projects.active_version_id`.

```sql
    UPDATE projects
    SET active_version_id = v_new_version_id
    WHERE project_id = v_project_id;
```

8. **Commit / Rollback**  
   Pokud všechny kroky proběhnou úspěšně, transakce se commitne.  
   V případě chyby se provede rollback, aby nedošlo k nekonzistenci dat.

```sql
    COMMIT;

    RETURN v_new_version_id;

EXCEPTION

    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE ERROR 'Menu or project not found';

    WHEN OTHERS THEN
        ROLLBACK;
        RAISE ERROR 'Unexpected error';

END;
```
