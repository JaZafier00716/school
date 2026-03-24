# AGENTS Guide

## Project Scope
- Primary code lives in `template/`; treat this as the real C++ project root (`CMakeLists.txt`, `tasks.h`, `tasks.cpp`, `tests.cpp`).
- `template.zip` is a packaging artifact, not a source of truth.

## Big Picture Architecture
- The assignment combines **two independent domains** in one module pair:
  - `UTF8String` API in `template/tasks.h`, implementation in `template/tasks.cpp`.
  - `Tree` / `BigData` API expected in `template/tasks.h` and implementation in `template/tasks.cpp`.
- Behavioral contract is test-driven: `template/tests.cpp` is the authoritative spec (old UTF-8 behavior + new iterator/move behavior + tree semantics).
- `template/main.cpp` is only a manual debug playground; grading behavior is effectively defined by `tests` target.

## Build, Test, Debug Workflow
- Configure/build from `template/` using CMake (C++17, `-Wall -Wextra -pedantic`, debug symbols on):
  - `cmake -S . -B cmake-build-debug`
  - `cmake --build cmake-build-debug --target tests -j`
- Run tests with doctest executable:
  - `./cmake-build-debug/tests`
  - `./cmake-build-debug/tests --test-suite="UTF8String new"`
  - `./cmake-build-debug/tests --test-suite="Tree"`
- AddressSanitizer is enabled by default in `template/CMakeLists.txt`; disable only when needed:
  - `cmake -S . -B cmake-build-debug -DDISABLE_ASAN=ON`

## Project-Specific Conventions (Important)
- Prefer tests-first changes: implement only APIs exercised by `template/tests.cpp` and keep signatures compatible.
- UTF-8 API expectations include:
  - construction from `const char*`, `std::string`, `std::vector<CodePoint>`, and `std::vector<uint8_t>` (see `Iterate codepoints 2` test),
  - move semantics (`Move string`, `Move assign` tests),
  - `bytes()` random-access iterable and `codepoints()` bidirectional iterable.
- Iterator requirement from `template/assignment.md`: implement iterators manually; do not reuse STL iterator types as the primary implementation.
- Tree ownership pattern expected by tests/assignment:
  - child links transferred with `std::unique_ptr<Tree>` (`set_*_child`, `take_*_child`),
  - shared node value via `std::shared_ptr<BigData>`,
  - `get_parent()` returns pointer-like parent access and must stay consistent after reparenting.
- `replace_value(...)` must propagate one shared value through a subtree (validated in `Replace shared values`).

## Current Repository State (Discoverable)
- Current `template/tasks.cpp` does not compile against `template/tasks.h`/`template/tests.cpp` (manual-pointer code remains while `data` is a `std::vector<uint8_t>`).
- `Tree` is currently only forward-declared in `template/tasks.h`; tree tests expect a full class definition and iterator support (bonus inorder traversal).

## UTF8String First-Fix Roadmap
- P0: Make `UTF8String` internally consistent with `std::vector<uint8_t>` in `template/tasks.cpp` and `template/tasks.h` (`to_string()`, `resize()`, copy/append/join paths should use vector operations, not raw `new[]`/`delete[]`/`memcpy` on object itself).
- P1: Add missing constructor `UTF8String(const std::vector<uint8_t>&)` required by `Iterate codepoints 2` in `template/tests.cpp`.
- P1: Implement/verify move constructor and move assignment semantics so moved-from strings satisfy `Move string` and reassignment behavior in `Move assign`.
- P2: Implement `bytes()` iterable with a manual random-access iterator (supports `++`, `--`, `+=`, `-=`, `+`, `-`, dereference, begin/end, const use) to satisfy `Iterate bytes*` tests.
- P2: Implement `codepoints()` iterable with a manual bidirectional iterator over decoded UTF-8 code points to satisfy `Bidirectional codepoint iterators` and `Iterate codepoints*` tests.

## AI Instruction Files Discovery
- One glob scan for existing AI conventions found no matching files (`.github/copilot-instructions.md`, `AGENT*.md`, `CLAUDE.md`, cursor/windsurf/cline rule files, `README.md`).
- This `AGENTS.md` is therefore the first agent-specific guidance file in this workspace.

