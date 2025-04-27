
#include <stdio.h>

void bubble( int *tp_int, int t_N );
void strup( char *t_str );
// char nejcastejsi( char *t_str, int *vyskyty);
char nejcastejsi(char *t_str);
long prvocislo( long t_cislo );

int main () {
  long is_prime = prvocislo(10);
  if(is_prime == -1) {
    printf("NOT prime :(\n");
  } else {
    printf("%ld is prime :)\n", is_prime);
  }
}

int main3 () {
  char str[] = "Ahoj, jakkkk se k,masa-k?";
  for (int i = 0; i < 18; i++)
  {
    printf("0x%x ", str[i]);
  }
  printf("\n");

  int mcc = nejcastejsi(str);   // Most common character

  printf("0x%x - %c\n", mcc, mcc);

  return 0;
}

int main2 () {
  char str[] = "Ahoj, jak se mas?";

  strup(str);

  printf("%s\n", str);

  return 0;
}

int main1()
{
  int pole[10] = {1,5,6,0,10,-11,4,6,2,9};
  int pole2[10] = {1,5,6,0,10,-11,4,6,2,9};
  int change;
  do {
    change = 0;
    int i = 0;
    for (; i < 10-1; i++)
    {
      if(pole[i] > pole[i+1]) {
        int temp = pole[i];
        pole[i] = pole[i+1];
        pole[i+1] = temp;
        change = 1;
      }
    }
  } while(change);

  for (int i = 0; i < 10; i++)
  {
    printf("%d ", pole[i]);
  }
  printf("\n");


  bubble(pole2, 10);

  for (int i = 0; i < 10; i++)
  {
    printf("%d ", pole2[i]);
  }
  printf("\n");
  return 0;
}
