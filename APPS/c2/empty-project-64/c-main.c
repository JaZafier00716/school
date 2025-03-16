//***************************************************************************
//
// Program for education in subject "Assembly Languages"
// petr.olivka@vsb.cz, Department of Computer Science, VSB-TUO
//
// Empty project
//
//***************************************************************************

#include <stdio.h>
#include <math.h>

#define N 10
int g_int_pole[N] = {
    0x01020304,
    0x10101010,
    0x1C3D5E7F,
    0x9B2A4C6D,
    0xF0E1D2C3,
    0x4A6B8C9D,
    0xD3E2F1A0,
    0x5F7E9C8B,
    0xB2C3D4E5,
    0x08192A3B};

char g_int_N = 12;
int g_int_pocet = N;
long g_long_pole[10] = {
    0x0000000000000400, 
    0x0000000000000800,
    0x0000000000001000,
    0x0000000000002000,
    0x0000000000000400,
    0x0000000000000800,
    0x0000000000001000,
    0x0000000000002000,
    0x0000000000000400,
    0x0010000000000800,
};

char g_char_str[] = " progra mova ni je   za bava ! ";
int g_int_mezer;

void byte_sum(int *pole, int NUM);
int count_mod(long *pole, int NUM);
void find_max_spaces(char *str);

int main()
{
  byte_sum(g_int_pole, N);

  for (int i = 0; i < N; i++)
  {
    printf("%d\n", g_int_pole[i]);
  }

  

  printf("divisible: %d\n", count_mod(g_long_pole, g_int_pocet));

  find_max_spaces(g_char_str);
  printf("%d", g_int_mezer);
}
