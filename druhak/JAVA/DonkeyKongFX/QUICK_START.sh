#!/bin/bash

cat << 'EOF'

╔══════════════════════════════════════════════════════════════╗
║                                                              ║
║                  DONKEYKONGFX - QUICK START                  ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝

✅ CORRECT WAY TO RUN:

    ./run.sh

Or from IntelliJ Terminal (Alt+F12):

    ./run-from-intellij.sh


❌ WRONG (Don't use JAR files):

    java -jar donkeykong-game/target/*.jar


WHY?

JavaFX uses Java Platform Module System (JPMS).
Modular JavaFX applications CANNOT run from JAR files.
You MUST use module path (--module-path).

The scripts handle this automatically.


DOCUMENTATION:

- README.md - Main documentation
- JAVAFX_FIX.md - JavaFX error fix
- BUILD_AND_RUN.md - Detailed build guide


HAVING ISSUES?

1. Make sure Java 21 is installed
2. Run: mvn clean package -DskipTests
3. Then: ./run.sh


────────────────────────────────────────────────────────────────

EOF

# Ask if user wants to run now
read -p "Run the application now? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo ""
    echo "Starting DonkeyKongFX..."
    echo ""
    ./run.sh
fi

