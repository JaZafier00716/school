#pragma once
#include "CardStack.h"

class Player
{
private:
  CardStack cards;
  unsigned int card_count;

public:
  Player();
  Player(CardStack cards);

  bool can_play_card(Card top_card, unsigned int card_id, string color); // returns whether player can play target card on the top card
  /*
    return values:
    -1  : invalid card
    0   : card has no effect
    1   : card is queen
    2   : draw 2 cards
  */
  Card play_card(Card top_card, unsigned int card_id, string color); // return effect of played card
  void addCard(Card card);
  void printHand();

  unsigned int cardNum();
};

Player::Player(CardStack cards)
{
  this->cards = cards;
  this->card_count = cards.size();
}
Player::Player()
{
  this->card_count = 0;
}

bool Player::can_play_card(Card top_card, unsigned int card_id, string color)
{
  if(top_card.getNumber() == -1) {
    return true;
  }
  if (card_id >= this->card_count)
  {
    return false;
  }

  if(top_card.getNumber() == 7) { // if number 7, you must play 7
    return this->cards.getAt(card_id).getNumber() == 7;
  }

  if (this->cards.getAt(card_id).getNumber() == 12)
  { // if the card is queen
    return true;
  }

  if(color != "Undefined") {  // if color has been changed, play card with that color
    return this->cards.getAt(card_id).getColor() == color;
  }

  if (this->cards.getAt(card_id).getSuitSymbol() == top_card.getSuitSymbol())
  { // if the symbols match
    return true;
  }
  if (this->cards.getAt(card_id).getNumber() == top_card.getNumber())
  { // if the numbers match
    return true;
  }
  return false;
}

Card Player::play_card(Card top_card, unsigned int card_id, string color)
{
  if (!can_play_card(top_card, card_id, color))
  {
    cout << "You cannot play this card!" << endl;
    return Card();
  }
  Card card = this->cards.getAt(card_id);
  this->cards.removeAt(card_id);
  this->card_count--;
  return card;
}

void Player::addCard(Card card)
{
  this->cards.push(card);
  this->card_count++;
}

void Player::printHand()
{
  for (int i = 0; i < this->card_count; i++)
  {
    cout << i << ": " << this->cards.getAt(i).getFullCard() << "\t";
  }
}

unsigned int Player::cardNum()
{
  return this->card_count;
}