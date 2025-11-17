#pragma once

#include <iostream>
#include <vector>
#include <sstream>
#include <fstream>
#include <string>
#include <queue>
#include <cstdint>

using std::cerr,
    std::cout,
    std::cin,
    std::endl,
    std::queue,
    std::vector;

/**
 * @brief Struct representing a node in the graph.
 */
struct Node {
  /** @brief Vector of neighboring node indices. */
  vector<size_t> neighbours;
  /** @brief Minimum eccentricity of the node. */
  size_t min_ecc = 0;
  /** @brief Active status of the node. */
  bool active = true;
};

/**
 * @brief Class representing an undirected graph and methods to find its center.
 */
class Graph {
private:
  /** @brief Vector of nodes representing the graph. */
  vector<Node> graph_nodes;
  size_t node_count = 0;
  size_t edge_count = 0;
  /** @brief Minimum eccentricity found in the graph. */
  size_t min_eccentricity = SIZE_MAX;
  bool connected = true;
  /** @brief Vector to store distances during BFS.
   * @brief Initialized to SIZE_MAX to indicate unvisited nodes.
   * @brief Used to track distances from the start node during BFS.
   */
  vector<size_t> distances;
  /** @brief Vector to track visited nodes during BFS.
   * @brief Used to reset distances efficiently after each BFS.
   * @brief Prevents the need to clear the entire distances vector each time.
   */
  vector<size_t> visited_nodes;

  /**
   * @brief Reads integers from a file to construct the graph.
   * @param filename The path to the file containing the graph data.
   * @return A vector of Nodes representing the graph. or an empty vector if the file cannot be opened.
   */
  vector<Node> readIntegersFromFile(const std::string &filename);

  /**
   * @brief Performs a breadth-first search (BFS) to calculate distances from the start node.
   * @param start_node The node from which to start the BFS.
   * @return The eccentricity of the start node.
   */
  [[nodiscard]] size_t bfs(const size_t start_node);

public:
  /**
   * @brief Constructs a Graph object by reading from a file.
   * @param filename The path to the file containing the graph data.
   */
  Graph(const std::string &filename);

  /**
   * @brief Prints the adjacent nodes for each node in the graph.
   */
  void print_adjacent_nodes() const;

  /**
   * @brief Finds the center of the graph based on node eccentricities.
   * @return A vector of node indices representing the graph center.
   */
  [[nodiscard]] vector<size_t> graph_center();

  /**
   * @brief Prints the results of the graph center finding algorithm.
   * @brief Including information about the graph's number of nodes, edges, connectivity, and center nodes.
   */
  void print_results();

  /**
   * @brief Gets the number of nodes in the graph.
   * @brief Used to verify successful graph construction.
   * @return The number of nodes.
   */
  [[nodiscard]] size_t get_node_count() const;
};