#pragma once
#include "ICollection.h"
#include "StackIterator.h"

template <typename T>
class Stack : public ICollection<T>
{
private:
  T *data;
  int top;
  int size;
public:
  Stack(int size);
  ~Stack();
  void push(T value);
  virtual IIterator<T>* getIterator() override;
};

template <typename T>
Stack<T>::Stack(int size) {
  this->size = size;
  this->top = -1;
  this->data = new T[this->size];
}

template <typename T>
Stack<T>::~Stack() {
  delete[] this->data;
}

template <typename T>
void Stack<T>::push(T value) {
  this->data[++this->top] = value;
}


template <typename T>
IIterator<T>* Stack<T>::getIterator() {
  return new StackIterator<T>(this->data, this->top);
}

