#include <iostream>
#include <stack>
#include <climits>
#include <cmath>
#include <string>
using namespace std;

typedef struct Stack
{
  int capacity = 4;
  int *data = nullptr;
  int index = -1;
  string name;
  Stack();
  Stack(int capacity)
  {
    this->name;
    this->capacity = capacity;
    this->data = new int[this->capacity];
    // malloc vyhradi pamet
    // new navic zavola konstruktor na kazdy novy prvek
    // v nasem pripade konstruktor intu
  }
  Stack(int capacity, string name)
  {
    this->name = name;
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
      // cout << "Stack is empty" << endl;
      return INT_MAX;
    }
  }

  int pull()
  {
    if (this->index >= 0)
    {
      int data = this->data[0];
      for (int i = 0; i < this->index; i++)
      {
        this->data[i] = this->data[i + 1];
      }
      this->index--;
      return data;
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
  Stack stack(5);

  stack.Push(1);
  stack.Push(2);
  stack.Push(3);
  stack.Push(4);
  stack.Push(5);
  cout << stack.pull() << endl;

  cout << stack.pull() << endl;

  cout << stack.pull() << endl;

  cout << stack.pull() << endl;

  cout << stack.pull() << endl;

  return 0;
}

void erastothenes(int N, int *L)    // vysledna slozitost je n * log (log n)
{
  int *A = new int[N + 1];

  // Naplneni pole
  for (int p = 2; p <= N; p++)  // linearni - ale neresi algoritmus, takze podle nej nepocitame slozitost
  {
    A[p] = p;
  }

  // Prochazeni pole
  for (int p = 2; p <= floor(sqrt(N)); p++) // logiratmicky
  {
    if (A[p] != 0)
    {
      // int j = p*p;  // Protoze mensi nasobky jsou jiz vyeliminovane
      // p*p meni slozitost nasledujiciho cyklu z linearniho na logiratmicky, jelikoz slozitost se exponencialne snizuje
      for (int j = p * p; j <= N; j += p) // linearni
      {
        A[j] = 0;
      }
    }
  }

  int i = 0;
  for (int p = 2; p <= N; p++)  // linearni - ale neresi algoritmus, takze podle nej nepocitame slozitost
  {
    if (A[p] != 0)
    {
      L[i++] = A[p];
    }
  }
  delete[] A;
}

int main3()
{
  int N = 100;
  int *L = new int[N];
  // Linearni zavislost
  for (int i = 0; i < N; i++)
  {
    L[i] = 0;
  }

  erastothenes(N, L);
  int i = 0;
  while (L[i] > 0)
  {
    cout << L[i++] << "\t";
  }
  cout << endl;

  delete[] L;
  L = nullptr;

  return 0;
}

void hanoi(int count, int src, int dest, int help)
{
  if (count == 1)
  {
    cout << "move: " << count << " from: " << src << " to: " << dest << endl;
    return;
  }
  hanoi(count - 1, src, help, dest);
  cout << "move: " << count << " from: " << src << " to: " << dest << endl;
  hanoi(count - 1, help, dest, src);
}

int main4()
{
  hanoi(4, 1, 3, 2);
  return 0;
}

int find(int *pole, int N, int x) { // Worst case: linear, Best case: constant (on 1st position) A
  for(int i = 0; i < N; i++) {
    if(pole[i] == x) {
      return i;
    }
  }
  return -1;
}

void unique(int *pole, int N) {
  cout << "Unique: ";
  int found = 0;
  for (int i = 0; i < N; i++)
  {
    found = 0;
    for (int j = 0; j < N; j++)
    {
      if(pole[i] == pole[j] && j != i) {
        found = 1;
        break;
      }
    }
    if(found == 0) {
      cout << pole[i] << "\t";
    }
    
  }
}


void unique2(int *pole, int N) {
  cout << "Unique: ";
  int found = 0;

  bool *u = new bool[N];
  int *result = new int[N];
  for (int i = 0; i < N; i++)
  {
    u[i] = true;
  }
  

  for (int i = 0; i < N; i++)
  {
    for (int j = i+1; j < N; j++)
    {
      if(pole[i] == pole[j]) {
        u[i] = false;
        u[j] = false;
        break;
      }
    }
  }


  int idx = 0;
  for (int i = 0; i < N; i++)
  {
    if(u[i]) {
      result[idx] = pole[i];
    }
  }

  delete[] u;
}

int fact(int n) {
  if(n == 0) {
    return 1;
  }
  return n * fact(n-1);
}

int main() {
  srand((unsigned int)time(nullptr));

  // int N = 10;
  // int *arr = new int[N];

  // int value = 5;

  // for (int i = 0; i < N; i++)
  // {
  //   arr[i] = rand() % N;
  //   cout << arr[i] << "\t";
  // }
  // cout << endl;

  // cout << "Value: " << value << " found at: " << find(arr, N, value) << endl;
  

  // int pole[] = {1,2,1,1,3,4,3,5,6,8,5,9};

  cout << fact(5);

  // unique(pole, 12);
}
