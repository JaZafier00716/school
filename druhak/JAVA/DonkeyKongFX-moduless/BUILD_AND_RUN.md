# DonkeyKongFX - Maven Build and Run Instructions

## Prerequisites
- Java 21 or higher
- Maven 3.6+ installed and in PATH

## Building the Project

To build the project and create the JAR file with all dependencies:

```bash
mvn clean package
```

This will:
1. Compile the source code
2. Run tests (use `-DskipTests` to skip)
3. Create `target/DonkeyKongFX-0.0.1-SNAPHOST.jar`
4. Copy all dependencies to `target/libs/`
5. Create a FAT JAR at `target/DonkeyKongFX-0.0.1-SNAPHOST-jar-with-dependencies.jar`

## Running the Application

### Option 1: Using the run scripts (recommended)

**Linux/Mac:**
```bash
./run.sh
```

**Windows:**
```cmd
run.bat
```

### Option 2: Manual execution

**Using the regular JAR with dependencies in libs:**
```bash
java --enable-native-access=javafx.graphics --module-path target/libs --add-modules javafx.controls,javafx.fxml -jar target/DonkeyKongFX-0.0.1-SNAPHOST.jar
```

**Using the FAT JAR (all dependencies included):**
```bash
java --enable-native-access=javafx.graphics --module-path target/DonkeyKongFX-0.0.1-SNAPHOST-jar-with-dependencies.jar --add-modules javafx.controls,javafx.fxml -m javafx.graphics/vsb.cz.fei.donkeykongfx.App
```

Or simply:
```bash
java -jar target/DonkeyKongFX-0.0.1-SNAPHOST-jar-with-dependencies.jar
```

## Maven Plugins Used

- **maven-compiler-plugin** (3.13.0) - Compiles Java source code with Java 21
- **maven-surefire-plugin** (3.0.0-M7) - Runs unit tests
- **maven-jar-plugin** (3.4.2) - Creates the main JAR file with MANIFEST.MF
- **maven-dependency-plugin** (3.8.1) - Copies all dependencies to target/libs
- **maven-assembly-plugin** (3.6.0) - Creates FAT JAR with all dependencies included

## Main Class

The main entry point is: `vsb.cz.fei.donkeykongfx.App`

## Project Structure

```
DonkeyKongFX/
├── pom.xml                  # Maven configuration
├── run.sh                   # Linux/Mac run script
├── run.bat                  # Windows run script
├── src/
│   └── main/
│       ├── java/            # Java source files
│       └── resources/       # FXML, CSS, images, fonts
└── target/                  # Build output (created after mvn package)
    ├── DonkeyKongFX-0.0.1-SNAPHOST.jar
    ├── DonkeyKongFX-0.0.1-SNAPHOST-jar-with-dependencies.jar
    └── libs/                # Dependencies copied here
```

## Troubleshooting

If you get "command not found: mvn", you need to:
1. Install Maven: https://maven.apache.org/install.html
2. Add Maven to your PATH environment variable

If the application doesn't start, make sure:
1. You have Java 21 or higher installed
2. You've built the project first with `mvn clean package`
3. The JAR file and libs directory exist in the target folder
4. You run with `--enable-native-access=javafx.graphics` on Java 21+
