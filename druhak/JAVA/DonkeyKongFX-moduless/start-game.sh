#!/usr/bin/env bash
# Launcher for donkeykong-game on Linux using the bundled libs directory
# Usage:
#   ./start-game.sh [<DONKEYKONG_WEB_URL>]
# If you prefer an external web gateway URL, pass it as the first argument or set env DONKEYKONG_WEB_URL

set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
LIBS="$ROOT_DIR/donkeykong-game/target/libs"
JAR="$ROOT_DIR/donkeykong-game/target/donkeykong-game-0.0.1-SNAPHOST-jar-with-dependencies.jar"

if [ ! -f "$JAR" ]; then
  echo "Game JAR not found: $JAR"
  echo "Build it with: mvn -pl donkeykong-game clean package -DskipTests"
  exit 1
fi

# If user passed a URL as the first arg, export it for the game to pick up
if [ "$#" -ge 1 ]; then
  export DONKEYKONG_WEB_URL="$1"
fi

# Default module list required by the application
MODULES="javafx.controls,javafx.fxml"

# Run with module-path pointing to the copied dependency libs
# Note: on Linux the javafx native jars with '-linux' classifier are present in the libs directory
echo "Starting DonkeyKong game..."
exec java --module-path "$LIBS" --add-modules "$MODULES" -jar "$JAR"

