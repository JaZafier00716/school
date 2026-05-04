# Menu System Console App - Quick Start Guide

## 🚀 Get Started in 5 Minutes

### 1. Build the Console Application

From the project root:

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp

# Build console module only
mvn -pl menu-system-console clean package -DskipTests
```

This generates: `menu-system-console/target/menu-system-console-1.0.0.jar`

### 2. Create the Stored Function

Execute SQL script in Oracle database:

```bash
# Option A: Using SQLPlus command line
sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle

# Inside sqlplus, execute:
@menu-system-console/src/main/resources/create-function.sql
EXIT;

# Option B: Using SQLDeveloper
# 1. Open Oracle SQLDeveloper
# 2. Open connection to ZAM0074@bayer.cs.vsb.cz:1521:oracle
# 3. Open file menu-system-console/src/main/resources/create-function.sql
# 4. Click Run Script (F5)
```

### 3. Run the Application

```bash
# Navigate to console module
cd menu-system-console

# Run with default parameters (Menu ID: 1, User ID: 1)
java -jar target/menu-system-console-1.0.0.jar

# Run with custom parameters
java -jar target/menu-system-console-1.0.0.jar <menuId> <userId>

# Example: Create version for menu 1, by user 1
java -jar target/menu-system-console-1.0.0.jar 1 1
```

### Expected Output

```
=================================================
✅ New Menu Version Created Successfully!
=================================================
Version ID:      5
Menu ID:         1
Version Number:  3
With Prices:     Yes
Template ID:     1
=================================================
Details: MenuVersionDto{versionId=5, menuId=1, versionNumber=3, withPrices=true, templateId=1}
=================================================
```

## 📋 What the Application Does

1. **Connects to Oracle**: Uses JDBC with credentials from `db.properties`
2. **Calls Stored Function**: Invokes `CreateNewMenuVersion(menuId, userId)`
   - Validates user is project collaborator
   - Clones the active menu version
   - Creates new version with incremented version number
   - Clones all sections and menu items
   - Updates project's active version
3. **Verifies Result**: Queries the new version from database
4. **Prints Output**: Displays results in formatted table

## 🔧 Configuration

Edit `menu-system-console/src/main/resources/db.properties`:

```properties
db.url=jdbc:oracle:thin:@bayer.cs.vsb.cz:1521:oracle
db.username=ZAM0074
db.password=osVSOwCvA6yO96Ao
```

## 📚 Project Structure

```
menu-system-console/
├── pom.xml                              # Maven configuration
├── README.md                            # Full documentation
├── src/main/java/com/example/cateringapp/
│   ├── MainApp.java                    # Entry point
│   ├── db/DatabaseConfig.java          # Connection management
│   ├── dao/MenuVersionDao.java         # Database access
│   └── dto/MenuVersionDto.java         # Data transfer object
└── src/main/resources/
    ├── db.properties                   # Database config
    ├── create-function.sql             # Stored function definition
    └── simplelogger.properties         # Logging config
```

## 🆘 Troubleshooting

| Issue | Solution |
|-------|----------|
| `ORA-04043: object ... does not exist` | Create the stored function using: `@create-function.sql` |
| `db.properties file not found` | Ensure file is in `src/main/resources/` and rebuild with `mvn clean package` |
| `User is not a collaborator` | Add user as project collaborator or use valid user/project combination |
| `Menu not found` | Ensure menu exists: `SELECT menu_id FROM menus;` |
| `Cannot connect to database` | Check database credentials in `db.properties` and database availability |

## ✨ Key Features

✅ Pure JDBC implementation (no ORM)  
✅ No Spring Framework  
✅ No Hibernate, JPA, or any ORM  
✅ Clean DAO/DTO architecture  
✅ Oracle stored function integration  
✅ Proper error handling and logging  
✅ Automatic resource cleanup  
✅ Command-line arguments support  

## 📖 For More Information

See full documentation in: `menu-system-console/README.md`

## 🧪 Testing with SQL

Test the stored function directly without the Java app:

```sql
-- Connect as ZAM0074
SET SERVEROUTPUT ON;

DECLARE
    v_new_version_id NUMBER;
BEGIN
    v_new_version_id := CreateNewMenuVersion(1, 1);
    DBMS_OUTPUT.PUT_LINE('New Version ID: ' || v_new_version_id);
    COMMIT;
END;
/

-- Verify the created version
SELECT version_id, menu_id, version_number
FROM menu_versions
WHERE menu_id = 1
ORDER BY version_number DESC
FETCH FIRST 1 ROW ONLY;
```

## 📝 Activity Log

The application logs all operations to console:

```
2026-05-04 14:32:15 === Menu System Console Application ===
2026-05-04 14:32:15 Starting application...
2026-05-04 14:32:16 Creating new menu version...
2026-05-04 14:32:16 Successfully obtained database connection
2026-05-04 14:32:16 Calling CreateNewMenuVersion stored function with menuId=1, userId=1
2026-05-04 14:32:16 Successfully created new menu version with ID: 5
2026-05-04 14:32:16 Verifying created version...
2026-05-04 14:32:16 Fetching menu version with ID: 5
2026-05-04 14:32:16 Successfully retrieved menu version: MenuVersionDto{...}
2026-05-04 14:32:16 === Application completed successfully ===
```

Control verbosity with `simplelogger.properties`.

---

**What's Next?**

- Extend DAO layer with more operations (ProjectDao, ItemDao, SectionDao)
- Add integration tests
- Implement connection pooling (HikariCP)
- Add batch operations support

