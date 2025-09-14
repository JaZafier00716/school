#include "Potion.h"


Potion::Potion(const string& name, double healAmount) : Item(name), healAmount(healAmount) {}


string Potion::ToString()
{
  return "Potion:\t" + this->getName() + "\n" + "\t heal amount:\t" + to_string(this->healAmount);
}