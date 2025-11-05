#include <iostream>
#include <vector>
#include <map>
#include <set> // binary tree
#include <stack>
#include <queue>
#include <algorithm>
#include <unordered_set> // hash table
#include <unordered_map>
#include <ranges>
#include <string>

using std::cout, std::cin, std::endl, std::vector;
using ScalarType = int;
using Matrix = vector<vector<ScalarType>>;


vector<ScalarType> DistributionCountingSort(vector<ScalarType>&arr) {
  if(arr.size() < 2) {
    return arr;
  }
  vector<ScalarType> output(arr.size());

  ScalarType min = arr[0], max = arr[0];

  for(ScalarType num : arr) {
    min = std::min(min, num);
    max = std::max(max, num);
  }

  int range = max - min + 1;
  vector<int> count(range, 0);
  
  for (ScalarType num : arr) {
    count[num - min]++;
  }
  
  for (size_t i = 1; i < arr.size()+1; i++) {
    count[i] += count[i - 1];
  }

  for (size_t i = 0; i < arr.size(); i++) {
    size_t idx = arr[i] - min;
    output[count[idx] - 1] = arr[i];
    count[idx]--;
  }
  
  return output;
}

int horspoolFinder(const std::string &text, const std::string &pattern) {
  int n = text.size();
  int m = pattern.size();
  
  if (m == 0 || n == 0 || m > n) {
    return -1;
  }

  std::unordered_map<char, int> shiftTable;
  // shiftTable.reserve(256);
  
  for (unsigned char c = 32; c < 0x7E; c++) {
    shiftTable[c] = m;
  }

  for(size_t i=0; i < pattern.size(); i++) {
    shiftTable[pattern[i]] = m - i - 1;
  }

  for(const auto & [key, value] : shiftTable) {
    cout << key << " : " << value << endl;
  }

  size_t i = m - 1;

  while(i < n) {
    int k = 0;
    while(k < m && text[i - k] == pattern[m - 1 - k]) {
      k++;
    }
    if(k == m + 1) {
      cout << "Pattern found at index: " << i - m + 1 << endl;
      return i - m + 1;
    }
      i += shiftTable[text[i]];
  }

  return -1;
}

int main () {
  vector<ScalarType> arr = {4, 2, 2, 8, 3, 3, 1};
  
  vector<ScalarType> sorted = DistributionCountingSort(arr);

  for(ScalarType num : sorted) {
    cout << num << " ";
  }

  std::string text = "lorem ipsum abc dolor sit amet abc consectetur abc adipiscing elit";
  std::string pattern = " eli";
  horspoolFinder(text, pattern);
  cout << endl;
  return 0;
}
