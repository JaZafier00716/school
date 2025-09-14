// gcc c-main.c c-module.c  -o main
#include <stdio.h>

int g_int_a = 0x12345678, g_int_b = 0xAABBCCDD;
int g_int_rgb =0x445566;
char g_char_r, g_char_g, g_char_b;
int g_int_max;
int g_int_sum, g_int_pole[] = {1,2,3,4,5,6,7,8,9,10};
// void c_funkce(); // muzeme napsat extern, ale u funkci to neni nutne
void asm_funkce();
void asm_funkce2();
void asm_funkce3();

int main () {
  printf("a = 0x%x b = 0x%x\n", g_int_a, g_int_b);
  // c_funkce();
  // asm_funkce();
  asm_funkce2();
  printf("max=0x%x\n", g_int_max);

  asm_funkce3();
  printf("suma=%d\n", g_int_sum);
  // printf("a = 0x%x b = 0x%x\n", g_int_a, g_int_b);
  // printf("rgb=0x%x r=0x%x g=0x%x b=0x%x\n", g_int_rgb, g_char_r, g_char_g, g_char_b);
  return 0;
}