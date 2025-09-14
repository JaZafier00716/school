// #include "Stack.h"
// #include "StackIterator.h"

// template <typename T>
// Stack<T>::Stack(int size) {
//   this->size = size;
//   this->top = -1;
//   this->data = new T[this->size];
// }

// template <typename T>
// Stack<T>::~Stack() {
//   delete[] this->data;
// }

// template <typename T>
// void Stack<T>::push(T value) {
//   this->data[++this->top] = value;
// }


// template <typename T>
// IIterator* Stack<T>::getIterator() {
//   return new StackIterator(this->data, this->top);
// }