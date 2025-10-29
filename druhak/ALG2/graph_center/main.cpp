#include "functions.h"



int main(const int argc, const char * argv[]) {
    if (argc != 3) {
        cerr << "Usage: " << argv[0] << " <path_to_input_file> <path_to_results_file>" << endl;
        return -1;
    }

    auto graph = Graph(argv[1]);
    // graph.print_adjacent_nodes();

    graph.print_results();




}