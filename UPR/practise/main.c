#include <stdio.h>

int main () {
  int x = 67;
  float y;

  char c = 'a';

  printf("Nacti realne cislo:\t");
  scanf("%f", &y);

  printf("%c %i", x, c);
  printf("Hello world!, %f%d\n", y, x);

  printf("%d\n", 5+6);
  printf("%f", 5/7);

  return 0;
}