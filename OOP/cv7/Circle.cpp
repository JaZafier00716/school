#include "Circle.h"


Circle::Circle(double r) {
  this->r = r;
}

double Circle::getArea() {

  return M_PI * this->r * this->r;
}

double Circle::getCircumference() {
  
  return M_PI * 2 * this->r;
}