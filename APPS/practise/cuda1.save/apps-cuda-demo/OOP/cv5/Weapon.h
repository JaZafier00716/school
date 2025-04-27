#pragma once
#include <iostream>
#include <string>
#include <time.h>
#include <stdio.h>
using namespace std;


class Weapon
{
private:
  double damage;
  int piercing;
public:
  Weapon(double damage);
  Weapon(double damage, int piercing);
  
  double GetDamage() const;
  int GetPiercing() const;
};

Weapon::Weapon(double damage) : damage(damage), piercing(0){}
Weapon::Weapon(double damage, int piercing) : damage(damage), piercing(piercing){}


double Weapon::GetDamage() const {
  return this->damage;
}
int Weapon::GetPiercing() const {
  return this->piercing;
}