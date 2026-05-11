#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
# Build runtime classpath for all runtime dependencies
mvn -q -pl donkeykong-game -DincludeScope=runtime dependency:build-classpath -Dmdep.outputFile=/tmp/donkeykong-game.cp
CP=$(cat /tmp/donkeykong-game.cp)
echo "Starting donkeykong-game as module (module-path run)..."
"${JAVA_HOME:-/home/jan/.jdks/openjdk-25.0.2}/bin/java" --module-path "donkeykong-game/target/classes:${CP}" -m vsb.cz.fei.donkeykongfx/vsb.cz.fei.donkeykongfx.App

