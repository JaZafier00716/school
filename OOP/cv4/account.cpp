#include "account.h"

Account::Account(int a_n, Client o, double i_r) : accountNum(a_n), owner(o), interestRate(i_r)
{
}


int Account::getAccountNum() const {
  return this->accountNum;
}

double Account::getBalance() const {
  return this->balance;
}

Client Account::getOwner() const {
  return this->owner;
}

double Account::getInterestRate() const {
  return this->interestRate;
}