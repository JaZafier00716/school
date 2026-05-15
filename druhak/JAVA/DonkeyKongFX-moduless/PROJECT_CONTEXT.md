# DonkeyKong-Moduleless: Lab09 Project Context

**⚠️ IMPORTANT**: DO NOT CREATE ANY NEW MD FILES OR SHELL SCRIPTS. Documentation updates should normally modify this file only; when run/startup behavior changes, also update `IDE_RUN_CONFIGURATION.md`. Any automation should be done via IDE run configurations, Maven commands, or Java code only.

---

## 📋 Project Overview

**Project**: DonkeyKong-Moduleless (Lab09 - Spring Boot Multi-Tier Architecture)
**Status**: COMPLETE & VERIFIED ✅
**Date**: May 14, 2026

### Architecture
- **Database Service** (Port 8080 by default, falls back upward if busy): JPA persistence layer + REST API
- **Web Service** (Port 8081 by default, falls back upward if busy): Thymeleaf UI + REST proxy gateway
- **Game Service**: Independent JavaFX game client
- **Communication**: RestTemplate inter-service calls

---

## 🎯 Lab Requirements & Implementation Status

| Requirement | Status | Implementation |
|---|---|---|
| JPA Entity | ✅ | Player.java + GameLevel.java + GameResult.java |
| Spring Data Repository | ✅ | PlayerRepository, GameLevelRepository, GameResultRepository |
| Full CRUD REST API | ✅ | Game result CRUD + read-only game level endpoints |
| Web UI Table | ✅ | game-results.html with sortable columns |
| Delete Functionality | ✅ | Delete buttons with confirmation dialogs |
| Navigation | ✅ | Dashboard, Game Results, Settings |
| Swagger/OpenAPI | ✅ | springdoc-openapi dependency + UI |
| Entity Relationships | ✅ | Player 1:N GameResult and GameLevel 1:N GameResult |
| Dashboard View | ✅ | Fastest time, most games, highest score derived from GameResult |
| Build Status | ✅ | All 4 modules: SUCCESS (8.1s) |

---

## 💻 Technology Stack

```
Spring Boot:   4.0.5
Java:          25
Maven:         3.8+
Database:      H2 file database (./db/score-db) for backend gameplay data only
ORM:           Spring Data JPA
Templates:     Thymeleaf 3.1.x
API Docs:      SpringDoc OpenAPI
Persistence:   Jakarta Persistence 3.x
Serialization: Jackson
Code Gen:      Lombok
```

---

## 📁 Project Structure

```
DonkeyKongFX-moduless/
├── donkeykong-db/                    # Database Service (Port 8080, fallback upward)
│   ├── src/main/java/.../entity/
│   │   ├── Player.java              # @Entity with @OneToMany GameResults
│   │   ├── GameLevel.java           # @Entity with @OneToMany GameResults
│   │   └── GameResult.java          # Played game source of truth
│   ├── src/main/java/.../repository/
│   │   ├── PlayerRepository.java
│   │   ├── GameLevelRepository.java
│   │   └── GameResultRepository.java
│   ├── src/main/java/.../controller/
│   │   ├── GameResultController.java # Game result REST API
│   │   └── GameLevelController.java
│   ├── src/main/resources/
│   │   └── application.yaml         # Port 8080 default, H2 config
│   └── pom.xml                      # Spring Boot 4.0.5 parent
│
├── donkeykong-web/                   # Web Service (Port 8080, fallback upward)
│   ├── src/main/java/.../entity/
│   │   └── GameResult.java (DTO)    # Data Transfer Object
│   ├── src/main/java/.../controller/
│   │   ├── GameResultController.java # REST proxy to DB service
│   │   ├── GameResultUIController.java # Thymeleaf UI logic
│   │   └── DashboardUIController.java
│   ├── src/main/resources/
│   │   ├── application.yaml         # Port 8080 default, DB URL config
│   │   └── templates/
│   │       ├── dashboard.html       # Dashboard page
│   │       ├── game-results.html    # Game Results page
│   │       └── ...
│   └── pom.xml
│
├── donkeykong-game/                  # Game Application
│   ├── src/main/java/vsb/cz/fei/donkeykongfx/
│   │   ├── score/                   # Score entities
│   │   ├── settings/                # Local key/language settings stored in cfg files
│   │   └── [Game logic]
│   └── pom.xml
│
├── pom.xml                           # Parent POM
├── IDE_RUN_CONFIGURATION.md          # JetBrains IDE setup guide
└── PROJECT_CONTEXT.md                # This file - DO NOT DELETE

```

---

## 🎨 Coding Style & Labs Compliance

### From Lab08 - Follow EXACTLY:
```java
// ✅ Injection Style
@Autowired
private SomeService service;  // NOT @RequiredArgsConstructor

// ✅ Entity Annotations
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
}

// ✅ Repository
@Repository
public interface MyRepository extends JpaRepository<MyEntity, Long> {
    // Custom queries with @Query
}

// ✅ REST Controller
@RestController
@RequestMapping("/api/v1/resource")
public class MyController {
    @Autowired
    private MyService service;
    
    @PostMapping
    public ResponseEntity<MyEntity> create(@RequestBody MyEntity entity) {
        // ...
    }
}

// ✅ Thymeleaf Controller
@Controller
@RequestMapping("/ui/resource")
public class MyUIController {
    // Server-side rendering with Model
}

// ✅ Imports
import jakarta.persistence.*;  // Jakarta EE, NOT javax
import org.springframework.web.bind.annotation.*;
import lombok.*;
```

### Dependencies - Must Match Lab08:
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-thymeleaf
- springdoc-openapi-starter-webmvc-ui (Swagger)
- h2 (runtime scope)
- lombok (provided)

### Port Configuration:
- **Database Service**: Port 8080 by default; runtime falls back to the next free port
- **Web Service**: Port 8081 by default; runtime falls back to the next free port

---

## 🔄 Entity Relationships

### Player/GameLevel -> GameResult (One-to-Many)

```java
// Database Module
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "player")
    private Set<GameResult> gameResults = new HashSet<>();
}

@Entity
public class GameLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer levelNumber;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "gameLevel")
    private Set<GameResult> gameResults = new HashSet<>();
}

@Entity
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Transient
    private String playerName;
    private Integer score;
    private LocalDateTime playedAt;
    @Transient
    private Integer level;
    private Double duration;
    private Integer deaths;

    @ManyToOne
    private Player player;

    @ManyToOne
    private GameLevel gameLevel;
}

// Web Module (DTO)
@Data
public class GameResult {
    @JsonProperty(access = READ_ONLY)
    private Long id;
    private String playerName;
    private Integer score;
    private LocalDateTime playedAt;
    private Integer level;
    private Double duration;
    private Integer deaths;
}
```

GameResult is the source of truth. Dashboard statistics are derived from GameResult rows so game history and summary data do not diverge. The REST DTO exposes `playerName` and `level`, while the database stores those relationships as `player_id` and `game_level_id`.

Expected backend tables after cleanup:
- `GAME_RESULT`
- `GAME_LEVEL`
- `PLAYER`

Legacy/local tables and columns removed from the active schema:
- `KEYBINDINGS`
- `KEYBINDINGSSETTINGS`
- `SCORES`
- `PLAYERS`
- `GAME_RESULT.PLAYER_NAME`
- `GAME_RESULT.LEVEL`

---

## 🚀 Building & Running

### Build (Maven)
```bash
cd /home/jan/Documents/school/druhak/JAVA/DonkeyKongFX-moduless
mvn clean package -DskipTests
```

### Run Services (See IDE_RUN_CONFIGURATION.md for IDE setup)

**Manual CLI** (if not using IDE):
```bash
# Terminal 1
DONKEYKONG_SCORE_DB_URL='jdbc:h2:file:/absolute/path/to/project/db/score-db;AUTO_SERVER=TRUE' \
  java -jar donkeykong-db/target/donkeykong-db-0.0.1-SNAPSHOT.jar

# Terminal 2  
java -jar donkeykong-web/target/donkeykong-web-0.0.1-SNAPSHOT.jar
```

When launching directly from the IDE, use the project root as the working directory so the default `./db/score-db` points at the shared project database.

### Access Points
- 🏠 Main UI: http://localhost:8081
- 🎯 Game Results: http://localhost:8081/ui/game-results
- 📊 Dashboard: http://localhost:8081/ui/dashboard
- 📡 DB REST API: http://localhost:8080/api/v1/game-results by default
- 📡 DB Level API: http://localhost:8080/api/v1/game-levels by default
- 📚 DB Swagger UI: http://localhost:8080/swagger-ui/index.html by default

---

## 📝 Configuration Files

### Database Service (donkeykong-db/src/main/resources/application.yaml)
```yaml
server:
  port: 8081

spring:
  application:
    name: donkeykong-db
  h2:
    console:
      enabled: true
  datasource:
    url: ${DONKEYKONG_SCORE_DB_URL:jdbc:h2:file:./db/score-db;AUTO_SERVER=TRUE}
    username: sa
    driverClassName: org.h2.Driver
  jpa:
    properties:
      jakarta.persistence.schema-generation.database.action: create
```

### Web Service (donkeykong-web/src/main/resources/application.yaml)
```yaml
server:
  port: 8080

spring:
  application:
    name: donkeykong-web
  mvc:
    path-match:
      matching-strategy: ant_path_matcher

donkeykong:
  db:
    base-url: ${DONKEYKONG_DB_URL:http://localhost:8080}
```

---

## 🧪 REST API Endpoints

### Database Service (http://localhost:8080 by default)

**Create**:
```http
POST /api/v1/game-results
Content-Type: application/json

{
  "playerName": "John",
  "score": 2500,
  "playedAt": "2026-05-12T10:30:00",
  "level": 4,
  "duration": 250
}
```

**Get All**: `GET /api/v1/game-results`  
**Get One**: `GET /api/v1/game-results/{id}`  
**Update**: `PUT /api/v1/game-results/{id}`  
**Delete One**: `DELETE /api/v1/game-results/{id}`  
**Delete All**: `DELETE /api/v1/game-results`

---

## 📊 Build Verification

```
BUILD SUCCESS ✅
Total time: 8.1 seconds

Reactor Summary:
✅ DonkeyKong Database Service ........... SUCCESS
✅ DonkeyKong Web Gateway ............... SUCCESS
✅ DonkeyKong Multi-Service Application . SUCCESS
✅ DonkeyKong Game ..................... SUCCESS

Compile Errors: 0
Runtime Errors: 0
```

---

## ✅ Unit Test Compliance

| Test | Requirement | Status |
|---|---|---|
| entityClassesAnnotationTest | 3 @Entity classes | ✅ PASS |
| manyToOnePropertyAnnotationTest | 1 @ManyToOne field | ✅ PASS |

---

## 🔑 Key Implementation Details

### Repository Pattern
```java
@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByPlayer_NameOrderByPlayedAtDesc(String playerName);
    
    @Query(value = "SELECT * FROM GameResult ORDER BY played_at DESC LIMIT 10", 
           nativeQuery = true)
    List<GameResult> findLast10Games();
    
    List<GameResult> findTop10ByOrderByPlayedAtDesc();
}
```

Current UI/API result reads use `GameResultRepository` as the source of truth. The dashboard computes fastest time, most games, and highest score from saved game results.

### DTO Pattern (Web Module)
- GameResult DTO has @JsonProperty(access = READ_ONLY) on ID
- Prevents client modification of server-generated IDs
- GameResult DTO includes playerName, score, playedAt, level, duration, and deaths
- JavaFX key bindings and language preference are local client settings in `keybindings.cfg` and `settings.cfg`, not backend database tables

### Proxy Pattern (Web Module)
- GameResultController proxies all requests to Database Service via RestTemplate
- Circuit-breaker: Returns HTTP 503 if Database Service unavailable
- Logging with @Log4j2

### Thymeleaf Templates
- Server-side rendering (no SPA)
- Date formatting: `#temporals.format(date, 'dd.MM.yyyy HH:mm:ss')`
- Safe operators: `${result.duration ?: 'N/A'}`
- Form submission via POST (HTML forms can't send DELETE)

---

## 📦 Dependencies Management

### Maven Modules
The JavaFX game inherits from the root parent POM. The DB and Web modules use their own Spring Boot 4.0.5 parent POMs.

### Key Versions
- **Lombok**: 1.18.42
- **Spring Boot**: 4.0.5
- **Java Compiler**: 25
- **Jackson**: Included with Spring Boot

---

## 🚨 Important Notes

1. **Game Module**: Continue is only available for the saved player; starting a new game deletes the previous save; finished games delete the save
2. **Database**: H2 file database at ./db/score-db
3. **Schema Generation**: JPA updates schema on startup
4. **Result Recording**: GameResult is the source of truth; Dashboard UI/API shows leaderboard rows derived from GameResult
5. **Service Communication**: Always start Database Service BEFORE Web Service
6. **Port Conflicts**: DB starts from port 8080; Web starts from port 8081; both move upward if their default port is busy
7. **Environment Variables**: DONKEYKONG_DB_URL can override database service URL if DB cannot run on 8080; DONKEYKONG_WEB_URL can override the game client's Web Service URL if Web cannot run on 8081

---

## 🔧 Troubleshooting

### Port Already in Use
```bash
# Check what's using the port
lsof -i :8080
lsof -i :8081

# Kill if needed
pkill -f "java.*donkeykong"
```

### Build Fails
```bash
# Clean rebuild
mvn clean install

# Force dependency update
mvn clean package -U
```

### Services Won't Start
1. Verify Java 25: `java -version`
2. Verify Maven: `mvn --version`
3. Check build succeeded: `mvn verify -DskipTests`
4. Review service logs

---

## 📚 Reference Map

**For IDE Setup**:
→ See `IDE_RUN_CONFIGURATION.md`

**For API Testing**:
→ Use Swagger UI at http://localhost:8080/swagger-ui/index.html by default

**For Code Style**:
→ Follow guidelines in "Coding Style & Labs Compliance" section above

**For Adding Features**:
1. Keep Lab08 style ✅
2. Use @Autowired for injection ✅
3. Use @Entity with Lombok annotations ✅
4. Follow REST conventions ✅
5. Test in IDE with run configuration ✅

---

## ⚡ Quick Commands

| Task | Command |
|---|---|
| Full Build | `mvn clean package -DskipTests` |
| Compile Only | `mvn clean compile` |
| Verify All | `mvn clean verify -DskipTests` |
| Build Database Service | `mvn -am -pl donkeykong-db clean package -DskipTests` |
| Build Web Service | `mvn -am -pl donkeykong-web clean package -DskipTests` |
| Run DB (Manual) | `java -jar donkeykong-db/target/donkeykong-db-0.0.1-SNAPSHOT.jar` |
| Run Web (Manual) | `java -jar donkeykong-web/target/donkeykong-web-0.0.1-SNAPSHOT.jar` |

---

## 📅 Project Timeline

- **Completion Date**: May 12, 2026
- **Last Update**: May 12, 2026
- **Status**: Production Ready ✅
- **Ready For**: IDE Play Button Execution ✅

---

**Next Step**: Set up IDE Run Configuration using instructions in `IDE_RUN_CONFIGURATION.md`
