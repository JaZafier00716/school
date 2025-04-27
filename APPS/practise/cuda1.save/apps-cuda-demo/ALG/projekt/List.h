#pragma once
#include <iostream>
#include <vector>
#include <climits>
#include <string>
#include <fstream>
using namespace std;

/**
 * @class List
 * @brief Represents a list of integers with sequential access.
 */
class List
{
private:
  vector<int> values;         /**< Vector holding integer values */
  unsigned int current_index; /**< Current index for sequential access */
public:
  /**
   * @brief Constructs a list with a pre-allocated size.
   * @param size Capacity to reserve in the vector.
   */
  List(int size);
  /**
   * @brief Constructs an empty list.
   */
  List();

  /**
   * @brief Loads integers from a file into the list.
   * @param input_file Path to the input file.
   */
  void load_file(string input_file);

  /**
   * @brief Adds a value to the list.
   * @param value The integer value to add.
   */
  void addValue(int value);

  /**
   * @brief Prints all values in the list.
   */
  void print_list();

  /**
   * @brief Saves the list values to a file.
   * @param filename Path to the output file.
   */
  void save_file(string filename);

  /**
   * @brief Gets the number of elements in the list.
   * @return The number of elements.
   */
  int getSize();

  /**
   * @brief Gets the current element.
   * @return The first (current) element.
   */
  int get_first_element();

  /**
   * @brief Advances to the next element in the list. (Sets next element as a first element)
   */
  void next_element();

  /**
   * @brief Checks if there are any remaining elements.
   * @return True if no elements remain, false otherwise.
   */
  bool empty();

  /**
   * @brief Checks whether the list is sorted in ascending order.
   * @return True if sorted, false otherwise.
   */
  bool list_sorted();
};
