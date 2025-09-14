#include "Weapon.h"

Weapon::Weapon(const string& name, double bonusDamage) : Item(name), bonusDamage(bonusDamage){}

string Weapon::ToString()
{
  return "Weapon:\t" + this->getName() + "\n" + "\t bonus damage:\t" + to_string(this->bonusDamage);
}