#include "ExtendedArray.h"

unsigned int ExtendedArray::objectCounter = 0;

ExtendedArray::ExtendedArray(unsigned int capacity)
{
  this->capacity = capacity;
  this->counter = 0;
  this->arr = new int[capacity];
  objectCounter++;
}
void ExtendedArray::add(int value)
{
  if (this->counter < this->capacity)
  {
    this->arr[this->counter++] = value;
  }
}
ExtendedArray::~ExtendedArray()
{
  objectCounter--;
  delete[] this->arr;
}

ExtendedArray ExtendedArray::zeros(unsigned int amount)
{
  ExtendedArray result(amount);

  for (size_t i = 0; i < amount; i++)
  {
    result.add(0);
  }

  return result;
}