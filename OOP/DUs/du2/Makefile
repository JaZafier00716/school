CXX = g++
CXXFLAGS = -Wall -Wextra -std=c++17 -I. -Istudent -Igrades -Iclassroom


SRC_DIRS = student grades classroom
SRC_FILES = $(wildcard $(addsuffix /*.cpp, $(SRC_DIRS))) main.cpp
OBJ_FILES = $(SRC_FILES:.cpp=.o)

TARGET = program

all: $(TARGET)

$(TARGET): $(OBJ_FILES)
	$(CXX) $(CXXFLAGS) $^ -o $@

%.o: %.cpp %.h
	$(CXX) $(CXXFLAGS) -c $< -o $@

clean:
	rm -f $(OBJ_FILES) $(TARGET)