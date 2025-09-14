#include "Set.h"

int main () {
  Set first(5);
  Set second(4);

  if(!first.addNum(1)) {
    cout << "Could not add num" << endl;
  };
  if(!first.addNum(1)) {  // duplicite
    cout << "Could not add num" << endl;
  };
  if(!first.addNum(2)) {
    cout << "Could not add num" << endl;
  };

  if(!first.addNum(3)) {
    cout << "Could not add num" << endl;
  };

  if(!first.addNum(4)) {
    cout << "Could not add num" << endl;
  };

  if(!first.addNum(5)) {
    cout << "Could not add num" << endl;
  };

  if(!first.addNum(1)) {  // arr is full
    cout << "Could not add num" << endl;
  };

  for (int i = 0; i < 4; i++)
  {
    second.addNum(i*2);
  }

  cout << "First set:" << endl;
  cout << first << endl;
  
  cout << "Second set:" << endl;
  cout << second << endl;
  
  Set *union_set = first.unionSets(&second);

  cout << "Union set:" << endl;
  cout << *union_set << endl;

  delete union_set;


  Set *complement = first.complement(&second);

  cout << "Union set:" << endl;
  cout << *complement << endl;

  delete complement;

  return 0;
}