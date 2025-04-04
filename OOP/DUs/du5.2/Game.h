#pragma once
#include "Player.h"

class Game
{
private:
  CardStack *deck;
  Player p1;
  Player p2;
  bool p1_turn;
  Card last_card;
  int current_draw_pool;
  string color;

public:
  static unsigned int start_card_num;
  Game(CardStack *deck);
  Card drawCard();
  void drawHands();

  void turn();

  int playerWin();
};

unsigned int Game::start_card_num = 7;

Game::Game(CardStack *deck)
{
  this->p1_turn = true;
  this->deck = deck;
  this->p1 = Player();
  this->p2 = Player();
  this->last_card = Card();
  current_draw_pool = 0;
  this->color = "Undefined";
}

Card Game::drawCard()
{
  Card card = this->deck->peek();
  this->deck->pop();

  return card;
}

void Game::drawHands()
{
  for (int i = 0; i < start_card_num; i++)
  {
    this->p1.addCard(this->drawCard());
    this->deck->pop();
    this->p2.addCard(this->drawCard());
    this->deck->pop();
  }
}

void Game::turn()
{
  int action;
  Card playedCard;
  do
  {

    if (this->p1_turn)
    {
      cout << "===============" << endl;
      cout << "P1 turn:" << endl;
      cout << "===============" << endl;
      cout << "Current draw pool: " << this->current_draw_pool << endl;
      cout << "Last played card: " << (this->last_card.getNumber() == -1 ? "None" : this->last_card.getFullCard()) << endl;
      p1.printHand();
      cout << endl;

      cout << "===============" << endl;
      cout << "-1 : Draw a card / card pool" << endl;
      int N = p1.cardNum();
      cout << (N > 1 ? "0-" : "") << N - 1 << ": Play card" << endl;
      cout << "===============" << endl;
      cout << "action:\t";
      cin >> action;

      if (action == -1)
      {
        // Draw cards
        if (this->current_draw_pool == 0)
        {
          p1.addCard(this->drawCard());
        }
        else
        {
          for (int i = 0; i < current_draw_pool; i++)
          {
            p1.addCard(this->drawCard());
          }
          current_draw_pool = 0;
        }
        break;
      }
      else
      {
        // Play cards
        playedCard = p1.play_card(this->last_card, abs(action), this->color);
        if (playedCard.getNumber() == -1)
        {
          cout << "Card is not playable!!!" << endl << endl;
          continue; // Card could not be played
        }
        else
        {
          if (playedCard.getNumber() == 12)
          { // if the card is queen
            int color_num;
            do
            {
              cout << "Choose new color (0 - red, 1, black):\t";
              cin >> color_num;
            } while (color_num != 1 && color_num != 0);
            this->color = (color_num == 0 ? "Red" : "Black");
          }
          switch (playedCard.getNumber())
          {
          case 7:
            this->current_draw_pool += 2;
          default:
            this->last_card = playedCard; // set last card as played card
            this->color = "Undefined";    // reset color
          }
        }
      }
    }
    else
    {
      cout << "===============" << endl;
      cout << "P2 turn:" << endl;
      cout << "===============" << endl;
      cout << "Current draw pool: " << this->current_draw_pool << endl;
      cout << "Last played card: " << (this->last_card.getNumber() == -1 ? "None" : this->last_card.getFullCard()) << endl;
      p2.printHand();
      cout << endl;

      cout << "===============" << endl;
      cout << "-1 : Draw a card / card pool" << endl;
      int N = p2.cardNum();
      cout << (N > 1 ? "0-" : "") << N - 1 << ": Play card" << endl;
      cout << "===============" << endl;
      cout << "action:\t";
      cin >> action;

      if (action == -1)
      {
        // Draw cards
        if (this->current_draw_pool == 0)
        {
          p2.addCard(this->drawCard());
        }
        else
        {
          for (int i = 0; i < current_draw_pool; i++)
          {
            p2.addCard(this->drawCard());
          }
          current_draw_pool = 0;
        }
        break;
      }
      else
      {
        // Play cards
        playedCard = p2.play_card(this->last_card, abs(action), this->color);
        if (playedCard.getNumber() == -1)
        {
          cout << "Card is not playable!!!" << endl << endl;
          continue; // Card could not be played
        }
        else
        {
          if (playedCard.getNumber() == 12)
          { // if the card is queen
            int color_num;
            do
            {
              cout << "Choose new color (0 - red, 1, black):\t";
              cin >> color_num;
            } while (color_num != 1 && color_num != 0);
            this->color = (color_num == 0 ? "Red" : "Black");
          }
          switch (playedCard.getNumber())
          {
          case 7:
            this->current_draw_pool += 2;
          default:
            this->last_card = playedCard; // set last card as played card
            this->color = "Undefined";    // reset color
          }
        }
      }
    }
  } while (playedCard.getNumber() == -1);
  this->p1_turn = !p1_turn;
}

int Game::playerWin() {
  if(p1.cardNum() == 0) {
    return 1;
  }
  if(p2.cardNum() == 0) {
    return 2;
  }
  return 0;
}