#include <iostream>
using namespace std;


void insertionSort(int *arr, int N) {
  for (int i = 1; i < N; i++)
  {
    int key = arr[i];
    int j = i-1;

    while(j >= 0 && arr[j] > key) {
      arr[j+1] = arr[j];

      j--;
    }
    arr[j+1] = key;
  }
}


int binSearchRec(int *arr, int l, int r, int a) {
  if(l > r) {
    return -1;
  }
  int m = (r+r)/2;
  if(arr[m] == a) {
    return m;
  } 

  if(arr[m] > a) {
    return binSearchRec(arr, l, m-1, a);
  } else {
    return binSearchRec(arr, m+1, r, a);
  }
}

int binSearchRec(int *arr, int N, int a) {
  return binSearchRec(arr, 0, N, a);
}

int binarySearch(int *arr, int N, int a) {
  int l = 0;
  int r = N;
  while(l <= r) {
    int m = (l+r)/2;
    if(arr[m] == a) {
      return m;
    }
    if(arr[m] > a) {
      r = m-1;
    } else {
      l = m+1;
    }
  }
  return -1;
}

int main () {
  int arr[10] = {0, 15, 9, 4, 8, 10, 16, 32, 2, 11};

  insertionSort(arr, 10);
  
  cout << "Arr:" << endl;
  for(int i = 0; i < 10; i++) {
    cout << arr[i] << " ";
  }
  cout << endl;

  int value = 5;
  cout << "Value " << value << (binSearchRec(arr, 10, value) >= 0 ? " " : " NOT ") << "found" << endl;

  return 0;
}