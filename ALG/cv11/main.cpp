#include <iostream>
#include <climits>
using namespace std;

void printArray(int*arr, int N) {
  for (int i = 0; i < N; i++)
  {
    cout << arr[i] << "\t";
  }
  cout << endl;
  
}

void permutace(int*arr, int N, int l, int r) {
  if(l==r) {
    printArray(arr, N);
  }

  for(int i = l; i <=r;i++) {
    swap(arr[i], arr[l]);
    permutace(arr, N, l+1, r);
    swap(arr[i], arr[l]);
  }
}

int partition(int *arr, int l, int r) {
  int pivot = arr[r];
  int j = l-1;
  for (int i = l; i < r; i++)
  {
    if(arr[i] < pivot) {
      j++;
      swap(arr[i], arr[j]);
    }
  }
  j++;
  swap(arr[r], arr[j]);
  return j;  
}

int kadane(int *arr, int N) {
  int max_sum = INT_MIN;
  int curr_sum = 0;

  for (int i = 0; i < N; i++)
  {
    curr_sum = max(curr_sum + arr[i], arr[i]);
    max_sum = max(curr_sum, max_sum);
  }
  return max_sum;
}


void quickSort(int *arr, int l, int r) {
  if(l >=r) {
    return;
  }

  int pivotIndex = partition(arr, l, r);

  quickSort(arr, l, pivotIndex-1);
  quickSort(arr, pivotIndex+1, r);
}


void initArray(int *arr, int N) {
  for (int i = 0; i < N; i++)
  {
    arr[i] = rand() % N * (rand() %2 ? -1 : 1);
  }
}

int main () {
  int N = 3;
  // int arr[N] = {1,2,3};

  // permutace(arr, N, 0, N-1);


  N = 5;
  int arr[N];

  initArray(arr, N);

  quickSort(arr, 0, N-1);

  printArray(arr, N);

  return 0;
}