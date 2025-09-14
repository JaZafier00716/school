#pragma once
#include "Shape.cpp"

class Rectangle : public Shape
{
private:
  double a, b;
public:
  Rectangle(double a, double b);
  double getArea() override;  // prekryti - prepsani funkcionality metody z parent class v potomkovi
  double getCircumference() override; // override - kontroluje prekryti 

};
