//***************************************************************************
//
// Program for education in subject "Assembly Languages"
// petr.olivka@vsb.cz, Department of Computer Science, VSB-TUO
//
// Empty project
//
//***************************************************************************

#include <stdio.h>
#define SIZE 10


void uprav_pole(int *t_pole, int t_N);
int hledej_max_msb(long *t_pole, int t_N);
int pocet_cislic(char *t_str);
void nahrada_mezer(char *t_str, char t_nahradni_znak);

int main()
{
  char str[] = "alkjdaf015 98,s adf kk  1094";
  char str2[] = "ahoj jak se mas";
  int pole[SIZE] = {5, -4, 8, 10, -123, 0, 1, -90, 2, -10};
  long l_pole[SIZE] = {
    0x1000000000000000,
    0x1200000000000000, 
    0x1100000000000000,
    0x0100111111111111,
    0x4000000000000000,
    0x6200000000000000,
    0x1000000000000000,
    0x0100000000000000,
    0x1111111111111111,
    0x0188888888888800};
  for (int i = 0; i < SIZE; i++)
  {
    printf("0x%x ", pole[i]);
  }
  printf("\n");
  

  uprav_pole(pole, SIZE);
  for (int i = 0; i < SIZE; i++)
  {
    printf("0x%x ", pole[i]);
  }
  printf("\n");

  for (int i = 0; i < SIZE; i++)
  {
    printf("0x%lx ", l_pole[i]);
  }
  printf("\n");
  int max = hledej_max_msb(l_pole, SIZE);
  printf("max value: %d\t0x%lx\n", max, l_pole[max]);
  for (int i = 0; i < SIZE; i++)
  {
    printf("0x%lx ", l_pole[i]);
  }
  printf("\n");

  printf("numbers count: %d\n", pocet_cislic(str));

  printf("%s -> ", str2);
  nahrada_mezer(str2, '_');
  printf("%s\n", str2);
  return 0;
}
