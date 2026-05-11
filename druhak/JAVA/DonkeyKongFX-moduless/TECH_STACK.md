# DonkeyKongFX - Web Server Tech Stack

## Project Information
- **Project Name**: DonkeyKongFX
- **Version**: 0.0.1-SNAPSHOT
- **Group ID**: cz.vsb.fei
- **Artifact ID**: DonkeyKongFX
- **Location**: `/donkeykong-web` directory

## Technology Stack

### Build & Deployment
- **Build Tool**: Maven 3.x
- **Packaging**: JAR (Fat JAR with all dependencies included)
- **Spring Boot Version**: 4.0.5
- **Java Version**: 25

### Backend Framework
- **Framework**: Spring Boot 4.0.5
- **Web Server**: Apache Tomcat 11.0.20 (embedded)
- **Port**: 8080

### Core Dependencies

#### Spring Framework
- **spring-boot-starter-web** - Web MVC support with Spring Web
- **spring-boot-starter-data-jpa** - JPA and Hibernate support
- **spring-boot-starter-thymeleaf** - Template engine

#### Database
- **H2 Database** v2.4.240 - In-memory SQL database
- **Hibernate ORM** 7.2.7.Final - JPA implementation
- **jakarta.persistence-api** v3.2.0 - JPA specification API

#### API Documentation
- **SpringDoc OpenAPI** v3.0.2 - Swagger UI integration
- **OpenAPI 3.0** specification support

#### Development Tools
- **Lombok** v1.18.42 - Code generation (annotations for boilerplate)
- **Maven Compiler Plugin** v3.14.1 - Java compilation
- **Maven Surefire Plugin** v3.0.0-M7 - Test runner

#### Testing
- **JUnit 5** v5.11.0 (Jupiter API & Engine)
- **Mockito** - Mocking framework

### Project Structure
```
donkeykong-web/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── cz/vsb/fei/DonkeyKongFX/
│   │   │       ├── DonkeyKongFxApplication.java (Main entry point)
│   │   │       ├── entity/
│   │   │       │   └── HighScore.java (JPA Entity)
│   │   │       ├── repository/
│   │   │       │   └── HighScoreRepository.java (Spring Data JPA Repository)
│   │   │       └── controller/
│   │   │           └── HighScoreController.java (REST Controller)
│   │   └── resources/
│   │       ├── application.yaml (Configuration)
│   │       └── static/ (Static files)
│   └── test/
│       └── java/
├── pom.xml
└── target/
    └── DonkeyKongFX-0.0.1-SNAPSHOT.jar (Built artifact)
```

## API Specification

### REST Endpoints
All endpoints are prefixed with `/api/v1/high-scores`

#### CRUD Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/high-scores` | Get all high scores |
| GET | `/api/v1/high-scores/{id}` | Get single high score by ID |
| POST | `/api/v1/high-scores` | Create a new high score |
| PUT | `/api/v1/high-scores/{id}` | Update existing high score |
| DELETE | `/api/v1/high-scores/{id}` | Delete high score by ID |
| DELETE | `/api/v1/high-scores` | Delete all high scores |

#### Additional Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/high-scores/top/10` | Get top 10 high scores |
| GET | `/api/v1/high-scores/player/{playerName}` | Get all scores for a player |
| GET | `/api/v1/high-scores/level/{level}` | Get all scores for a specific level |

### Documentation URLs
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Docs**: http://localhost:8080/v3/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## Database Configuration

### H2 Database
- **Type**: In-Memory SQL Database
- **URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (empty)
- **Dialect**: H2Dialect (auto-detected)
- **DDL Generation**: `create-drop` (auto-create tables on startup, drop on shutdown)

### HighScore Entity
```
Table: high_scores
├── id (Long, Primary Key, Auto-increment)
├── playerName (String, 50 chars, NOT NULL)
├── score (Integer, NOT NULL)
├── level (Integer, NOT NULL)
├── recordedAt (LocalDateTime, NOT NULL, auto-set)
└── notes (String, 500 chars, optional)
```

## Build Instructions

### Compile
```bash
cd donkeykong-web
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Package
```bash
mvn clean package
```

### Run Application
```bash
java -jar target/DonkeyKongFX-0.0.1-SNAPSHOT.jar
```

## Features Implemented

### 1. JPA Entity
- **HighScore.java** - Represents a game score record
  - Automatic ID generation
  - Auto-timestamp on creation
  - Lombok annotations for cleaner code
  - Proper JPA annotations

### 2. Repository
- **HighScoreRepository** - Spring Data JPA Repository
  - Basic CRUD operations
  - Custom query methods:
    - Find by player name
    - Find top 10 scores
    - Find by level
    - Count by player name

### 3. REST Controller
- **HighScoreController** - Full REST API
  - All CRUD operations
  - Additional query endpoints
  - Proper HTTP status codes (201 for creation, 204 for deletion, 404 for not found)
  - OpenAPI/Swagger documentation annotations
  - Error handling

### 4. Configuration
- **application.yaml** - Centralized configuration
  - Database settings
  - Hibernate configuration
  - H2 console enabled
  - SpringDoc/Swagger settings
  - JPA settings with SQL formatting

## Running the Application

1. **Start the application:**
   ```bash
   java -jar target/DonkeyKongFX-0.0.1-SNAPSHOT.jar
   ```

2. **Access the API:**
   - Base URL: http://localhost:8080/api/v1/high-scores
   - Swagger UI: http://localhost:8080/swagger-ui/index.html

3. **Example cURL commands:**
   ```bash
   # Create a new high score
   curl -X POST http://localhost:8080/api/v1/high-scores \
     -H "Content-Type: application/json" \
     -d '{
       "playerName": "Mario",
       "score": 15000,
       "level": 1,
       "notes": "Great game!"
     }'

   # Get all high scores
   curl http://localhost:8080/api/v1/high-scores

   # Get top 10 scores
   curl http://localhost:8080/api/v1/high-scores/top/10

   # Get scores for a player
   curl http://localhost:8080/api/v1/high-scores/player/Mario
   ```

## Status

✅ **Project successfully created and tested**
- Build: SUCCESSFUL
- Tests: PASSING
- Application: RUNNING
- API: FULLY FUNCTIONAL
- Documentation: GENERATED (Swagger UI & OpenAPI)

