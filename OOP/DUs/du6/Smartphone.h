#pragma once
#include "Elektronika.h"

class Smartphone : public Elektronika
{
private:
  string znacka;
  double velikost;
  int kapacita_baterie;
public:
  Smartphone(
    string znacka, double velikost, int kapacita_baterie) : znacka(znacka), velikost(velikost), kapacita_baterie(kapacita_baterie){};

  void printParameters() const override {
      cout << "Smartphone:" << endl;
      cout << " - znacka: " << this->znacka << endl;
      cout << " - velikost: " << this->velikost << "\"" << endl;
      cout << " - kapacita baterie: " << this->kapacita_baterie << "mAh" << endl;
    }
};

