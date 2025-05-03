#include <iostream>
#include "Hero.h"
#include "Weapon.h"
#include "Potion.h"
using namespace std;

int main() {
    Hero hero;

    hero.GetInventory()->addItem(new Weapon("Fiery sword of purgatory", 15));
    hero.GetInventory()->addItem(new Potion("Elixir of life", 50));

    hero.GetInventory()->PrintAllItems();

    return 0;
}
