#pragma once
#include "List.h"

typedef struct TValue
{
  int value;
  int list_index;
} TValue;

class ListArray
{
private:
  vector<List *> lists;
  int total_size;

public:
  ListArray(int list_num);
  ~ListArray();

  void FillArray(string input_dir); // Loads data from files into lists
  void printLists();                              // Prints out contents of all lists
  TValue get_min_value();                         // returns minimal value among first values of lists
  List merge_lists();                             // Merges lists together
};
