#pragma once
#include "Weapon.h"
#include "Shield.h"

class Bojovnik
{
private:
  string name;
  Weapon weapon;
  Shield shield;

  double health;

public:
  Bojovnik(string name);
  Bojovnik(string name, double health);
  Bojovnik(string name, Weapon weapon);
  Bojovnik(string name, Shield shield);
  Bojovnik(string name, Shield shield, double health);
  Bojovnik(string name, Weapon weapon, double health);
  Bojovnik(string name, Weapon weapon, Shield shield);
  Bojovnik(string name, Weapon weapon, Shield shield, double health);

  string GetName();

  double attack(Bojovnik* bojovnik);  // return amount of damage dealt
  void takeDamage(double damage);
  bool dead() const;
};


Bojovnik::Bojovnik(string name) : name(name), weapon(Weapon(1)), shield(Shield(0)), health(100) {}
Bojovnik::Bojovnik(string name, double health) : name(name), weapon(Weapon(1)), shield(Shield(0)), health(health) {}
Bojovnik::Bojovnik(string name, Weapon weapon) : name(name), weapon(weapon), shield(Shield(0)), health(100) {}
Bojovnik::Bojovnik(string name, Shield shield) : name(name), weapon(Weapon(1)), shield(shield), health(100) {}
Bojovnik::Bojovnik(string name, Shield shield, double health) : name(name), weapon(Weapon(1)), shield(shield), health(health) {}
Bojovnik::Bojovnik(string name, Weapon weapon, double health) : name(name), weapon(weapon), shield(Shield(0)), health(health) {}
Bojovnik::Bojovnik(string name, Weapon weapon, Shield shield) : name(name), weapon(weapon), shield(shield), health(100){}
Bojovnik::Bojovnik(string name, Weapon weapon, Shield shield, double health) : name(name), weapon(weapon), shield(shield), health(health){}


double Bojovnik::attack(Bojovnik* bojovnik) {
  double totalDamage = this->weapon.GetPiercing() * this->weapon.GetDamage();
  int defence = bojovnik->shield.GetDefence();
  if(defence > 0) {
    int x = rand() % defence;
    totalDamage = abs(totalDamage - x);

    bojovnik->takeDamage(totalDamage);
  } else {
    bojovnik->takeDamage(totalDamage);
  }

  return totalDamage;
}



void Bojovnik::takeDamage(double damage) {
  this->health -= damage;
}

bool Bojovnik::dead() const {
  return this->health <= 0;
}

string Bojovnik::GetName() {
  return this->name;
}