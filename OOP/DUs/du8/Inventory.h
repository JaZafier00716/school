#pragma once
#include <vector>
#include "Item.h"

class Inventory
{
private:
  vector<Item*> items;
public:
  Inventory(const vector<Item*>& items = {});
  ~Inventory();
  void addItem(Item* item);
  void PrintAllItems();
};

