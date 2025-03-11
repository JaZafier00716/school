#include "funkce.h"

void Knihovna::SeekErrors(int ErrorID) {
	switch (ErrorID) {
		case -1:
			cout << "Pole bylo zaplneno" << endl << endl;
			break;
		case -2:
			perror("Nastala chyba pri otevirani souboru");
			break;
		case -3:
			cout << "Nazev nebyl nalezen" << endl << endl;
			break;
		case -4: {
			cout << "Kniha nebyla nalezena" << endl << endl;
			break;
		}
		default:
			cout << "Tento error neni znam" << endl << endl;
			break;
	}
}

void Knihovna::SetPocet(int pocetKnih) {
	if (pocetKnih < 0) {
		SeekErrors(pocetKnih);
		return;
	}
	this->pocetKnih = pocetKnih;
}

int Knihovna::AddBook() {
	if (this->pocetKnih >= MAX - 1) {
		return -1;
	}

	this->library[this->pocetKnih].NewBook(this->pocetKnih);

	return this->pocetKnih + 1;
}

float Knihovna::PrumernaCena() {
	float sum = 0
	            ;
	for (int i = 0; i < this->pocetKnih; i++) {
		sum +=	this->library[i].GetPrice();
	}

	return sum / pocetKnih;
}

float Knihovna::findPrice(string nazev) {
	for (int i = 0; i < this->pocetKnih; i++) {
		if (this->library[i].GetName() == nazev) {
			return this->library[i].GetPrice();
		}
	}
	return -3;
}

int PriceSortUp(const void *a, const void *b) {
	Kniha *arg1 = (Kniha*) a;
	Kniha *arg2 = (Kniha*) b;

	if (arg1->GetPrice() < arg2->GetPrice()) {
		return -1;
	} else if (arg1->GetPrice() > arg2->GetPrice()) {
		return 1;
	} else {
		return 0;
	}
}
int PriceSortDown(const void *a, const void *b) {
	Kniha *arg1 = (Kniha*) a;
	Kniha *arg2 = (Kniha*) b;

	if (arg1->GetPrice() < arg2->GetPrice()) {
		return 1;
	} else if (arg1->GetPrice() > arg2->GetPrice()) {
		return -1;
	} else {
		return 0;
	}
}

int NameSortUp(const void *a, const void *b) {
	Kniha *arg1 = (Kniha*) a;
	Kniha *arg2 = (Kniha*) b;

	if (arg1->GetName() < arg2->GetName()) {
		return -1;
	} else if (arg1->GetName() > arg2->GetName()) {
		return 1;
	} else {
		return 0;
	}
}
int NameSortDown(const void *a, const void *b) {
	Kniha *arg1 = (Kniha*) a;
	Kniha *arg2 = (Kniha*) b;

	if (arg1->GetName() < arg2->GetName()) {
		return 1;
	} else if (arg1->GetName() > arg2->GetName()) {
		return -1;
	} else {
		return 0;
	}
}

void Knihovna::sort(int typ, int smer) {
	if (typ == 0) {
		if (smer == 0) {
			qsort(this->library, this->pocetKnih, sizeof(Kniha), PriceSortUp);
		} else {
			qsort(this->library, this->pocetKnih, sizeof(Kniha), PriceSortDown);
		}
	} else {
		if (smer == 0) {
			qsort(this->library, this->pocetKnih, sizeof(Kniha), NameSortUp);
		} else {
			qsort(this->library, this->pocetKnih, sizeof(Kniha), NameSortDown);
		}
	}
}

void Knihovna::edit(int id) {
	Kniha kniha;

	kniha.NewBook(id);

	this->library[id].SetKniha(kniha);
}

void Knihovna::minPrice() {
	float minPrice = this->library[0].GetPrice();

	for (int i = 0; i < this->pocetKnih; i++) {
		if (this->library[i].GetPrice() < minPrice) {
			minPrice = this->library[i].GetPrice();
		}
	}
	cout << "----------------------------" << endl;
	cout << "| knihy s minimalni cenou: |" << endl;
	cout << "----------------------------" << endl;
	for (int i = 0; i < this->pocetKnih; i++) {
		if (this->library[i].GetPrice() == minPrice) {
			cout << "-------------" << endl;
			cout << library[i] << endl;
			cout << "-------------" << endl;
		}
	}
}

int Knihovna::Remove(int id) {
	if (id > pocetKnih) {
		return -4;
	}
	for (int i = id; i < this->pocetKnih - 1; i++) {
		this->library[i] = this->library[i + 1];
	}
	return this->pocetKnih - 1;
}

ostream &operator << (ostream &print, Knihovna x) {
	for (int i = 0; i < x.pocetKnih; i++) {
		cout << "-------------" << endl;
		print << x.library[i] << endl;
		cout << "-------------" << endl;
	}

	return print;
}
