# Menu System

Multi-module Spring Boot application for catering menu management with strict versioning, collaborators, sections, and items.

## Overview

This project implements:
- Strict relational schema (including PostgreSQL enums)
- Layered architecture (domain, persistence, service, web)
- REST API + Thymeleaf UI
- Transactional menu version cloning (`createNewMenuVersion(menuId, userId)`)

## Architecture

The repository is organized as a Maven multi-module project:

- `menu-system-parent` (root `pom.xml`, packaging `pom`)
- `menu-system-domain`
  - JPA entities and enums only
- `menu-system-persistence`
  - Spring Data JPA repositories
- `menu-system-service`
  - Business logic, DTOs, services, custom exceptions
- `menu-system-web`
  - Spring Boot application, REST controllers, Thymeleaf controllers/views, runtime config

### Dependency flow

`web -> service -> persistence -> domain`

No circular dependencies.

## Tech Stack

- Java 21
- Maven 3.9+
- Spring Boot 3.5.x
- Spring Data JPA (Hibernate)
- Oracle Database
- Thymeleaf
- Lombok (compile-time only)

## Requirements

- JDK 21 (full JDK, including `javac`)
- Maven installed (`mvn`)
- Oracle Database running and accessible
- Database user with appropriate data definition and DML privileges

## Project Structure

```text
.
├── pom.xml
├── menu-system-domain
├── menu-system-persistence
├── menu-system-service
└── menu-system-web
    ├── src/main/java/com/example/cateringapp
    └── src/main/resources
        ├── application.properties
        ├── schema.sql
        ├── data.sql
        └── templates/
```

## Configuration

Main runtime config is in:
- `menu-system-web/src/main/resources/application.properties`

Default Oracle DB config:
- URL: `jdbc:oracle:thin:@bayer.cs.vsb.cz:1521:oracle`
- Username: `ZAM0074`
- Password: `osVSOwCvA6yO96Ao`

Update these values for your environment before running.

### Schema initialization

The app uses SQL init scripts from `menu-system-web/src/main/resources`:
- `schema-oracle.sql` (creates tables, sequences, constraints, etc.)
- `data-oracle.sql` (seed data)

`spring.jpa.hibernate.ddl-auto=validate` is enabled, so schema must match exactly.

## Build

From repository root:

```bash
mvn clean install
```

If tests are not needed:

```bash
mvn clean install -DskipTests
```

## Run

Run from root using the web module:

```bash
mvn -pl menu-system-web spring-boot:run
```

Or package and run jar from web module:

```bash
mvn -pl menu-system-web -am package
java -jar menu-system-web/target/menu-system-web-1.0.0.jar
```

Application default URL:
- `http://localhost:8080`

## API Endpoints

Implemented REST endpoints:

- `GET /templates`
- `POST /menus`
- `GET /menus/{id}/versions`
- `PUT /projects/{id}/active-version`
- `POST /menus/{id}/versions`
- `GET /versions/{id}/sections`
- `POST /sections`
- `PUT /sections/{id}`
- `GET /items?name=`
- `POST /items`
- `PUT /items/{id}`

## UI Screens (Thymeleaf)

- `/ui/menus/create` (Create Menu)
- `/ui/menus/{id}/edit` (Edit Menu, versions, active version switch, sections)
- `/ui/sections/create?versionId=...` (Create Section)
- `/ui/sections/{id}/edit` (Edit Section)
- `/ui/items/create` (Create Item)
- `/ui/items/{id}/edit` (Edit Item)

## Core Business Transaction

`MenuService#createNewMenuVersion(menuId, userId)`:
- `@Transactional`
- Validates user is project collaborator
- Resolves active/source version
- Creates new version with `version_number = max + 1`
- Clones sections and menu items
- Maintains old section -> new section mapping
- Updates `projects.active_version_id`
- Executes atomically

## Java Module System (JPMS)

Each module contains `module-info.java`.
- Domain exports entity package
- Persistence exports repository package
- Service exports service/dto/exception packages
- Web is the application entry module

## Error Handling

- Service layer throws domain-specific runtime exceptions (`NotFoundException`, `BadRequestException`, `ForbiddenOperationException`)
- Web layer maps API exceptions via `@RestControllerAdvice`

## Development Notes

- Keep entities in `domain` free from Spring stereotypes
- Keep SQL out of controllers
- Keep business logic in `service`
- Use repositories only through service layer
- Service classes use Lombok (`@AllArgsConstructor`, `@NoArgsConstructor`) for constructor generation:
  - Required for Spring AOP/proxy support with `@Transactional`
  - Both annotations work together: no-args for CGLIB, full-args for DI

## Troubleshooting

### `release version X not supported`
Your environment is using an unsupported JDK. Install/configure JDK 21 and ensure:

```bash
java -version
javac -version
```

both resolve to Java 21.

### Oracle connection errors
Ensure the configured user credentials and connection string are correct:
- URL format: `jdbc:oracle:thin:@host:port:SID`
- User has appropriate permissions to read/write tables and sequences

### Validation/startup errors with JPA
Confirm DB schema matches `schema-oracle.sql` exactly; application is configured to validate schema, not auto-create.

### CGLIB Proxy issues at startup (AOP/Transactional)
All `@Service` classes have `@NoArgsConstructor` and `@AllArgsConstructor` Lombok annotations:
- `@NoArgsConstructor` provides no-args constructor for CGLIB proxy instantiation
- `@AllArgsConstructor` provides full constructor for Spring dependency injection
- Fields are non-final to allow proper proxy initialization

## Suggested Next Improvements

- Add integration tests for the version-cloning transaction
- Add Flyway/Liquibase migrations for production deployment
- Add authentication/authorization layer (currently data model supports roles/collaborators)
- Add OpenAPI/Swagger docs for REST API
