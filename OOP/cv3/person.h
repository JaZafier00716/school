#pragma once
#include <iostream>
#include <string>
using std::cout, std::cin, std::endl, std::string;

class Address
{
private:
  string street;
  string city;
  string zipCode;
  string houseNumber;

public:
  Address();
  Address(
      string street,
      string city,
      string zipCode,
      string houseNumber);
  string getFullAddress();
};

class Person
{
private:
  string firstName;
  string lastName;

public:
  Person();
  Person(string lastName);
  Person(string firstName, string lastName);
  string getFullName();
};
