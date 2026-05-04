# Menu System Transformation Summary

## Overview

The Menu System project has been successfully extended with a **pure backend console application** that:

✅ Connects to Oracle database using plain JDBC  
✅ Calls stored function `CreateNewMenuVersion`  
✅ Verifies results by querying database  
✅ Prints formatted output to console  
✅ Contains NO ORM (Hibernate, JPA, Spring Data)  
✅ Contains NO frontend (web, JavaFX, etc.)  
✅ Contains NO Spring Framework  

## What Was Added

### New Module: `menu-system-console`

A standalone Maven module with clean layered architecture:

```
menu-system-console/
├── pom.xml                                    # Maven configuration
├── README.md                                  # Full documentation
├── src/main/
│   ├── java/
│   │   ├── module-info.java                  # Java module definition
│   │   └── com/example/cateringapp/
│   │       ├── MainApp.java                  # Application entry point
│   │       ├── db/
│   │       │   └── DatabaseConfig.java      # Database connection mgmt
│   │       ├── dao/
│   │       │   ├── MenuVersionDaoInterface.java  # Interface
│   │       │   └── MenuVersionDao.java           # Implementation
│   │       └── dto/
│   │           ├── MenuVersionDto.java      # Data transfer object
│   │           └── ProjectDto.java          # Data transfer object
│   └── resources/
│       ├── db.properties                    # Database config
│       ├── create-function.sql              # Stored function SQL
│       └── simplelogger.properties          # Logging config
└── target/
    └── menu-system-console-1.0.0.jar        # Compiled JAR
```

## Architecture

### Layered Design

```
┌─────────────────────────────────────────────┐
│  Application Layer (MainApp)                │
│  - Orchestrates execution flow              │
│  - Handles CLI arguments                    │
│  - Formats and prints output                │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│  DAO Layer (MenuVersionDao)                 │
│  - Implements MenuVersionDaoInterface       │
│  - Executes stored function calls           │
│  - Maps JDBC ResultSets to DTOs             │
│  - Manages database resources               │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│  Database Layer (DatabaseConfig)            │
│  - Manages JDBC connections                 │
│  - Loads configuration from properties      │
│  - Provides connection pooling ready API    │
└────────────────────┬────────────────────────┘
                     │
                 ┌───▼───┐
                 │ Oracle│
                 │  JDBC │
                 └───────┘
```

### DTO Layer

- `MenuVersionDto`: Transfers menu version data (ID, version number, template, etc.)
- `ProjectDto`: Transfers project data (ID, name, status, active version)

### DAO Interface

```java
interface MenuVersionDaoInterface {
    long createNewMenuVersion(long menuId, long userId);
    MenuVersionDto getMenuVersion(long versionId);
}
```

## Implementation Details

### Database Connection (`DatabaseConfig.java`)

- Loads configuration from `db.properties`
- Provides static `getConnection()` method
- Proper resource management
- Logging with SLF4J

```java
public static Connection getConnection() throws SQLException
```

### Stored Function Execution (`MenuVersionDao.java`)

Uses `CallableStatement` for stored function invocation:

```java
CallableStatement stmt = conn.prepareCall("{ ? = call CreateNewMenuVersion(?, ?) }");
stmt.registerOutParameter(1, Types.BIGINT);
stmt.setLong(2, menuId);
stmt.setLong(3, userId);
stmt.execute();
long newVersionId = stmt.getLong(1);
```

### Oracle Stored Function (`create-function.sql`)

The `CreateNewMenuVersion` function:

1. Validates menu exists
2. Checks user is project collaborator
3. Resolves active menu version
4. Calculates next version number
5. Creates new version entry
6. Clones sections from active version
7. Clones menu items for each section
8. Updates project's active version reference
9. Returns new version ID

### Main Application (`MainApp.java`)

Flow:
1. Parse command-line arguments (menuId, userId)
2. Initialize DAO layer
3. Call `createNewMenuVersion()`
4. Verify created version by query
5. Print formatted results

## Technologies

| Component | Technology |
|-----------|-----------|
| Java Version | Java 21 |
| Database | Oracle Database |
| JDBC Driver | Oracle JDBC Driver (ojdbc11) 23.4.0.24.05 |
| Logging | SLF4J + slf4j-simple 2.0.11 |
| Build | Maven 3.9+ |
| Module System | Java 9+ JPMS |

## Key Features

### ✅ No ORM
- Pure JDBC with `Connection`, `CallableStatement`, `PreparedStatement`
- No Hibernate, JPA, Spring Data, or any ORM framework
- Direct SQL execution with proper type mapping

### ✅ No Spring Framework
- Standalone application
- No Spring Boot, Spring Data, or any Spring dependencies
- Simple POJO design with manual dependency management

### ✅ No Frontend
- Console-only application
- No web UI, no JavaFX, no REST controllers
- Direct output to System.out with formatting

### ✅ Clean Architecture
- Interface-based DAO pattern for extensibility
- DTOs for data transfer between layers
- Separation of concerns (app, DAO, DTO, DB)
- Proper resource management with try-finally blocks

### ✅ Proper Error Handling
- SQL exceptions caught and logged
- Meaningful error messages to user
- Resource cleanup in finally blocks
- Application error exit codes

### ✅ Logging
- SLF4J Simple for console logging
- Configurable log levels
- Timestamp and thread information
- Debug logging for database operations

## Usage

### Build

```bash
cd /home/jan/Documents/School/druhak/JAVA/CateringApp
mvn -pl menu-system-console clean package -DskipTests
```

Output: `menu-system-console/target/menu-system-console-1.0.0.jar`

### Setup Stored Function

1. Execute SQL script in Oracle:
```sql
@menu-system-console/src/main/resources/create-function.sql
```

2. Or use SQL command line:
```bash
sqlplus ZAM0074/osVSOwCvA6yO96Ao@bayer.cs.vsb.cz:1521:oracle < create-function.sql
```

### Run Application

Default parameters (Menu ID: 1, User ID: 1):
```bash
java -jar menu-system-console/target/menu-system-console-1.0.0.jar
```

Custom parameters:
```bash
java -jar menu-system-console/target/menu-system-console-1.0.0.jar 1 2
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

## Configuration

Edit `menu-system-console/src/main/resources/db.properties`:

```properties
# Database connection parameters
db.url=jdbc:oracle:thin:@bayer.cs.vsb.cz:1521:oracle
db.username=ZAM0074
db.password=osVSOwCvA6yO96Ao
```

Control logging in `simplelogger.properties`:

```properties
org.slf4j.simpleLogger.defaultLogLevel=INFO
org.slf4j.simpleLogger.log.com.example.cateringapp.dao.MenuVersionDao=DEBUG
```

## What Changed in Parent Project

1. **Updated parent `pom.xml`**: Added `menu-system-console` module
2. **Created new module**: `menu-system-console` with independent structure
3. **No changes to existing modules**: `menu-system-domain`, `-persistence`, `-service`, `-web` remain untouched

The new console module is completely isolated and can be used independently or alongside the web application.

## Database Requirements

Required Oracle tables (from schema-oracle.sql):
- `menu_versions`
- `sections`
- `menu_items`
- `projects`
- `project_collaborators`
- `menus`
- `users`
- `items`

Ensure these tables exist before running the console application.

## Error Scenarios

| Error | Cause | Solution |
|-------|-------|----------|
| `ORA-04043: object ... does not exist` | Stored function not created | Run create-function.sql |
| `db.properties file not found` | Bad classpath | Rebuild with `mvn clean package` |
| `User is not a collaborator` | Invalid user/project combination | Verify user is in project_collaborators |
| `Menu not found` | Invalid menu ID | Use valid menu from database |
| `Cannot connect` | Database offline or bad credentials | Check database connection and properties |

## Code Quality

- ✅ Proper null handling
- ✅ Exception handling with logging
- ✅ Resource cleanup (try-finally, close())
- ✅ Consistent naming conventions (camelCase)
- ✅ Comprehensive JavaDoc comments
- ✅ Meaningful error messages
- ✅ No static business logic
- ✅ Interface-based design

## Running Tests

The stored function can be tested directly in SQL:

```sql
-- Test in Oracle SQL
DECLARE
    v_result NUMBER;
BEGIN
    v_result := CreateNewMenuVersion(1, 1);
    DBMS_OUTPUT.PUT_LINE('New Version ID: ' || v_result);
    COMMIT;
END;
/
```

## Files Added Summary

| File | Purpose |
|------|----------|
| `pom.xml` | Maven module configuration with JDBC + SLF4J |
| `MainApp.java` | Application entry point and orchestration |
| `DatabaseConfig.java` | Connection management and config loading |
| `MenuVersionDaoInterface.java` | DAO interface contract |
| `MenuVersionDao.java` | JDBC implementation of DAO |
| `MenuVersionDto.java` | Menu version data transfer object |
| `ProjectDto.java` | Project data transfer object |
| `db.properties` | Database configuration |
| `create-function.sql` | Oracle stored function definition |
| `simplelogger.properties` | Logging configuration |
| `module-info.java` | Java module definition |
| `README.md` | Comprehensive documentation |

## Next Steps (Optional Enhancements)

1. Add more DAO classes:
   - `ProjectDao` for project operations
   - `SectionDao` for section operations
   - `ItemDao` for item operations

2. Connection pooling:
   - Add HikariCP for production-grade connection pooling

3. Features:
   - Batch operations optimization
   - Transaction management utilities
   - Query builder for common operations

4. Testing:
   - Unit tests with mocked database
   - Integration tests with embedded Oracle
   - Flyway/Liquibase migration testing

5. CLI Enhancement:
   - Apache Commons CLI for advanced argument parsing
   - Interactive menu system
   - Batch operation support

## References

- Full documentation: `menu-system-console/README.md`
- Quick start guide: `CONSOLE-QUICKSTART.md`
- Stored function: `menu-system-console/src/main/resources/create-function.sql`

---

**Project Status**: ✅ **COMPLETE**

Console application successfully transformed from Spring Boot web application into pure backend JDBC-based console application with clean architecture and proper error handling.

