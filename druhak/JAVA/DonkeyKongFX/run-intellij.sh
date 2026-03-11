#!/bin/bash

# IntelliJ Run Configuration Fixer
# This script helps diagnose and fix module path issues when running from IntelliJ

set -e

export JAVA_HOME=/home/jan/.jdks/openjdk-25.0.2
export PATH="$JAVA_HOME/bin:$PATH"

cd "$(dirname "$0")"

echo "=========================================="
echo "DonkeyKongFX - IntelliJ Run Configuration"
echo "=========================================="
echo ""

# Check if FAT JAR exists
if [[ ! -f "donkeykong-game/target/donkeykong-game-1.0-SNAPHOST-jar-with-dependencies.jar" ]]; then
    echo "❌ FAT JAR not found!"
    echo "Building project..."
    mvn clean package -DskipTests -q
    echo "✅ Build complete"
fi

echo ""
echo "=========================================="
echo "Running via FAT JAR (Recommended)"
echo "=========================================="
echo ""
echo "Execute this command in IntelliJ terminal:"
echo ""
echo "java -jar donkeykong-game/target/donkeykong-game-1.0-SNAPHOST-jar-with-dependencies.jar"
echo ""
echo "Or run from Run menu: Run → Edit Configurations → Add 'Application'"
echo "  - Main class: vsb.cz.fei.donkeykongfx.App"
echo "  - Module: donkeykong-game"
echo "  - VM options: --add-modules javafx.controls,javafx.fxml"
echo ""
echo "=========================================="
echo "Starting application..."
echo "=========================================="
echo ""

java -jar donkeykong-game/target/donkeykong-game-1.0-SNAPHOST-jar-with-dependencies.jar

