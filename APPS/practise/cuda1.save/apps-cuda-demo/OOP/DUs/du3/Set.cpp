#include "Set.h"

Set::Set(int length) : length(length), count(0)
{
  this->nums = new int[length];
}

Set::~Set()
{
  delete[] this->nums;
}

int Set::getCount() const
{
  return this->count;
}

int Set::getAt(int i) const
{
  return this->nums[i];
}

bool Set::addNum(int num)
{
  if (this->count < this->length)
  {
    for (int i = 0; i < this->count; i++)
    {
      if(this->nums[i] == num) {
        return false;
      }
    }
    
    this->nums[this->count++] = num;
    return true;
  } else {
    return false;
  }
}

Set* Set::unionSets(Set* secondSet) const {
  Set *new_set = new Set(this->getCount() + secondSet->getCount());

  // First arr
  for (int i = 0; i < this->getCount(); i++)
  {
    new_set->addNum(this->getAt(i));
  }
  
  // Second arr
  for (int i = 0; i < secondSet->getCount(); i++)
  {
    new_set->addNum(secondSet->getAt(i));
  }

  return new_set;
}

Set* Set::complement(Set* secondSet) const {
  Set *new_set = new Set(this->getCount());

  for (int i = 0; i < this->getCount(); i++)
  {
    bool in_second = false;
    for (int j = 0; j < secondSet->getCount(); j++)
    {
      if(this->getAt(i) == secondSet->getAt(j)) {
        in_second = true;
        break;
      }
    }
    if(!in_second) {
      new_set->addNum(this->getAt(i));
    }
  }
  return new_set;
}

ostream &operator<< (ostream &print, const Set &s) {
  for (int i = 0; i < s.getCount(); i++)
  {
    print << s.getAt(i) << " ";
  }
  return print;  
}
