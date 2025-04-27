#pragma once
#include "client.h"

class Account
{
private:
  int accountNum;
  double balance;
  Client owner;
  double interestRate;
public:
  Account(int a_n, Client o);
  Account(int a_n, Client o, double i_r);

  int getAccountNum() const;
  double getBalance() const;
  Client getOwner() const;
  double getInterestRate() const;
};

