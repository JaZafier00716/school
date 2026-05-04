# Setup & Verification Checklist

Before running the console application, verify all prerequisites are in place.

## ✅ Step 1: Prerequisites

- [ ] Java 21 installed
  ```bash
  java -version
  # Should show Java 21.x
  ```

- [ ] Maven 3.9+ installed
  ```bash
  mvn -version
  # Should show Maven 3.9.x or higher
  ```

- [ ] Oracle Database accessible
  ```bash
  sqlplus -version
  # Or try connecting directly
  ```

## ✅ Step 2: Build Verification

- [ ] Clone/access project directory
  ```bash
  cd /home/jan/Documents/School/druhak/JAVA/CateringApp
  ```

- [ ] Build console module successfully
  ```bash
  mvn -pl menu-system-console clean package -DskipTests
  # Should end with "BUILD SUCCESS"
  ```

- [ ] JAR file created
  ```bash
  ls -la menu-system-console/target/menu-system-console-1.0.0.jar
  # Should exist and be > 1MB (includes Oracle JDBC driver)
  ```

## ✅ Step 3: Database Prerequisites

- [ ] Column exists: `projects.active_version_id`
  ```sql
  SELECT COUNT(*) FROM all_tab_columns
  WHERE table_name = 'PROJECTS' 
  AND column_name = 'ACTIVE_VERSION_ID';
  -- Should return 1
  ```

- [ ] Foreign key exists: `fk_projects_active_version`
  ```sql
  SELECT constraint_name FROM all_constraints
  WHERE table_name = 'PROJECTS'
  AND constraint_name = 'FK_PROJECTS_ACTIVE_VERSION';
  -- Should return one row
  ```

- [ ] Sample data exists
  ```sql
  SELECT COUNT(*) FROM menus;
  SELECT COUNT(*) FROM projects;
  SELECT COUNT(*) FROM project_collaborators;
  SELECT COUNT(*) FROM users;
  -- All should return > 0
  ```

## ✅ Step 4: Stored Function Setup

### Option A: Using SQLPlus

- [ ] Connect to database
  ```bash
  sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle
  ```

- [ ] Check if function already exists
  ```sql
  SELECT COUNT(*) FROM user_objects
  WHERE object_type = 'FUNCTION'
  AND object_name = 'CREATENEWMENUVERSION';
  -- If 0: need to create function
  -- If 1: function already exists (skip creation)
  ```

- [ ] Create the stored function (if not exists)
  ```sql
  @/home/jan/Documents/School/druhak/JAVA/CateringApp/menu-system-console/src/main/resources/create-function.sql
  ```

- [ ] Verify function created
  ```sql
  SELECT object_name, object_type, status 
  FROM user_objects 
  WHERE object_name = 'CREATENEWMENUVERSION';
  -- Should show status = VALID
  ```

### Option B: Using SQL Developer

- [ ] Open Oracle SQL Developer
- [ ] Create connection to ZAM0074@bayer.cs.vsb.cz:1521:oracle
- [ ] Open file: `menu-system-console/src/main/resources/create-function.sql`
- [ ] Execute script (F5 or Run Script button)
- [ ] Check for errors in Output window

## ✅ Step 5: Configuration Verification

- [ ] Database properties file exists
  ```bash
  cat menu-system-console/src/main/resources/db.properties
  # Should show db.url, db.username, db.password
  ```

- [ ] Properties contain correct values
  ```bash
  grep db.url menu-system-console/src/main/resources/db.properties
  # Should show: db.url=jdbc:oracle:thin:@bayer.cs.vsb.cz:1521:oracle
  ```

## ✅ Step 6: Test Stored Function Directly

Before running Java app, verify the function works:

```sql
-- In SQLPlus/SQL Developer connected as ZAM0074

SET SERVEROUTPUT ON;

DECLARE
    v_new_version_id NUMBER;
BEGIN
    v_new_version_id := CreateNewMenuVersion(1, 1);
    DBMS_OUTPUT.PUT_LINE('Test passed! New Version ID: ' || v_new_version_id);
    
    -- Verify version was created
    SELECT version_id, menu_id, version_number 
    FROM menu_versions 
    WHERE version_id = v_new_version_id;
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
        ROLLBACK;
END;
/
```

Expected output:
```
Test passed! New Version ID: [some_number]
VERSION_ID MENU_ID VERSION_NUMBER
---------- ------- --------------
         5       1              3
```

## ✅ Step 7: Run the Console Application

- [ ] Navigate to console directory or project root

- [ ] Run with default parameters
  ```bash
  java -jar menu-system-console/target/menu-system-console-1.0.0.jar
  ```

- [ ] Verify output includes
  ```
  ✅ New Menu Version Created Successfully!
  Version ID:      [some_number]
  Menu ID:         1
  Version Number:  [some_number]
  ```

- [ ] Try with custom parameters
  ```bash
  java -jar menu-system-console/target/menu-system-console-1.0.0.jar 1 1
  ```

## ✅ Step 8: Troubleshooting Checklist

If build fails:
- [ ] `java -version` shows Java 21
- [ ] `mvn -version` shows Maven 3.9+
- [ ] No network proxy issues (download dependencies)
- [ ] Re-run: `mvn -pl menu-system-console clean package -DskipTests`

If runtime connection fails:
- [ ] Database is online: `ping bayer.cs.vsb.cz`
- [ ] Credentials are correct
- [ ] Firewall allows port 1521
- [ ] Network can reach Oracle port: `telnet bayer.cs.vsb.cz 1521`

If function not found:
- [ ] Function was created: `SELECT * FROM user_objects WHERE object_type='FUNCTION';`
- [ ] Function name matches: `CREATENEWMENUVERSION` (all caps in Oracle)
- [ ] Function is valid: `status` column should be 'VALID' not 'INVALID'

If user not collaborator error:
- [ ] Verify user and project exist:
  ```sql
  SELECT distinct user_id, project_id FROM project_collaborators;
  ```
- [ ] Add user as collaborator:
  ```sql
  INSERT INTO project_collaborators 
  (project_id, user_id, role_in_project, added_by)
  VALUES (1, 2, 'editor', 1);
  COMMIT;
  ```

If menu version already exists error:
- [ ] This is normal! Function creates new versions incrementally
- [ ] Check version count:
  ```sql
  SELECT menu_id, version_number FROM menu_versions ORDER BY menu_id, version_number;
  ```

## ✅ Step 9: Verify Data Integrity

After successful run, verify in database:

```sql
-- Check new version was created
SELECT COUNT(*) as version_count FROM menu_versions WHERE menu_id = 1;

-- Check sections were cloned
SELECT version_id, COUNT(*) as section_count 
FROM sections 
WHERE version_id IN (SELECT version_id FROM menu_versions WHERE menu_id = 1)
GROUP BY version_id
ORDER BY version_id DESC;

-- Check menu items were cloned
SELECT v.version_id, COUNT(mi.menu_item_id) as item_count
FROM menu_versions v
LEFT JOIN sections s ON v.version_id = s.version_id
LEFT JOIN menu_items mi ON s.section_id = mi.section_id
WHERE v.menu_id = 1
GROUP BY v.version_id
ORDER BY v.version_id DESC
FETCH FIRST 3 ROWS ONLY;

-- Check project active version was updated
SELECT project_id, active_version_id, name FROM projects;
```

## ✅ Step 10: Success Criteria

All of the following should be true:

- ✅ Maven build completes successfully
- ✅ JAR file generated and executable
- ✅ Stored function exists and is valid
- ✅ Database connection successful
- ✅ Function executes without errors
- ✅ New menu version created in database
- ✅ Sections cloned correctly
- ✅ Menu items cloned correctly
- ✅ Project active version updated
- ✅ Console application prints formatted output

## Quick Verification Script

Run all checks at once:

```bash
#!/bin/bash

echo "=== Menu System Console - Verification ==="
echo ""

echo "1. Checking Java version..."
java -version 2>&1 | grep "java version"

echo ""
echo "2. Checking Maven version..."
mvn -version | grep Apache

echo ""
echo "3. Building console module..."
cd /home/jan/Documents/School/druhak/JAVA/CateringApp
mvn -pl menu-system-console clean package -DskipTests -q

if [ $? -eq 0 ]; then
    echo "✅ Build successful"
else
    echo "❌ Build failed"
    exit 1
fi

echo ""
echo "4. Checking JAR exists..."
if [ -f "menu-system-console/target/menu-system-console-1.0.0.jar" ]; then
    echo "✅ JAR file present"
    ls -lh menu-system-console/target/menu-system-console-1.0.0.jar
else
    echo "❌ JAR file not found"
    exit 1
fi

echo ""
echo "=== Verification Complete ==="
echo ""
echo "Next steps:"
echo "1. Create stored function:   @menu-system-console/src/main/resources/create-function.sql"
echo "2. Run application:          java -jar menu-system-console/target/menu-system-console-1.0.0.jar"
```

Save as `verify.sh` and run:
```bash
chmod +x verify.sh
./verify.sh
```

---

## Support

If you encounter issues:

1. **Check documentation**: See `menu-system-console/README.md`
2. **Review logs**: Enable DEBUG in `simplelogger.properties`
3. **Test function directly**: Run SQL test shown in Step 6
4. **Verify prerequisites**: Ensure all items in this checklist are marked ✅

---

**Last Updated**: 2026-05-04  
**Version**: 1.0.0

