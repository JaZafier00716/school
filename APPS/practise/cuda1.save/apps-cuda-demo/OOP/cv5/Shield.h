#pragma once

class Shield
{
private:
  int defence;
public:
  Shield();
  Shield(int defence);
  

  int GetDefence() const;
};
Shield::Shield() : defence(0){}
Shield::Shield(int defence): defence(defence) {}


int Shield::GetDefence() const {
  return this->defence;
}