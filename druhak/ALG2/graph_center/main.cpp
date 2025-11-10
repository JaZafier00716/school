// #include "paralell.h"
#include <chrono>
using namespace std::chrono;
#include "classes.h"

/**
 * @brief Main function to execute the graph center finding algorithm on three input files.
 * @brief Measures and prints the execution time for each graph.
 *
 * @param argc The number of command-line arguments.
 * @param argv The array of command-line argument strings.
 * @return Exit status code.
 */
int main(const int argc, const char *argv[]) {
  if (argc != 4) {
    cerr << "Usage: " << argv[0] << " <path_to_input_file1> <path_to_input_file2> <path_to_input_file3>" << endl;
    return -1;
  }

  auto start_graph = high_resolution_clock::now();
  auto graph = Graph(argv[1]);
  if (graph.get_node_count() == 0) {
    return -2;
  }
  graph.print_results();
  auto end_graph = high_resolution_clock::now();

  auto duration_graph = duration_cast<milliseconds>(end_graph - start_graph);
  cout << "-----------------------" << endl;
  cout << "Graph1 took: " << duration_graph.count() << " ms" << endl;
  cout << "-----------------------" << endl << endl;


  start_graph = high_resolution_clock::now();
  graph = Graph(argv[2]);
  if (graph.get_node_count() == 0) {
    return -2;
  }
  graph.print_results();
  end_graph = high_resolution_clock::now();

  duration_graph = duration_cast<milliseconds>(end_graph - start_graph);
  cout << "-----------------------" << endl;
  cout << "Graph2 took: " << duration_graph.count() << " ms" << endl;
  cout << "-----------------------" << endl << endl;


  start_graph = high_resolution_clock::now();
  graph = Graph(argv[3]);
  if (graph.get_node_count() == 0) {
    return -2;
  }
  graph.print_results();
  end_graph = high_resolution_clock::now();

  duration_graph = duration_cast<milliseconds>(end_graph - start_graph);
  cout << "-----------------------" << endl;
  cout << "Graph3 took: " << duration_graph.count() << " ms" << endl;
  cout << "-----------------------" << endl << endl;
}
