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

  bool can_play_card(Card top_card, unsigned int card_id, int current_draw_pool, int stall); // returns whether player can play target card on the top card
  /*
    return values:
    -1  : invalid card
    0   : card has no effect
    1   : card is queen
    2   : draw 2 cards
  */
  Card play_card(Card top_card, unsigned int card_id, int current_draw_pool, int stall); // return effect of played card
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

bool Player::can_play_card(Card top_card, unsigned int card_id, int current_draw_pool, int stall)
{
  if(stall && top_card.getNumber() != 14) {
    return false;
  }

  if(top_card.getNumber() == -1) {
    return true;
  }
  if (card_id >= this->card_count)
  {
    return false;
  }

  if(top_card.getNumber() == 7 && current_draw_pool != 0) { // if number 7, you must play 7
    return this->cards.getAt(card_id).getNumber() == 7;
  }

  if (this->cards.getAt(card_id).getNumber() == 12)
  { // if the card is queen
    return true;
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

Card Player::play_card(Card top_card, unsigned int card_id, int current_draw_pool, int stall)
{
  if (!can_play_card(top_card, card_id, current_draw_pool, stall))
  {
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