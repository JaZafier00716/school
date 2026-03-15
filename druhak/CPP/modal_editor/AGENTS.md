# AGENTS.md

## Project Scope
- This repository is an early-stage C++ modal editor prototype with two core domains: editor orchestration and text storage.
- Build target is a single executable: `modal_editor` (see `CMakeLists.txt`).
- Source layout is flat and minimal: public headers in `include/`, implementations in `src/`.

## Architecture Map (Current State)
- `GapBuffer` is the intended text-storage primitive (`include/gap_buffer.h`).
- `editor::BufferIndex` is defined in the same header and represents a typed index wrapper around `std::size_t`.
- `Editor` is currently a placeholder type with no members (`include/editor.h`).
- `src/gap_buffer.cpp` only contains `#include "gap_buffer.h"` and an empty `namespace editor {}` block.
- `src/editor.cpp` only includes `"Editor.h"` (note case mismatch with `include/editor.h`).

## Critical Build/Run Workflow
- CMake config is currently:
  - `cmake_minimum_required(VERSION 4.1)`
  - `set(CMAKE_CXX_STANDARD 14)`
  - executable sources: `src/editor.cpp`, `src/gap_buffer.cpp`
- Typical local build pattern in this repo is out-of-source under `cmake-build-debug/` (already present).
- No tests are configured yet in `CMakeLists.txt` (no `enable_testing()`/`add_test(...)`).

## Project-Specific Conventions to Follow
- Keep editor-facing index types strongly typed (`editor::BufferIndex`) rather than passing raw integers.
- Keep public API declarations in `include/*.h` and implementation in `src/*.cpp`.
- Maintain include guard style used in headers, e.g. `MODAL_EDITOR_GAP_BUFFER_H`.
- Be strict about filename case on Linux: `src/editor.cpp` includes `"Editor.h"`, but the file is `include/editor.h`.

## Integration and Dependency Notes
- Standard library types are used in headers (`std::size_t`, `std::string_view`) but headers currently do not include `<cstddef>`/`<string_view>`.
- There are no third-party dependencies declared in CMake.
- No runtime/service/network integrations exist yet; all communication is currently in-process C++ types.

## High-Value First Checks for Agents
- Before implementing features, fix compile blockers caused by header includes/casing.
- When adding editor logic, define clear ownership between `Editor` (commands/state machine) and `GapBuffer` (text mutation/storage).
- Prefer small, compilable increments because many files are scaffolds and interfaces will evolve quickly.

