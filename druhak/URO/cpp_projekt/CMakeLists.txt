cmake_minimum_required(VERSION 3.21)
project(discgolf_tracker_qt LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

find_package(Qt6 QUIET COMPONENTS Widgets)

enable_testing()

if (Qt6_FOUND)
    add_executable(discgolf_tracker
        src/main.cpp
        src/course_window.cpp
        src/game_history_window.cpp
        src/round_repository.cpp
        src/settings_dialog.cpp
        src/round_summary_dialog.cpp
        src/theme_manager.cpp
    )

    target_include_directories(discgolf_tracker PRIVATE include)
    target_link_libraries(discgolf_tracker PRIVATE Qt6::Widgets)
    target_compile_definitions(discgolf_tracker PRIVATE DG_PROJECT_ROOT="${CMAKE_SOURCE_DIR}")
    set_target_properties(discgolf_tracker PROPERTIES
        AUTOMOC ON
        AUTORCC ON
        AUTOUIC ON
    )
else()
    message(WARNING "Qt6 Widgets not found. GUI target 'discgolf_tracker' will not be built.")
endif()

add_executable(model_smoke_test
    tests/model_smoke.cpp
)

target_include_directories(model_smoke_test PRIVATE include)
add_test(NAME model_smoke_test COMMAND model_smoke_test)

