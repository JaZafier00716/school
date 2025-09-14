#pragma once
#include <iostream>

class ExtendedArray
{
private:
  int *arr;
  unsigned int counter;
  unsigned int capacity;

public:
  ExtendedArray(unsigned int capacity);
  ~ExtendedArray();
  void add(int value);
  static ExtendedArray zeros(unsigned int amount);
  static unsigned int objectCounter;
};
