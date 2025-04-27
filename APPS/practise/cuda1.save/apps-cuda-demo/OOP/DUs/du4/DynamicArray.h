#pragma once
#include <iostream>
#include <string>
using namespace std;

class DynamicArray
{
private:
  int *arr;
  unsigned int count;
  unsigned int size;
  static const int defaultSize;

public:
  DynamicArray();
  DynamicArray(unsigned int n);
  DynamicArray(int *arr, unsigned int length);
  ~DynamicArray();

  int getAt(unsigned int index) const;
  int getSize() const;
  int getCount() const;
  void setAt(unsigned int index, int value);

  void Add(int number);
  int removeAt(unsigned int index);
};

const int DynamicArray::defaultSize = 100;

DynamicArray::DynamicArray()
{
  this->arr = new int[DynamicArray::defaultSize];
  this->count = 0;
  this->size = DynamicArray::defaultSize;
}
DynamicArray::DynamicArray(unsigned int n)
{
  this->arr = new int[n];
  this->count = 0;
  this->size = n;
}
DynamicArray::DynamicArray(int *arr, unsigned int length)
{
  this->arr = new int[2 * length];
  this->count = length;
  this->size = 2 * length;

  for (int i = 0; i < length; i++)
  {
    this->arr[i] = arr[i];
  }
}

DynamicArray::~DynamicArray()
{
  delete[] this->arr;
}

int DynamicArray::getAt(unsigned int index) const
{
  return this->arr[index];
}

int DynamicArray::getSize() const
{
  return this->size;
}

int DynamicArray::getCount() const
{
  return this->count;
}

void DynamicArray::setAt(unsigned int index, int value)
{
  this->arr[index] = value;
}

void DynamicArray::Add(int number)
{
  if (this->count >= this->size)
  {
    int new_size = 2 * this->size;
    int *new_arr = new int[new_size];

    for (int i = 0; i < this->count; i++)
    {
      new_arr[i] = this->arr[i];
    }
    delete[] this->arr;
    this->arr = new_arr;
    this->size = new_size;
  }
  this->arr[this->count] = number;
  this->count++;
}

int DynamicArray::removeAt(unsigned int index)
{
  this->count--;
  for (int i = index; i < this->count; i++)
  {
    this->setAt(i, this->getAt(i + 1));
  }

  return this->count;
}
