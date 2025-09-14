#include <iostream>
#include "ICollection.h"
#include "Stack.h"
using std::cout, std::endl;

template<typename T>
void printCollection(ICollection<T>* collection) {
  IIterator<T>* it = collection->getIterator();
  
  while(it->hasNext()) {
    cout << it->next() << endl;
  }
}


int main () {
  Stack<double> s(6);

  s.push(4.6);
  s.push(5);
  s.push(2);
  s.push(3.9);
  s.push(14);
  s.push(40);

  printCollection(&s);
  return 0;
}


