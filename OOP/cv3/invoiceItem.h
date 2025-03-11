#pragma once
#include <iostream>
#include <string>
using std::cout, std::cin, std::endl, std::string;

class InvoiceItem
{
private:
  string name;
  double price;
public:
  InvoiceItem(string name, double price);
  string getName();
  double getPrice();
};


