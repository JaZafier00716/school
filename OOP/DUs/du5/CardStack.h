#pragma once
#include "DynamicArray.h"

class CardStack
{
private:
  DynamicArray cardDeck;

public:
  CardStack();
  CardStack(unsigned int size);

  void pushCard(Card card);
  void popCard();
  Card peekCard();
  void printDeck();
  static CardStack *createSevenToAceDeck();
  void shuffle();
};

CardStack::CardStack()
{
  this->cardDeck = DynamicArray();
}
CardStack::CardStack(unsigned int size)
{
  this->cardDeck = DynamicArray(size);
}

void CardStack::pushCard(Card card)
{
  this->cardDeck.AddFirst(card);
}
void CardStack::popCard()
{
  this->cardDeck.removeFirst();
}
Card CardStack::peekCard()
{
  return this->cardDeck.getFirst();
}

CardStack *CardStack::createSevenToAceDeck()
{
  CardStack *deck = new CardStack(32);
  for (int i = 0; i < 4; i++)
  {
    for (int j = 7; j <= 14; j++)
    {
      Card newCard(i, j);
      deck->cardDeck.Add(newCard);
      // cout << "Card initialized: " << newCard.getFullCard() << endl;
    }
  }
  return deck;
}

void CardStack::printDeck()
{
  for (int i = 0; i < this->cardDeck.getCount(); i++)
  {
    cout << this->cardDeck.getAt(i).getFullCard() << "\t";
  }
}

void CardStack::shuffle() {
  this->cardDeck.shuffle();
}

// void CardStack::shuffle()
// {
//   vector<Card> shuffledDeck;
//   int deckSize = this->cardDeck.getCount();
//   shuffledDeck.reserve(deckSize);

//   while (deckSize > 0)
//   {
//     unsigned int rand_index = rand() % deckSize;
//     Card card = this->cardDeck.getAt(rand_index);

//     cout << rand_index << "\t" << deckSize << "\t" << card.getFullCard() << endl;

//     if (card.getNumber() == -1)
//     {
//       cout << "skipping invalid index" << rand_index << endl;
//       continue;
//     }

//     shuffledDeck.push_back(card);

//     this->cardDeck.removeAt(rand_index);
//     deckSize--;
//   }
//   for(const Card& card : shuffledDeck) {
//     this->cardDeck.Add(card);
//   }
// }