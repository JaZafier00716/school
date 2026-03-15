cmake_minimum_required(VERSION 3.20)
project(modal_editor LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# ---------------------------------------------------------
# 1. THE CORE LIBRARY
# Compile all your logic into a modular library target
# ---------------------------------------------------------
add_library(editor_core
        src/editor/gap_buffer.cpp
        src/editor/document.cpp
        include/editor/document.h
        # src/editor.cpp  <-- Uncomment this if you have code in it!
)

# Tell CMake where to find the headers for this library.
# PUBLIC means anything that links to this library also gets these headers.
target_include_directories(editor_core PUBLIC include)

# ---------------------------------------------------------
# 2. THE TEST EXECUTABLE
# A completely separate program just for testing
# ---------------------------------------------------------
add_executable(test_runner tests/test_buffer.cpp)

# Link the test program to your editor logic
target_link_libraries(test_runner PRIVATE editor_core)

# ---------------------------------------------------------
# 3. THE MAIN APP (For later)
# ---------------------------------------------------------
# add_executable(modal_editor main.cpp)
# target_link_libraries(modal_editor PRIVATE editor_core)