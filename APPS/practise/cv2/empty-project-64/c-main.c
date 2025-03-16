#include <stdio.h>
#include <string.h>

// Start Task 1
const int SIZE= 10;
int g_int_pole[10] = {
  0x10101010, 
  0x10111213, 
  0x80808080, 
  0x00000000, 
  0x00000001, 
  0x11111111,
  0x12345678,
  0x09876543,
  0x22222222,
  0x33333333 
};
// End Task 1

// Start Task 2
char g_int_N = 3;    // N
int g_int_pocet =0;
int g_long_pole[10] = {
  0x12345678,
  0x10000000,
  0x01000000,
  0x23478910,
  0x01000000,
  0x00001000,
  0x87652901,
  0x89017287,
  0x09876801,
  0x00000000,
};
// End Task 2

// Start Task 3
char g_char_str[] = " progra mova   ni je  za bava ! ";
int g_int_mezer;
// End Task 3

void sum_bytes();
void count_pow();
void get_max_spaces();

int main()
{

  // Start Task 1
  for (int i = 0; i < SIZE; i++)
  {
    printf("0x%u ", g_int_pole[i]);
  }
  printf("\n");
  
  sum_bytes();

  for (int i = 0; i < SIZE; i++)
  {
    printf("%d ", g_int_pole[i]);
  }
  printf("\n");
  // End Task 1

  // Start Task 2
  int count = 0;

  int power = 1;
  for (int i = 0; i < g_int_N; i++)
  {
    power *= 2;
  }
  

  for (int i = 0; i < SIZE; i++)
  {
    printf("%u ", g_long_pole[i]);
    if((g_long_pole[i] % power) == 0) {
      count++;
    }
  }
  count_pow();
  printf("asm: %d, math: %d : %d\n", g_int_pocet, power, count);
  // End Task 2

  //Start Task 3
  get_max_spaces();

  printf("max spaces: %d\n", g_int_mezer);
  
}
