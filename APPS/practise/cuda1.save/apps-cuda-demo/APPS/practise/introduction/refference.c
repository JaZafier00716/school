/*
  Referencni kod pro uvod do assembly
*/

#include <stdio.h>

char *msg = "Enter a number:";
char *msg2 = "Looping %d of %d";
char *format = "%d";
int num = 0;

int main () {
  int i;

  printf(msg);
  scanf(format, &num);

  i=0;
  do {
    printf(msg2, i, num);
    i++;
  }while(i < num);
  return 0;
}
