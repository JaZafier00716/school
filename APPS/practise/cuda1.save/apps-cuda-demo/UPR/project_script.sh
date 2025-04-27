#!/bin/bash

# Prompt for project name
read -p "Enter your project name: " PROJECT_NAME

# Set up project directory and subdirectories
mkdir -p "$PROJECT_NAME"/{src,include,public,data,build}

# Create a CMakeLists.txt file with basic configuration for SDL2
cat > "$PROJECT_NAME/CMakeLists.txt" <<EOL
cmake_minimum_required(VERSION 3.10)
project($PROJECT_NAME C)

# Set C standard
set(CMAKE_C_STANDARD 11)

# Find SDL2
find_package(SDL2 REQUIRED)

# Include directories
include_directories(\${SDL2_INCLUDE_DIRS} include)

# Source files
file(GLOB SRC "src/*.c")
file(GLOB HEADERS "include/*.h")

# Create executable
add_executable(\${PROJECT_NAME} \${SRC} \${HEADERS})

# Link SDL2 libraries
target_link_libraries(\${PROJECT_NAME} \${SDL2_LIBRARIES})
EOL

# Create a README file with basic information
cat > "$PROJECT_NAME/README.md" <<EOL
# $PROJECT_NAME

This project is organized into the following folders:

- **src/**: Contains the source files (.c).
- **include/**: Contains header files (.h).
- **public/**: Contains any files you want to be publicly accessible (e.g., images, icons).
- **data/**: Used for any additional data files your project may need.
- **build/**: This is where the compiled files will go.

### Requirements
This project uses SDL2. Make sure this library is installed.

### Building the Project
To build the project, navigate to the project root and run:
\`\`\`
mkdir -p build
cd build
cmake ..
make
\`\`\`

This will generate an executable in the \`build/\` directory.
EOL

# Create a basic main.c file
cat > "$PROJECT_NAME/src/main.c" <<EOL
#include "header.h"
#include <SDL2/SDL.h>
#include <stdio.h>

int main(int argc, char* argv[]) {
    if (SDL_Init(SDL_INIT_VIDEO) != 0) {
        fprintf(stderr, "SDL_Init Error: %s\\n", SDL_GetError());
        return 1;
    }

    // Create a simple SDL window
    SDL_Window *window = SDL_CreateWindow("Hello SDL2", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 800, 600, SDL_WINDOW_SHOWN);
    if (!window) {
        fprintf(stderr, "SDL_CreateWindow Error: %s\\n", SDL_GetError());
        SDL_Quit();
        return 1;
    }

    // Wait for 3 seconds
    SDL_Delay(3000);

    // Clean up
    SDL_DestroyWindow(window);
    SDL_Quit();
    
    return 0;
}
EOL

# Create a basic header file main.h
cat > "$PROJECT_NAME/include/header.h" <<EOL
#ifndef MAIN_H
#define MAIN_H

// Include SDL2 headers here or other common headers for the project

#endif // MAIN_H
EOL

# Finish
echo "Project $PROJECT_NAME created successfully!"
