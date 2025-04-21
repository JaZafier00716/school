#include "Hero.h"
#include <random>
using std::random_device, std::mt19937, std::uniform_int_distribution;

Hero::Hero(double HP, double baseDmd, string name, double critChance) : Sprite(HP, baseDmd) {
  this->name = name;
  this->critChance = critChance;
}

void Hero::setName(string name) {
  this->name = name;
}
string Hero::getName() {
  return this->name;
}

void Hero::setCritChance(double critChance) {
  this->critChance = critChance;
}
double Hero::getCritChance() {
  return this->critChance;
}

double Hero::calculateDamage(double dmg) {
	random_device rd;
  mt19937 gen(rd());
  uniform_int_distribution<> distr(0, 100);

  bool crit = 100*this->critChance <= distr(gen);

  return dmg * (crit ? CRIT_MULTIPLIER : 1);
}