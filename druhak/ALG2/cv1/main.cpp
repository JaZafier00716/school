#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <stack>
#include <queue>
#include <algorithm>


using std::cout, std::cin, std::endl, std::vector;
using bigInt = long long int;

class phiNumber {
  private:

  public:
  bigInt int_part;
  bigInt phi_part;

  phiNumber() : int_part(0), phi_part(0) {};
  phiNumber(bigInt a) : int_part(a), phi_part(0) {};
  phiNumber(bigInt a, bigInt b) : int_part(a), phi_part(b) {};

  phiNumber operator+(const phiNumber& other) {
    return phiNumber(int_part + other.int_part, phi_part + other.phi_part);
  }

  phiNumber operator*(const phiNumber& other) {
    return phiNumber(int_part * other.int_part, phi_part * other.phi_part);
  }
};

struct fibonacciRecurrent {
  private:
  vector<int> cache;
  bigInt compute(int n) {
    if(n < cache.size()) {
      return cache.at(n);
    }
    bigInt newValue = compute(n-1) + compute(n-2);
    cache.push_back(newValue); 

    return newValue;
  }
  
  public:
  fibonacciRecurrent() {
    cache.push_back(0);
    cache.push_back(1);
  }

  bigInt getNumber(const int n) {
    if(n < 0) {
      throw std::invalid_argument("n has to be greater than or equal to 0");
    }
    return compute(n);
  }
};


bigInt power(int base, int exponent) {
  bigInt result = 1;
  while (exponent > 0)
  {
    if(exponent % 2 == 1) {
      result *= base;
    }
    base = base * base;
    exponent /= 2;
  }
  return result;
} 


bigInt fibonacci(const int n)
{
  if (n < 0)
  {
    return -1;
    throw std::invalid_argument("n has to be greater than 0");
  }

  vector<int> results;
  results.push_back(0);
  results.push_back(1);
  bigInt newValue;
  for (int i = 2; i < n; i++)
  {
    newValue = results.at(i - 1) + results.at(i - 2);
  }

  return newValue;
}

bigInt fibonacciNaive(const int n)
{
  if(n == 0) {
    return 0;
  }
  if(n == 1) {
    return 1;
  }

  return fibonacciNaive(n-1) + fibonacciNaive(n-1);
}

std::pair<int, int> findTwoHighest(const vector<int> &vec)
{
  // std::sort(v.begin(), v.end());
  int highest = vec.at(0);
  int secondHighest = vec.at(1);

  if (highest < secondHighest)
  {
    std::swap(highest, secondHighest);
  }

  for (int i = 2; i < vec.size(); i++)
  {
    if (highest < vec.at(i))
    {
      highest = vec.at(i);
    }
    if (secondHighest < vec.at(i))
    {
      secondHighest = vec.at(i);
    }
  }
  return {secondHighest, highest};
}

int main()
{
  vector<int> v = {4, 5, 6, 1, 12, 3, -1, 0};

  for (const auto &item : v)
  {
    cout << item << " ";
  }

  cout << endl;

  cout << "hello\n";
  return 0;
}