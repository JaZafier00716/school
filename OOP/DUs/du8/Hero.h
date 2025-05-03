#pragma once
#include "Inventory.h"

class Hero
{
private:
  Inventory inventory;
public:
  Hero(const Inventory& inventory = {});

  // void AddItem(Item* item);
  Inventory* GetInventory();
};

