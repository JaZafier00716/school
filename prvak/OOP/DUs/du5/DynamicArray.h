#pragma once
#include "Card.h"
#define DEFAULT_SIZE
using namespace std;

class DynamicArray
{
private:
  Card *arr;
  unsigned int count;
  unsigned int size;
  static const int defaultSize;

public:
  DynamicArray();
  DynamicArray(unsigned int N);
  DynamicArray(Card *arr, unsigned int length);
  ~DynamicArray();

  Card getFirst() const;
  Card getAt(unsigned int i) const;
  int getSize() const;
  int getCount() const;

  void Add(Card card);
  void AddFirst(Card card);
  int removeFirst();
  int removeAt(unsigned int index);
  void shuffle();
};

const int DynamicArray::defaultSize = 32;

DynamicArray::DynamicArray()
{
  this->arr = new Card[DynamicArray::defaultSize];
  this->count = 0;
  this->size = DynamicArray::defaultSize;
}
DynamicArray::DynamicArray(unsigned int N)
{
  this->arr = new Card[N];
  this->count = 0;
  this->size = N;
}
DynamicArray::DynamicArray(Card *arr, unsigned int length)
{
  this->arr = new Card[2 * length];
  this->count = length;
  this->size = 2 * length;

  for (int i = 0; i < length; i++)
  {
    this->arr[i] = arr[i];
  }
}

DynamicArray::~DynamicArray()
{
  delete[] this->arr;
}

Card DynamicArray::getFirst() const
{
  if (this->count > 0)
  {
    return this->arr[0];
  }
  return Card();
}

Card DynamicArray::getAt(unsigned int i) const
{
  if (i < this->count)
  {
    return this->arr[i];
  }
  else
  {
    cout << "Index " << i << " is out of bounds!" << endl;
    cout << "The max index is " << this->count-1 << endl;
    return Card();
  }
}

int DynamicArray::getSize() const
{
  return this->size;
}

int DynamicArray::getCount() const
{
  return this->count;
}

void DynamicArray::Add(Card card)
{
  if (this->count >= this->size)
  {
    int new_size = 2 * this->size;
    Card *new_arr = new Card[new_size];

    for (int i = 0; i < this->count; i++)
    {
      new_arr[i] = this->arr[i];
    }
    delete[] this->arr;
    this->arr = new_arr;
    this->size = new_size;
  }
  this->arr[this->count] = card;
  this->count++;
}

void DynamicArray::AddFirst(Card card)
{
  if (this->count >= this->size)
  {
    int new_size = 2 * this->size;
    Card *new_arr = new Card[new_size];

    for (int i = 0; i < this->count; i++)
    {
      new_arr[i] = this->arr[i];
    }
    delete[] this->arr;
    this->arr = new_arr;
    this->size = new_size;
  }
  this->count++;
  for (int i = this->count - 1; i > 0; i--)
  {
    this->arr[i] = this->arr[i - 1];
  }
  this->arr[0] = card;
}

int DynamicArray::removeFirst()
{
  this->count--;
  for (int i = 0; i < this->count; i++)
  {
    this->arr[i] = this->arr[i + 1];
  }

  return this->count;
}

int DynamicArray::removeAt(unsigned int index)
{
  if(index < this->count) {
    this->count--;
    for (int i = index; i < this->count; i++)
    {
      this->arr[i] = this->arr[i + 1];
    }
  } else {
    cout << "Index " << index << " is out of bounds!" << endl;
    cout << "The max index is " << this->count-1 << endl;
  }

  return this->count;
}


void DynamicArray::shuffle() {
  vector<Card> shuffledDeck;

  while(this->count > 0)
  {
    int rand_index = rand() % this->count;
    Card card = this->arr[rand_index];
    cout << rand_index << "\t" << this->count << card.getFullCard() << endl;
    if(card.getNumber() == -1) {
      continue;
    } else {
      shuffledDeck.push_back(card);
      this->removeAt(rand_index);
    }
  }
  for (int i = 0; i < shuffledDeck.size(); i++)
  {
    this->arr[i] = shuffledDeck[i];
  }
  this->count = shuffledDeck.size();
}