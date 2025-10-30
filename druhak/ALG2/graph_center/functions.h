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
};

class Graph {
private:
  vector<Node> graph_nodes;
  size_t node_count = 0;
  size_t edge_count = 0;
  size_t min_eccentricity = SIZE_MAX;
  bool connected = true;

  vector<Node> readIntegersFromFile(const std::string &filename) {
    std::ifstream file(filename);
    std::vector<Node> numbersVec;
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
      if (numbersVec.size() <= maxIndex) {
        numbersVec.resize(maxIndex + 1);
      }

      numbersVec[num1].neighbours.push_back(num2);
      numbersVec[num2].neighbours.push_back(num1);
    }

    file.close();
    return numbersVec;
  }

public:
  explicit Graph(const std::string &filename) {
    graph_nodes = readIntegersFromFile(filename);
    node_count = graph_nodes.size();
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

  [[nodiscard]] vector<size_t> bfs(const size_t start_node) const {
    vector<size_t> distances(node_count, SIZE_MAX);
    queue<size_t> q;
    distances[start_node] = 0;
    q.push(start_node);

    // size_t max_dist = 0;

    while (!q.empty()) {
      const auto current = q.front();
      q.pop();

      for (const auto &neighbour: graph_nodes[current].neighbours) {
        if (distances[neighbour] == SIZE_MAX) {
          distances[neighbour] = distances[current] + 1;
          // max_dist = std::max(distances[neighbour], max_dist);
          q.push(neighbour);
        }
      }
    }

    return distances;
  }

  [[nodiscard]] vector<size_t> graph_center() {
    // Set initial eccentricities to a large value
    vector<size_t> ecc(node_count, LLONG_MAX);

    for (size_t vertice = 0; vertice < node_count; vertice++) {
      // Find the distances to all nodes from a starting node
      auto dist = bfs(vertice);

      size_t ecc_v = 0;
      for (size_t i = 0; i < node_count; i++) {
        if (dist[i] == SIZE_MAX) {
          connected = false; // Graph is not connected
          continue;
        }
        graph_nodes[i].min_ecc = std::max(graph_nodes[i].min_ecc, dist[i]);
        ecc_v = std::max(ecc_v, dist[i]);
      }

      ecc[vertice] = ecc_v;
      // Update the minimum eccentricity found so far
      min_eccentricity = std::min(min_eccentricity, ecc_v);
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