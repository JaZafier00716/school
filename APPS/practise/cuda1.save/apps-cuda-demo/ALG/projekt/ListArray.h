#pragma once
#include "List.h"

/**
 * @struct TValue
 * @brief Represents a value and its origin list index.
 */
typedef struct TValue
{
  int value;      /**< The value itself */
  int list_index; /**< Index of the list the value came from */
} TValue;

/**
 * @class ListArray
 * @brief Manages multiple lists and supports merging operations.
 */
class ListArray
{
private:
  vector<List *> lists; /**< Vector of pointers to List objects */
  int total_size;       /**< Total size of all lists combined */

public:
  /**
   * @brief Constructs a ListArray with a fixed number of lists.
   * @param list_num Number of lists to allocate.
   */
  ListArray(int list_num);

  /**
   * @brief Destructor to free list memory.
   */
  ~ListArray();

  /**
   * @brief Loads data into all lists from a directory with numbered files.
   * @param input_dir Directory path prefix.
   */
  void FillArray(string input_dir);

  /**
   * @brief Prints contents of all lists.
   */
  void printLists();

  /**
   * @brief Gets the minimal value among the first values of all lists. (Used for method 1 of list merging)
   * @return A TValue struct containing the smallest value and its list index.
   */
  TValue get_min_value();

  /**
   * @brief Merges all lists into a single sorted list (method 1).
   * @return The merged List.
   */
  List merge_lists();

  /**
   * @brief Helper to find index of the list with the current minimum element. (Used for method 2 of list merging)
   * @return Index of list with the smallest current value, or -1 if all are empty.
   */
  int get_min_index();

  /**
   * @brief Merges all lists into a single sorted list (method 2).
   * @return The merged List.
   */
  List merge_lists2();
};
