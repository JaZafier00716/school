#pragma once
#include <vector>
#include <string>
#include <iostream>
#include <algorithm>
using std::vector, std::string, std::cout, std::endl, std::cerr;
using Matrix = vector<vector<bool>>;

vector<vector<bool>> generate_matrix(int n)
{
  Matrix matrix = Matrix(n * n, vector<bool>(n * n, 0));
  const std::pair<int, int> directions[5] =
      {
          {0, 0},
          {-1, 0},
          {1, 0},
          {0, -1},
          {0, 1}};
  for (int y = 0; y < n; y++)
  {
    for (int x = 0; x < n; x++)
    {
      int button_index = x + n * y;

      for (const auto &[dx, dy] : directions)
      {
        int nx = x + dx;
        int ny = y + dy;
        if (nx >= 0 && nx < n && ny >= 0 && ny < n)
        {
          int affected_index = nx + n * ny;
          matrix[affected_index][button_index] = 1;
        }
      }
    }
  }
  return matrix;
}

void print_matrix(const Matrix &matrix)
{
  for (const auto &row : matrix)
  {
    for (const auto &val : row)
    {
      cout << val << " ";
    }
    cout << endl;
  }
}

vector<bool> lights_out(const Matrix &A, vector<bool> &b)
{
  Matrix U = A;

  // Transform to upper triangular matrix
  for (size_t i = 0; i < U.size(); i++) // For each pivot row
  {
    // Find pivot
    if (!U[i][i])
    {
      for (size_t j = i + 1; j < U.size(); j++)
      {
        if (U[j][i])
        {
          std::swap(U[i], U[j]);
          bool temp_b = b[i];
          b[i] = b[j];
          b[j] = temp_b;
          break;
        }
      }
    }
    if(!U[i][i]) { // No pivot found, skip this column
      cerr << "No unique solution exists!" << endl;
      return {};
    }

    // Convert to Upper Triangular Matrix
    for(size_t j = i+1; j < U.size(); j++) { // For each row below pivot
      if(!U[j][i]) continue; // No need to eliminate if already zero
      for (size_t k = i; k < U.size(); k++) { // For each column in the row

        U[j][k] = U[j][k] ^ U[i][k];
      }
      b[j] = b[j] ^ b[i];
    }
  }

  // Solve with upper triangular matrix
  for (int i = U.size() - 1; i >= 0; i--)
  {
    for (size_t j = i + 1; j < U.size(); j++)
    {
      if(U[i][j]) {
        b[i] = b[i] ^ b[j];
      }
    }
  }

  return b;
}