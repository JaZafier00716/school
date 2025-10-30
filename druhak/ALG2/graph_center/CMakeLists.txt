cmake_minimum_required(VERSION 4.0)
project(graph_center)

set(CMAKE_CXX_STANDARD 26)

# Create executable
add_executable(graph_center main.cpp functions.h)

# Enable warnings and treat them as errors
if(MSVC)
    # Visual Studio
    target_compile_options(graph_center PRIVATE /W4 /WX /permissive-)
else()
    # GCC or Clang
    target_compile_options(graph_center PRIVATE
            -Wall          # All common warnings
            -Wextra        # Extra warnings
            -Wpedantic     # Standards compliance
            -Wshadow       # Warn on variable shadowing
            -Wnon-virtual-dtor
            -Wold-style-cast
            -Wcast-align
            -Wunused
            -Woverloaded-virtual
            -Wconversion
            -Wsign-conversion
            -Wnull-dereference
            -Wdouble-promotion
            -Wformat=2
            -Werror        # Treat warnings as errors
    )
endif()

# Enable optimization (for release builds)
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -O3 -march=native -flto")
