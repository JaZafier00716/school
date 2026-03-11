#!/bin/bash

# Build script for DonkeyKongFX
# This ensures all modules are compiled and available before running

set -e  # Exit on error

echo "Building DonkeyKongFX multi-module project..."

# Use Java 21
export JAVA_HOME=/home/jan/.jdks/openjdk-25.0.2
export PATH="$JAVA_HOME/bin:$PATH"

cd "$(dirname "$0")"

# Build all modules
mvn clean package -DskipTests

echo ""
echo "Build complete!"
echo ""
echo "To run the application, use one of these methods:"
echo ""
echo "1. Using the assembly JAR (recommended):"
echo "   java -jar donkeykong-game/target/donkeykong-game-1.0-SNAPHOST-jar-with-dependencies.jar"
echo ""
echo "2. Using module path with individual modules:"
echo "   java --module-path donkeykong-api/target/donkeykong-api-1.0-SNAPSHOT.jar:donkeykong-db/target/donkeykong-db-1.0-SNAPSHOT.jar:donkeykong-game/target/donkeykong-game-1.0-SNAPHOST.jar:donkeykong-game/target/dependency/* \\"
echo "        --add-modules javafx.controls,javafx.fxml \\"
echo "        -m cs.vsb.cz.fei.java2.game/vsb.cz.fei.donkeykongfx.App"
echo ""
echo "3. Using the local Maven repository:"
echo "   java -p ~/.m2/repository/cs/vsb/cz/fei/java2/api/donkeykong-api/1.0-SNAPSHOT/donkeykong-api-1.0-SNAPSHOT.jar:\\"
echo "        ~/.m2/repository/cs/vsb/cz/fei/java2/db/donkeykong-db/1.0-SNAPSHOT/donkeykong-db-1.0-SNAPSHOT.jar:\\"
echo "        ~/.m2/repository/cs/vsb/cz/fei/java2/game/donkeykong-game/1.0-SNAPHOST/donkeykong-game-1.0-SNAPHOST.jar:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout -f donkeykong-game/pom.xml) \\"
echo "        --add-modules javafx.controls,javafx.fxml \\"
echo "        -m cs.vsb.cz.fei.java2.game/vsb.cz.fei.donkeykongfx.App"

