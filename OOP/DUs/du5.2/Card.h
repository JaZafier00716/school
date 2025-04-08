#pragma once
#include <iostream>
#include <string>
#define RED   "\033[31m"
#define BLUE  "\033[34m"
#define RESET "\033[0m"
using namespace std;

enum Suits
{
  hearts,
  diamonds,
  clubs,
  spades
};

class Card
{
private:
  Suits suit;
  int number;

public:
  Card();
  Card(int suit, int number);

  string getSuitSymbol() const;
  string getColor() const;
  int getNumber() const;
  string getNumberString() const;
  string getFullCard() const;
  int getSuitID() const;
  void setSuit(Suits suit);
};

Card::Card()
{
  this->suit = (Suits)-1;
  this->number = -1;
}

Card::Card(int suit, int number)
{
  this->suit = (Suits)suit;
  this->number = number;
}

string Card::getSuitSymbol() const
{
  switch (this->suit)
  {
  case hearts:
    return "♥";
  case diamonds:
    return "♦";
  case clubs:
    return "♣";
  case spades:
    return "♠";
  default:
    return "unknown";
  }
}

int Card::getSuitID() const 
{
  return this->suit;
}

string Card::getColor() const
{
  switch (this->suit)
  {
  case hearts:
  case diamonds:
    return "Red";
    break;
  case clubs:
  case spades:
    return "Black";
    break;
  default:
    return "Unknown";
    break;
  }
}

int Card::getNumber() const
{
  return this->number;
}

string Card::getNumberString() const
{
  if (this->number <= 10)
  {
    return (this->number < 10 ? " " : "") + to_string(this->number);
  }
  switch (this->number)
  {
  case 11:
    return " J";
    break;
  case 12:
    return " Q";
    break;
  case 13:
    return " K";
  case 14:
    return " A";
  default:
    return "Unknown";
    break;
  }
}

string Card::getFullCard() const
{
  return (
      (this->getColor() == "Red" ? RED : (this->getColor() == "Black" ? BLUE : RESET)) + this->getNumberString() + " " + this->getSuitSymbol() + RESET);
}

void Card::setSuit(Suits suit) {
  this->suit = suit;
}