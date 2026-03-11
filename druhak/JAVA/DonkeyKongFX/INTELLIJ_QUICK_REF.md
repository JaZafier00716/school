# 📋 IntelliJ IDEA Run Configuration - Quick Reference

## ✅ Configuration Already Created!

The run configuration is ready at:
```
.idea/runConfigurations/DonkeyKong.xml
```

**How to use:**
1. Open IntelliJ IDEA
2. Select **"DonkeyKong"** from Run dropdown
3. Click **Run** button (▶️)

Done! No scripts needed! 🎮

---

## If You Need to Create It Manually

### Quick Copy-Paste

**Name:**
```
DonkeyKong
```

**Main Class:**
```
vsb.cz.fei.donkeykongfx.App
```

**Module:**
```
donkeykong-game
```

**VM Options (COPY THIS ENTIRE LINE):**
```
--module-path donkeykong-api/target/classes:donkeykong-db/target/classes:donkeykong-game/target/classes:donkeykong-game/target/libs/* --add-modules javafx.controls,javafx.fxml
```

**Working Directory:**
```
$PROJECT_DIR$
```

**JRE:**
```
/home/jan/.jdks/openjdk-25.0.2
```

---

## Where to Enter These Values

```
Run → Edit Configurations → + → Application

┌─────────────────────────────────────────┐
│ Name: [DonkeyKong                    ] │
│ Module: [donkeykong-game           ▼] │
│ Main class: [vsb.cz.fei.donkeykongfx.App [...]] │
│ VM options: [--module-path donkeykong-api/...  ] │
│ Working dir: [$PROJECT_DIR$         [...]] │
│ JRE: [openjdk-25.0.2               ▼] │
│                                         │
│ Before launch:                          │
│   ✓ Build 'donkeykong-game'            │
└─────────────────────────────────────────┘
```

---

## Important!

### The VM Options MUST Include Both:

1. ✅ `--module-path` (where to find modules)
2. ✅ `--add-modules` (which JavaFX modules to load)

### Full VM Options Line:
```
--module-path donkeykong-api/target/classes:donkeykong-db/target/classes:donkeykong-game/target/classes:donkeykong-game/target/libs/* --add-modules javafx.controls,javafx.fxml
```

---

## Troubleshooting

### Problem: Configuration not appearing

**Solution:**
```
File → Invalidate Caches → Invalidate and Restart
```

### Problem: "Module not found" error

**Solution:** Check VM options include `--module-path` with all three modules

### Problem: "JavaFX components missing"

**Solution:** Check VM options include `--add-modules javafx.controls,javafx.fxml`

---

## More Help

- See: `INTELLIJ_RUN_CONFIG_GUIDE.md` for detailed step-by-step
- See: `INTELLIJ_COPY_PASTE_CONFIG.md` for all values
- Or just use: `./run.sh` if configuration doesn't work

---

**✅ The configuration file is already created and ready to use!**

Select "DonkeyKong" and click Run! ▶️

