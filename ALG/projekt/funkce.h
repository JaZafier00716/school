#pragma once
#include <iostream>
#include <vector>
#include <climits>

/*
  Literatura:
    https://www.geeksforgeeks.org/vector-in-cpp-stl/
    https://www.w3schools.com/cpp/cpp_vectors.asp

*/

using namespace std;

typedef struct TValue
{
  int value;
  int list_index;
} TValue;

class List
{
private:
  vector<int> values;
  unsigned int current_index;
public:
  List(int size)
  {
    this->values.reserve(size);
    this->current_index = 0;
  }

  void addValue(int value)
  {
    this->values.push_back(value); // add element to the end
  }

  void addValuesSize()
  {
    int new_value;
    for (int i = this->values.size(); i < this->values.capacity(); i++)
    {
      cout << "Enter value:\t";
      cin >> new_value;
      this->values.push_back(new_value);
    }
  }

  void print_list()
  {
    for (int value : this->values)
    {
      cout << value << " ";
    }
    cout << endl;
  }

  int get_first_element()
  {
    return this->values[current_index];
  }

  void next_element() // set next element as first
  {
    this->current_index++;
  }

  bool empty()  // check if there are any elements left
  {
    return this->current_index >= this->values.size();
  }
};

class ListArray
{
private:
  vector<List *> lists;
  int total_size;

public:
  ListArray(int list_num) : total_size(0)
  {
    this->lists.reserve(list_num); // Reserve memory for vector

    for (int i = 0; i < list_num; i++)
    {
      // Get size
      int size;
      cout << "Enter number of values for list " << i << ":\t";
      do
      {
        cin >> size;
        if (size < 0)
        {
          cout << "size must be greater then 0!!!" << endl;
        }
      } while (size < 0);

      this->lists.push_back(new List(size)); // create new list at the end of the vector
      this->total_size += size;              // calculate total size for merge list

      this->lists.back()->addValuesSize(); // access last list and add values to it
    }
  }
  ~ListArray()
  {
    for (List *list : this->lists) // Go through all lists and delete them
    {
      delete list;
    }
  }

  void printLists()
  {
    for (int i = 0; i < this->lists.size(); i++)
    {
      cout << "List " << i << ":" << endl;
      lists[i]->print_list();
      cout << "----------------" << endl;
    }
  }

  TValue get_min_value()  // returns minimal value among first values of lists
  {
    TValue min_value = {INT_MAX, -1};   

    for (int i = 0; i < lists.size(); i++)   // Find actual min
    {
      int value = this->lists[i]->get_first_element();  // Get first element of current list

      if (!this->lists[i]->empty() && (value < min_value.value))
      {
        min_value = {value, i}; // Set new min
      }
    }
    return min_value;
  }

  List merge_lists()
  {
    List merged_list(this->total_size);

    for (int i = 0; i < this->total_size; i++)
    {
      // get min value and stack index
      TValue min_value = get_min_value();

      // add min value to merged list
      merged_list.addValue(min_value.value);

      this->lists[min_value.list_index]->next_element();
    }

    return merged_list;
  }
};
