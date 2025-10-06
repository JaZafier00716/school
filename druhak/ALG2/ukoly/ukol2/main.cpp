#include "header.h"

int main(int argc, char **argv)
{
  const int n = std::stoi(argv[1]);
  if (argc != n * n + 2)
  {
    cerr << "Wrong number of arguments!" << endl;
    return -1;
  }

  vector<bool> rhs;
  for (int i = 0; i < n * n; i++)
  {
    rhs.push_back(std::stoi(argv[i + 2]));
  }

  vector<vector<bool>> A = generate_matrix(n);

  // cout << "RHS: ";
  // for (const auto &val : rhs)
  // {
  //   cout << val << " ";
  // }
  // cout << endl;
  // print_matrix(A);

  vector<bool> solution = lights_out(A, rhs);
  if (solution.empty())
  {
    cerr << "No solution found!" << endl;
    return -1;
  }
  for (const auto &val : solution)
  {
    cout << val << " ";
  }
  cout << endl;
  return 0;
}
