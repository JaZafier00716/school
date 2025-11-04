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

struct Node {
  vector<size_t> neighbours;
  size_t min_ecc = 0;
  bool active = true;
};

class Graph {
private:
  vector<Node> graph_nodes;
  size_t node_count = 0;
  size_t edge_count = 0;
  size_t min_eccentricity = SIZE_MAX;
  bool connected = true;
  vector<size_t> distances;
  vector<size_t> visited_nodes;

  vector<Node> readIntegersFromFile(const std::string &filename) {
    std::ifstream file(filename);
    std::vector<Node> numbersVec;
    edge_count = 0;

    if (!file.is_open()) {
      std::cerr << "Unable to open file: " << filename << std::endl;
      return numbersVec;
    }

    size_t num1, num2;
    // change if to while to read the whole file
    while (file >> num1 >> num2) {
      edge_count++;

      size_t maxIndex = std::max(num1, num2);
      if (numbersVec.size() <= maxIndex) {
        numbersVec.resize(maxIndex + 1);
      }

      numbersVec[num1].neighbours.push_back(num2);
      numbersVec[num2].neighbours.push_back(num1);
    }

    file.close();
    return numbersVec;
  }

  [[nodiscard]] size_t bfs(const size_t start_node) {
    // reset visited nodes
    for (const size_t idx : visited_nodes) {
      distances[idx] = SIZE_MAX;
    }
    visited_nodes.clear();

    queue<size_t> q;
    distances[start_node] = 0;
    q.push(start_node);
    visited_nodes.push_back(start_node);

    size_t ecc = 0;

    while (!q.empty()) {
      const auto current = q.front();
      q.pop();
      const auto current_distance = distances[current];

      if (current_distance > min_eccentricity) {
        return SIZE_MAX;
      }

      for (const auto &neighbour: graph_nodes[current].neighbours) {
        if (distances[neighbour] == SIZE_MAX) {
          distances[neighbour] = current_distance + 1;
          visited_nodes.push_back(neighbour);
          q.push(neighbour);
          ecc = std::max(distances[neighbour], ecc);
        }
      }
    }

    // If not all nodes visited, mark disconnected
    if (connected && visited_nodes.size() < node_count)
      connected = false;

    return ecc;
  }

public:
  explicit Graph(const std::string &filename) {
    graph_nodes = readIntegersFromFile(filename);
    node_count = graph_nodes.size();
    distances.assign(node_count, SIZE_MAX);
    visited_nodes.reserve(node_count);
  }

  void print_adjacent_nodes() const {
    for (size_t i = 0; i < graph_nodes.size(); ++i) {
      std::cout << i << ": ";
      for (const auto &node: graph_nodes[i].neighbours) {
        std::cout << node << " ";
      }
      std::cout << std::endl;
    }
  }


  [[nodiscard]] vector<size_t> graph_center() {
    // Set initial eccentricities to a large value
    vector<size_t> ecc(node_count, SIZE_MAX);

    for (size_t vertice = 0; vertice < node_count; vertice++) {
      if (!graph_nodes[vertice].active) {
        continue;
      }

      // Find the distances to all nodes from a starting node
      auto ecc_v = bfs(vertice);


      ecc[vertice] = ecc_v;
      // Update the minimum eccentricity found so far
      min_eccentricity = std::min(min_eccentricity, ecc_v);

      for (const auto visited : visited_nodes) {
        graph_nodes[visited].min_ecc = std::max(graph_nodes[visited].min_ecc, distances[visited]);
        if (graph_nodes[visited].active && graph_nodes[visited].min_ecc > min_eccentricity) {
          graph_nodes[visited].active = false;
        }
      }
    }

    // Find the minimum eccentricity
    vector<size_t> centers;
    for (size_t i = 0; i < node_count; i++) {
      if (ecc[i] == min_eccentricity) {
        centers.push_back(i);
      }
    }

    return centers;
  }

  void print_results() {
    const auto centers = graph_center();
    cout << "Number of vertices: " << node_count << endl;
    cout << "Number of edges: " << edge_count << endl;
    cout << "Is connected: " << (connected ? "True" : "False") << endl;
    if (centers.size() == 1) {
      cout << "Graph center: " << centers[0] << endl;
      cout << "Graph center eccentricity: " << min_eccentricity << endl;
    } else {
      cout << "Graph center eccentricity: " << min_eccentricity << endl;
      cout << "Graph center: total " << centers.size() << " vertices, see below" << endl;
      for (const auto c: centers) {
        cout << c << endl;
      }
    }
  }
};