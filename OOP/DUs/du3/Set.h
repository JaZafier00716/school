#pragma once
#include <iostream>
#include <string>
using std::cin, std::cout, std::endl, std::ostream;

class Set
{
private:
  int *nums;
  int length;
  int count;

public:
  Set(int length);
  ~Set();
  int getCount() const;
  int getAt(int i) const;
  bool addNum(int num);

  Set* unionSets(Set* secondSet) const;
  Set* complement(Set* secondSet) const;

  friend ostream &operator<< (ostream &print, const Set &s);
};