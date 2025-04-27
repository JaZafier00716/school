#include "invoice.h"

// Invoice::Invoice(int id, Person *person, Address *address, unsigned int itemCapacity)
// {
//   this->person = person;
//   this->address = address;
//   this->itemCapacity = itemCapacity;

//   this->itemCount = 0;
//   this->items = new InvoiceItem*[itemCapacity];
// }
// Invoice::~Invoice()
// {
//   delete this->person;
//   delete this->address;
//   for (int i = 0; i < this->itemCount; i++)
//   {
//     delete this->items[i];
//   }
//   delete[] items;
// }
// void Invoice::addItem(InvoiceItem *item) {
//   this->items[this->itemCount++] = item;

// }

// double Invoice::totalPrice() {
//   double price = 0;
//   for (int i = 0; i < this->itemCount; i++) {
//     price += items[i]->getPrice();
//   }
//   return price;
// }

Invoice::Invoice(const Person& person, const Address& address, unsigned int itemCapacity)
{
  this->person = person;
  this->address = address;
  this->itemCapacity = itemCapacity;

  this->itemCount = 0;
  this->items = new InvoiceItem[itemCapacity];
}

Invoice::Invoice(int id, string firstName, string lastName, const Address& address, unsigned int itemCapacity)
: person(firstName, lastName)
{
  this->address = address;
  this->itemCapacity = itemCapacity;

  this->itemCount = 0;
  this->items = new InvoiceItem[itemCapacity];
}


Invoice::~Invoice()
{
  delete[] items;
}

void Invoice::addItem(const InvoiceItem &item)
{
  this->items[this->itemCount++] = item;
}

double Invoice::totalPrice()
{
  double price = 0;
  for (int i = 0; i < this->itemCount; i++)
  {
    price += items[i].getPrice();
  }
  return price;
}