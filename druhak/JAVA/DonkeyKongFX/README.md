# DonkeyKongFX - Multi-Module JavaFX Game

> **⚠️ IMPORTANT:** This is a **modular JavaFX application**. Do NOT use `java -jar` - it won't work with JavaFX modules!
> 
> **✅ CORRECT WAY TO RUN:**
> ```bash
> ./run.sh
> ```
> 
> **From IntelliJ Terminal (Alt+F12):**
> ```bash
> ./run-from-intellij.sh
> ```
> 
> See [JAVAFX_FIX.md](./JAVAFX_FIX.md) if you get "JavaFX runtime components are missing" error.

A multi-module Java 21 project implementing Donkey Kong using JavaFX, with modular architecture and database persistence.

## Quick Start

```bash
./run.sh
```

That's it! The script will build and run the application with proper module path configuration.

## Project Structure

```
├── donkeykong-api/       - Public APIs and interfaces
├── donkeykong-db/        - Database module (H2 + Score persistence)
├── donkeykong-game/      - Main game application
└── pom.xml               - Parent POM (multi-module coordination)
```

## Building

```bash
# Full build
mvn clean package -DskipTests

# Single module (example)
mvn clean package -DskipTests -pl donkeykong-game
```

## Running

### ✅ Option 1: Using run.sh (RECOMMENDED)
```bash
./run.sh
```

This automatically builds and runs with the correct module path.

### ✅ Option 2: From IntelliJ Terminal
```bash
# Press Alt+F12 to open terminal, then:
./run-from-intellij.sh
```

### ✅ Option 3: Using JavaFX Maven Plugin
```bash
cd donkeykong-game
mvn javafx:run
```

### ✅ Option 4: Manual with Module Path
```bash
# Build first
mvn clean package -DskipTests

# Run with module path
java --module-path donkeykong-api/target/classes:donkeykong-db/target/classes:donkeykong-game/target/classes:donkeykong-game/target/libs/* \
     --add-modules javafx.controls,javafx.fxml \
     --module cs.vsb.cz.fei.java2.game/vsb.cz.fei.donkeykongfx.App
```

### ❌ WRONG: Don't Use JAR Files!

```bash
# ❌ THIS WILL FAIL with "JavaFX runtime components are missing"
java -jar donkeykong-game/target/donkeykong-game-1.0-SNAPHOST-jar-with-dependencies.jar
```

**Why?** JavaFX uses Java Platform Module System (JPMS). Modular JavaFX apps **cannot run from JAR files**. You must use module path.

See [JAVAFX_FIX.md](./JAVAFX_FIX.md) for details.

## Documentation

- **[BUILD_AND_RUN.md](./BUILD_AND_RUN.md)** - Detailed build and run instructions
- **[SOLUTION_SUMMARY.md](./SOLUTION_SUMMARY.md)** - Overview of all fixes applied
- **[FIXES_APPLIED.md](./FIXES_APPLIED.md)** - Technical details of each fix
- **[MODULE_RESOLUTION.md](./MODULE_RESOLUTION.md)** - Explanation of Java module system
- **[COMPLETION_CHECKLIST.md](./COMPLETION_CHECKLIST.md)** - Verification checklist

## System Requirements

- **Java**: 21 or higher
- **Maven**: 3.6 or higher
- **OS**: Linux, macOS, or Windows

## Features

- ✅ Multi-module Maven architecture
- ✅ Java 21 module system
- ✅ JavaFX GUI
- ✅ H2 Database with score persistence
- ✅ Modular design with clear separation of concerns
- ✅ FAT JAR distribution (all-in-one deployment)

## Architecture

### Module Dependencies
```
donkeykong-game (Main App)
  └── requires: donkeykong-db
      └── requires: donkeykong-api
          └── (public API module)
```

### Key Classes
- `vsb.cz.fei.donkeykongfx.App` - Application entry point
- `cs.vsb.cz.fei.java2.db.score.ScoreRepository` - Score persistence
- Game controllers and game logic in donkeykong-game module

## Build Artifacts

After `mvn clean package`:

```
donkeykong-game/target/
├── donkeykong-game-1.0-SNAPHOST.jar
└── donkeykong-game-1.0-SNAPHOST-jar-with-dependencies.jar  ← Use this one!
```

The **jar-with-dependencies** file is the recommended distribution (FAT JAR with everything included).

## Troubleshooting

### Build fails
```bash
# Clean and rebuild
mvn clean package -DskipTests -U
```

### Module not found error
Make sure you're using the FAT JAR or have all modules on the module path.

### JavaFX not loading
Ensure Java 21+ is installed and set as JAVA_HOME.

## Development

To contribute:
1. Modify source files
2. Run `mvn clean package -DskipTests`
3. Test with `java -jar donkeykong-game/target/...jar-with-dependencies.jar`

## CI/CD Integration

For Kelvin automated testing:
```bash
mvn compile -f donkeykong-game/pom.xml
```

The build automatically handles module resolution through the parent POM.

## License

School project - VŠB-TUO

## Contact

Project Structure:
- `/home/jan/Documents/school/druhak/JAVA/DonkeyKongFX`

