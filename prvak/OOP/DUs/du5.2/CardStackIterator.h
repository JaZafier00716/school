#pragma once
#include "CardStack.h"

class CardStackIterator
{
private:
  CardStack *deck;
  unsigned int index;
public:
  CardStackIterator(CardStack *deck);
  ~CardStackIterator();

  bool hasNext();
  Card next();
};

CardStackIterator::CardStackIterator(CardStack *deck)
{
  this->deck = deck;
  this->index = 0;
}

CardStackIterator::~CardStackIterator()
{
  delete[] deck;
}

bool CardStackIterator::hasNext() {
  return this->index < this->deck->size();
}

Card CardStackIterator::next() {
  if(this->hasNext()) {
    return this->deck->getAt(index++);
  }
}