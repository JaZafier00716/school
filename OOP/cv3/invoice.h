#pragma once
#include "invoiceItem.h"
#include "person.h"

class Invoice
{
private:
  int id;
  // Person *person;
  // Address *address;
  // InvoiceItem **items;
  InvoiceItem *items;
  Person person;
  Address address;
  unsigned int itemCapacity;
  unsigned int itemCount;
public:
  // Invoice(int id, Person *person, Address *address, unsigned int itemCapacity);
  // void addItem(InvoiceItem *item);
  Invoice(const Person& person, const Address& address, unsigned int itemCapacity);
  Invoice(int id, string firstNmae, string lastName, const Address& address, unsigned int itemCapacity);
  ~Invoice();
  void addItem(const InvoiceItem& item);
  double totalPrice();
};


