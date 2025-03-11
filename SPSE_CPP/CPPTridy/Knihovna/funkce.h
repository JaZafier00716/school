#ifndef FUNKCE_H
#define FUNKCE_H
#include <iostream>
#include <fstream>
#include <string>
#define MAX 100
using namespace std;

class Kniha {
private:
	uint64_t id;
	string name;
	float price;
public:
	void NewBook(uint64_t id);
	int GetPrice() {
		return this->price;
	}
	string GetName() {
		return this->name;
	}
	void SetKniha(Kniha x) {
		this->name = x.name;
		this->price = x.price;
	}
	void SetName(string x) {
		this->name = x;
	}
	void SetPrice(float x) {
		this->price = x;
	}
	void SetID(uint64_t id) {
		this->id = id;
	}
	
	friend ostream &operator<<(ostream &print, Kniha x);
};

class Knihovna {
private:
	int pocetKnih=0;
	Kniha library[MAX];
	
public:
	void SeekErrors(int ErrorID);
	void SetPocet(int pocetKnih);
	int GetPocet() {
		return this->pocetKnih;
	}
	
	int AddBook(); 						// funkce vraci pocet nebo chybovy stav
	float PrumernaCena(); 				// funkce vrati prumernou cenu
	float findPrice(string nazev); 		// vyhleda cenu podle nazvu a vrati cenu nebo chybovou hlasku
	void sort(int typ, int smer); 		// seradi pole podle ceny [0] nebo podle nazvu [1] a vzestupne [0] nebo sestupne [1]
	void edit(int id);					// Upravi knihu v knihovne
	void minPrice();					// Vypise vsechny knihy s minimalni cenou
	int Remove(int id);					// Odstrani knihu z knihovny podle ID
	
	int fprint(string nazev); 			// Funkce ulozi knihovnu do souboru a vrati chybu, pokud nejaka nastane
	int fscan(string nazev);			// Funkce nacte ze souboru do pole knihovnu a vrati chybu, pokud nejaka nastane
	
	friend ostream &operator << (ostream &print, Knihovna x);
};
#endif
