# DonkeyKong Multi-Service Architecture

## Overview

The project has been restructured into a **multi-service architecture** with a separate database service (`donkeykong-db`) that both the game and web applications can safely share.

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                   Shared Database                           │
│           (H2 File: score-db at repository root)           │
└────────────────────────┬────────────────────────────────────┘
                         │
          ┌──────────────┴──────────────┐
          │                             │
    ┌─────▼──────┐           ┌──────────▼─────┐
    │ donkeykong- │           │  donkeykong-  │
    │     db      │           │     web       │
    │ (Port 8082) │ ◄─ REST ─ │  (Port 8081)  │
    │             │   Calls   │               │
    │  - Entity   │           │ - Proxy       │
    │  - Repo     │           │ - REST Client │
    │  - REST API │           │ - Swagger UI  │
    └─────────────┘           └───────────────┘
```

## Services

### 1. `donkeykong-db` (Port 8082)

**Purpose**: Central database service that owns the H2 database and exposes a REST API.

**Location**: `/donkeykong-db/`

**Responsibilities**:
- Manages the H2 file database (`score-db`)
- Exposes REST endpoints for high-score CRUD operations
- Provides OpenAPI/Swagger documentation
- Single source of truth for all score data

**Configuration**:
- `application.yaml`: Database URL configurable via `SCORE_DB_PATH` environment variable (defaults to `${user.dir}/../score-db`)
- Uses portable relative path: `${SCORE_DB_PATH:${user.dir}/../score-db}` for portability

**Key Files**:
- `src/main/java/cz/vsb/fei/donkeykong/entity/HighScore.java` - Real JPA Entity
- `src/main/java/cz/vsb/fei/donkeykong/repository/HighScoreRepository.java` - Data access layer
- `src/main/java/cz/vsb/fei/donkeykong/controller/HighScoreController.java` - REST API endpoints

**Endpoints**:
- `GET /api/v1/high-scores` - Get all scores
- `GET /api/v1/high-scores/{id}` - Get score by ID
- `POST /api/v1/high-scores` - Create new score
- `PUT /api/v1/high-scores/{id}` - Update score
- `DELETE /api/v1/high-scores/{id}` - Delete score
- `GET /api/v1/high-scores/top/10` - Get top 10 scores
- `GET /api/v1/high-scores/player/{playerName}` - Get player's scores
- `DELETE /api/v1/high-scores` - Delete all scores

**Documentation**:
- Swagger UI: `http://localhost:8082/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`
- H2 Console: `http://localhost:8082/h2-console`

---

### 2. `donkeykong-web` (Port 8081)

**Purpose**: Web application that delegates to `donkeykong-db` service for all database operations.

**Location**: `/donkeykong-web/`

**Responsibilities**:
- Exposes the same REST API endpoints as `donkeykong-db`
- Acts as a proxy/facade to `donkeykong-db`
- Provides Swagger/OpenAPI documentation
- Can call `donkeykong-db` service transparently

**Configuration**:
- `application.yaml`: Service URL configurable via `DONKEYKONG_DB_URL` environment variable (defaults to `http://localhost:8082`)

**Key Files**:
- `src/main/java/cz/vsb/fei/DonkeyKongFX/entity/HighScore.java` - Data Transfer Object (DTO, no JPA)
- `src/main/java/cz/vsb/fei/DonkeyKongFX/controller/HighScoreController.java` - REST client proxy
- `src/main/java/cz/vsb/fei/DonkeyKongFX/config/RestTemplateConfig.java` - HTTP client configuration

**Dependencies Removed**:
- Spring Data JPA
- H2 Database driver
- Hibernate
- Database console

**Dependencies Kept**:
- Spring Web (for REST endpoints and RestTemplate)
- SpringDoc OpenAPI (for Swagger UI)
- Thymeleaf (if needed for web views)
- Lombok (for code generation)

---

## Building and Running

### Build Both Services

```bash
cd /home/jan/Documents/school/druhak/JAVA/DonkeyKongFX-moduless

# Build both subprojects
mvn clean package -DskipTests
```

### Run the Services

#### Option 1: Using the startup script

```bash
bash /tmp/start-services.sh
```

This script:
1. Starts `donkeykong-db` on port 8082
2. Waits for it to fully boot
3. Starts `donkeykong-web` on port 8081
4. Tests both services
5. Shows the output

#### Option 2: Manual start

Start `donkeykong-db` first (in one terminal):
```bash
cd /home/jan/Documents/school/druhak/JAVA/DonkeyKongFX-moduless/donkeykong-db
mvn spring-boot:run
```

Then start `donkeykong-web` (in another terminal):
```bash
cd /home/jan/Documents/school/druhak/JAVA/DonkeyKongFX-moduless/donkeykong-web
mvn spring-boot:run
```

### Test the Services

Both services serve the same endpoints:

```bash
# Get all high scores from donkeykong-db
curl http://localhost:8082/api/v1/high-scores

# Get all high scores from donkeykong-web (proxied to donkeykong-db)
curl http://localhost:8081/api/v1/high-scores

# Create a new high score via donkeykong-db
curl -X POST http://localhost:8082/api/v1/high-scores \
  -H "Content-Type: application/json" \
  -d '{"playerName":"TestPlayer","score":5000}'

# Same via donkeykong-web
curl -X POST http://localhost:8081/api/v1/high-scores \
  -H "Content-Type: application/json" \
  -d '{"playerName":"TestPlayer","score":5000}'

# Access Swagger UI
# - donkeykong-db: http://localhost:8082/swagger-ui/index.html
# - donkeykong-web: http://localhost:8081/swagger-ui/index.html
```

---

## Portability

The services are now fully portable across machines.

### How to move to another machine/directory

1. **Copy the entire project folder** to the new location

2. **Update environment variables** (optional, if you want a different database path):
   ```bash
   export SCORE_DB_PATH=/path/to/new/score-db
   ```

3. **Run the services**:
   - The applications will automatically use the relative path `../score-db` from the service folder
   - Or use the absolute path you provided via `SCORE_DB_PATH`

Neither service has any hardcoded absolute paths that depend on your home directory!

---

## Benefits of This Architecture

✅ **No file-locking conflicts** - Services don't fight over the same database  
✅ **Portable** - Can move to any machine/directory without reconfiguration  
✅ **Scalable** - Easy to add more services that consume the DB service  
✅ **Maintainable** - Single source of truth for database schema  
✅ **Testable** - Each service can be tested independently  
✅ **Separated concerns** - `donkeykong-db` owns database, `donkeykong-web` provides web API  

---

## Configuration Reference

### `donkeykong-db/src/main/resources/application.yaml`

```yaml
server:
  port: 8082
spring:
  application:
    name: DonkeyKongDB
  datasource:
    url: jdbc:h2:file:${SCORE_DB_PATH:${user.dir}/../score-db};AUTO_SERVER=TRUE
    driver-class-name: org.h2.Driver
    username: ""
    password: ""
  jpa:
    hibernate:
      ddl-auto: update
```

### `donkeykong-web/src/main/resources/application.yaml`

```yaml
server:
  port: 8081
spring:
  application:
    name: DonkeyKongFX
donkeykong:
  db:
    base-url: ${DONKEYKONG_DB_URL:http://localhost:8082}
```

---

## Future Enhancements

1. **Migrate to a real database** (PostgreSQL, MySQL) instead of H2 file
2. **Add authentication** - Secure the donkeykong-db service with API keys or OAuth
3. **Add caching** - Cache frequently accessed scores in the web layer
4. **Add monitoring** - Export metrics from both services
5. **Add health checks** - Expose `/actuator/health` endpoints
6. **Containerize** - Create Docker images for each service

---

## Troubleshooting

### "Connection refused" error
- Make sure `donkeykong-db` is started before `donkeykong-web`
- Check that port 8082 is not blocked by a firewall

### "Database not found" error
- Check that the `score-db.mv.db` file exists in the repository root
- Or ensure `SCORE_DB_PATH` is set to a valid path

### Port already in use
- Check what's using the port: `lsof -i :8081` or `lsof -i :8082`
- Kill the process or change the port in `application.yaml`


