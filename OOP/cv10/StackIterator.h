#pragma once
#include "IIterator.h"


template <typename T>
class StackIterator : public IIterator<T>
{
private:
  T *data;
  int top;
public:
  StackIterator(T* data, int top);
  ~StackIterator();
  bool hasNext() override;
  T next() override;
};

template <typename T>
StackIterator<T>::StackIterator(T* data, int top)
{
  this->data = data;
  this->top = top;
}

template <typename T>
StackIterator<T>::~StackIterator()
{
  delete[] this->data;
}


template <typename T>
bool StackIterator<T>::hasNext() {
  return this->top >= 0;
}

template <typename T>
T StackIterator<T>::next()  {
  return this->data[this->top--];
}

