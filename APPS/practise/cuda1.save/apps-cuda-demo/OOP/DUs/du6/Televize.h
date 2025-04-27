#pragma once
#include "Elektronika.h"

class Televize : public Elektronika
{
private:
  double uhlopricka;
  string technologie;
  string rozliseni;
public:
  Televize(double uhlopricka, string technologie, string rozliseni) : uhlopricka(uhlopricka), technologie(technologie), rozliseni(rozliseni){};

  void printParameters() const override {
    cout << "Televize:" << endl;
    cout << " - Uhlopricka: " << this->uhlopricka << "\"" << endl;
    cout << " - Technologie: " << this->technologie << endl;
    cout << " - Rozliseni: " << this->rozliseni << endl;
  }
};

