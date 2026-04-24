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

2. **Získání aktuální verze menu**  
   Načte se `active_version_id` z tabulky `projects` dle `project_id`.  
   Je ověřeno, že aktivní verze existuje.

3. **Výpočet čísla nové verze**  
   Z tabulky `menu_versions` se zjistí `MAX(version_number) + 1`.

4. **Vytvoření nové verze menu**  
   Vytvoří se nový záznam v tabulce `menu_versions` na základě aktivní verze.

5. **Kopírování sekcí**  
   Pro každou sekci z aktuální verze se vytvoří nový záznam v tabulce `sections`.  
   Mezi starými a novými sekcemi je vytvořeno mapování: old_section_id -> new_section_id.

6. **Kopírování položek menu**  
   Pro každou sekci se zkopírují položky z tabulky `menu_items`.  
   Při kopírování se využívá mapování sekcí, aby položky odkazovaly na nové sekce.

7. **(Volitelné) Nastavení nové verze jako aktivní**  
   V závislosti na návrhu systému může být nová verze nastavena jako aktivní aktualizací `projects.active_version_id`.

8. **Commit / Rollback**  
   Pokud všechny kroky proběhnou úspěšně, transakce se commitne.  
   V případě chyby se provede rollback, aby nedošlo k nekonzistenci dat.

---

## Pseudokód
```sql
PROCEDURE CreateNewMenuVersion(
 p_menu_id IN NUMBER,
 p_user_id IN NUMBER
) IS

 v_new_version_id NUMBER;
 v_new_version_number NUMBER;
 v_project_id NUMBER;
 v_active_version_id NUMBER;

 TYPE section_map_type IS TABLE OF NUMBER INDEX BY NUMBER;
 v_section_map section_map_type;

BEGIN

-- 1. validace přístupu
SELECT m.project_id
INTO v_project_id
FROM menus m
WHERE m.menu_id = p_menu_id;

IF NOT EXISTS (
 SELECT 1
 FROM project_collaborators pc
 WHERE pc.project_id = v_project_id
   AND pc.user_id = p_user_id
) THEN
 RAISE_APPLICATION_ERROR(-20001, 'User has no access to this project');
END IF;

-- 2. zjištění aktivní verze
SELECT active_version_id
INTO v_active_version_id
FROM projects
WHERE project_id = v_project_id;

-- 2b. validace existence aktivní verze
IF v_active_version_id IS NULL THEN
    RAISE_APPLICATION_ERROR(-20004, 'No active version set for this project');
END IF;

-- ověření, že verze existuje
DECLARE
    v_dummy NUMBER;
BEGIN
    SELECT 1
    INTO v_dummy
    FROM menu_versions
    WHERE version_id = v_active_version_id;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20005, 'Active version does not exist');
END;

-- 3. výpočet čísla nové verze
SELECT NVL(MAX(version_number), 0) + 1
INTO v_new_version_number
FROM menu_versions
WHERE menu_id = p_menu_id;

-- 4. vytvoření nové verze
INSERT INTO menu_versions (
 version_id,
 menu_id,
 template_id,
 version_number,
 with_prices,
 created_at
)
SELECT
 menu_versions_seq.NEXTVAL,
 mv.menu_id,
 mv.template_id,
 v_new_version_number,
 mv.with_prices,
 SYSDATE
FROM menu_versions mv
WHERE mv.version_id = v_active_version_id
RETURNING version_id INTO v_new_version_id;

-- 5. kopírování sekcí
FOR rec IN (
 SELECT section_id, name, display_order
 FROM sections
 WHERE version_id = v_active_version_id
) LOOP

 DECLARE
     v_new_section_id NUMBER;
 BEGIN
     INSERT INTO sections (
         section_id,
         version_id,
         name,
         display_order,
         created_at
     )
     VALUES (
         sections_seq.NEXTVAL,
         v_new_version_id,
         rec.name,
         rec.display_order,
         SYSDATE
     )
     RETURNING section_id INTO v_new_section_id;

     v_section_map(rec.section_id) := v_new_section_id;
 END;

END LOOP;

-- 6. kopírování položek
FOR rec IN (
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
     menu_item_id,
     section_id,
     item_id,
     servings_per_person,
     price_at_version,
     display_order,
     notes,
     created_at
 )
 VALUES (
     menu_items_seq.NEXTVAL,
     v_section_map(rec.section_id),
     rec.item_id,
     rec.servings_per_person,
     rec.price_at_version,
     rec.display_order,
     rec.notes,
     SYSDATE
 );

END LOOP;

COMMIT;

EXCEPTION

WHEN NO_DATA_FOUND THEN
 ROLLBACK;
 RAISE_APPLICATION_ERROR(-20002, 'Menu or project not found');

WHEN TOO_MANY_ROWS THEN
 ROLLBACK;
 RAISE_APPLICATION_ERROR(-20003, 'Unexpected multiple rows');

WHEN OTHERS THEN
 ROLLBACK;
 RAISE_APPLICATION_ERROR(-20099, 'Unexpected error: ' || SQLERRM);

END;