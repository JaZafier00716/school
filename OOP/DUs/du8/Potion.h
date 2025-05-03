#pragma once
#include "Item.h"


class Potion : public Item
{
private:
  double healAmount;
public:
  Potion(const string& name="", double healAmount = 0);
  string ToString() override;
};

