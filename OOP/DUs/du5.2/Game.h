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
  // Suits suit;
  bool stall;
  string str_suit;

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
  // this->suit = (Suits)-1;
  this->stall = false;
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
  int N;
  do
  {
    do
    {

      cout << "===============" << endl;
      cout << (this->p1_turn ? "P1 turn:" : "P2 turn:") << endl;
      cout << "===============" << endl;
      cout << "Current draw pool: " << this->current_draw_pool << endl;
      cout << "Last played card: " << (this->last_card.getNumber() == -1 ? "None" : this->last_card.getFullCard()) << endl;
      this->p1_turn ? p1.printHand() : p2.printHand();
      cout << endl;

      cout << "===============" << endl;
      cout << "-1 : Draw a card / card pool" << endl;
      (stall && cout << "-2: accept stall" << endl);
      N = this->p1_turn ? p1.cardNum() : p2.cardNum();
      cout << (N > 1 ? "0-" : "") << N - 1 << ": Play card" << endl;
      cout << "===============" << endl;
      cout << "action:\t";
      cin >> action;
    } while (action > N | action < -1);

    if (action == -2)
    {
      stall = false;
      break;
    }

    if (action == -1)
    {
      // Draw cards
      if (this->current_draw_pool == 0)
      {
        this->p1_turn ? p1.addCard(this->drawCard()) : p2.addCard(this->drawCard());
      }
      else
      {
        for (int i = 0; i < current_draw_pool; i++)
        {
          this->p1_turn ? p1.addCard(this->drawCard()) : p2.addCard(this->drawCard());
        }
        current_draw_pool = 0;
      }
      break;
    }
    else
    {
      // Play cards
      playedCard = this->p1_turn ? p1.play_card(this->last_card, abs(action), /*this->suit,*/ this->current_draw_pool, this->stall) : p2.play_card(this->last_card, abs(action), /*this->suit,*/ this->current_draw_pool, this->stall);
      if (playedCard.getNumber() == -1)
      {
        cout << "Card is not playable!!!" << endl
             << endl;
        continue; // Card could not be played
      }
      else
      {
        switch (playedCard.getNumber())
        {
        case 12: // if the card is queen
          int suit_num;
          do
          {
            cout << "Choose new Suit (0 - ♥, 1 - ♦, 2 - ♣, 3 - ♠):\t" << endl;

            cin >> suit_num;
          } while (suit_num < 0 | suit_num >= 4);
          // this->suit = (Suits)suit_num;
          playedCard.setSuit((Suits)suit_num);
          break;
        case 14:
          this->stall = true;
          break;
        case 7:
          this->current_draw_pool += 2;
        }
        this->last_card = playedCard; // set last card as played card
        // playedCard.setSuit(this->suit);
        // this->suit = (Suits)-1;    // reset color
      }
    }

  } while (playedCard.getNumber() == -1);
  this->p1_turn = !p1_turn;
}

int Game::playerWin()
{
  if (p1.cardNum() == 0)
  {
    return 1;
  }
  if (p2.cardNum() == 0)
  {
    return 2;
  }
  return 0;
}