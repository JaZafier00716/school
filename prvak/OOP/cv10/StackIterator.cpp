// #include "StackIterator.h"


// template <typename T>
// StackIterator<T>::StackIterator(T* data, int top)
// {
//   this->data = data;
//   this->top = top;
// }

// template <typename T>
// StackIterator<T>::~StackIterator()
// {
//   delete[] this->data;
// }


// template <typename T>
// bool StackIterator<T>::hasNext() {
//   return this->top >= 0;
// }

// template <typename T>
// T StackIterator<T>::next()  {
//   return this->data[this->top--];
// }