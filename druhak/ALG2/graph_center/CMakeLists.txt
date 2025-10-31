cmake_minimum_required(VERSION 3.20)
project(graph_center)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(graph_center main.cpp functions.h
        paralell.h
        two-bfs.h)

# Enable OpenMP manually (MinGW-friendly)
target_compile_options(graph_center PRIVATE -fopenmp)
target_link_options(graph_center PRIVATE -fopenmp)

# Warnings
if(MSVC)
    target_compile_options(graph_center PRIVATE /W4 /WX /permissive-)
else()
    target_compile_options(graph_center PRIVATE
            -Wall -Wextra -Wpedantic -Wshadow
            -Wnon-virtual-dtor -Wold-style-cast -Wcast-align
            -Wunused -Woverloaded-virtual -Wconversion
            -Wsign-conversion -Wnull-dereference
            -Wdouble-promotion -Wformat=2 -Werror
    )
endif()

# Optimization flags (for release)
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -O3 -march=native -flto -fopenmp")
