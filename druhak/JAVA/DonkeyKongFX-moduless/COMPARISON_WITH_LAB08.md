# Comparison: DonkeyKong-DB vs Java2-Lab08

## Task Requirements Analysis

The original task for donkeykong-moduleless stated:
> "Create a Spring server using https://start.spring.io with specific dependencies and implement REST API with CRUD operations for a JPA entity"

**Required Dependencies:**
- Lombok ✓
- Spring Web ✓
- SpringDoc OpenAPI ⚠ (Currently removed)
- Thymeleaf ⚠ (Currently removed from DB module)
- Spring Data JPA ✓
- H2 Database ✓

**Endpoints requested:**
- http://localhost:8080 ✓ (but we use 8082)
- http://localhost:8080/swagger-ui/index.html ✗ (Removed during refactoring)
- http://localhost:8080/v3/api-docs ✗ (Removed during refactoring)

## Detailed Comparison

### 1. PROJECT STRUCTURE

| Aspect | Java2-Lab08 | DonkeyKong-DB |
|--------|------------|--------------|
| POM Parent | Spring Boot 4.0.5 | Project Parent (refactored) |
| Spring Boot Version | 4.0.5 | ❌ Missing (downgraded via refactoring) |
| Java Version | 25 | 21 (from parent) |

### 2. DEPENDENCIES

#### Java2-Lab08 (Original Task Req)
```xml
- spring-boot-h2console ✓
- spring-boot-starter-data-jpa ✓
- spring-boot-starter-thymeleaf ✓
- spring-boot-starter-webmvc ✓
- springdoc-openapi-starter-webmvc-ui ✓
- h2 (runtime) ✓
- lombok ✓
- Hibernate JPA Model Generator ✓
- Test: data-jpa-test, thymeleaf-test, webmvc-test ✓
```

#### DonkeyKong-DB (Current - After Refactoring)
```xml
- spring-boot-starter-web (simplified from webmvc) ⚠
- spring-boot-starter-data-jpa ✓
- h2 ✓
- lombok ✓
- springdoc-openapi (REMOVED) ✗
- thymeleaf (REMOVED from db module) ⚠
- spring-boot-h2console (REMOVED) ✗
- Hibernate JPA Model Generator (REMOVED) ✗
- Test: starter-test only ⚠
```

### 3. POJO ENTITY

#### Java2-Lab08 (Player.java)
```java
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    
    private String firstName;
    private String lastName;
    private LocalDate dayOfBirth;
}
```

#### DonkeyKong-DB (HighScore.java)
```java
@Entity
@Table(name = "Scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String playerName;
    
    @Column(name = "points", nullable = false)
    private Integer score;
}
```

**Differences:**
- ❌ Lab08 uses @EqualsAndHashCode configuration, DK-DB doesn't
- ❌ Lab08 uses AUTO generation, DK-DB uses IDENTITY
- ❌ Lab08 uses simple types, DK-DB maps to specific columns
- ⚠ Lab08 doesn't have Jackson annotation, DK-DB does

### 4. REPOSITORY

#### Java2-Lab08
```java
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByFirstName(String name);
    
    @Query("SELECT p FROM Player p WHERE p.dayOfBirth < :date")
    List<Player> findBeforeChristmas(LocalDate date);
}
```

#### DonkeyKong-DB
```java
@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {
    List<HighScore> findByPlayerNameOrderByScoreDesc(String playerName);
    
    @Query(value = "SELECT * FROM Scores ORDER BY points DESC LIMIT 10", nativeQuery = true)
    List<HighScore> findTop10HighScores();
    
    Integer countByPlayerName(String playerName);
}
```

**Differences:**
- ✓ Both have custom methods ✓
- ❌ Lab08 uses JPQL Query, DK-DB uses native SQL
- ✓ Both extend JpaRepository

### 5. REST CONTROLLER

#### Java2-Lab08 (PlayerController)
```java
@RestController
@RequestMapping("/api/players")
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;    // Field injection
    
    @GetMapping({"/", ""})
    public List<Player> findAll() {
        return playerRepository.findAll();
    }
    
    @PostMapping({"/", ""})
    public Player save(@RequestBody Player player) {
        return playerRepository.save(player);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            return ResponseEntity.ok(player);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found");
        }
    }
    
    @DeleteMapping({"/{id}"})
    public ResponseEntity<String> delete(@PathVariable Long id) {
        playerRepository.deleteById(id);
        return ResponseEntity.ok("Player deleted");
    }
    
    // Additional custom methods
}
```

#### DonkeyKong-DB (HighScoreController)
```java
@RestController
@RequestMapping("/api/v1/high-scores")
@RequiredArgsConstructor
public class HighScoreController {
    private final HighScoreRepository highScoreRepository;    // Constructor injection
    
    @GetMapping
    public ResponseEntity<List<HighScore>> getAllHighScores() {
        List<HighScore> scores = highScoreRepository.findAll();
        return ResponseEntity.ok(scores);
    }
    
    @PostMapping
    public ResponseEntity<HighScore> createHighScore(@RequestBody HighScore highScore) {
        highScore.setId(null);
        HighScore savedHighScore = highScoreRepository.save(highScore);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedHighScore);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HighScore> getHighScoreById(@PathVariable Long id) {
        Optional<HighScore> highScore = highScoreRepository.findById(id);
        return highScore.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHighScore(@PathVariable Long id) {
        if (highScoreRepository.existsById(id)) {
            highScoreRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
```

**Key Differences:**

| Aspect | Lab08 | DK-DB |
|--------|-------|-------|
| Dependency Injection | @Autowired field | @RequiredArgsConstructor constructor |
| Route Prefix | `/api/players` | `/api/v1/high-scores` |
| Return Types | Mix: List & ResponseEntity | Consistent: ResponseEntity<T> |
| Error Responses | String messages | Empty body or 404 status |
| GET Empty Path | {"/", ""} | Just / |
| POST Response | Direct entity | ResponseEntity with CREATED status |
| DELETE Response | ResponseEntity<String> | ResponseEntity<Void> |
| HTTP Status Codes | 200/404 for not found | 201/204/404 - more explicit |
| Custom Methods | Simple (findByName, findBefore) | Complex (top10, top scores, delete all) |

### 6. APPLICATION CONFIGURATION

#### Java2-Lab08
```yaml
spring:
  application:
    name: java2-lab08
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:file:./db/score-db
    username: sa
    driverClassName: org.h2.Driver
  jpa:
    properties:
      jakarta:
        persistence:
          schema-generation:
            database.action: create
```

#### DonkeyKong-DB
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
  h2:
    console:
      enabled: true
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        validator:
          message_interpolator: org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator
  mvc:
    path-match:
      matching-strategy: ant_path_matcher
```

**Differences:**
- ❌ DK-DB has server port (lab08 uses default 8080)
- ✓ Both have H2 console enabled
- ⚠ Different database URL strategy (fixed vs environment variable)
- ⚠ Different DDL strategy (create vs update)
- ⚠ DK-DB has additional Hibernate config

## TASK COMPLIANCE SUMMARY

### Lab08 ✓ FULLY COMPLIANT
```
✓ Spring Boot 4.0.5
✓ All required dependencies
✓ YAML configuration
✓ Swagger UI endpoints available
✓ OpenAPI docs available
✓ Simple JPA entity
✓ Custom repository methods
✓ REST controller with CRUD
✓ @Autowired injection style
✓ Proper error handling
```

### DonkeyKong-DB ⚠ PARTIALLY COMPLIANT (After Refactoring)
```
❌ Spring Boot version mismatch (downgraded)
❌ Missing: springdoc-openapi (Swagger removed)
❌ Missing: swagger-ui endpoints
❌ Missing: /v3/api-docs endpoint
⚠ Missing: Thymeleaf in DB module (not needed for API-only)
⚠ Different injection style (@RequiredArgsConstructor vs @Autowired)
⚠ Different configuration approach
✓ YAML configuration
✓ JPA entity
✓ Custom repository methods
✓ REST controller with CRUD
✓ Proper error handling with ResponseEntity
```

## RECOMMENDATION

**To make DonkeyKong-DB match Lab08 style completely, need to:**

1. ⚠ **DECIDE:** Keep current refactored version or restore original?
   - **Option A (Current):** Simplified, modern approach - use @RequiredArgsConstructor
   - **Option B (Lab08):** Match lab exactly - use @Autowired, add all original dependencies

2. If keeping current style:
   - ✓ Restore springdoc-openapi dependency for Swagger UI
   - ✓ Restore Spring Boot 4.0.5 as parent POM
   - ✓ Add swagger-ui and OpenAPI endpoints documentation

3. If restoring Lab08 style:
   - Replace @RequiredArgsConstructor with @Autowired
   - Restore all original dependencies
   - Revert POM to Spring Boot 4.0.5 parent
   - Add missing test dependencies
   - Switch to JPQL queries instead of native SQL

## CONCLUSION

**Current Implementation Status:**
- ✗ NOT identical to Lab08
- ⚠ Following different patterns (modern vs original)
- ⚠ Missing required Swagger endpoints
- ⚠ Using different Spring Boot version/parent

**To Answer Your Question Directly:**
> "Could you check whether the REST is implemented the same as in java2-lab08 folder?"

**Answer: NO**, the implementations are significantly different after the refactoring:

1. **Dependencies** - Missing springdoc-openapi, Thymeleaf
2. **Injection Style** - @RequiredArgsConstructor vs @Autowired
3. **Spring Boot** - Different parent POM structure
4. **Configuration** - More complex in DK-DB
5. **REST endpoints** - Different paths and response styles

Would you like me to restore the Lab08 style or enhance the current version?

