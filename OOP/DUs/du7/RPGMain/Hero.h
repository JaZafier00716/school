#pragma once
#include "Sprite.h"
#define CRIT_MULTIPLIER 5

class Hero : public Sprite
{
private:
  string name;
  double critChance; // number between 0 and 1
public:
  Hero(double HP, double baseDmd, string name, double critChance = 0.0);
  void setName(string name);
  string getName();
  void setCritChance(double critChance);
  double getCritChance();
  double Hero::calculateDamage(double dmg) override;
};

