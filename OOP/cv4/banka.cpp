#include "banka.h"

Bank::Bank(Account *accounts, int accountNum, string name) : accounts(accounts), accountNum(accountNum), name(name) {}

Bank::~Bank()
{
  delete[] accounts;
}


void Bank::createAccount(Client client, int id) {
  Account acc = Account(id, client);

  this->accounts[this->accountNum++] = acc;
}