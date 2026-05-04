# Menu System Console - Execution & Deployment Guide

## Quick Start (Choose One Method)

### Method 1: Fat JAR (Recommended - Simplest!)

The fat JAR includes all dependencies in a single file:

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp/menu-system-console

# Run with default parameters
java -jar target/menu-system-console-all-jar-with-dependencies.jar

# Run with custom parameters
java -jar target/menu-system-console-all-jar-with-dependencies.jar 1 1
```

**Advantages:**
- ✅ Single executable file (~7 MB)
- ✅ No classpath configuration needed
- ✅ Easy to deploy
- ✅ No dependency management

**File:** `menu-system-console-all-jar-with-dependencies.jar` (~7 MB)

---

### Method 2: Thin JAR + Dependencies Directory

Run the thin JAR with dependencies from `lib/` directory:

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp/menu-system-console/target

# Run with default parameters
java -cp "menu-system-console-1.0.0.jar:lib/*" com.example.cateringapp.MainApp

# Run with custom parameters (Menu ID: 1, User ID: 2)
java -cp "menu-system-console-1.0.0.jar:lib/*" com.example.cateringapp.MainApp 1 2
```

**Advantages:**
- ✅ Smaller main JAR (15 KB)
- ✅ Dependencies in separate files
- ✅ Easy to update individual libraries
- ✅ Better for modular deployments

**Files:**
- `menu-system-console-1.0.0.jar` (15 KB)
- `lib/ojdbc11-23.4.0.24.05.jar` (6.9 MB)
- `lib/slf4j-api-2.0.11.jar` (67 KB)
- `lib/slf4j-simple-2.0.11.jar` (16 KB)

---

### Method 3: Maven Exec Plugin

Run directly from Maven (no manual classpath):

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp

# Run console module with default parameters
mvn -pl menu-system-console exec:java -Dexec.mainClass="com.example.cateringapp.MainApp"

# Run with command-line arguments
mvn -pl menu-system-console exec:java -Dexec.mainClass="com.example.cateringapp.MainApp" \
    -Dexec.args="1 1"
```

**Advantages:**
- ✅ No compilation needed
- ✅ Maven manages classpath automatically
- ✅ Good for development
- ✅ Can set JVM args easily

**Note:** Requires Maven installed and `maven-exec-plugin`

---

## Build Artifacts Generated

After `mvn package`:

```
menu-system-console/target/
├── menu-system-console-1.0.0.jar                    # Thin JAR (15 KB)
├── menu-system-console-all-jar-with-dependencies.jar # Fat JAR (7 MB) ✅ EASIEST
├── lib/
│   ├── ojdbc11-23.4.0.24.05.jar                    # Oracle JDBC driver (6.9 MB)
│   ├── slf4j-api-2.0.11.jar                        # SLF4J API
│   └── slf4j-simple-2.0.11.jar                     # SLF4J Simple binding
├── classes/                                        # Compiled classes
└── ...
```

---

## Running the Application

### Prerequisites

Before running, ensure:

1. **Stored function exists** in Oracle
   ```sql
   SELECT COUNT(*) FROM user_objects
   WHERE object_type = 'FUNCTION' 
   AND object_name = 'CREATENEWMENUVERSION';
   ```
   If returns 0, execute:
   ```bash
   sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle
   @menu-system-console/src/main/resources/create-function.sql
   ```

2. **Database connectivity**
   - Oracle database accessible at `bayer.cs.vsb.cz:1521:oracle`
   - User `ZAM0074` has appropriate privileges

3. **Sample data exists** (if using default IDs)
   ```sql
   SELECT * FROM projects WHERE project_id = 1;
   SELECT * FROM menus WHERE menu_id = 1;
   SELECT * FROM project_collaborators 
   WHERE project_id = 1 AND user_id = 1;
   ```

### Execution Steps

#### Step 1: Build the Application

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp
mvn -pl menu-system-console clean package -DskipTests
```

Expected output: `BUILD SUCCESS`

#### Step 2: Create Stored Function (if not exists)

```bash
# Option A: Using SQLPlus
sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle
@menu-system-console/src/main/resources/create-function.sql
EXIT;

# Option B: Using @@ syntax
sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle < \
  menu-system-console/src/main/resources/create-function.sql
```

#### Step 3: Run the Application

Choose one of the three methods above.

#### Step 4: Verify Output

Expected output:

```
[INFO] === Menu System Console Application ===
[INFO] Starting application...
[INFO] Creating new menu version...
[INFO] Successfully obtained database connection
[INFO] Calling CreateNewMenuVersion stored function with menuId=1, userId=1
[INFO] Successfully created new menu version with ID: X
[INFO] Verifying created version...
[INFO] Fetching menu version with ID: X
[INFO] Successfully retrieved menu version: MenuVersionDto{...}

==================================================
✅ New Menu Version Created Successfully!
==================================================
Version ID:      X
Menu ID:         1
Version Number:  Y
With Prices:     Yes
Template ID:     1
==================================================
Details: MenuVersionDto{versionId=X, menuId=1, versionNumber=Y, withPrices=true, templateId=1}
==================================================

[INFO] === Application completed successfully ===
```

---

## Command Line Arguments

The application accepts 0-2 arguments:

```
java -jar menu-system-console-all-jar-with-dependencies.jar [menuId] [userId]
```

### Parameters

| Position | Name | Default | Example | Description |
|----------|------|---------|---------|-------------|
| 1 | menuId | 1 | `1` | Menu ID to create version for |
| 2 | userId | 1 | `2` | User ID performing the action |

### Examples

```bash
# Default: Menu 1, User 1
java -jar target/menu-system-console-all-jar-with-dependencies.jar

# Custom: Menu 2, User 3
java -jar target/menu-system-console-all-jar-with-dependencies.jar 2 3

# Valid only if menu 1 exists, user 5 is collaborator
java -jar target/menu-system-console-all-jar-with-dependencies.jar 1 5
```

---

## Troubleshooting Execution

### Issue: "Cannot find method main(String[])"
**Cause:** Thin JAR used without dependencies on classpath  
**Solution:** Use fat JAR or set classpath: `java -cp "jar:lib/*" com.example.cateringapp.MainApp`

### Issue: "ORA-12514: TNS:listener does not currently know of service requested"
**Cause:** Oracle database connection failed  
**Solution:** 
- Check database is online: `ping bayer.cs.vsb.cz`
- Test port: `telnet bayer.cs.vsb.cz 1521`
- Verify credentials in `src/main/resources/db.properties`

### Issue: "ORA-04043: object CREATENEWMENUVERSION does not exist"
**Cause:** Stored function not created  
**Solution:** Execute SQL script to create function:
```bash
sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle
@menu-system-console/src/main/resources/create-function.sql
EXIT;
```

### Issue: "User is not a collaborator for project 1" error
**Cause:** User not added as project collaborator  
**Solution:** Add user to project:
```sql
INSERT INTO project_collaborators 
(project_id, user_id, role_in_project, added_by)
VALUES (1, 2, 'editor', 1);
COMMIT;
```

### Issue: "java.lang.ClassNotFoundException: com.oracle.database.jdbc.OracleDriver"
**Cause:** Oracle JDBC driver not on classpath  
**Solution:**
- Use fat JAR: `java -jar target/menu-system-console-all-jar-with-dependencies.jar`
- Or include dependency: `java -cp "jar:lib/ojdbc11-*.jar" ...`

---

## Production Deployment

### Option A: Single Fat JAR (Recommended)

```bash
#!/bin/bash
# Deploy script

JAR="menu-system-console-all-jar-with-dependencies.jar"
MENU_ID=${1:-1}
USER_ID=${2:-1}

java -jar "$JAR" "$MENU_ID" "$USER_ID"
```

Save as `run.sh`:
```bash
chmod +x run.sh
./run.sh 1 1
```

### Option B: Thin JAR + Lib Directory

```
deployment/
├── menu-system-console-1.0.0.jar
├── lib/                          (all JARs)
├── db.properties                 (configuration)
├── run.sh                        (execution script)
└── README.txt
```

Script (`run.sh`):
```bash
#!/bin/bash
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JAR="$SCRIPT_DIR/menu-system-console-1.0.0.jar"
LIB="$SCRIPT_DIR/lib/*"
MENU_ID=${1:-1}
USER_ID=${2:-1}

java -cp "$JAR:$LIB" com.example.cateringapp.MainApp "$MENU_ID" "$USER_ID"
```

### Option C: Docker Deployment

```dockerfile
FROM openjdk:21-slim

WORKDIR /app

# Copy fat JAR
COPY menu-system-console-all-jar-with-dependencies.jar /app/

# Copy properties file (update with production credentials)
COPY db.properties /app/

CMD ["java", "-jar", "menu-system-console-all-jar-with-dependencies.jar"]
```

Build and run:
```bash
docker build -t menu-system-console .
docker run menu-system-console 1 1
```

---

## Performance Considerations

### Execution Time Breakdown

| Phase | Time | Notes |
|-------|------|-------|
| JVM startup | 500ms-1s | One-time cost |
| Database connection | 100-500ms | Network latency |
| Function execution | 500-2000ms | Data volume dependent |
| Verification query | 50-200ms | Single row select |
| **Total** | **1-3.5s** | End to end |

### Memory Usage

```
JVM Base:         ~100-200 MB
JDBC Driver:      ~20 MB
Application:      ~5-10 MB
Total:            ~150-250 MB
```

### Optimization Tips

1. **Database Connection:**
   - Runs on same network: 200-300ms
   - Potential round-trip time

2. **Function Execution:**
   - Clones sections: O(n) where n = sections count
   - Clones items: O(m) where m = items per section

3. **Network:**
   - Use connection pooling for repeated calls
   - Consider batch operations for multiple versions

---

## Batch Execution

Run multiple versions programmatically:

```bash
#!/bin/bash
# Batch script to create versions for multiple menus

FAT_JAR="menu-system-console-all-jar-with-dependencies.jar"

for MENU_ID in 1 2 3; do
    echo "Creating version for menu $MENU_ID..."
    java -jar "$FAT_JAR" "$MENU_ID" 1
    if [ $? -eq 0 ]; then
        echo "✅ Success for menu $MENU_ID"
    else
        echo "❌ Failed for menu $MENU_ID"
    fi
done
```

---

## Monitoring & Logging

### Enable Debug Logging

Edit `src/main/resources/simplelogger.properties`:

```properties
org.slf4j.simpleLogger.defaultLogLevel=DEBUG

# This will show all SQL execution and timing
org.slf4j.simpleLogger.log.com.example.cateringapp.dao.MenuVersionDao=DEBUG
org.slf4j.simpleLogger.log.com.example.cateringapp.db.DatabaseConfig=DEBUG
```

Then rebuild and run.

### Log Capture (redirect to file)

```bash
java -jar menu-system-console-all-jar-with-dependencies.jar > app.log 2>&1
tail -f app.log
```

### Check Execution Status

```bash
java -jar menu-system-console-all-jar-with-dependencies.jar 1 1
if [ $? -eq 0 ]; then
    echo "✅ Execution successful"
else
    echo "❌ Execution failed with code: $?"
fi
```

---

## Post-Execution Verification

After running, verify in database:

```sql
-- Count new versions created
SELECT menu_id, COUNT(*) as version_count 
FROM menu_versions 
WHERE menu_id = 1
GROUP BY menu_id;

-- Check latest version
SELECT version_id, menu_id, version_number, with_prices
FROM menu_versions
WHERE menu_id = 1
ORDER BY version_number DESC
FETCH FIRST 1 ROW ONLY;

-- Verify sections cloned
SELECT v.version_id, COUNT(s.section_id) as section_count
FROM menu_versions v
JOIN sections s ON v.version_id = s.version_id
WHERE v.menu_id = 1
GROUP BY v.version_id
ORDER BY v.version_id DESC;

-- Verify project active version updated
SELECT project_id, active_version_id, name
FROM projects
WHERE project_id IN (SELECT project_id FROM menus WHERE menu_id = 1);
```

---

## Summary

| Method | Ease | File | Command |
|--------|------|------|---------|
| **Fat JAR** | ⭐⭐⭐⭐⭐ | 7 MB | `java -jar jar-with-deps.jar` |
| **Thin JAR + Lib** | ⭐⭐⭐ | 15KB + 7MB | `java -cp "jar:lib/*" Main` |
| **Maven Exec** | ⭐⭐⭐⭐ | N/A | `mvn exec:java -Dexec.mainClass=...` |

**Recommended:** Use the fat JAR (`menu-system-console-all-jar-with-dependencies.jar`) for simplest deployment.

