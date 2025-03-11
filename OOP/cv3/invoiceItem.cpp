#include "invoiceItem.h"


InvoiceItem::InvoiceItem(string name, double price)
{
  this->name = name;
  this->price = price;
}

string InvoiceItem::getName() {
  return this->name;
}

double InvoiceItem::getPrice() {
  return this->price;
}