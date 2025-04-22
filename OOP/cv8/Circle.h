#pragma once
#include "Shape.h"
#include <cmath>

class Circle : public Shape
{
private:
  double r;
public:
  Circle(double r);
  double getArea() override;
  double getCircumference() override

};

