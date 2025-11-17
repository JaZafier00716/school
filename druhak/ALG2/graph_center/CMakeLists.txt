cmake_minimum_required(VERSION 3.20)
project(graph_center)

set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(graph_center
        main.cpp
        graph.h
        graph.cpp
)

# Enable OpenMP manually (MinGW-friendly)
#target_compile_options(graph_center PRIVATE -fopenmp)
#target_link_options(graph_center PRIVATE -fopenmp)

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
            -Wno-unused-result -Wdangling-pointer=1 -Werror=uninitialized -Werror=return-type -Werror=vla
    )
endif()

# Optimization flags (for release)
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -fsanitize=address -O3 -march=native -flto")
