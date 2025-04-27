#pragma once
#include "Card.h"
#include <vector>
#include <random>
#include <algorithm>

class CardStack
{
private:
  vector<Card> deck;
public:
  static unsigned int default_size;
  static CardStack *createSevenToAceDeck();

  CardStack();
  CardStack(int size);

  void push(Card card);
  void pop();
  Card peek() const;
  bool empty() const;
  int size() const;
  Card getAt(unsigned int index) const;
  void removeAt(unsigned int index);
  void printDeck() const;
  void shuffle();
};

unsigned int CardStack::default_size = 32;

CardStack *CardStack::createSevenToAceDeck()
{
  CardStack *newDeck = new CardStack[32];

  for (int number = 7; number <= 14; number++)
  {
    for (int suit = 0; suit < 4; suit++)
    {
      Card newCard(suit, number);
      newDeck->push(newCard);
    }
  }
  return newDeck;
}

CardStack::CardStack()
{
  this->deck.reserve(default_size);
}
CardStack::CardStack(int size)
{
  this->deck.reserve(size);
}

void CardStack::push(Card card) {
  this->deck.insert(this->deck.begin(), card);
}

void CardStack::pop() {
  this->deck.erase(this->deck.begin());
}

Card CardStack::peek() const {
  return this->deck.front();
}

bool CardStack::empty() const {
  return this->deck.empty();
}

int CardStack::size() const {
  return this->deck.size();
}

void CardStack::printDeck() const {
  for(Card card : this->deck) {
    cout << card.getFullCard() << "\t";
  }
}

Card CardStack::getAt(unsigned int index) const {
  return this->deck.at(index);
}

void CardStack::removeAt(unsigned int index) {
  this->deck.erase(deck.begin()+index);
}

// void CardStack::shuffle() {
//   Card *shuffledDeck = new Card[this->deck.size()];
//   unsigned int deck_size = this->deck.size();
//   random_device rd;
//   mt19937 gen(rd());
//   uniform_int_distribution<> distr(0, deck_size); // random index generator

//   while(!this->deck.empty()) {
//     int random_index = distr(gen);
//     if(shuffledDeck[random_index].getNumber() == -1) {
//       // this space is free
//       shuffledDeck[random_index] = this->peek();
//       this->pop();
//     }
//   }

//   for (int i = 0; i < deck_size; i++)
//   {
//     this->deck.push_back(shuffledDeck[i]);
//   }
// }

void CardStack::shuffle() {
  random_device rd;
  mt19937 gen(rd());
  std::shuffle(deck.begin(), deck.end(), gen);
}
