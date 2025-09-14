#pragma once
#include <string>
using namespace std;

class Item
{
private:
  string itemName;

protected:
  string getName() const;
  Item(const string& name = "");

public:
  virtual ~Item() = default;

  virtual string ToString() = 0;
};
