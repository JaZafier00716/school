# DiscGolf Tracker (Qt, C++23)

This repository now contains a C++23 rewrite of the original Python UI project using Qt Widgets.

## What is implemented

- Course screen with:
  - current hole + score
  - par + distance display
  - throw counter (+ / -)
  - previous / next hole navigation
  - finish round action
- Settings screen with:
  - nickname editing (max 10 chars)
  - dark mode toggle
  - metric/imperial unit switch
- Round summary screen with:
  - total par, total throws, average throws
  - hole-by-hole table
  - save/delete demo actions
- A small model smoke test via CTest.

## Build (Linux/macOS)

Prerequisites:

- CMake 3.21+
- C++23 compiler
- Qt6 Widgets development package

```bash
cmake -S . -B build -DCMAKE_BUILD_TYPE=Release
cmake --build build -j
ctest --test-dir build --output-on-failure
```

Run:

```bash
./build/discgolf_tracker
```

## Project layout

- `include/models.hpp` - core app state and scoring logic
- `include/course_window.hpp` / `src/course_window.cpp` - main course screen
- `include/settings_dialog.hpp` / `src/settings_dialog.cpp` - settings dialog
- `include/round_summary_dialog.hpp` / `src/round_summary_dialog.cpp` - summary dialog
- `include/theme_manager.hpp` / `src/theme_manager.cpp` - dark/light theme setup
- `tests/model_smoke.cpp` - basic model-level test

## Notes

- Legacy Python files are kept in the repo for reference during migration.
- Save/Delete actions in summary are placeholders in this rewrite.

