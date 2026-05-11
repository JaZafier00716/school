#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mvn -q -pl donkeykong-web -DincludeScope=runtime dependency:build-classpath -Dmdep.outputFile=/tmp/donkeykong-web.cp
CP=$(cat /tmp/donkeykong-web.cp)
echo "Starting donkeykong-web with classpath..."
"${JAVA_HOME:-/home/jan/.jdks/openjdk-25.0.2}/bin/java" -cp "${CP}:donkeykong-web/target/classes" cz.vsb.fei.DonkeyKongFX.DonkeyKongFxApplication

