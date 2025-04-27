//***************************************************************************
//
// Program for education in subject "Assembly Languages"
// petr.olivka@vsb.cz, Department of Computer Science, VSB-TUO
//
// Empty project
//
//***************************************************************************

#include <stdio.h>

// Functions
// void some_asm_function();

// Variables
// type g_some_c_variable;
char g_char_ahoj[] = "Ahoj.";
long g_pole[] = {33, 44, 0x66554433221100};
int g_int_1, g_int_2;
long g_heslo = 0x70617373776f7264;
char g_char_heslo[9];
void switch_dot();
void switch_first_second();
void cut();
void heslo_to_char();

int main()
{
  printf("%s\n", g_char_ahoj);

  switch_dot();

  printf("%s\n", g_char_ahoj);

  for (int i = 0; i < 3; i++)
  {
    printf("0x%lx, ", g_pole[i]);
  }
  printf("\n");
  switch_first_second();
  
  for (int i = 0; i < 3; i++)
  {
    printf("0x%lx, ", g_pole[i]);
  }
  printf("\n");

  cut();
  printf("1: %x, 2: %x\n", g_int_1, g_int_2);

  heslo_to_char();
  printf("0x%lx, %s\n", g_heslo, g_char_heslo);

  return 0;
}
