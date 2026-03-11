# IntelliJ IDEA Run Configuration - Copy & Paste Values

## Quick Setup: Just Copy These Values

### Configuration Name
```
DonkeyKong
```

### Module
```
donkeykong-game
```

### Main Class
```
vsb.cz.fei.donkeykongfx.App
```

### VM Options (IMPORTANT - Copy this exactly)
```
--module-path donkeykong-api/target/classes:donkeykong-db/target/classes:donkeykong-game/target/classes:donkeykong-game/target/libs/* --add-modules javafx.controls,javafx.fxml
```

### Working Directory
```
$PROJECT_DIR$
```

### JRE Path
```
/home/jan/.jdks/openjdk-25.0.2
```

---

## Step-by-Step with Screenshots Descriptions

### Step 1: Open Run Configurations
- Location: **Run** menu → **Edit Configurations**
- Or: Click dropdown next to Run button → **Edit Configurations**

### Step 2: Add New Configuration
- Click **+** button (top left corner)
- Select **Application** from the list

### Step 3: Fill in Basic Settings

**Name field:**
- Enter: `DonkeyKong`

**Main class field:**
- Click the **...** button
- Search for: `vsb.cz.fei.donkeykongfx.App`
- Select it

**Module dropdown:**
- Select: `donkeykong-game`

### Step 4: Add VM Options

**First, enable VM options:**
- Click **Modify options** (top right)
- Check ✅ **Add VM options**

**VM options field appears - paste this:**
```
--module-path donkeykong-api/target/classes:donkeykong-db/target/classes:donkeykong-game/target/classes:donkeykong-game/target/libs/* --add-modules javafx.controls,javafx.fxml
```

### Step 5: Set Working Directory

**Working directory field:**
- Should already show: `$PROJECT_DIR$`
- If not, paste: `$PROJECT_DIR$`

### Step 6: Set JRE

**First, enable alternative JRE:**
- Click **Modify options** (if needed)
- Check ✅ **Use alternative JRE**

**JRE dropdown:**
- Select: `openjdk-25.0.2` (or your Java 21 installation)
- Or paste path: `/home/jan/.jdks/openjdk-25.0.2`

### Step 7: Add Build Step

**Before launch section (at bottom):**
- Click **+** button
- Select **Build** (not "Build project")
- Should show: "Build 'donkeykong-game'"

### Step 8: Save and Test

- Click **OK** button (bottom right)
- Configuration now appears in dropdown
- Click **Run** button (▶️) or press **Shift+F10**

---

## What Each Setting Does

| Setting | Purpose |
|---------|---------|
| **--module-path** | Tells Java where to find all modules (api, db, game) and dependencies |
| **--add-modules** | Explicitly adds JavaFX modules to the module graph |
| **Working directory** | Sets the base directory for file operations |
| **Build step** | Ensures code is compiled before running |

---

## Common Mistakes to Avoid

❌ **WRONG VM Options:**
```
--add-modules javafx.controls,javafx.fxml
```
(Missing `--module-path`)

✅ **CORRECT VM Options:**
```
--module-path donkeykong-api/target/classes:donkeykong-db/target/classes:donkeykong-game/target/classes:donkeykong-game/target/libs/* --add-modules javafx.controls,javafx.fxml
```

❌ **WRONG Configuration Type:**
- JAR Application
- Maven
- Shell Script

✅ **CORRECT Configuration Type:**
- Application

---

## Expected Result

After clicking Run (▶️):

1. ✅ Build starts automatically
2. ✅ Console shows: "Module cs.vsb.cz.fei.java2.game starting..."
3. ✅ JavaFX window opens
4. ✅ No "Module not found" error
5. ✅ No "JavaFX runtime components missing" error

---

## Already Configured!

I've already created the configuration file for you at:
```
.idea/runConfigurations/DonkeyKong.xml
```

**To use it:**
1. Open IntelliJ
2. Look at the Run configuration dropdown
3. You should see "DonkeyKong" configuration
4. Select it and click Run ▶️

**If you don't see it:**
- File → Invalidate Caches → Invalidate and Restart
- After restart, it should appear

---

## Alternative: Import from File

If manual setup is too complex, you can:

1. Make sure `.idea/runConfigurations/DonkeyKong.xml` exists
2. Restart IntelliJ IDEA
3. Configuration will be auto-imported
4. Select "DonkeyKong" from dropdown
5. Click Run ▶️

---

## Verification Checklist

Before clicking Run, verify:

- ✅ Configuration name is set
- ✅ Main class is `vsb.cz.fei.donkeykongfx.App`
- ✅ Module is `donkeykong-game`
- ✅ VM options include `--module-path` AND `--add-modules`
- ✅ Working directory is `$PROJECT_DIR$`
- ✅ JRE is Java 21+
- ✅ Before launch has "Build" step

---

## Summary

**Configuration is ready to use!**

Just select "DonkeyKong" from the Run dropdown and click ▶️

No scripts needed! 🎮

