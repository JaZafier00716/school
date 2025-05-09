#pragma once
#include "IIterator.h"

template<typename T>
class ICollection
{
public:
  virtual IIterator<T>* getIterator() = 0;
};

