#include <stdio.h>
#include <string.h>
/*
- standard zavisly na architekture
- u 64b sjednocene pro linux i windows
- u 32b ne

Pro predavani parametru:
- vyhrazeno 6 registru
- rdi, rsi, rdx, rcx, r8, r9 (a jejich zlomkove casti)
- pro 7+ parametru je nutno pouzit zasobnik

Pro navratovou hodnotu: akumulator
    - al (8b), ax (16b), eax (32b), rax (64b)

- r10, r11 - muzeme prepsat ve funkci

- rbp, rsp - pouziva se pro zasobnik !!! neprepisovat

- rbx, r12, r13, r14, r15 - nutno obnovit po pouziti na puvodni hodnoty

Volani funkci:
- !!! parametry se pripravuji z prava



*/ 

// int funkce(int a, int b, int c) {
//   printf("a=%d, b=%d c=%d", a,b,c);

//   return 0;
// }
int sum_int(int a, int b);
int sum_long(long a, long b);
int asm_strlen(char *str);
int asm_otoc_znamenko( int *pole, int N);
long asm_max_long(long *pole, int N);


int main () {
  
  int pole[10] = {1, -2, 3, -4, 5, -6, 7, -8, 9, -10};

  long l_pole[10] = {1, -2, 3, -4, 5, -6, 7, -8, 9, -10};
  printf("%d\n", sum_int(5, 4));

  printf("%ld\n", sum_long(0x01234567890123456, 0x789456123078945));

  char hello[] = "Hello Programmer!";
  printf("%s;\tasm:%d\tstr:%d\n", hello, asm_strlen(hello), strlen(hello));

  asm_otoc_znamenko(pole, 10);
  for (int i = 0; i < 10; i++)
  {
    printf("%d ", l_pole[i]);
  }
  printf("\n");
  printf("%ld\n", asm_max_long(l_pole, 10));

  return 0;
}


int mmain () { 
  // int x = 0x12345678;
  // char pozdrav [] = "ahoj";
  // int x[1]; // alternativa k int x;
  // x[0] = 2;
  // char r,g,b;
  // char rgb[4] = {0xab, 0xbc, 0xcd, 0};
  // int irgb = * (int *) rgb;
  // printf("0x%x\n", irgb);

  // // ukazka jak se pripravuji parametry
  // int x = 0;
  // funkce(x++, x++, x++);

  return 0;
}