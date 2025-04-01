#include "ListArray.h"
#include <chrono>

int main(int argc, char const *argv[])
{
  if (argc != 4)
  {
    cout << "Wrong number of arguments " << argc;
    return -1;
  }

  int list_count = stoi(argv[1]);
  string input_file_dir = argv[2];
  string output_file = argv[3];

  ListArray arr(list_count);
  arr.FillArray(input_file_dir);
  ListArray arr2(list_count);
  arr2.FillArray(input_file_dir);

  // cout << "lists:" << endl;
  // arr.printLists();

  auto start = std::chrono::high_resolution_clock::now();
  List merged = arr.merge_lists();
  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> elapsed = end - start;
  cout << "List\tsorted:\t" << (merged.list_sorted() ? "true" : "false") << ",\tin: " << elapsed.count() << "s" << endl;

  start = std::chrono::high_resolution_clock::now();
  List merged2 = arr2.merge_lists2();
  end = std::chrono::high_resolution_clock::now();
  elapsed = end - start;
  cout << "List2\tsorted:\t" << (merged2.list_sorted() ? "true" : "false") << ",\tin: " << elapsed.count() << "s" << endl;

  merged.save_file(output_file);
  return 0;
}
