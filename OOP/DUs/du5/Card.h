#pragma once
#include <iostream>
#include <string>
#include <random>
#define RED "\033[31m"
#define BLUE "\033[34m"
#define RESET "\033[0m"
using namespace std;

enum Suit
{
  hearts,
  diamonds,
  clubs,
  spades
};

class Card
{
private:
  Suit suit;
  int number;
public:
  Card()
  {
    this->suit = (Suit)-1;
    this->number = -1;
  }
  Card(int suit, int number)
  {
    this->suit = (Suit)suit;
    this->number = number;
  }

  string getSuitSymbol() const
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

  string getColor() const
  {
    switch (suit)
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
      return "unknown";
      break;
    }
  }

  int getNumber() const
  {
    return this->number;
  }

  string getNumberString() const
  {
    if (this->number <= 10)
    {
      return (this->number < 10 ? " " : "") + to_string(this->number);
    }
    switch (number)
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

  string getFullCard()
  {
    return (this->getColor() == "Red" ? RED : BLUE) + getNumberString() + " " + getSuitSymbol() + RESET;
  }
};
