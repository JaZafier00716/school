#include "Hero.h"

Hero::Hero(const Inventory& inventory) : inventory(inventory) {}


Inventory* Hero::GetInventory() {
  return &(this->inventory);
}


// void Hero::AddItem(Item* item) {
//   this->inventory.addItem(item);
// }