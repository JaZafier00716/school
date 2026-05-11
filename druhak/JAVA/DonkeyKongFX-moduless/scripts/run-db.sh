#!/usr/bin/env bash
set -euo pipefail
# Build the project and run donkeykong-db on the classpath (non-modular run)
cd "$(dirname "$0")/.."
# build dependencies classpath
mvn -q -pl donkeykong-db -DincludeScope=runtime dependency:build-classpath -Dmdep.outputFile=/tmp/donkeykong-db.cp
CP=$(cat /tmp/donkeykong-db.cp)
echo "Starting donkeykong-db with classpath..."
"${JAVA_HOME:-/home/jan/.jdks/openjdk-25.0.2}/bin/java" -cp "${CP}:donkeykong-db/target/classes" cz.vsb.fei.donkeykong.DonkeykongDbApplication

