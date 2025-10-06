#include <iostream>
#include <vector>
#include <map>
#include <set> // binary tree
#include <stack>
#include <queue>
#include <algorithm>
#include <unordered_set> // hash table
#include <unordered_map>

using std::cout, std::cin, std::endl, std::vector;
using bigInt = long long int;

struct intervalPoint
{
  int value;
  bool opening;

  bool operator<(const intervalPoint &other)
  {
    return value < other.value;
  }
};

vector<intervalPoint> data = [{-1.5, true}, {0, true}, {1, true}];

std::sort(data.begin(), data.end());

bool presortedUniqueness(const vector<int> &vec)
{
  for (size_t i = 1; i < vec.size(); i++)
  {
    if (vec.at(i) == vec.at(i - 1))
    {
      return false;
    }
  }
  return true;
}

bool unsortedUniqueness(const vector<int> &vec)
{
  std::set<int> uniqueValues; // Tree structure

  // brute force pres vsechny dvojice
  // for(size_t i = 0; i < vec.size(); i++) {
  //   for(size_t j = 0; j < vec.size(); j++) {
  //     if(vec.at(i) == vec.at(j)) {
  //       return false;
  //     }
  //   }
  // }

  uniqueValues.insert(vec.at(0));

  for (size_t i = 0; i < vec.size(); i++)
  {
    if (uniqueValues.find(vec.at(i)) == uniqueValues.end())
    {
      uniqueValues.insert(vec.at(i));
    }
    else
    {
      return false;
    }
  }
  return true;
}

int presortedMode(const vector<int> &vec)
{
  int mode_frequency = 0;
  int mode_value;
  size_t i = 0;
  int run_length, run_value;
  while (i < vec.size())
  {
    run_length = 1;
    run_value = vec.at(i);
    while (i + run_length < vec.size() && vec.at(i + run_length) == run_value)
    {
      run_length++;
    }
    if (run_length > mode_frequency)
    {
      mode_frequency = run_length;
      mode_value = run_value;
    }
    i += run_length;
  }
  return mode_value;
}

int unsortedMode(const vector<int> &vec)
{
  if (!vec.size())
  {
    std::cerr << "invalud input\n";
    throw std::invalid_argument("input must not be empty\n");
  }

  std::map<int, int> frequencyMap;

  for (const int item : vec)
  {
    frequencyMap[item] += 1;
  }

  int modus;
  int modus_frequency = 0;
  for (const auto &[number, frequency] : frequencyMap)
  {
    if (frequency > modus_frequency)
    {
      modus_frequency = frequency;
      modus = number;
    }
  }

  return modus;
}

int main()
{
  vector<int> data = {1, 1, 2, 3, 4, 4, 4, 5, 2, 1, 2, 4, 6, 7, 7, 8};
  vector<int> empty = {};

  bool uniqueness = unsortedUniqueness(data);
  cout << "unsortedUnique " << (uniqueness ? "true" : "false") << endl;

  int mode = unsortedMode(data);
  cout << "unsortedMode " << mode << endl;

  // Sort
  std::sort(data.begin(), data.end());
  for (const auto datum : data)
  {
    cout << datum << " ";
  }

  cout << endl;

  uniqueness = presortedUniqueness(data);
  cout << "presortedUnique " << (uniqueness ? "true" : "false") << endl;

  mode = presortedMode(data);
  cout << "presortedMode " << mode << endl;

  return 0;
}