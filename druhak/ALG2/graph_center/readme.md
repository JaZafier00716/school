# Graph Center Finder

C++ project that computes the center(s) of an undirected graph using BFS-based eccentricity computations. 
Handles disconnected graphs (sets `Is connected` to `False`) and prints timing information.

## Features
- Compute graph center(s) and their eccentricity.  
- Detect disconnected graphs.  
- Prints number of vertices and edges and execution time.  
- Simple plain-text edge list input.

## Requirements
- C\+\+17 or later  
- CMake (for command-line or CLion)  
- Tested on Windows (CLion) and Linux (terminal)

## Build (command-line)
```bash
mkdir build
cd build
cmake ..
cmake --build .
```

Alternatively open the project in `CLion` or any other CMAKE\-capable IDE and build directly.

## Usage
Run the binary with three input files (program requires exactly three input paths):
```bash
./GraphCenter path/to/input1.txt path/to/input2.txt path/to/input3.txt
```
Example input files are provided in the `tests/` folder. And can be tested as:
```bash
./GraphCenter ./tests/Graph1.txt ./tests/Graph2.txt ./tests/Graph3.txt
```

## Output
Formatted output includes:
- Number of vertices  
- Number of edges  
- Is connected: True/False  
- Graph center node(s) and center eccentricity  
- Execution time per graph

## Input format
Each input file is a plain text edge list.  
Each line contains two non\-negative integers `u v` representing an undirected edge between vertices `u` and `v`.  
Vertex indices are 0\-based.  
Example:
```
0 1
1 2
2 3
0 3
```

The loader is implemented in `classes.h` / `class.cpp` in the function `readIntegersFromFile`.

## Implementation notes
- Graph stored as adjacency lists in `vector<Node>`;  
- BFS from nodes computes eccentricities.  
- The code prunes BFS calls using a running minimum eccentricity and an `active` flag per node to avoid unnecessary work.

## Complexity and algorithmic notes
- The current approach determines the graph center by performing iterative BFS from multiple vertices with pruning optimizations.  
- In the worst case, the algorithm runs in **O(n × (n + m))**, where *n* is the number of vertices and *m* the number of edges.   
- While the *“double BFS”* technique (two BFS passes from an arbitrary node and then from its farthest node) reliably identifies the diameter and center in trees, 
it does not guarantee correct results in general graphs with cycles, as the true center may not lie on the diameter path.   
- Therefore, to ensure correctness for all graph types, the implementation relies on iterative BFS to compute exact eccentricities.   
- Performance is improved through pruning strategies such as tracking **node activity** and **early BFS termination** once the current eccentricity exceeds the best (minimum) value found so far. 
These enhancements maintain full accuracy while reducing redundant exploration in practice.
## Files
- `main.cpp` \- program entry and timing  
- `classes.h` / `class.cpp` \- graph representation and center computation

## Disclaimer
This implementation is provided as-is for educational purposes. Further optimizations and algorithmic improvements may be possible for specific graph classes or applications.

## Citations
This implementation is inspired by standard graph algorithms and BFS techniques commonly found in algorithm textbooks and online resources.   
Artificial Intelligence assistance was used to help generate and refine portions of this documentation as well as consultation on algorithmic approaches and optimisations.

## Acknowledgements

**Resources:**
- [GeeksforGeeks](https://www.geeksforgeeks.org) – for ideas on BFS and graph representation.
- [Wikipedia – Graph Center](https://en.wikipedia.org/wiki/Graph_center) – for algorithmic approaches and explanations.

**AI Tools Used:**
- [ChatGPT](https://chat.openai.com) by OpenAI
- [Gemini](https://gemini.google.com) by Google
- [GitHub Copilot](https://github.com/features/copilot) by GitHub


