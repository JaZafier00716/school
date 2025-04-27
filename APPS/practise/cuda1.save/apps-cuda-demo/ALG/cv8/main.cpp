#include "BSTree.h"


int main () {
  BSTree groot;

  groot.iAmGroot(10);
  groot.iAmGroot(5);
  groot.iAmGroot(30);
  groot.iAmGroot(1);
  groot.iAmGroot(7);
  groot.iAmGroot(20);
  groot.iAmGroot(12);
  groot.iAmGroot(25);
  groot.iAmGroot(11);
  groot.iAmGroot(15);

  cout << "root count:\t" << groot.countRoots() << endl;
  groot.printSorted(false);
  cout << endl;
  groot.printSorted();
  cout << endl;
  cout << "height:\t" << groot.height() << endl;;


  cout << "Is balanced:\t" << groot.isBalanced() << endl;
  return 0;
}