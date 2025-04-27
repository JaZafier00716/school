#pragma once
#include <string>
#include <iostream>
using namespace std;

class Person
{
private:
  string name;
  string id;
public:
  Person(string name, string id) : name(name), id(id){};
  string getName() {
    return this->name;
  };
  string getID() {
    return this->id;
  };

};
