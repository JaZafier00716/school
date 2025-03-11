cmake_minimum_required(VERSION 3.4)

# Název projektu
project(snowflake)

# Přidání přepínačů překladače
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fsanitize=address")

# Vytvoření programu s názvem `snowflakes`
# Vyjmenujte zde všechny zdrojové soubory, které budete používat
add_executable(snowflakes main.c sdl.c dynamic_array.c)

# Přilinkování knihoven k programu (obdoba -l)
target_link_libraries(snowflakes SDL2 SDL2_image)
