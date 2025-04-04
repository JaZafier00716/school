#include "CardStackIterator.h"

int main() {
  CardStack *cardDeck = CardStack::createSevenToAceDeck();
  CardStackIterator deckIterator(cardDeck);

  cout << "32 card deck:" << endl;
  cardDeck->printDeck();
  cout << endl;

  cout << "Shuffled deck:" << endl;
  cardDeck->shuffle();
  cardDeck->printDeck();
  cout << endl;

  cout << "Iterator print:" << endl; 
  while(deckIterator.hasNext())
  {
    cout << deckIterator.next().getFullCard() << "\t";
  }
  cout << endl;
}