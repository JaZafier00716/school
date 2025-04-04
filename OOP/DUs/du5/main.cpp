#include "CardStack.h"

int main () {
  srand (time(nullptr));
  CardStack *deck = CardStack::createSevenToAceDeck();

  cout << "original" << endl;
  deck->printDeck();
  cout << endl;

  cout << "shuffle" << endl;
  deck->shuffle();
  deck->printDeck();
  cout << endl;

  cout << "pop" << endl;
  deck->popCard();
  deck->printDeck();
  cout << endl;

  cout << "peek" << endl;
  cout << deck->peekCard().getFullCard();
  cout << endl;

  cout  << "push: " << endl;
  deck->pushCard(Card(2, 10));
  deck->printDeck();

  return 0;
}