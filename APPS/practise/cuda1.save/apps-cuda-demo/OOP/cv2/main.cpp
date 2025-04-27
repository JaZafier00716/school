#include <iostream>
#include <math.h>
using std::cin, std::cout, std::endl;

class Vector
{
private:
  double x, y;
public:
  Vector() {
    this->x = 0;
    this->y = 0;
  };
  Vector(double x, double y) {
    this->x = x;
    this->y = y;
  }
  void SetVector(double x, double y) {
    this->x = x;
    this->y = y;
  }
  double GetX() {
    return this->x;
  };
  double GetY() {
    return this->y;
  }; 
  double GetSize() {
    return sqrt(pow(this->x, 2) + pow(this->y, 2));
  }
};


class Point
{
private:
  double x, y;

public:
  Point() {
    this->x = 0;
    this->y = 0;
  };
  Point(double x, double y){
    this->x = x;
    this->y = y;
  };
  void SetPoint(double x, double y) {
    this->x = x;
    this->y = y;
  };
  double GetX() {
    return this->x;
  };
  double GetY() {
    return this->y;
  };
  Vector GetVector(Point b) {
    Vector v;
    v.SetVector(b.GetX() - this->x, b.GetY() - this->y);

    return v;
  };
};



int main()
{
  Point a(5, 4), b(-3, 7.2);

  Vector v = a.GetVector(b);

  cout << " A  = [" << a.GetX() << ", " << a.GetY() << "]" << endl;
  cout << " B  = [" << b.GetX() << ", " << b.GetY() << "]" << endl;
  cout << " v  = (" << v.GetX() << ", " << v.GetY() << ")" << endl;
  cout << "|v| = " << v.GetSize() << endl; 

  return 0;
}