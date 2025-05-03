#pragma once
#include "Item.h"

class Weapon : public Item
{
private:
  double bonusDamage;
public:
  Weapon(const string& name="", double bonusDamage = 0);
  string ToString() override;
};

