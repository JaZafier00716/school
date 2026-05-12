# JetBrains IDE Run Configuration Setup

## Overview
This guide sets up JetBrains IntelliJ IDEA to run both services (Database Service on 8082 and Web Service on 8080) using play buttons.

---

## ✅ Option 1: Zero-Configuration (Recommended)

### 1. Create Compound Run Configuration (Automatic Setup)

1. **Open Run → Edit Configurations** (or `Shift+Alt+F10` → Edit Configurations)
2. **Click `+` → JavaScript Debug** (or **Application** for pure Java)
3. **For Database Service**:
   - Name: `DonkeyKong DB Service`
   - Main class: `org.springframework.boot.loader.JarLauncher`
   - VM options: `-Dspring.profiles.active=dev`
   - Working directory: `$PROJECT_DIR$/donkeykong-db`
   - Module classpath: `donkeykong-db`
   - Click ✅ (Apply)

4. **For Web Service**:
   - Name: `DonkeyKong Web Service`
   - Main class: `org.springframework.boot.loader.JarLauncher`
   - VM options: `-Dspring.profiles.active=dev -DDONKEYKONG_DB_URL=http://localhost:8082`
   - Working directory: `$PROJECT_DIR$/donkeykong-web`
   - Module classpath: `donkeykong-web`
   - Click ✅ (Apply)

5. **Create Compound Configuration**:
   - Click `+` → **Compound**
   - Name: `DonkeyKong - All Services`
   - Configurations:
     - ✅ DonkeyKong DB Service
     - ✅ DonkeyKong Web Service
   - Click ✅ (OK)

---

## ✅ Option 2: Automatic Spring Boot Run Configurations

### Step 1: Enable Spring Boot Auto-Configuration
1. Open **File → Settings → Plugins**
2. Search for **Spring Boot** plugin → Ensure enabled
3. Click **OK**

### Step 2: IntelliJ Auto-Detects Spring Boot Modules
1. Right-click on `donkeykong-db` module
2. Select **Run 'DonkeyKongDatabaseServiceApplication'** or similar
3. Copy & create named run configuration "DonkeyKong DB Service"
4. Repeat for `donkeykong-web`

### Step 3: Configuration Details
Both modules should have Spring Boot auto-detected configs with:
- **VM options**: `-Dspring.config.location=classpath:application.yaml`
- **Environment variables**: `DONKEYKONG_DB_URL=http://localhost:8082`
- **Working directory**: Module root

---

## ✅ Option 3: Maven Configurations (Simplest)

If Spring Boot plugin detection doesn't work:

### Database Service Configuration
1. **Run → Edit Configurations → +Maven**
2. **Name**: `DonkeyKong DB Service`
3. **Working directory**: `$PROJECT_DIR$/donkeykong-db`
4. **Command line**: `spring-boot:run -DSPRING_CONFIG_LOCATION=classpath:application.yaml`
5. **Environment variables**: `DONKEYKONG_DB_URL=http://localhost:8082`
6. Click ✅

### Web Service Configuration  
1. **Run → Edit Configurations → +Maven**
2. **Name**: `DonkeyKong Web Service`
3. **Working directory**: `$PROJECT_DIR$/donkeykong-web`
4. **Command line**: `spring-boot:run -DSPRING_CONFIG_LOCATION=classpath:application.yaml`
5. **Environment variables**: `DONKEYKONG_DB_URL=http://localhost:8082`
6. Click ✅

### Run Both Together
1. **Run → Edit Configurations → +Compound**
2. **Name**: `DonkeyKong - All Services`
3. Add both Maven configurations
4. Click ✅

---

## 🚀 Running the Project

### Method 1: Run All Services (Recommended)
1. From toolbar dropdown, select: **DonkeyKong - All Services** (or your Compound name)
2. Click **Green Play ▶️ button**
3. Services start automatically in sequence

### Method 2: Run Individual Services
1. From toolbar dropdown, select: **DonkeyKong DB Service**
2. Click **Green Play ▶️ button**
3. Wait for message: `Tomcat started on port(s): 8082`
4. From toolbar dropdown, select: **DonkeyKong Web Service**
5. Click **Green Play ▶️ button**
6. Wait for message: `Tomcat started on port(s): 8080`

### Method 3: Debug Mode
- Click **Bug 🐞 icon** instead of Play button to run with debugger

---

## ✅ Verification

After starting services:

1. **Check Database Service**:
   - Open: http://localhost:8082/swagger-ui/index.html
   - Should see Swagger API documentation
   - Try: GET /api/v1/game-results

2. **Check Web Service**:
   - Open: http://localhost:8080
   - Should see High Scores page
   - Navigate to "🎯 Game Results" tab

3. **Check Integration**:
   - Create a game result via http://localhost:8082/swagger-ui/index.html
   - Verify it appears in http://localhost:8080/ui/game-results

---

## 🔧 Advanced: Custom Environment Setup

### If You Want No VM Options Added
The applications use default profiles:
- Database Service: Reads from `donkeykong-db/src/main/resources/application.yaml`
- Web Service: Reads from `donkeykong-web/src/main/resources/application.yaml`
- No VM options needed (already configured)

### If You Want Custom Ports
Edit the respective `application.yaml`:

**donkeykong-db/src/main/resources/application.yaml**:
```yaml
server:
  port: 8082  # Change here
```

**donkeykong-web/src/main/resources/application.yaml**:
```yaml
server:
  port: 8080  # Change here
```

---

## 🚨 Troubleshooting

### Configuration Doesn't Appear
1. Go to **File → Invalidate Caches and Restart**
2. Choose **Invalidate and Restart**
3. Wait for IDE to restart
4. Open Run Configurations again

### Port Already in Use
1. Kill existing Java processes: `pkill -f java`
2. Or change ports in `application.yaml` files
3. Update `DONKEYKONG_DB_URL` to new port if changed

### Spring Boot Plugin Not Auto-Detecting
1. Install Spring Boot plugin: **File → Settings → Plugins → Search "Spring Boot"**
2. Or use Maven configuration (Option 3) instead

### Services Don't Start
1. Check Maven build succeeds: **Build → Build Project**
2. Verify no compilation errors in IDE
3. Check console for error messages
4. Ensure Java 25 installed: `Settings → Project Settings → SDK`

---

## 📁 File Locations for Run Configs

If you want to version control your run configs:

- **Stored in**: `.idea/runConfigurations/`
- **Format**: XML files
- **Example**: `.idea/runConfigurations/DonkeyKong_All_Services.xml`

To share with team: Commit these XML files to Git

---

## ⚡ Keyboard Shortcuts

| Action | Shortcut |
|---|---|
| Open Run Configurations | `Shift+Alt+F10` |
| Run Current Configuration | `Shift+F10` |
| Debug Current Configuration | `Shift+F9` |
| Stop Running Process | `Ctrl+F2` |
| Toggle Between Configs | `Alt+1` then `Alt+2` (in Run tool window) |

---

## 🎯 Quick Start Summary

1. **Open**: Run → Edit Configurations
2. **Create** two Spring Boot/Maven run configs (DB + Web)
3. **Create** Compound configuration with both
4. **Select** Compound config from dropdown
5. **Click** Green Play ▶️ button
6. **Done!** Services run in IDE with debug support

---

## 📚 Additional Resources

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **IntelliJ Run Configurations**: https://www.jetbrains.com/help/idea/debug-code.html
- **Project Context**: See `PROJECT_CONTEXT.md` for architecture details

---

**Status**: Ready for play button execution ✅


