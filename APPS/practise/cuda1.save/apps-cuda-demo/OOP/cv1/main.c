#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdbool.h>

int find_max(int arr[], int count);
int dec_to_oct(int num);
bool palindrome(char *str);
bool mul_matrices(int **m1, int m1_r, int m1_c, int **m2, int m2_r, int m2_c, int **m);
void print_matrice(int **m, int m_r, int m_c);
void allocate_2D(int ***a, int rows, int cols);
void deallocate_2d(int ***a, int rows);
void set_matrice(int **m, int m_r, int m_c);

int main()
{
  int arr[] = {1, 35, 5, 60, 8, 7, 4, 2, 25};
  int count = 9;
  char not_pal[] = "ahoj";
  char pal[] = "abcba";
  int m1_r = 3, m1_c = 2;
  int m2_r = 2, m2_c = 4;
  int **m1, **m2, **m;
  
  
  allocate_2D(&m1, m1_r, m1_c);
  allocate_2D(&m2, m2_r, m2_c);
  allocate_2D(&m, m1_r, m2_c);
  
  //Find max
  printf("MAX: %d\n", find_max(arr, count));


  // Dec to oct
  int num;
  printf("Enter number for conversion: ");
  scanf("%d", &num);
  printf("%d is %d in oct", num, dec_to_oct(num));

  // Palindrome
  bool pl;
  pl = palindrome(not_pal);
  if (pl)
  {
    printf("%s is palindrome\n", not_pal);
  }
  else
  {
    printf("%s is not palindrome\n", not_pal);
  }

  pl = palindrome(pal);
  if (pl)
  {
    printf("%s is palindrome\n", pal);
  }
  else
  {
    printf("%s is not palindrome\n", pal);
  }


  // Multiply matrices
  printf("Enter values for m1:\n");
  set_matrice(m1, m1_r, m1_c);
  printf("Enter values for m2:\n");
  set_matrice(m2, m2_r, m2_c);

  pl = mul_matrices(m1, m1_r, m1_c, m2, m2_r, m2_c, m);

  if (!pl)
  {
    printf("You cannot multiply those matrices\n");
  } else {
    print_matrice(m, m1_r, m2_c);
  }
  deallocate_2d(&m1, m1_r);
  deallocate_2d(&m, m1_r);
  deallocate_2d(&m2, m2_r);

  return 0;
}

int find_max(int arr[], int count)
{
  int max = arr[0];
  for (int i = 0; i < count; i++)
  {
    if (arr[i] > max)
    {
      max = arr[i];
    }
  }
  return max;
}

int dec_to_oct(int num)
{
  int temp = num;
  int length = 0;
  while(num > pow(8, length)) {
    length++;
  }
  char *str = (char*)malloc(length +1);
  str[length] = '\0';

  for (int i = length-1; i >=0; i--)
  {
    str[i] = temp % 8 + '0';
    temp /= 8;
  }
  return atoi(str);
}

bool palindrome(char *str)
{
  for (int i = 0, j = strlen(str) - 1; i < strlen(str) / 2 + 1; i++, j--)
  {
    if (str[i] != str[j])
    {
      printf("%c != %c\n", str[i], str[j]);
      return false;
    }
  }

  return true;
}

bool mul_matrices(int **m1, int m1_r, int m1_c, int **m2, int m2_r, int m2_c, int **m)
{
  if (m1_c != m2_r)
  {
    return false;
  }

  for (int i = 0; i < m1_r; i++)
  {
    for (int j = 0; j < m2_c; j++)
    {
      for (int k = 0; k < m1_c; k++)
      {
        m[i][j] += m1[i][k] * m2[k][j];
      }
    }
  }
  return true;
}

void print_matrice(int **m, int m_r, int m_c)
{
  for (int i = 0; i < m_r; i++)
  {
    printf("[");
    for (int j = 0; j < m_c; j++)
    {
      printf("%d ", m[i][j]);
    }
    printf("]\n");
  }
}

void allocate_2D(int ***a, int rows, int cols)
{
  *a = (int **)malloc(sizeof(int *) * rows);
  for (int i = 0; i < rows; i++)
  {
    (*a)[i] = (int *)malloc(sizeof(int) * cols);
  }
}

void deallocate_2d(int ***a, int rows)
{
  for (int i = 0; i < rows; i++)
  {
    free((*a)[i]);
  }
  free(*a);
  *a = NULL;
}


void set_matrice(int **m, int m_r, int m_c) {
  for (int i = 0; i < m_r; i++)
  {
    for (int j = 0; j < m_c; j++)
    {
      printf("Enter [%d][%d] value: ", i, j);
      scanf("%d", &m[i][j]);
    }
  }
}