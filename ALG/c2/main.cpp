#include <iostream>
#include <stack>
#include <climits>
using namespace std;

typedef struct Stack
{
  int capacity = 4;
  int *data = nullptr;
  int index = -1;
  Stack();
  Stack(int capacity)
  {
    this->capacity = capacity;
    this->data = new int[this->capacity];
    // malloc vyhradi pamet
    // new navic zavola konstruktor na kazdy novy prvek
    // v nasem pripade konstruktor intu
  }
  ~Stack()
  {
    delete[] this->data; // nezapomen na zavorky!!! jinak dealokuje jen pamet prvniho prvku ne celeho pole
  }

  void Push(int value)
  {
    if (this->index < this->capacity - 1)
    {
      this->index++;
      this->data[this->index] = value;
    }
    else
    {
      cout << "Stack is full" << endl;
    }
  }

  int pop()
  {
    if (this->index >= 0)
    {
      return this->data[this->index--];
    }
    else
    {
      cout << "Stack is empty" << endl;
      return INT_MAX;
    }
  }
} Stack;

int main2()
{
  Stack stack(4);

  stack.Push(1);
  stack.Push(1);
  stack.Push(1);
  stack.Push(1);
  stack.Push(1);

  cout << stack.pop() << endl;
  cout << stack.pop() << endl;
  cout << stack.pop() << endl;
  cout << stack.pop() << endl;
  cout << stack.pop() << endl;

  return 0;
}

bool check_parity(string str)
{
  Stack stack(str.length());

  for (int i = 0; i < str.length(); i++)
  {
    if (str[i] == '(')
    {
      stack.Push(str[i]);
    }
    if (str[i] == ')')
    {
      if(stack.pop() == INT_MAX) {
        return false;
      }
      
    }
  }
  return stack.index <= -1;
}

int main()
{
  string str = "((.) (.))";
  int counter = 0;

  cout << check_parity(str) << endl;



  return 0;
}
