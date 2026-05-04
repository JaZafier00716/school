# Menu System Console Application

Pure backend console application for creating menu versions using Oracle JDBC and stored procedures.

## Overview

This console application demonstrates:
- Plain JDBC database connection (no ORM)
- Calling Oracle stored functions
- Pure data access layer (DAO) pattern
- No frontend, no Spring, no Hibernate
- Clean layered architecture

## Architecture

```
app/
├── MainApp.java                 # Application entry point
├── db/
│   └── DatabaseConfig.java      # Database connection management
├── dao/
│   ├── MenuVersionDaoInterface.java
│   └── MenuVersionDao.java      # Data access implementation
└── dto/
    ├── MenuVersionDto.java
    └── ProjectDto.java
```

## Technology Stack

- Java 21
- Oracle JDBC Driver
- SLF4J Logging
- Maven 3.9+

## Prerequisites

- JDK 21
- Maven 3.9+
- Oracle Database (with connection: bayer.cs.vsb.cz:1521:oracle)
- Oracle user credentials (ZAM0074:osVSOwCvA6yO96Ao)

## Setup

### 1. Create the Stored Function

Before running the console application, you must create the `CreateNewMenuVersion` Oracle function.

Execute the SQL script in your Oracle database:

```bash
sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle

-- Then execute the script
@menu-system-console/src/main/resources/create-function.sql
```

Or use SQLDeveloper or your preferred Oracle client to execute the script.

### 2. Update Configuration (Optional)

Edit database connection parameters in:
```
menu-system-console/src/main/resources/db.properties
```

Default values:
- URL: `jdbc:oracle:thin:@bayer.cs.vsb.cz:1521:oracle`
- Username: `ZAM0074`
- Password: `osVSOwCvA6yO96Ao`

### 3. Build the Application

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp

# Build only the console module
mvn -pl menu-system-console clean package

# Or build the entire parent (including console)
mvn clean install -DskipTests
```

## Running the Application

### Default Parameters (Menu ID: 1, User ID: 1)

```bash
cd menu-system-console
java -jar target/menu-system-console-1.0.0.jar
```

### With Custom Menu and User IDs

```bash
java -jar menu-system-console-1.0.0.jar <menuId> <userId>

# Example: Create version for Menu 1 by User 2
java -jar menu-system-console-1.0.0.jar 1 2
```

## Expected Output

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

## Application Flow

1. **Initialize DAO**: Creates MenuVersionDao instance
2. **Call Stored Function**: Invokes `CreateNewMenuVersion(menuId, userId)`
   - Validates user is project collaborator
   - Gets active menu version
   - Calculates next version number
   - Creates new version in database
   - Clones sections and menu items
   - Updates project's active version
3. **Verify Result**: Queries the newly created version
4. **Print Results**: Displays version details in formatted output

## Stored Function: CreateNewMenuVersion

Location: `src/main/resources/create-function.sql`

### Function Signature
```sql
FUNCTION CreateNewMenuVersion(
    p_menu_id IN NUMBER,
    p_user_id IN NUMBER
) RETURN NUMBER
```

### Parameters
- `p_menu_id`: ID of the menu to create version for
- `p_user_id`: ID of the user performing the action

### Returns
- `version_id` of the newly created menu version

### Logic
1. Validates menu exists
2. Checks user is a collaborator for the project
3. Resolves the active version
4. Calculates next version number
5. Creates new version with same template and price settings
6. Clones all sections from active version
7. Clones all menu items for each section
8. Updates project's active version reference
9. Returns new version ID

## Error Handling

All exceptions are caught and meaningful error messages are printed:

```
❌ ERROR: Menu not found: 999
❌ ERROR: User is not a collaborator for project 1
❌ ERROR: Menu has no versions to clone
```

## Logging

Application uses SLF4J with Simple binding. Logs are printed to console in format:

```
[INFO] com.example.cateringapp.MainApp - Creating new menu version...
[DEBUG] com.example.cateringapp.dao.MenuVersionDao - Fetching menu version with ID: 5
```

Configure logging level in `src/main/resources/simplelogger.properties` (optional).

## Database Schema Requirements

Ensure these tables exist:
- `menu_versions`
- `sections`
- `menu_items`
- `projects`
- `project_collaborators`
- `menus`

See parent project's `schema-oracle.sql` for full schema definition.

## Development

### Project Structure

```
menu-system-console/
├── pom.xml                      # Maven build configuration
├── README.md                    # This file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── module-info.java
│   │   │   └── com/example/cateringapp/
│   │   │       ├── MainApp.java             # Entry point
│   │   │       ├── app/
│   │   │       ├── db/
│   │   │       │   └── DatabaseConfig.java
│   │   │       ├── dao/
│   │   │       │   ├── MenuVersionDaoInterface.java
│   │   │       │   └── MenuVersionDao.java
│   │   │       └── dto/
│   │   │           ├── MenuVersionDto.java
│   │   │           └── ProjectDto.java
│   │   └── resources/
│   │       ├── db.properties
│   │       └── create-function.sql
│   └── test/
│       └── java/  # (tests can be added here)
└── target/        # Build output
```

### Clean Code Principles

- **No ORM**: Pure JDBC only
- **No Spring**: Standalone application
- **No Annotations**: Minimal dependencies
- **Clear Separation**: DAO, DTO, DB layers
- **Interface-based**: MenuVersionDaoInterface for extensibility
- **Proper Resource Management**: Try-with-resources and manual cleanup

### Adding New DAO Methods

1. Add method to `MenuVersionDaoInterface`
2. Implement in `MenuVersionDao`
3. Use `DatabaseConfig.getConnection()` for connection
4. Handle `SQLException` and log appropriately
5. Close resources in finally block

Example:
```java
@Override
public ProjectDto getProject(long projectId) throws Exception {
    // Implementation using plain JDBC
}
```

## Troubleshooting

### Issue: "db.properties file not found"
**Solution**: Ensure `db.properties` is in `src/main/resources/` and Maven properly includes resources.

Run: `mvn clean package` to rebuild.

### Issue: "Cannot create a new instance of type 'com.oracle.database.jdbc.OracleDriver'"
**Solution**: Ensure Oracle JDBC driver is in classpath. Rebuild with `mvn clean package`.

### Issue: "ORA-01722: invalid number"
**Solution**: Check that menu ID and user ID are valid. List existing values:
```sql
SELECT menu_id FROM menus;
SELECT user_id FROM users;
```

### Issue: "User is not a collaborator for project X"
**Solution**: Ensure the user is added as a project collaborator:
```sql
INSERT INTO project_collaborators (project_id, user_id, role_in_project, added_by)
VALUES (1, 2, 'editor', 1);
```

## Testing the Stored Function Directly

To test without the console app:

```sql
DECLARE
    v_new_version_id NUMBER;
BEGIN
    v_new_version_id := CreateNewMenuVersion(1, 1);
    DBMS_OUTPUT.PUT_LINE('New Version ID: ' || v_new_version_id);
    COMMIT;
END;
/
```

## Future Enhancements

- Add more DAO methods (ItemDao, SectionDao, ProjectDao)
- Connection pooling with HikariCP
- Batch operations optimization
- Unit tests with embedded database
- Flyway/Liquibase migration support
- Configuration from environment variables
- CLI argument parser for more options

## License

Same as parent project.

