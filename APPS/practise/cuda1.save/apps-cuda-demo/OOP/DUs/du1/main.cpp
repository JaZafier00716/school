#include "funkce.h"

int main () {
  Node *first = new Node();
  double value;
  int id = 0;
  int count;

  cout << "Enter first node:\t";
  cin >> value;

  first->SetKey(id);
  first->SetValue(value);
  id++;

  cout << "Enter number of nodes to be added:\t";
  cin >> count;

  for (int i = 0; i < count; i++, id++)
  {
    cout << "Enter value:\t";
    cin >> value;
    first->CreateNext(id, value);
  }

  cout << "print:" << endl;
  first->PrintTree();
  cout << endl << endl;
  first->AltPrint(0);

  return 0;
}
