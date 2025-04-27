#pragma once
#include <iostream>
#include <string>
using namespace std;

class Client
{
private:
  string firstName;
  string lastName;
  int id;
public:
  Client();
  Client(string firstName, string lastName, int id);

  const string getFullName() const;
};



