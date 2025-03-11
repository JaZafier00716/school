#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct {
  int vyska, vaha, vek, vlasu;
}TClovek;

int main () {
  TClovek lide[2];

  lide[0].vyska = 180;
  lide[0].vaha = 90;
  lide[0].vek = 30;
  lide[0].vlasu = 100000;

  printf("Zadejte Vysku:");
  scanf("%d", &lide[1].vyska);
  printf("Zadejte Vek:");
  scanf("%d", &lide[1].vek);
  printf("Zadejte Vahu:");
  scanf("%d", &lide[1].vaha);
  printf("Zadejte Pocet Vlasu:");
  scanf("%d", &lide[1].vlasu);

  double ab = lide[0].vyska * lide[1].vyska + lide[0].vaha * lide[1].vaha + lide[0].vek * lide[1].vek + lide[0].vlasu * lide[1].vlasu;
  double abs_a = sqrt((double)fabs(lide[0].vyska * lide[0].vyska + lide[0].vaha * lide[0].vaha + lide[0].vlasu * lide[0].vlasu + lide[0].vek * lide[0].vek));
  double abs_b = sqrt((double)fabs(lide[1].vyska * lide[1].vyska + lide[1].vaha * lide[1].vaha + lide[1].vlasu * lide[1].vlasu + lide[1].vek * lide[1].vek));
  

  double korelace = ab / (abs_a * abs_b);

  printf("korelace: %lf\n", korelace);


  return 0;
}