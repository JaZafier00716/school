#include "List.h"

List::List(int size)
{
  this->values.reserve(size);
  this->current_index = 0;
}
List::List()
{
  this->current_index = 0;
}
int List::getSize() {
  return this->values.size();
}

void List::load_file(string input_file)
{
  ifstream f(input_file);
  vector<int> new_values;

  if (!f.is_open())
  {
    cout << "Could not open file" << endl;
    return;
  }

  string buffer;
  while (getline(f, buffer))
  {
    new_values.push_back(stoi(buffer));
  }
  this->values = new_values;  

  f.close();
}

void List::addValue(int value)
{
  this->values.push_back(value); // add element to the end
}

void List::print_list()
{
  for (int value : this->values)
  {
    cout << value << " ";
  }
  cout << endl;
}

void List::save_file(string filename) {
  ofstream f(filename);

  if(!f.is_open()) {
    cout << "Could not open file" << endl;
    return;
  }

  for (int value : this->values)
  {
    f << value << endl;
  }
  f.close();
  
}

int List::get_first_element()
{
  return this->values[current_index];
}

void List::next_element() // set next element as first
{
  this->current_index++;
}

bool List::empty() // check if there are any elements left
{
  return this->current_index >= this->values.size();
}

bool List::list_sorted() {
  for (int i = 0; i < this->values.size()-1; i++)
  {
    if(this->values[i] > this->values[i+1]) {
      return false;
    }
  }
  return true;
}