#include "Bojovnik.h"


int main () {
  srand(time(NULL));
  Bojovnik b1("Pepa", Weapon(2.05, 3) ,Shield(2));

  Bojovnik b2("Franta", Weapon(1.7, 2),Shield(5), 150);

  bool b1_turn = true;

  do
  {
    if(b1_turn) {
      cout << b1.GetName() << " has dealt " << b1.attack(&b2) << " damage to " << b2.GetName() << endl;
    } else {
      cout << b2.GetName() << " has dealt " << b2.attack(&b1) << " damage to " << b1.GetName() << endl;
    }
    b1_turn = !b1_turn;
  } while (!b1.dead() && !b2.dead());

  if(b1.dead()) {
    cout << b2.GetName() << " WON!!!" << endl;
  } else {
    cout << b1.GetName() << " WON!!!" << endl;
  }
  return 0;
}