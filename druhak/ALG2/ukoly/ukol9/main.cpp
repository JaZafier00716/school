#include <algorithm>
#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
using std::cerr, std::cout, std::endl, std::vector;

class Edge {
private:
  size_t v1;
  size_t v2;
  size_t weight;
public:
  Edge(const size_t vertex1, const size_t vertex2, const size_t w) : v1(vertex1), v2(vertex2), weight(w) {}
  size_t getWeight() const { return weight; }
  size_t getV1() const { return v1; }
  size_t getV2() const { return v2; }
};

class Graph {
private:
  vector<size_t> parent; // parent[i] = parent of vertex i
  vector<size_t> rank; // rank[i] = rank of vertex i
public:

  Graph(const size_t vertex_count) {
    parent.resize(vertex_count);
    rank.resize(vertex_count);
    for (size_t i = 0; i < vertex_count; i++) {
      parent[i] = i;
      rank[i] = 1;
    }
  }

  size_t findParent(const size_t vertex) {
    if (parent[vertex] != vertex) {
      parent[vertex] = findParent(parent[vertex]);
    }
    return parent[vertex];
  }

  void unionVertices(const size_t vertex1, const size_t vertex2) {
    const size_t parent1 = findParent(vertex1);
    const size_t parent2 = findParent(vertex2);
    if (parent1 != parent2) {
      if (rank[parent1] < rank[parent2]) { // parent 1 has smaller rank than parent 2 -> attach it under parent 2
        parent[parent1] = parent2;
        return;
      }
      if (rank[parent1] > rank[parent2]) { // parent 2 has smaller rank than parent 1 -> attach it under parent 1
        parent[parent2] = parent1;
        return;
      }
      // both have same rank -> attach parent 2 under parent 1 and increase rank of parent 1
      parent[parent2] = parent1;
      rank[parent1]++;
    }
  }
};

size_t loadFromFile(const char *filename, vector<Edge> &edges) {
  std::ifstream file(filename);

  if (!file.is_open()) {
    cerr << "Error opening file " << filename << endl;
    return 0;
  }

  std::string line;
  size_t i=0;
  while (std::getline(file, line)) {
    std::stringstream lineAsStream(line);
    size_t num;
    size_t j = 0;
    while (lineAsStream >> num) {
      Edge edge(i, j, num);
      if (j >= i && num != 0) { // To avoid duplicate edges in undirected graph and non-existent edges
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
  std::sort(edges.begin(), edges.end(), [](const Edge &a, const Edge &b) {
    return a.getWeight() < b.getWeight();
  });

  return i;
}

size_t construct_spanning_tree(const size_t vertex_count, const vector<Edge> &edges) {
  Graph spanning_tree(vertex_count);
  size_t edge_count = 0;
  size_t weight_sum = 0;
  for (const auto &edge : edges) {
    if (spanning_tree.findParent(edge.getV1()) != spanning_tree.findParent(edge.getV2())) {
      // cout << "Adding edge: (" << edge.getV1() << ", " << edge.getV2() << ") weight: " << edge.getWeight() << endl;
      spanning_tree.unionVertices(edge.getV1(), edge.getV2());
      weight_sum += edge.getWeight();
      edge_count++;
      if (edge_count == vertex_count - 1) {
        // Spanning tree is complete, because tree has exactly V-1 edges
        break;
      }
    }
  }
  return weight_sum;
}

int main(const int argc, char *argv[]) {
  if (argc != 2) {
    cerr << "Usage: " << argv[0] << " <filename>" << endl;
    return -1;
  }
  vector<Edge> edges;
  const size_t vertex_count = loadFromFile(argv[1], edges);
  if (vertex_count == 0) {
    return 1;
  }
  // cout << "vertex_count: " << vertex_count << endl;
  // for (const auto &edge : edges) {
  //   cout << "Edge: (" << edge.getV1() << ", " << edge.getV2() << ") weight: " << edge.getWeight() << endl;
  // }

  // cout << "spanning_tree cost: " << construct_spanning_tree(vertex_count, edges) << endl;
  cout << construct_spanning_tree(vertex_count, edges) << endl;
  return 0;
}
