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
  static int counter;
  static int defaultDamage;
  static int defaultHP;
  static Bojovnik defaultBojovnik();
  Bojovnik(string name);
  Bojovnik(string name, double health);
  Bojovnik(string name, Weapon weapon);
  Bojovnik(string name, Shield shield);
  Bojovnik(string name, Shield shield, double health);
  Bojovnik(string name, Weapon weapon, double health);
  Bojovnik(string name, Weapon weapon, Shield shield);
  Bojovnik(string name, Weapon weapon, Shield shield, double health);
  ~Bojovnik();

  string GetName();

  double attack(Bojovnik* bojovnik);  // return amount of damage dealt
  void takeDamage(double damage);
  bool dead() const;
};

int Bojovnik::counter = 0;
int Bojovnik::defaultDamage = 1;
int Bojovnik::defaultHP = 100;

Bojovnik::Bojovnik(string name) : name(name), weapon(Weapon(defaultDamage)), shield(Shield(0)) {
  this->health = defaultHP;
  counter++;
}
Bojovnik::Bojovnik(string name, double health) : name(name), weapon(Weapon(defaultDamage)), shield(Shield(0)), health(health) {
  counter++;
}
Bojovnik::Bojovnik(string name, Weapon weapon) : name(name), weapon(weapon), shield(Shield(0)){
  counter++;
}
Bojovnik::Bojovnik(string name, Shield shield) : name(name), weapon(Weapon(defaultDamage)), shield(shield) {
  this->health = defaultHP;
  counter++;
}
Bojovnik::Bojovnik(string name, Shield shield, double health) : name(name), weapon(Weapon(defaultDamage)), shield(shield), health(health) {
  counter++;
}
Bojovnik::Bojovnik(string name, Weapon weapon, double health) : name(name), weapon(weapon), shield(Shield(0)), health(health) {
  counter++;
}
Bojovnik::Bojovnik(string name, Weapon weapon, Shield shield) : name(name), weapon(weapon), shield(shield) {
  this->health = defaultHP;
  counter++;
}
Bojovnik::Bojovnik(string name, Weapon weapon, Shield shield, double health) : name(name), weapon(weapon), shield(shield), health(health){
  counter++;
}
Bojovnik::~Bojovnik() {
  counter--;
}

Bojovnik Bojovnik::defaultBojovnik() {
  Bojovnik bojovnik("Bojovnik");

  return bojovnik;
}

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