#pragma once
#include <iostream>
#include <vector>
#include <queue>
#include <fstream>
#include <algorithm>
#include <limits>
#ifdef _OPENMP
#include <omp.h>
#endif

using std::vector;
using std::size_t;
using std::queue;
using std::cout;
using std::cerr;
using std::endl;

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
  size_t min_eccentricity = std::numeric_limits<size_t>::max();
  bool connected = true;

  vector<Node> readIntegersFromFile(const std::string &filename) {
    std::ifstream file(filename);
    vector<Node> numbersVec;
    edge_count = 0;

    if (!file.is_open()) {
      cerr << "Unable to open file: " << filename << endl;
      return numbersVec;
    }

    size_t num1, num2;
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

  size_t bfs_thread_safe(size_t start_node,
                              vector<size_t> &distances,
                              vector<size_t> &visited_nodes) {
    distances.assign(node_count, SIZE_MAX);
    visited_nodes.clear();

    queue<size_t> q;
    distances[start_node] = 0;
    q.push(start_node);
    visited_nodes.push_back(start_node);

    size_t ecc = 0;

    while (!q.empty()) {
      size_t current = q.front();
      q.pop();
      size_t current_distance = distances[current];

      for (const auto &neighbor : graph_nodes[current].neighbours) {
        if (distances[neighbor] == SIZE_MAX) {
          distances[neighbor] = current_distance + 1;
          visited_nodes.push_back(neighbor);
          q.push(neighbor);
          ecc = std::max(distances[neighbor], ecc);
        }
      }
    }

    // Connected check (only done by main thread if desired)
    return ecc;
  }


  vector<size_t> graph_center() {
    vector<size_t> ecc(node_count, std::numeric_limits<size_t>::max());
    min_eccentricity = std::numeric_limits<size_t>::max();


#ifdef _OPENMP
#pragma omp parallel for schedule(dynamic)
#endif
    for (size_t vertice = 0; vertice < node_count; ++vertice) {
      if (!graph_nodes[vertice].active) continue;

      vector<size_t> local_distances(node_count, SIZE_MAX);
      vector<size_t> local_visited;
      local_visited.reserve(node_count);

      size_t ecc_v = bfs_thread_safe(vertice, local_distances, local_visited);

#ifdef _OPENMP
#pragma omp critical
#endif
      {
        ecc[vertice] = ecc_v;
        if (ecc_v < min_eccentricity) {
          min_eccentricity = ecc_v;
        }
      }
    }

    for (size_t i = 0; i < node_count; i++) {
      graph_nodes[i].active = true;
    }

    for (size_t vertice = 0; vertice < node_count; ++vertice) {
      if (ecc[vertice] > min_eccentricity) {
        graph_nodes[vertice].active = false;
      }
    }

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
    cout << "Graph center eccentricity: " << min_eccentricity << endl;
    cout << "Graph center: total " << centers.size() << " vertices" << endl;
    for (const auto c: centers) {
      cout << c << endl;
    }
  }
};
