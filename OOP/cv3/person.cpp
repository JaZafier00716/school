#include "person.h"

Address::Address(
    string street,
    string city,
    string zipCode,
    string houseNumber)
{
  this->street = street;
  this->city = city;
  this->zipCode = zipCode;
  this->houseNumber = houseNumber;
}
string Address::getFullAddress()
{
  return this->street + ", " + this->houseNumber + "\n" + this->city + ", " + this->zipCode;
}

Person::Person(
  string firstName,
  string lastName
) {
  this->firstName = firstName;
  this->lastname = lastName;
}

string Person::getFullName() {
  return this->firstName + " " + this->lastname;
}