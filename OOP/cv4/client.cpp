#include "client.h"

Client::Client(string firstName, string lastName, int id) : firstName(firstName), lastName(lastName), id(id) {}

const string Client::getFullName() const {
  return this->firstName + " " + this->lastName;
}