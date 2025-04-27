#include "funkce.h"

void Kniha::NewBook(uint64_t id) {
	fflush(stdin);
	cout << "Zadejte nazev knihy: ";
	getline(cin, this->name);
	fflush(stdin);
	cout << "Zadejte cenu knihy: ";
	cin >> this->price;
	
	this->id = id;
}

ostream &operator<<(ostream &print, Kniha x) {
	print << "ID knihy: " << x.id << endl;
	print << "Nazev knihy: " << x.name << endl;
	print << "Cena knihy: " << x.price;
	
	return print;
}

