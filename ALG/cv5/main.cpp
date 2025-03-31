#include <iostream>
#include <stack>
#include <climits>
#include <cmath>
#include <string>
using namespace std;

bool isMoveValid(const stack<int> &src, const stack<int> &dest) {
  return !src.empty() && (dest.empty() || src.top() < dest.top());
}

void moveDisk(stack<int> &src, stack<int> &dest)
{
  if(isMoveValid(src, dest)) {
    dest.push(src.top());
    src.pop();
  } else {
    src.push(dest.top());
    dest.pop();
  }

}


void printTowers(stack<int> A, stack<int> B, stack<int> C, int N)
{
  for (int i = N; i > 0; i--)
  {
    if (A.size() >= i)
    {
      cout << A.top() << "\t";
      A.pop();
    }
    else
    {
      cout << "|\t";
    }
    if (B.size() >= i)
    {
      cout << B.top() << "\t";
      B.pop();
    }
    else
    {
      cout << "|\t";
    }
    
    if (C.size() >= i)
    {
      cout << C.top() << "\t" << endl;
      C.pop();
    }
    else
    {
      cout << "|" << endl;
    }
  }
  cout << endl;
}
void towersOfHanoi(stack<int> &src, stack<int> &aux, stack<int> &dest, int N)
{
  if (N == 1)
  {
    moveDisk(src, dest);
    return;
  }

  towersOfHanoi(src, dest, aux, N - 1); // presunu n-1 disku z src na aux pomoci dest veze
  printTowers(src, aux, dest, 4);
  moveDisk(src, dest);  // presunu posledni disk na cil
  printTowers(src, aux, dest, 4);
  towersOfHanoi(aux, src, dest, N-1); // presunu z aux do dest pomoci src
}

int main2()
{
  int N = 4;

  stack<int> A;
  stack<int> B;
  stack<int> C;

  for (int i = N; i > 0; i--)
  {
    A.push(i);
  }

  // B.push(5);

  // C.push(4);
  // C.push(3);

  printTowers(A, B, C, N);
  towersOfHanoi(A, B, C, N);
  printTowers(A, B, C, N);

  return 0;
}


void bubblesort1(int*arr, int N) {
  for (int i = 0; i < N; i++)
  {
    for (int j = 0; j < N-1; j++)
    {
      if(arr[j] > arr[j+1]) {
        int tmp = arr[j];
        arr[j] = arr[j+1];
        arr[j+1] = tmp;
      }
    }
    
  }
}


void bubblesort2(int*arr, int N) {
  for (int i = 0; i < N; i++)
  {
    for (int j = 0; j < N-1-i; j++)   // posledni hodnoty jsou serazene
    {
      if(arr[j] > arr[j+1]) {
        int tmp = arr[j];
        arr[j] = arr[j+1];
        arr[j+1] = tmp;
      }
    }
    
  }
}


void bubblesort3(int*arr, int N) {
  for (int i = 0; i < N; i++)
  {
    for (int j = 0; j < N-1-i; j++)   // posledni hodnoty jsou serazene
    {
      if(arr[j] > arr[j+1]) {
        int tmp = arr[j];
        arr[j] = arr[j+1];
        arr[j+1] = tmp;
      }
    }
  }
}

void bubblesort(int *arr, int N) {
  bool change;

  do
  {
    change = false;
    for (int i = 0; i < N-1; i++)
    {
      if(arr[i] > arr[i+1]) {
        int temp = arr[i];
        arr[i] = arr[i+1];
        arr[i+1] = temp;
        change = true;
      }
    }
  } while (change);
  
  

}


int main() {
  int arr[] = {1,5,4,9,0,3};

  bubblesort1(arr, 6);

  for (int i = 0; i < 6; i++)
  {
    cout << arr[i] << ", ";
  }
  cout << endl;
  
}




