#!/bin/bash

# DonkeyKongFX - IntelliJ Run Fix
# This script runs the modular JavaFX application with proper module path
# Use this when IntelliJ's Run button gives "Module not found" error

set -e

export JAVA_HOME=/home/jan/.jdks/openjdk-25.0.2
export PATH="$JAVA_HOME/bin:$PATH"

cd "$(dirname "$0")"

echo "=========================================="
echo "  DonkeyKongFX - IntelliJ Run Fix"
echo "=========================================="
echo ""
echo "Running modular JavaFX application with proper module path"
echo ""

# Check if modules are built
if [[ ! -f "donkeykong-game/target/classes/module-info.class" ]]; then
    echo "⚠️  Modules not built. Building now..."
    echo ""
    mvn clean package -DskipTests
    echo ""
    echo "✅ Build complete!"
    echo ""
fi

echo "=========================================="
echo "  Starting DonkeyKongFX..."
echo "=========================================="
echo ""

# Build the module path
MODULE_PATH="donkeykong-api/target/classes"
MODULE_PATH="${MODULE_PATH}:donkeykong-db/target/classes"
MODULE_PATH="${MODULE_PATH}:donkeykong-game/target/classes"

# Add all dependency JARs
for jar in donkeykong-game/target/libs/*.jar; do
    MODULE_PATH="${MODULE_PATH}:${jar}"
done

# Run the modular JavaFX application
java --module-path "${MODULE_PATH}" \
     --add-modules javafx.controls,javafx.fxml \
     --module cs.vsb.cz.fei.java2.game/vsb.cz.fei.donkeykongfx.App

echo ""
echo "Application closed."

