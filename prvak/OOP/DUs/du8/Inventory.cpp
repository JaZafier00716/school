#include "Inventory.h"
#include "iostream"


Inventory::Inventory(const vector<Item*>& items) : items(items) {}

Inventory::~Inventory()
{
  for(auto item : this->items) {
    delete item;
  }
}


void Inventory::PrintAllItems() {
  for(const auto item : this->items) {
    cout << item->ToString() << endl;
  }
}


void Inventory::addItem(Item* item) {
  this->items.push_back(item);
}