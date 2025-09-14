#ifndef FUNKCE_H
#define FUNKCE_H
#include <iostream>
using namespace std;

class Node
{
private:
  int key;
  double value;

  Node *left;  // Smaller
  Node *right; // Larger
public:
  Node();
  Node(int key, double value);
  ~Node();

  void SetLeft(Node *new_node);
  void SetRight(Node *new_node);
  void SetKey(int key);
  void SetValue(double value);
  
  Node* GetLeft();
  Node* GetRight();

  void CreateNext(int key, double value);

  void PrintTree();
  void AltPrint(int level);
};


#endif // !FUNKCE_H