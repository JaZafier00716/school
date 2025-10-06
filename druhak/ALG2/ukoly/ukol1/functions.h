#pragma once
#include <iostream>
#include <vector>
#include <map>
#include <set> // binary tree
#include <stack>
#include <queue>
#include <algorithm>
#include <unordered_set> // hash table
#include <unordered_map>
#include <fstream>
#include <string>
#include <sstream>

using std::cin, std::cout, std::endl, std::vector;

vector<int> readIntegersFromFile(const std::string &filename)
{
  std::ifstream file(filename);
  vector<int> numbersVec;

  if (!file.is_open())
  {
    std::cerr << "Unable to open file: " << filename << std::endl;
    return numbersVec;
  }

  std::string line;

  // change if to while to read the whole file
  if (std::getline(file, line))
  {
    std::stringstream lineAsStream(line);
    int num;
    while (lineAsStream >> num)
    { // NOTE: this assumes that the input file has the data we want
      numbersVec.push_back(num);
    }
  }

  file.close();
  return numbersVec;
}

bool binarySearch(const vector<int> &data, int number)
{
  if (data.empty())
  {
    return false;
  }

  int start_index = 0;
  int end_index = data.size() - 1;

  while (start_index <= end_index)
  {
    int middle_index = start_index + (end_index - start_index) / 2;

    if (data[middle_index] == number)
    {
      return true;
    }
    else if (data[middle_index] < number)
    {
      start_index = middle_index + 1;
    }
    else
    {
      end_index = middle_index - 1;
    }
  }
  return false;
}
