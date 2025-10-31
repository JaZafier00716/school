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
};

class Graph {
private:
    vector<Node> graph_nodes;
    size_t node_count = 0;
    size_t edge_count = 0;
    size_t min_eccentricity = 0;
    bool connected = true;

    vector<Node> readIntegersFromFile(const std::string &filename) {
        std::ifstream file(filename);
        vector<Node> nodes;
        edge_count = 0;

        if (!file.is_open()) {
            cerr << "Unable to open file: " << filename << endl;
            return nodes;
        }

        size_t u, v;
        while (file >> u >> v) {
            edge_count++;
            size_t maxIndex = std::max(u, v);
            if (nodes.size() <= maxIndex) nodes.resize(maxIndex + 1);
            nodes[u].neighbours.push_back(v);
            nodes[v].neighbours.push_back(u);
        }

        file.close();
        return nodes;
    }

    [[nodiscard]] vector<size_t> bfs(size_t start_node) const {
        vector<size_t> distances(node_count, std::numeric_limits<size_t>::max());
        if (start_node >= node_count) return distances;

        queue<size_t> q;
        distances[start_node] = 0;
        q.push(start_node);

        while (!q.empty()) {
            size_t current = q.front();
            q.pop();
            for (size_t neighbor : graph_nodes[current].neighbours) {
                if (distances[neighbor] == std::numeric_limits<size_t>::max()) {
                    distances[neighbor] = distances[current] + 1;
                    q.push(neighbor);
                }
            }
        }
        return distances;
    }

public:
    explicit Graph(const std::string &filename) {
        graph_nodes = readIntegersFromFile(filename);
        node_count = graph_nodes.size();

        // Check if connected
        if (node_count > 0) {
            auto dist = bfs(0);
            connected = std::ranges::all_of(dist,
                                            [](size_t d) { return d != std::numeric_limits<size_t>::max(); });
        }
    }

    vector<size_t> graph_center() {
        vector<size_t> centers;
        if (node_count == 0) return centers;

        // 1. BFS from arbitrary node (0)
        auto dist0 = bfs(0);

        // 2. Find the farthest distance
        size_t max_dist = 0;
        for (size_t i = 0; i < node_count; i++)
            if (dist0[i] != std::numeric_limits<size_t>::max() && dist0[i] > max_dist)
                max_dist = dist0[i];

        // 3. Collect all farthest vertices from 0
        vector<size_t> farthest_vertices;
        for (size_t i = 0; i < node_count; i++)
            if (dist0[i] == max_dist)
                farthest_vertices.push_back(i);

        // 4. Initialize eccentricities
        vector<size_t> eccentricities(node_count, 0);

        // 5. BFS from each farthest vertex (parallelized)
#ifdef _OPENMP
        #pragma omp parallel for schedule(dynamic)
#endif
        for (const unsigned long long farthest_vertice : farthest_vertices) {
            auto dist = bfs(farthest_vertice);
            for (size_t i = 0; i < node_count; i++) {
                if (dist[i] != std::numeric_limits<size_t>::max()) {
#ifdef _OPENMP
                    #pragma omp critical
#endif
                    eccentricities[i] = std::max(eccentricities[i], dist[i]);
                }
            }
        }

        // 6. Determine min eccentricity and centers
        min_eccentricity = *std::ranges::min_element(eccentricities);
        for (size_t i = 0; i < node_count; i++) {
            if (eccentricities[i] == min_eccentricity)
                centers.push_back(i);
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
        for (const auto c : centers) {
            cout << c << endl;
        }
    }
};
