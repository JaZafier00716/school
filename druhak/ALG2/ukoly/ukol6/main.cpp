#include "funtions.h"


int main(const int argc, char** argv) {
  if (argc != 2) {
    std::cerr << "Usage: " << argv[0] << " <input_file>" << std::endl;
  }

  vector<int> arr = readIntegersFromFile(argv[1]);
  const auto max = std::max_element(arr.begin(), arr.end());

  const auto sorted = RadixSort(arr, *max, 1);

  print_array(sorted);

  return 0;
}