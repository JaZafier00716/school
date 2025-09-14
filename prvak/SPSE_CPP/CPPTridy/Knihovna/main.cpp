#include "funkce.h"

int main() {
	Knihovna library;
	int akce, newPocet, error, typRazeni, smerRazeni, idKnihy;
	float prumernaCena, cena;
	string nazevSouboru, nazevKnihy;
	
	do{
		fflush(stdin);
		cout << "| 0  |\tUkonceni" << endl;
		cout << "| 1  |\tPridani knihy do knihovny" << endl;
		cout << "| 2  |\tVypis knihovny" << endl;
		cout << "| 3  |\tVypis prumerne ceny v poli" << endl;
		cout << "| 4  |\tUlozeni knihovny do souboru" << endl;
		cout << "| 5  |\tVyhleda cenu knihy podle nazvu" << endl;
		cout << "| 6  |\tVypise maximalni ID v knihovne" << endl;
		cout << "| 7  |\tSeradi knihy podle ceny nebo nazvu" << endl;
		cout << "| 8  |\tEditace knihy" << endl;
		cout << "| 9  |\tVypise knihy s minimalni cenou" << endl;
		cout << "| 10 |\tNacteni knihovny do pole ze souboru" << endl;
		cout << "| 11 |\tOdstraneni knihy z knihovny" << endl;
		cout << "Zvolte akci: ";
		cin >> akce;
		
		switch (akce) {
		case 0:
			system("pause");
			return 0;
		case 1:
			newPocet = library.AddBook();
			library.SetPocet(newPocet);
			break;
		case 2:
			cout << "-------------" << endl;
			cout << "| Knihovna: |" << endl;
			cout << "-------------" << endl;
			cout << library << endl << endl;
			break;
		case 3:
			prumernaCena = library.PrumernaCena();
			cout << "Prumerna cena: " << prumernaCena << endl << endl;	
			break;
		case 4:
			cout << "Zadejte nazev souboru: ";
			cin >> nazevSouboru;
			error = library.fprint(nazevSouboru);
			if(error != 0) {
				library.SeekErrors(error);
				break;
			}
			break;
		case 5:
			cout << "Zadejte nazev knihy: ";
			cin >> nazevKnihy;
			cena = library.findPrice(nazevKnihy);
			if(cena < 0) {
				error = cena;
				library.SeekErrors(error);
				break;
			}
			cout << "cena knihy " << nazevKnihy << " je: " << cena << endl << endl; 
			break;
		case 6:
			cout << "Maximalni ID je: " << library.GetPocet()-1 << endl;
			break;
		case 7:
			do{
				cout << "chcete seradit knihy podle ceny [0] nebo podle nazvu [1]: ";
				cin >> typRazeni;
			}while(typRazeni != 1 && typRazeni != 0);
			do{
				cout << "chcete seradit knihy vzestupne [0] nebo sestupne [1]: ";
				cin >> smerRazeni;
			}while(smerRazeni != 1 && smerRazeni != 0);
			
			library.sort(typRazeni, smerRazeni);
			break;
		case 8:
			cout << "Zadejte ID knihy: ";
			cin >> idKnihy;
			
			library.edit(idKnihy);
			break;
		case 9:
			library.minPrice();
			break;
		case 10:
			cout << "Zadejte nazev souboru: ";
			cin >> nazevSouboru;
			error = library.fscan(nazevSouboru);
			if(error != 0) {
				library.SeekErrors(error);
				break;
			}
			break;
		case 11:
			cout << "Zadejte ID knihy: ";
			cin >> idKnihy;
			
			newPocet = library.Remove(idKnihy);
			if(newPocet < 0) {
				error = newPocet;
				library.SeekErrors(error);
			}
			break;
		default:
			cout << "tato akce neexistuje" << endl << endl;
			break;
		}
		
	}while(1);	
}
