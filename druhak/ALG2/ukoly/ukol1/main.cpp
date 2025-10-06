#include "functions.h"

int main(int argc, char *argv[])
{
  if (argc < 3)
  {
    std::cerr << "Not enough arguments" << endl;
    return -1;
  }

  std::string data_file = argv[1];
  std::string numbers_file = argv[2];
  auto data = readIntegersFromFile(data_file);
  auto numbers = readIntegersFromFile(numbers_file);

  std::sort(data.begin(), data.end());

  for (size_t i = 0; i < numbers.size(); i++)
  {
    int number = numbers.at(i);
    cout << number << ": " << (binarySearch(data, number) ? "T" : "F") << endl;
  }
  

  return 0;
}