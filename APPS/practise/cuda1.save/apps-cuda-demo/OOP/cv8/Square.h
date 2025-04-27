#pragma once
#include "Rectangle.h"

class Square : public Rectangle
{
private:  
public:
  Square(double a) : Rectangle(a, a) {}
};

