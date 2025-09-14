#include "funkce.h"

int Knihovna::fprint(string nazev) {
	ofstream f;
	
	f.open(nazev);
	
	if(f.fail()) {
		return -2;
	}
	
	for(int i=0;i<this->pocetKnih;i++){
		f << this->library[i].GetName() << ";" << this->library[i].GetPrice() << ";" << endl;
	}
	
	f.close();
	
	return 0;
}

int Knihovna::fscan(string nazev) {
	ifstream f;
	string price;
	string name;
	int i=0;
	
	f.open(nazev);
	
	if(f.fail()) {
		return -2;
	}
	
	while(getline(f, name, ';') && getline(f, price, ';')){
		if(name[0] == '\n') {
			name = name.substr(1);
		}
		this->library[i].SetName(name);
		this->library[i].SetPrice(stof(price));
		this->library[i].SetID(i);
		
		i++;
		if(i == MAX) {
			return -1;
		}
	}
	this->pocetKnih = i;
	return 0;
}
