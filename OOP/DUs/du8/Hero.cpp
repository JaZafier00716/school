#include "Hero.h"

Hero::Hero(const Inventory& inventory) : inventory(inventory) {}


Inventory* Hero::GetInventory() {
  return &(this->inventory);
}
