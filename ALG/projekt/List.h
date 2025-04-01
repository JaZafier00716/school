#pragma once
#include <iostream>
#include <vector>
#include <climits>
#include <string>
#include <fstream>
using namespace std;

class List
{
private:
  vector<int> values;
  unsigned int current_index;
public:
  List(int size);
  List();

  void load_file(string input_file);   // Loads data from file to values vector
  void addValue(int value);           // Adds value to values vector
  void print_list();                        
  void save_file(string filename);    // Saves list to file
  int getSize();      
  int get_first_element();
  void next_element(); // set next element as first
  bool empty();  // check if there are any elements left
  bool list_sorted();
};

