#include "Monster.h"


Monster::Monster(double HP, double baseDmg, double damageResistance) : Sprite(HP, baseDmg) {
  this->damageResistance = damageResistance;
}

double Monster::getDamageResistance()
{
  return this->damageResistance;
}

void Monster::setDamageResistance(double damageResistance)
{
  this->damageResistance = damageResistance;
}

double Monster::calculateDamage(double dmg) {
	return dmg * this->damageResistance;
}