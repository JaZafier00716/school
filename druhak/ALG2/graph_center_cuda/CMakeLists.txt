cmake_minimum_required(VERSION 3.20)

# Explicitly set CUDA compiler for CUDA 13
set(CMAKE_CUDA_COMPILER "C:/Program Files/NVIDIA GPU Computing Toolkit/CUDA/v13.0/bin/nvcc.exe")

project(graph_center_cuda LANGUAGES CXX CUDA)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

if(MSVC)
    add_compile_options(/W4 /WX)
    set(CMAKE_CUDA_FLAGS "${CMAKE_CUDA_FLAGS} /W4 /WX")
else()
    add_compile_options(-Wall -Wextra -Wpedantic -Werror)
    set(CMAKE_CUDA_FLAGS "${CMAKE_CUDA_FLAGS} -Xcompiler -Wall,-Wextra,-Wpedantic,-Werror")
endif()

add_executable(${PROJECT_NAME}
        main.cpp
        cuda_functions.cu
        classes.h
)

set_target_properties(${PROJECT_NAME} PROPERTIES
        CUDA_SEPARABLE_COMPILATION ON
)
