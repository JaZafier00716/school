/**
 * @file main.cpp
 * @brief Implementation of Kruskal's algorithm for Minimum Spanning Tree (MST)
 *
 * This program reads an adjacency matrix from a file and computes the total
 * weight of the Minimum Spanning Tree using Kruskal's algorithm with a
 * Union-Find (Disjoint Set Union) structure.
 */

#include <algorithm>
#include <fstream>
#include <iostream>
#include <sstream>
#include <vector>
using std::cerr, std::cout, std::endl, std::vector;

/**
 * @class Edge
 * @brief Represents a weighted edge in an undirected graph
 */
class Edge
{
private:
  size_t v1;     ///< First vertex of the edge
  size_t v2;     ///< Second vertex of the edge
  size_t weight; ///< Weight of the edge

public:
  /**
   * @brief Constructs an edge between two vertices with a given weight
   * @param vertex1 First vertex index
   * @param vertex2 Second vertex index
   * @param w Weight of the edge
   */
  Edge(const size_t vertex1, const size_t vertex2, const size_t w) : v1(vertex1), v2(vertex2), weight(w) {}

  /**
   * @brief Gets the weight of the edge
   * @return The edge weight
   */
  size_t getWeight() const { return weight; }

  /**
   * @brief Gets the first vertex
   * @return Index of the first vertex
   */
  size_t getV1() const { return v1; }

  /**
   * @brief Gets the second vertex
   * @return Index of the second vertex
   */
  size_t getV2() const { return v2; }
};

/**
 * @class Graph
 * @brief Union-Find (Disjoint Set Union) for cycle detection in Kruskal's algorithm
 */
class Graph
{
private:
  vector<size_t> parent; ///< parent[i] = parent of vertex i in the forest
  vector<size_t> rank;   ///< rank[i] = approximate depth of subtree rooted at i

public:
  /**
   * @brief Constructs a Union-Find structure with the given number of vertices
   * @param vertex_count Number of vertices in the graph
   */
  Graph(const size_t vertex_count)
  {
    parent.resize(vertex_count);
    rank.resize(vertex_count);
    for (size_t i = 0; i < vertex_count; i++)
    {
      parent[i] = i;
      rank[i] = 1;
    }
  }

  /**
   * @brief Finds the root parent of a vertex with path compression
   * @param vertex The vertex to find the parent of
   * @return The root parent of the vertex's set
   */
  size_t findParent(const size_t vertex)
  {
    if (parent[vertex] != vertex)
    {
      parent[vertex] = findParent(parent[vertex]);
    }
    return parent[vertex];
  }

  /**
   * @brief Unites two sets containing the given vertices using union by rank
   * @param vertex1 First vertex
   * @param vertex2 Second vertex
   */
  void unionVertices(const size_t vertex1, const size_t vertex2)
  {
    const size_t parent1 = findParent(vertex1);
    const size_t parent2 = findParent(vertex2);
    if (parent1 != parent2)
    {
      if (rank[parent1] < rank[parent2])
      {
        parent[parent1] = parent2;
        return;
      }
      if (rank[parent1] > rank[parent2])
      {
        parent[parent2] = parent1;
        return;
      }
      parent[parent2] = parent1;
      rank[parent1]++;
    }
  }
};

/**
 * @brief Loads a graph from a file in adjacency matrix format
 * @param filename Path to the input file
 * @param edges Vector to store the loaded edges (output parameter)
 * @return Number of vertices in the graph (0 on error)
 */
size_t loadFromFile(const char *filename, vector<Edge> &edges)
{
  std::ifstream file(filename);

  if (!file.is_open())
  {
    cerr << "Error opening file " << filename << endl;
    return 0;
  }

  std::string line;
  size_t i = 0;
  while (std::getline(file, line))
  {
    std::stringstream lineAsStream(line);
    size_t num;
    size_t j = 0;
    while (lineAsStream >> num)
    {
      Edge edge(i, j, num);
      if (j >= i && num != 0)
      {
        edges.push_back(edge);
      }
      j++;
    }
    i++;
  }
  // Kelvin does not support ranges :(
  // std::ranges::sort(edges, [](const Edge &a, const Edge &b) {
  //   return a.getWeight() < b.getWeight();
  // });
  std::sort(edges.begin(), edges.end(), [](const Edge &a, const Edge &b)
            { return a.getWeight() < b.getWeight(); });

  return i;
}

/**
 * @brief Constructs a Minimum Spanning Tree using Kruskal's algorithm
 * @param vertex_count Number of vertices in the graph
 * @param edges Vector of edges sorted by weight in ascending order
 * @return Total weight of the minimum spanning tree
 */
size_t construct_spanning_tree(const size_t vertex_count, const vector<Edge> &edges)
{
  Graph spanning_tree(vertex_count);
  size_t edge_count = 0;
  size_t weight_sum = 0;
  for (const auto &edge : edges)
  {
    if (spanning_tree.findParent(edge.getV1()) != spanning_tree.findParent(edge.getV2()))
    {
      spanning_tree.unionVertices(edge.getV1(), edge.getV2());
      weight_sum += edge.getWeight();
      edge_count++;
      if (edge_count == vertex_count - 1)
      {
        break;
      }
    }
  }
  return weight_sum;
}

/**
 * @brief Main entry point of the program
 * @param argc Number of command-line arguments
 * @param argv Array of command-line argument strings
 * @return 0 on success, negative on error
 */
int main(const int argc, char *argv[])
{
  if (argc != 2)
  {
    cerr << "Usage: " << argv[0] << " <filename>" << endl;
    return -1;
  }
  vector<Edge> edges;
  const size_t vertex_count = loadFromFile(argv[1], edges);
  if (vertex_count == 0)
  {
    return 1;
  }

  cout << construct_spanning_tree(vertex_count, edges) << endl;
  return 0;
}
