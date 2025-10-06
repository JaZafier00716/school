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

template<typename T>
void printVector (const vector<T>& vec) {
  for (const auto& v : vec) {
    cout << v << " ";
  }
  cout << endl;
}


void printMatrix (const Matrix& mat) {
  for (const auto& row : mat) {
    printVector(row);
  }
  cout << endl;
}

vector<ScalarType> solveAxb(Matrix A, vector<ScalarType>& b) {
  // vector<T> vec(size, value);
  Matrix L(A.size(), vector<ScalarType>(A.size(), 0));
  Matrix U(A.size(), vector<ScalarType>(A.size(), 0));

  for (size_t i = 0; i < L.size(); i++)
  {
    L[i][i] = 1;
  }
  


  // Transform to upper triangular matrix
  for (size_t i = 0; i < A.size(); i++) {
    // Find pivot
    size_t pivot = i;
    ScalarType pivot_value = std::abs(A[i][i]);
    for (size_t j = i+1; j < A.size(); j++) {
      if (pivot_value < std::abs(A[j][i])) {
        pivot = j;
        pivot_value = std::abs(A[j][i]);
      }
    }
    if (pivot != i) {
      std::swap(A[i], A[pivot]);
      std::swap(b[i], b[pivot]);
    }   

    for(size_t j = i+1; j < A.size(); j++) {
      ScalarType temp = A[j][i] / A[i][i];
      L[j][i] = temp;
      for (size_t k = i; k < A.size(); k++) {
        A[j][k] -= temp * A[i][k];
      }
      b[j] -= temp * b[i];
    }
  }

  U = A;

  // Solve with upper triangular matrix
  for (int i = A.size() - 1; i >= 0; i--) {
    ScalarType sum_correction = 0;
    for (size_t j = i+1; j < A.size(); j++)
    {
      sum_correction += A[i][j] * b[j];
    }
    b[i] = (b[i] - sum_correction) / A[i][i];
    
  }
  
  return b;
}



int main () {
  Matrix mat = {
    {1, 2},
    {2, 1}
  };

  vector<ScalarType> rhs = {1, 2};  

  auto x = solveAxb(mat, rhs);

  printVector(x);

  return 0;
}