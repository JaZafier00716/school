/* 
prevzeti chovani a vlastnosti jine tridy

prenasi se uplne vsechno

protected porusuje zapouzdreni - i presto ze z ni dedi, porad je to jina trida
*/

// #include "Circle.h"
// #include "Square.h"
#include "Triangle.h"
#include <iostream>
using std::cout, std::endl;

int main () {
  // Rectangle r(5,4);

  // Shape *sh = new Rectangle(5,4);
  // // Rectangle *r2 = new Shape();
  // Square sq(6);
  // Circle c(1);


  // cout << r.getArea() << endl;
  // // cout << sh.getArea() << endl;
  // cout << sh->getArea() << endl;
  // cout << sq.getArea() << endl;
  // cout << c.getArea() << endl;

  Triangle t(3,4,5, 4);

  cout << t.getCircumference() << ", " << t.getArea() << endl;



  return 0;
}