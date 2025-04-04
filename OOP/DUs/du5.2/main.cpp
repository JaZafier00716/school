#include "Game.h"

int main() {
  CardStack *cardDeck = CardStack::createSevenToAceDeck();
  Game *prsi = new Game(cardDeck);
  prsi->drawHands();
  do {
    prsi->turn();
  } while(!prsi->playerWin());
  
  cout << "P" << prsi->playerWin() << " Won!!!" << endl;
}