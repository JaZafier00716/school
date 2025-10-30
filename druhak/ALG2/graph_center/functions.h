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
  vector<int> neighbours;
  bool active{};
  long long min_ecc{};
};

class Graph {
private:
  vector<Node> graph_node;
  unsigned long long node_count;
  unsigned long long edge_count;
  long long min_eccentricity;
  bool connected;

public:
  explicit Graph(const std::string &filename) {
    min_eccentricity = LLONG_MAX;
    edge_count = 0;
    connected = true;
    graph_node = readIntegersFromFile(filename);
    node_count = graph_node.size();
  }

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

      numbersVec[num1].active = true;
      numbersVec[num1].min_ecc = 0;
      numbersVec[num1].neighbours.push_back(num2);

      numbersVec[num2].active = true;
      numbersVec[num2].min_ecc = 0;
      numbersVec[num2].neighbours.push_back(num1);
    }

    file.close();
    return numbersVec;
  }

  void print_adjacent_nodes() const {
    for (size_t i = 0; i < graph_node.size(); ++i) {
      std::cout << i << ": ";
      for (const auto &node: graph_node[i].neighbours) {
        std::cout << node << " ";
      }
      std::cout << std::endl;
    }
  }

  [[nodiscard]] vector<long long> bfs(const int start_node) const {
    vector<long long> dist(node_count, -1);
    queue<long long> q;
    dist[start_node] = 0;
    q.push(start_node);

    while (!q.empty()) {
      const auto current = q.front();
      q.pop();

      for (const auto &neighbour: graph_node[current].neighbours) {
        if (dist[neighbour] == -1 && graph_node[neighbour].active) {
          dist[neighbour] = dist[current] + 1;
          q.push(neighbour);
        }
      }
    }

    return dist;
  }

  [[nodiscard]] vector<long long> graph_center() {
    // Set initial eccentricities to a large value
    vector<long long> ecc(node_count, LLONG_MAX);

    for (int vertice = 0; vertice < node_count; vertice++) {
      if (!graph_node[vertice].active) {
        continue; // Inactive nodes have no eccentricity
      }

      // Find the distances to all nodes from a starting node
      auto dist = bfs(vertice);

      // Find the eccentricity of the starting node
      auto ecc_v = *std::ranges::max_element(dist);

      // Check whether graph is connected
      if (ecc_v <= 0) {
        connected = false;
        continue;
      }

      ecc[vertice] = ecc_v;
      // Update the minimum eccentricity found so far
      min_eccentricity = std::min(min_eccentricity, ecc_v);
    }

    // Find the minimum eccentricity
    vector<long long> centers;
    min_eccentricity = *std::ranges::min_element(ecc);

    for (int i = 0; i < node_count; i++) {
      if (ecc[i] == min_eccentricity) {
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
      for (const auto c: centers) {
        cout << c << endl;
      }
    }
  }
};
