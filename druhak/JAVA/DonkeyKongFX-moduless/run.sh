#!/bin/bash

# Script to run DonkeyKongFX application
# Make sure you've built the project first with: mvn clean package

JAR_FILE="target/DonkeyKongFX-0.0.1-SNAPHOST.jar"
LIBS_DIR="target/libs"

# Check if JAR file exists
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Please build the project first with: mvn clean package"
    exit 1
fi

# Check if libs directory exists
if [ ! -d "$LIBS_DIR" ]; then
    echo "Error: libs directory not found at $LIBS_DIR"
    echo "Please build the project first with: mvn clean package"
    exit 1
fi

# Allow user-provided JVM options and enable JavaFX native access for Java 21+
JAVA_OPTS=${JAVA_OPTS:-}

# Run the application with JavaFX modules on module path
java $JAVA_OPTS --enable-native-access=javafx.graphics --module-path "$LIBS_DIR" --add-modules javafx.controls,javafx.fxml -jar "$JAR_FILE"
