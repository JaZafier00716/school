#include "ListArray.h"



int main(int argc, char const *argv[]) {
  if(argc != 4) {
    cout << "Wrong number of arguments " << argc;
    return -1;
  }

  int list_count = stoi(argv[1]);
  string input_file_dir = argv[2];
  string output_file = argv[3];

  ListArray arr(list_count);
  arr.FillArray(input_file_dir);

  // cout << "lists:" << endl;
  // arr.printLists();
  

  List merged = arr.merge_lists();

  merged.save_file(output_file);
  return 0;
}

