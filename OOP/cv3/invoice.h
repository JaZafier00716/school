#pragma once
#include "invoiceItem.h"
#include "person.h"

class Invoice
{
private:
  int id;
  Person *person;
  Address *address;
  InvoiceItem **items;
  unsigned int itemCapacity;
  unsigned int itemCount;
public:
  Invoice(int id, Person *person, Address *address, unsigned int itemCapacity);
  void addItem(InvoiceItem *item);
  double totalPrice();
  ~Invoice();
};


