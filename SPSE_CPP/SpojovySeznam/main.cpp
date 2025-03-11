#include "funkce.h"

int main() {
	uint16_t akce;
	List seznam;
	string name;
	int data;
	int dir;
	while (1) {
		cout << "================ MENU ================" << endl;
		cout << "| 0  |\tUkonceni" << endl;
		cout << "| 1  |\tPridani uzlu na zacatek" << endl;
		cout << "| 2  |\tPridani uzlu na konec" << endl;
		cout << "| 3  |\tVypis listu" << endl;
		cout << "| 4  |\tVyhledavani podle nazvu" << endl;
		cout << "| 5  |\tSerazeni seznamu podle dat" << endl;
		cout << "| 6  |\tSerazeni seznamu podle nazvu" << endl;
		cout << "| 7  |\tNacteni ze souboru" << endl;
		cout << "| 8  |\tUlozeni do souboru" << endl;
		cout << "| 9  |\tEditace nazvu" << endl;
		cout << "| 10 |\tOdstraneni zaznamu podle id" << endl;
		cout << "| 11 |\tUrceni minima a maxima v listu" << endl;
		cout << "| 12 |\tSoucet dat" << endl;
		cout << "| 13 |\tPrumerna hodnota dat" << endl;
		cout << "------------------------------" << endl;
		cout << "Zvolte akci: ";
		cin >> akce;

		switch (akce) {
			case 0:
				system("pause");
				return 0;
			case 1:
				cout << "Zadejte jmeno: ";
				getline(cin, name);
				fflush(stdin);
				cout << "Zadejte cislo: ";
				cin >> data;

				if (!seznam.NodeAddStart(name, data)) {
					cout << "Nepodarilo se pridat data do seznamu" << endl << endl;
				}

				break;
			case 2:
				cout << "Zadejte jmeno: ";
				getline(cin, name);
				fflush(stdin);
				cout << "Zadejte cislo: ";
				cin >> data;

				if (!seznam.NodeAddEnd(name, data)) {
					cout << "Nepodarilo se pridat data do seznamu" << endl << endl;
				}

				break;
			case 3:
				seznam.printList();
				break;
			case 4:
				cout << "Zadejte nazev: ";
				getline(cin, name);
				fflush(stdin);
				if (!seznam.Search(name)) {
					cout << "Nazev " << name << " nebyl v seznamu nalezen" << endl << endl;
				};
				break;
			case 5:
				do {
					cout << "Vzestupne (1) sestupne (0): ";
					cin >> dir;
				} while (dir != 1 && dir != 0);

				seznam.DataSort(dir == 1);
				break;
			case 6:
				do {
					cout << "Vzestupne (1) sestupne (0): ";
					cin >> dir;
				} while (dir != 1 && dir != 0);

				seznam.NameSort(dir == 1);
				break;
			case 7:
				cout << "Zadejte nazev souboru: ";
				getline(cin, name);
				fflush(stdin);

				if (!seznam.LoadTxt(name)) {
					cout << "Nepodarilo se nacist data ze souboru" << endl << endl;
				}
				break;
			case 8:
				cout << "Zadejte nazev souboru: ";
				getline(cin, name);
				fflush(stdin);

				if (!seznam.StoreTxt(name)) {
					cout << "Nepodarilo se ulozit data do souboru" << endl << endl;
				}

				break;
			case 9:
				cout << "Zadejte nazev, ktery chcete zmenit: ";
				getline(cin, name);
				fflush(stdin);

				if (!seznam.EditName(name)) {
					cout << "Nazev nebyl nalezen" << endl << endl;
				}

				break;
			case 10:
				cout << "zadejte id: ";
				cin >> data;

				if (!seznam.RemoveNode(data)) {
					cout << "ID nebylo nalezeno" << endl << endl;
				}

				break;
			case 11:
				if (!seznam.MinMax()) {
					cout << "Pole je prazdne" << endl << endl;
				}

				break;
			case 12:
				cout << "Suma: " << seznam.Sum() << endl;
				break;
			case 13:
				cout << "Avg: " << seznam.Avg() << endl;
				break;
			default:
				cout << "Akce neexistuje" << endl << endl;
				break;
		}
	}
}
