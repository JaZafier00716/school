#pragma once
#include <iostream>
#include <vector>
#include <ranges>
#include <string>
#include <fstream>
#include <sstream>
#include <algorithm>

using std::cout, std::cin, std::endl, std::vector;
using Matrix = vector<vector<int>>;

std::vector<int> readIntegersFromFile(const std::string& filename) {
  std::ifstream file(filename);
  std::vector<int> numbersVec;

  if (!file.is_open()) {
    std::cerr << "Unable to open file: " << filename << std::endl;
    return numbersVec;
  }

  std::string line;

  // change if to while to read the whole file
  if (std::getline(file, line)) {
    std::stringstream lineAsStream(line);
    int num;
    while (lineAsStream >> num) {  // NOTE: this assumes that the input file has the data we want
      numbersVec.push_back(num);
    }
  }

  file.close();
  return numbersVec;
}

vector<int> CountingSort(vector<int> &arr, const int exponent) {
  if(arr.size() < 2) {
    return arr;
  }
  vector<int> output(arr.size());

  vector<int> count(10, 0);

  // Store count of occurrences
  for (const int num : arr) {
    count[(num/exponent) % 10]++;
  }

  // Change count[i] so that it now contains actual position of this digit in output[]
  for (size_t i = 1; i < count.size(); i++) {
    count[i] += count[i - 1];
  }

  for (int i = arr.size()-1; i >= 0; i--) {
    output[count[(arr[i]/exponent) % 10] - 1] = arr[i];
    count[(arr[i]/exponent) % 10]--;
  }

  return output;
}

vector<int> RadixSort(vector<int> &arr, const int max, const int exp) {
  if (max / exp == 0) {
    return arr;
  }
  vector<int> sorted = CountingSort(arr, exp);
  return RadixSort(sorted, max, exp * 10);
}

void print_array(const vector<int>& arr) {
  for (const int num : arr) {
    std::cout << num << " ";
  }
  std::cout << std::endl;
}