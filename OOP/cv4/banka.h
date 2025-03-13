#pragma once
#include "account.h"

class Bank
{
private:
  Account *accounts;
  int accountNum;
  string name;

public:
  Bank(Account *accounts, int accountNum, string name);
  ~Bank();

  void Bank::createAccount(Client client, int id);
};
