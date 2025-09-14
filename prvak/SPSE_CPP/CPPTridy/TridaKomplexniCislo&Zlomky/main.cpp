#include "header.h"

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int main(void) {
//	ComplexNumber n1, n2(-5, 3), s, composite;
//	float result;
//	
//	cin >> n1;
//	
//	cout << "cislo:" << endl;
//	cout << n1 << endl;
//	result = n1.Absolute();
//	composite.CompositeNumber(n1);
//	cout << "absolutni hodnota " << n1 << " je " << result << endl;
//	cout << "komplexne sdruzene cislo od " << n1 << " je " << composite << endl;
//	
//	s = n1+n2;
//	cout << "soucet " << n1 << " a " << n2 << " je " << s << endl << endl;
//	s = n1*n2;
//	cout << "Soucin " << n1 << " a " << n2 << " je " << s << endl << endl;
//	s = n1-n2;
//	cout << "rozdil " << n1 << " a " << n2 << " je " << s << endl << endl;
//	s = n1/n2;
//	cout << "podil " << n1 << " a " << n2 << " je " << s << endl << endl;
	
	/*
		---------------------------------------------------------------------------
		Zlomky
		---------------------------------------------------------------------------
	*/
	Zlomek z1, z2(84, 4), z3(1, 4);
	Zlomek soucet, soucin, rozdil, podil;
	
	cin >> z1;
	
	cout << z1 << endl;
	cout << z2 << endl;
	cout << "Zkraceny zlomek: " << z1;
	z1.Base();
	cout << " je " << endl << z1 << endl;
	
	if(z1.Compare(z3) == 1) {
		cout << z1 << " je vetsi" << endl;
	}
	if(z1.Compare(z3) == -1) {
		cout << z3 << " je vetsi" << endl;
	}
	if(z1.Compare(z3) == 0) {
		cout << z1 << " a " << z3 << " jsou stejne" << endl;
	}
	soucet = z1 + z3;
	cout << endl << endl << "soucet " << z1 << " a " << z2 << " je " << soucet << endl;
	rozdil = z1 - z3;
	cout << "rozdil " << z1 << " a " << z3 << " je " << rozdil << endl;
	soucin = z1 * z3;
	cout << "soucin " << z1 << " a " << z3 << " je " << soucin << endl;
	podil = z1 / z3;
	cout << "podil " << z1 << " a " << z3 << " je " << podil << endl;
	
	return 0;
}
