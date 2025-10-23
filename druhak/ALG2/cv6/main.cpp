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
using ScalarType = double;
using Matrix = vector<vector<ScalarType>>;

int calculatePolynom(const vector<double>& coefficients, double x) {
  int result = 0;
  int power = 1;
  for (const auto& coeff : coefficients) {
    result += coeff * power;
    power *= x;
  }
  return result;
}

int hornerMethod(const vector<double>& coefficients, double x) {
  int result = 0;
  for (auto it = coefficients.rbegin(); it != coefficients.rend(); ++it) {
    result = result * x + *it;
  }
  return result;
}

vector<vector<int>> combinations(const int n, const int k) {
  vector<vector<int>> res;

  if(k < 0 || k > n) {
    return res;
  }

  vector<int> currentComb;
  currentComb.reserve(k);
  for(int i = 0; i < k; ++i) {
    currentComb.push_back(i);
  }

  while(true) {
    res.push_back(currentComb);

    int i = k - 1;
    while(i >= 0 && currentComb[i] == n - k + i) {
      i--;
    }
    if(i < 0) {
      break;
    }
    
    currentComb[i]++;
    for(size_t j = i+1; j < currentComb.size(); j++) {
      currentComb[j] = currentComb[j-1] + 1;
    }
  }
    

  return res;
}

vector<vector<int>> combinationsRecursiveHelper(const int n, const int k, const int start, vector<int>& currentComb) {
  vector<vector<int>> res;

  if(currentComb.size() == k) {
    res.push_back(currentComb);
    return res;
  }

  for(int i = start; i < n; ++i) {
    currentComb.push_back(i);
    auto subCombs = combinationsRecursiveHelper(n, k, i + 1, currentComb);
    res.insert(res.end(), subCombs.begin(), subCombs.end());
    currentComb.pop_back();
  }

  return res;
}

int main () {
  

  vector<double> coefficients = {-1, 2, -6, 2}; // Represents 2 + 0*x + 3*x^2
  double x = 3.0;
  int value = calculatePolynom(coefficients, x);
  cout << "The value of the polynomial at x = " << x << " is " << value << endl;


  const auto comb = combinations(5, 3);
  for(const auto& c : comb) {
    for(const auto& el : c) {
      cout << el << " ";
    }
    cout << endl;
  }




  return 0;
}
