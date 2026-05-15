# DonkeyKongFX Multi-Service Project

This repository contains a Maven multi-module Donkey Kong project split into
three application subprojects:

- `donkeykong-game` - JavaFX desktop game client
- `donkeykong-web` - Spring Boot web UI and REST gateway
- `donkeykong-db` - Spring Boot persistence service backed by H2/JPA

The main runtime flow is:

1. The player runs the JavaFX game from `donkeykong-game`.
2. When a score is loaded or saved, the game calls `donkeykong-web`.
3. `donkeykong-web` forwards game-result requests to `donkeykong-db`.
4. `donkeykong-db` stores and reads game results from the H2 database.

Default ports:

- Database service: `http://localhost:8080`
- Web service: `http://localhost:8081`
- Game score API target: `http://localhost:8081/api/v1/game-results`

## Repository Structure

```text
DonkeyKongFX-moduless/
├── donkeykong-db/      # Database REST service and JPA persistence
├── donkeykong-web/     # Web UI and REST proxy/gateway
├── donkeykong-game/    # JavaFX game client
├── db/                 # H2 database files
├── pom.xml             # Parent Maven aggregator
├── PROJECT_CONTEXT.md  # Extra architecture/lab notes
└── IDE_RUN_CONFIGURATION.md
```

The root `pom.xml` aggregates the three modules. `donkeykong-game` inherits
from the root parent POM. `donkeykong-db` and `donkeykong-web` use their own
Spring Boot parent POMs, so the root POM is mainly an aggregator for them.

## Module Responsibilities

### `donkeykong-db`

`donkeykong-db` is the persistent backend service. It owns the JPA entities,
repositories, and database-facing REST API.

Responsibilities:

- Store players, game levels, and game results.
- Expose CRUD endpoints for game results.
- Expose read endpoints for players and levels.
- Create missing `Player` and `GameLevel` records when results are saved.
- Persist data to an H2 file database at `./db/score-db` by default.

Important files:

- `DonkeykongDbApplication.java` - Spring Boot entry point. Starts on port
  `8080` by default and falls back upward if the port is busy.
- `entity/Player.java` - JPA entity for a player. Has a unique `name` and a
  one-to-many relationship with `GameResult`.
- `entity/GameLevel.java` - JPA entity for a level. Has a unique
  `levelNumber`, level name, and a one-to-many relationship with `GameResult`.
- `entity/GameResult.java` - JPA entity for one played game. Stores score,
  date/time, duration, deaths, and relations to `Player` and `GameLevel`.
- `repository/PlayerRepository.java` - Spring Data repository for player
  lookup and sorted listing.
- `repository/GameLevelRepository.java` - Spring Data repository for level
  lookup and sorted listing.
- `repository/GameResultRepository.java` - Spring Data repository for result
  queries such as all results, last 10 results, and player-specific results.
- `controller/GameResultController.java` - Main DB REST API under
  `/api/v1/game-results`.
- `controller/PlayerController.java` - Player API under `/api/v1/players`,
  including computed stats.
- `controller/GameLevelController.java` - Level API under
  `/api/v1/game-levels`.
- `src/main/resources/application.yaml` - DB service config, H2 file database,
  H2 console, and JPA `ddl-auto: update`.

### `donkeykong-web`

`donkeykong-web` is the web-facing layer. It does not own persistence. It
renders Thymeleaf pages and proxies REST calls to `donkeykong-db`.

Responsibilities:

- Provide a browser dashboard.
- Provide a game-results table with search, sorting, and delete actions.
- Expose a REST API for game clients.
- Forward REST calls to the DB service with `RestTemplate`.
- Provide language switching for the web UI.

Important files:

- `DonkeyKongFxApplication.java` - Spring Boot entry point. Starts on port
  `8081` by default and falls back upward if the port is busy.
- `config/RestTemplateConfig.java` - Creates the `RestTemplate` bean used for
  DB-service calls.
- `config/LocaleConfig.java` - Enables `?lang=...` language switching and
  stores the selected language in a cookie.
- `controller/GameResultController.java` - REST proxy under
  `/api/v1/game-results`. This is the endpoint the JavaFX game calls.
- `controller/DashboardUIController.java` - Renders the dashboard page and
  computes summary statistics from game results.
- `controller/GameResultUIController.java` - Renders the game-results table,
  sorting, filtering, and delete actions.
- `entity/GameResult.java` - DTO mirroring the game-result response from the
  DB service. It is not a JPA entity in this module.
- `templates/index.html` - Dashboard page.
- `templates/game-results.html` - Results table page.
- `static/css/donkeykong.css` - Web UI styling.
- `messages*.properties` - English, Czech, and German translations.
- `src/main/resources/application.yaml` - Web service config and DB base URL.
  The DB URL defaults to `http://localhost:8080` and can be overridden with
  `DONKEYKONG_DB_URL`.

### `donkeykong-game`

`donkeykong-game` is the JavaFX desktop game. It owns gameplay, rendering,
local settings, local save-state, and score submission.

Responsibilities:

- Start and switch between menu, game, and settings screens.
- Render the game using JavaFX canvas and sprite assets.
- Manage player input, keybindings, language, pause menu, and game-over menu.
- Simulate the level, enemies, collisions, scoring, lives, and win/loss state.
- Save and load local game state from `state.bin`.
- Load and save scores through the web service REST API.

Important top-level files:

- `App.java` - JavaFX entry point. Loads language, initializes score client,
  and switches between scenes.
- `DrawingThread.java` - Wraps JavaFX `AnimationTimer` and calls rendering each
  frame.
- `RenderHandler.java` - Callback interface used by `DrawingThread`.
- `GameState.java` - Serializable save-file model used for `state.bin`.
- `I18n.java` - Loads the `messages*.properties` resource bundles.
- `IntDimension2D.java` - Small width/height record.
- `module-info.java` - Java module descriptor for JavaFX, HTTP, Jackson,
  Lombok, and Log4j access.

Game controllers:

- `controllers/ResizableController.java` - Shared JavaFX controller base with
  app reference, timer management, canvas resizing, FPS drawing, and alerts.
- `controllers/ResizableDimension.java` - Stores current dimensions and scale.
- `controllers/SettingsAffected.java` - Adds keybinding load/save support to
  controllers.
- `controllers/MenuController.java` - Main menu, player name input,
  leaderboard, continue button, and animated Donkey Kong preview.
- `controllers/GameController.java` - Gameplay screen, keyboard input, pause,
  game over, save/load/delete state, and score saving on win.
- `controllers/SettingsController.java` - Keybinding editor and language
  selector.

Game world:

- `levels/Level.java` - Main game-world class. Creates the level, player,
  platforms, ladders, tokens, Donkey Kong, enemies, rendering order,
  collisions, spawning, pause handling, save/restore, and game-over events.

Game object base classes:

- `gameobjects/Renderable.java` - Common render/update interface.
- `gameobjects/RenderableObject.java` - Base position/animation/sprite drawing
  logic.
- `gameobjects/Collisionable.java` - Collision bounds and collision callback.
- `gameobjects/GameObject.java` - Renderable object with collision and removal
  state.
- `gameobjects/MovableGameObject.java` - Velocity, direction, gravity, ladder,
  platform, jumping, and grounding behavior.
- `gameobjects/MovableType.java` - Movement physics profile and movement
  application.
- `gameobjects/AnimationData.java` - Loads sprite sheets and calculates frame
  sizes.
- `gameobjects/AutonomousEntity.java` - Interface for objects with their own
  behavior thread.

Static and collectible objects:

- `gameobjects/Door.java` - Renders the door.
- `gameobjects/Princess.java` - Win target object.
- `gameobjects/Token.java` - Collectible score token.
- `gameobjects/platform/Platform.java` - Tile platform and ladder entrance.
- `gameobjects/ladder/Ladder.java` - Climbable ladder rendering and bounds.
- `gameobjects/ladder/TileCoord.java` - Tile coordinate record.
- `gameobjects/staticbarrel/StaticBarrel.java` - Decorative barrel stack.

Entities:

- `gameobjects/entities/EntityState.java` - Serializable state snapshot for
  save/load.
- `gameobjects/entities/barrel/Barrel.java` - Barrel enemy with rolling,
  climbing, collision, and behavior-thread logic.
- `gameobjects/entities/barrel/BarrelState.java` - Barrel state enum.
- `gameobjects/entities/donkeykong/DonkeyKong.java` - Donkey Kong animation and
  enemy-spawn signaling.
- `gameobjects/entities/donkeykong/KongState.java` - Donkey Kong state enum.
- `gameobjects/entities/flamyboi/FlamyBoi.java` - Fire enemy that falls, lands,
  and moves.
- `gameobjects/entities/flamyboi/FlamyBoiState.java` - Fire enemy state enum.
- `gameobjects/entities/player/Player.java` - Player movement, animation,
  collisions, score, lives, death, climbing, respawn, and jump-over scoring.
- `gameobjects/entities/player/Health.java` - Renders lives and notifies
  listeners when the player dies.
- `gameobjects/entities/player/HealthListener.java` - Health event callback.
- `gameobjects/entities/player/PlayerEvent.java` - Player event record.
- `gameobjects/entities/player/PlayerEventType.java` - Player event enum.
- `gameobjects/entities/player/PlayerListener.java` - Player event callback.
- `gameobjects/entities/player/PlayerState.java` - Player animation/movement
  state enum.

Score and settings:

- `score/Score.java` - Game-side score DTO.
- `score/ScoreRestClient.java` - HTTP client that loads and saves scores
  through `donkeykong-web`.
- `score/ScoreException.java` - Score API error wrapper.
- `score/Utilities.java` - Random fallback nickname generation.
- `settings/KeyBindings.java` - Action-to-key mapping with duplicate-key
  checks.
- `settings/KeyBindingRow.java` - Table row model for the settings screen.
- `settings/KeyBindingsRepository.java` - Reads/writes `keybindings.cfg` and
  `settings.cfg`.
- `settings/KeyBindingsException.java` - Settings persistence error wrapper.

Game resources:

- `game.fxml` - Gameplay screen layout.
- `menu.fxml` - Main menu layout.
- `options.fxml` - Settings screen layout.
- `application.css` - JavaFX UI styling.
- `messages*.properties` - Game translations.
- `images/**` - Sprite and tile assets.
- `fonts/PressStart2P.ttf` - Pixel font.
- `log4j2.xml` - Game logging configuration.

## Running the Project

Recommended startup order:

1. Start `donkeykong-db`.
2. Start `donkeykong-web`.
3. Start `donkeykong-game`.

Useful URLs after startup:

- DB Swagger/OpenAPI UI: `http://localhost:8080/swagger-ui/index.html`
- Web dashboard: `http://localhost:8081`
- Web game results table: `http://localhost:8081/ui/game-results`
- Web REST API: `http://localhost:8081/api/v1/game-results`
- DB REST API: `http://localhost:8080/api/v1/game-results`

Environment variables:

- `DONKEYKONG_SCORE_DB_URL` - Overrides the DB service H2 JDBC URL.
- `DONKEYKONG_DB_URL` - Overrides the DB URL used by `donkeykong-web`.
- `DONKEYKONG_WEB_URL` - Overrides the web URL used by `donkeykong-game`.
- `SERVER_PORT` - Overrides Spring Boot service port.

## Local Files Created at Runtime

- `db/score-db.mv.db` - H2 database file for backend score data.
- `state.bin` - JavaFX game save-state file.
- `keybindings.cfg` - Local game keybindings.
- `settings.cfg` - Local game settings such as selected language.

## Mental Model

The easiest way to understand the project is:

- `donkeykong-game` is the playable client.
- `donkeykong-web` is the browser UI and REST gateway.
- `donkeykong-db` is the source of truth for persisted results.
- `Level.java` is the core game-world orchestrator.
- `donkeykong-db`'s `GameResultController` is the core persistence API.
- `donkeykong-web`'s `GameResultController` is the proxy API used by the game.
