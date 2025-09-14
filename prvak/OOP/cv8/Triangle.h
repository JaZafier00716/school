#pragma once
#include "Shape.h"
#include <cmath>


class Triangle : public Shape
{
private:
  double a, b, c, v;
public:
  Triangle(double a, double b, double c, double va) {
    this->a = a;
    this->b = b;
    this->c = c;
    this->v = va;
  }
  double getCircumference() {
    return a+b+c;
  }
  double getArea() {
    return (a * v)/2;
  }
};
