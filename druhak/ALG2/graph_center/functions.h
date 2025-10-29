#pragma once
// <editor-fold desc="Includes">
#include <iostream>
#include <vector>
#include <algorithm>
#include <sstream>
#include <fstream>
#include <string>
#include <queue>
// </editor-fold>

using std::cerr,
    std::cout,
    std::cin,
    std::endl,
    std::queue,
    std::vector;

class Graph {
private:
  vector<vector<int> > adjacent_nodes;
  long long node_count;
  long long edge_count;
  long long min_eccentricity;
  bool connected;

public:
  explicit Graph(const std::string &filename) {
    min_eccentricity = INT_MAX;
    connected = true;
    adjacent_nodes = readIntegersFromFile(filename);
    node_count = adjacent_nodes.size();
  }

  vector<vector<int> > readIntegersFromFile(const std::string &filename) {
    std::ifstream file(filename);
    std::vector<vector<int> > numbersVec;
    edge_count = 0;
    if (!file.is_open()) {
      std::cerr << "Unable to open file: " << filename << std::endl;
      return numbersVec;
    }

    int num1, num2;
    // change if to while to read the whole file
    while (file >> num1 >> num2) {
      edge_count++;

      size_t maxIndex = std::max(num1, num2);
      if (numbersVec.size() <= maxIndex)
        numbersVec.resize(maxIndex + 1);

      numbersVec[num1].push_back(num2);
      numbersVec[num2].push_back(num1);
    }

    file.close();
    return numbersVec;
  }

  void print_adjacent_nodes() const {
    for (size_t i = 0; i < adjacent_nodes.size(); ++i) {
      std::cout << i << ": ";
      for (const auto &node: adjacent_nodes[i]) {
        std::cout << node << " ";
      }
      std::cout << std::endl;
    }
  }

  [[nodiscard]] vector<int> bfs(const int start_node) const {
    vector<int> dist(node_count, -1);
    queue<int> q;
    dist[start_node] = 0;
    q.push(start_node);

    while (!q.empty()) {
      const int current = q.front();
      q.pop();

      for (auto neighbour: adjacent_nodes[current]) {
        if (dist[neighbour] == -1) {
          dist[neighbour] = dist[current] + 1;
          q.push(neighbour);
        }
      }
    }
    return dist;
  }

  [[nodiscard]] vector<int> graph_center() {
    // Set initial eccentricities to a large value
    vector<int> ecc(node_count, INT_MAX);

    // Find the distances to all nodes from an arbitrary starting node (e.g., node 0)
    vector<int> dist = bfs(0);

    // Find the farthest node from the starting node
    int farthest_node = std::ranges::max_element(dist)-dist.begin();

    // Perform BFS from the farthest node found
    vector<int> dist_from_farthest1 = bfs(farthest_node);

    // Update eccentricities based on distances from the farthest node
    for (int i = 0; i < node_count; i++) {
      ecc[i] = std::min(ecc[i], dist_from_farthest1[i]);
    }

    // Find the new farthest node
    farthest_node = std::ranges::max_element(dist_from_farthest1)-dist_from_farthest1.begin();
    // Perform BFS from the new farthest node found
    vector<int> dist_from_farthest2 = bfs(farthest_node);
    for (int i = 0; i < node_count; i++) {
      if (dist_from_farthest1[i] != -1 && dist_from_farthest2[i] != -1) {
        ecc[i] = std::max(dist_from_farthest1[i], dist_from_farthest2[i]);
      } else {
        // Graph is not connected
        ecc[i] = INT_MAX;
        connected = false;
      }
    }

    // Find the minimum eccentricity
    min_eccentricity = *std::ranges::min_element(ecc);
    vector<int> centers;
    for (int i = 0; i < node_count; i++) {
      if (ecc[i] == min_eccentricity && ecc[i] != INT_MAX) {
        centers.push_back(i);
      }
    }
    return centers;
  }

  void print_results() {
    const auto centers = graph_center();
    cout << "Number of verticies: " << node_count << endl;
    cout << "Number of edges: " << edge_count << endl;
    cout << "Is connected: " << (connected ? "True" : "False") << endl;
    if (centers.size() == 1) {
        cout << "Graph center: " << centers[0] << endl;
        cout << "Graph center eccentricity: " << min_eccentricity << endl;
    } else {
      cout << "Graph center eccentricity: " << min_eccentricity << endl;
      cout << "Graph center: total " << centers.size() << " vertices, see below" << endl;
      for (const auto c : centers) {
          cout << c << endl;
      }
    }
  }
};
