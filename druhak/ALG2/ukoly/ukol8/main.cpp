#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>
#include <string>

using std::vector, std::cout, std::endl, std::max;
using Matrix = std::vector<std::vector<size_t> >;

// reads integers from the first line in the file
Matrix readIntegersFromFile(const std::string &filename) {
  std::ifstream file(filename);
  Matrix numbersVec;

  if (!file.is_open()) {
    std::cerr << "Unable to open file: " << filename << std::endl;
    return numbersVec;
  }

  std::string line;

  // change if to while to read the whole file
  while (std::getline(file, line)) {
    std::stringstream lineAsStream(line);
    int num;
    vector<size_t> row;
    while (lineAsStream >> num) {
      // NOTE: this assumes that the input file has the data we want
      row.push_back(num);
    }
    numbersVec.push_back(row);
  }

  file.close();
  return numbersVec;
}

void printMatrix(const Matrix &m) {
  for (const auto &row: m) {
    for (const auto &elem: row) {
      cout << (std::to_string(elem).length() == 1 ? " " : "") << elem << " ";
    }
    cout << endl;
  }
}


Matrix CoinCollectionMatrix(const Matrix &matrix) {
  const size_t n = matrix.size();
  const size_t m = matrix[0].size();
  Matrix collected(n, vector<size_t>(m, 0));

  collected[0][0] = matrix[0][0];
  for (size_t j = 1; j < m; j++) {
    collected[0][j] = collected[0][j-1] + matrix[0][j];
  }
  for (size_t i = 1; i < n; i++) {
    collected[i][0] = collected[i-1][0] + matrix[i][0];
    for (size_t j = 1; j < m; j++) {
      collected[i][j] = max(collected[i-1][j], collected[i][j-1]) + matrix[i][j];
    }
  }
  return collected;
}

size_t CoinCollectionValue(const Matrix &matrix) {
  const size_t n = matrix.size();
  const size_t m = matrix[0].size();
  Matrix collected(n, vector<size_t>(m, 0));

  collected[0][0] = matrix[0][0];
  for (size_t j = 1; j < m; j++) {
    collected[0][j] = collected[0][j-1] + matrix[0][j];
  }
  for (size_t i = 1; i < n; i++) {
    collected[i][0] = collected[i-1][0] + matrix[i][0];
    for (size_t j = 1; j < m; j++) {
      collected[i][j] = max(collected[i-1][j], collected[i][j-1]) + matrix[i][j];
    }
  }
  return collected[n-1][m-1];
}

int main(const int argc, char *argv[]) {
  if (argc != 2) {
    std::cerr << "Usage: " << argv[0] << " <file_name>" << std::endl;
  }

  const auto matrix = readIntegersFromFile(argv[1]);
  // const auto m_collected = CoinCollectionMatrix(matrix);
  // printMatrix(m_collected);

  cout << CoinCollectionValue(matrix) << endl;



  return 0;
}
