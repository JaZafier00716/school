#include <chrono>
#include "classes.h"
#include <cuda_runtime.h>
#include <cuda_device_runtime_api.h>
using namespace std::chrono;

int main(const int argc, const char *argv[]) {
  if (argc != 3) {
    cerr << "Usage: " << argv[0] << " <path_to_input_file> <path_to_results_file>" << endl;
    return -1;
  }

  // auto graph = Graph(argv[1]);
  // graph.print_adjacent_nodes();

  // graph.print_results();


  auto start_graph = high_resolution_clock::now();
  auto graph = Graph("./tests/Graph1.txt");
  graph.print_results();
  vector<size_t> eccentricities.reserve(graph.get_node_count());
  bfs_cuda(graph, eccentricities);
  auto end_graph = high_resolution_clock::now();

  auto duration_graph = duration_cast<milliseconds>(end_graph - start_graph);
  cout << "-----------------------" << endl;
  cout << "Graph1 took: " << duration_graph.count() << " ms" << endl;
  cout << "-----------------------" << endl << endl;

/*
  start_graph = high_resolution_clock::now();
  graph = Graph("./tests/Graph2.txt");
  graph.print_results();
  end_graph = high_resolution_clock::now();

  duration_graph = duration_cast<milliseconds>(end_graph - start_graph);
  cout << "-----------------------" << endl;
  cout << "Graph2 took: " << duration_graph.count() << " ms" << endl;
  cout << "-----------------------" << endl << endl;


  start_graph = high_resolution_clock::now();
  graph = Graph("./tests/Graph3.txt");
  graph.print_results();
  end_graph = high_resolution_clock::now();

  duration_graph = duration_cast<milliseconds>(end_graph - start_graph);
  cout << "-----------------------" << endl;
  cout << "Graph3 took: " << duration_graph.count() << " ms" << endl;
  cout << "-----------------------" << endl << endl;
*/
}
