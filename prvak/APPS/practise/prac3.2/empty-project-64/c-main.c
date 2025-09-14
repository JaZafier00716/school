#include <stdio.h>
#define LEN 10
void bubble( int *tp_int, int t_N );
void strup( char *t_str );
char nejcastejsi( char *t_str );
long prvocislo( long t_cislo );

int main () {
  long cislo = 11;

  printf("%d", prvocislo(cislo));
}

int main3 () {
  char str[] = "kkkkkkAhoj, jak se             mas?";

  char znak = nejcastejsi(str);

  printf("%c %d", znak, znak);

  return 0;
}

int main2() {
  char str[] = "Ahoj, jak se mas?";

  printf("%s\n", str);

  strup(str);

  printf("%s\n", str);

  return 0;
}

int main1()
{
  int pole[LEN] = {1,5,6,0,10,-11,4,6,2,9};

  for (int i = 0; i < LEN; i++)
  {
    printf("%d ", pole[i]);
  }
  printf("\n");

  bubble(pole, LEN);

  for (int i = 0; i < LEN; i++)
  {
    printf("%d ", pole[i]);
  }
  printf("\n");
  

  return 0;
}
