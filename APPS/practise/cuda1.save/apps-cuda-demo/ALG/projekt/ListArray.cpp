#include "ListArray.h"

ListArray::ListArray(int list_num) : total_size(0)
{
  this->lists.reserve(list_num); // Reserve memory for vector
}

ListArray::~ListArray()
{
  for (List *list : this->lists) // Go through all lists and delete them
  {
    delete list;
  }
}

void ListArray::FillArray(string input_dir)
{
  for (int i = 0; i < this->lists.capacity(); i++)
  {
    string file_dir = input_dir + to_string(i) + ".txt"; // change dir to file

    this->lists.push_back(new List());             // Create new list
    this->lists[i]->load_file(file_dir);           // Load data to new list
    this->total_size += this->lists[i]->getSize(); // calculate total size for merge list
  }
}

void ListArray::printLists()
{
  for (int i = 0; i < this->lists.size(); i++)
  {
    cout << "List " << i << ":" << endl;
    lists[i]->print_list();
    cout << "----------------" << endl;
  }
}

TValue ListArray::get_min_value() // returns minimal value among first values of lists
{
  TValue min_value = {INT_MAX, -1};

  for (int i = 0; i < lists.size(); i++) // Find actual min
  {
    int value = this->lists[i]->get_first_element(); // Get first element of current list

    if (!this->lists[i]->empty() && (value < min_value.value))
    {
      min_value = {value, i}; // Set new min
    }
  }
  return min_value;
}

List ListArray::merge_lists()
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

int ListArray::get_min_index()
{
  int min_index = -1;
  for (int i = 0; i < this->lists.size(); i++)
  {
    if (!this->lists[i]->empty())
    {
      min_index = i;
      break;
    }
  }

  if (min_index != -1)
  {
    for (int i = min_index + 1; i < this->lists.size(); i++)
    {
      if (!this->lists[i]->empty() && (this->lists[i]->get_first_element() < this->lists[min_index]->get_first_element()))
      {
        min_index = i;
      }
    }
  }

  return min_index;
}

List ListArray::merge_lists2()
{
  List merged_list(this->total_size);
  int min_index = -1;
  do
  {
    // get min value and stack index
    min_index = get_min_index();
    if (min_index != -1)
    {
      // add min value to merged list
      merged_list.addValue(this->lists[min_index]->get_first_element());

      this->lists[min_index]->next_element();
    }
  } while (min_index != -1);

  return merged_list;
}