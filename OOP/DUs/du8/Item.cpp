#include "Item.h"


Item::Item(const string& name) {
  this->itemName = name;
}

string Item::getName() const {
  return this->itemName;
}