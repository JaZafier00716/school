#!/bin/bash
set -e

# DonkeyKongFX Multi-Module Project Runner
# Runs the JavaFX application with proper module path configuration

export JAVA_HOME=/home/jan/.jdks/openjdk-25.0.2
export PATH="$JAVA_HOME/bin:$PATH"

# Run from project root (script location)
cd "$(dirname "$0")"

echo "=========================================="
echo "Building DonkeyKongFX (multi-module)"
echo "=========================================="
mvn clean package -DskipTests -q

echo ""
echo "=========================================="
echo "Starting application..."
echo "=========================================="

# Build the module path with all required modules and dependencies
MODULE_PATH="donkeykong-api/target/classes"
MODULE_PATH="${MODULE_PATH}:donkeykong-db/target/classes"
MODULE_PATH="${MODULE_PATH}:donkeykong-game/target/classes"

# Add all dependency JARs from libs directory
for jar in donkeykong-game/target/libs/*.jar; do
    MODULE_PATH="${MODULE_PATH}:${jar}"
done

echo "Module path configured with all dependencies"
echo ""

# Run the modular JavaFX application
java --module-path "${MODULE_PATH}" \
     --add-modules javafx.controls,javafx.fxml \
     --module cs.vsb.cz.fei.java2.game/vsb.cz.fei.donkeykongfx.App
