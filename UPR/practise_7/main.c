#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// struct Paradox
// {
//   char *jmeno;
//   int IQ;
//   char status[256];
// };

struct Paradox { // poradi promennych od nejmensi k nejvetsi
  int IQ;
  // unsigned int IQ : 1; // bitove pole 
  // unsigned int dalsi : 1; // struktura bude porad brat 4B (pokud budeme mit jen IQ a dalsi), jelikoz se porad vlezou do pameti
  // char jmeno - zpusobi, ze IQ a fav budou podlozeny 4B navic, aby byly delitelne 8
  float fav_water_volume;
  char *jmeno;
};

void paradox_init(struct Paradox **pripad) {
  *pripad = (struct Paradox*)malloc(sizeof(struct Paradox));
  (*pripad)->jmeno = (char*)malloc(sizeof(char)*256);

  strcpy((*pripad)->jmeno, "Adam Kijonka");
  (*pripad)->fav_water_volume = 0.5;
  (*pripad)->IQ = 160;

  //(*paradox)->IQ = 1;

  // strcpy((*pripad)->status, "dedecek");
}

void dealloc(struct Paradox **pripad) {
  free((*pripad)->jmeno);
  free(*pripad);
  *pripad = NULL;
}

int main()
{

  // nastaveni n-teho bitu na 1 : cislo |= 1 << n;
  // resetovani (do 0) n-teho bitu cislo &= ~(1 << n)  
  struct Paradox *pripad;
  paradox_init(&pripad);

  printf("jmeno: %s\n", pripad->jmeno);
  printf("fav: %f", pripad->fav_water_volume);
  // printf("pripad: %s\n", pripad->status);
  printf("IQ: %d\n", pripad->IQ);

  dealloc(&pripad);
  return 0;
}