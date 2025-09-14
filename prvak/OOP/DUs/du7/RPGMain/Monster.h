#pragma once
#include "Sprite.h"

class Monster : public Sprite
{
private:
  double damageResistance; // number between 0 and 1

public:
Monster(double HP, double baseDmg, double damageResistance);
  double getDamageResistance();
  void setDamageResistance(double damageResistance);
  double calculateDamage(double dmg) override;
};
