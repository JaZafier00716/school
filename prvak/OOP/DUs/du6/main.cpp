#include "Computer.h"
#include "Smartphone.h"
#include "Televize.h"

int main () {
  Computer c("Intel i7", 16, 512);
  Televize t(55, "OLED", "4K");
  Smartphone s("Samsung", 6.5, 4500);

  c.printParameters();
  t.printParameters();
  s.printParameters();

  return 0;
}