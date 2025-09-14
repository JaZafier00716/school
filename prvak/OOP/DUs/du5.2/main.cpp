#include "Game.h"

int main() {
  CardStack *cardDeck = CardStack::createSevenToAceDeck();
  cardDeck->shuffle();
  Game *prsi = new Game(cardDeck);
  // cardDeck->printDeck();
  prsi->drawHands();
  do {
    prsi->turn();
  } while(!prsi->playerWin());
  
  cout << "P" << prsi->playerWin() << " Won!!!" << endl;
}