#include "header.h"

/*
------------------------------------------------------------------------------
|	Komplexni cisla
------------------------------------------------------------------------------
*/

ComplexNumber::ComplexNumber() {
	this->realPart = 0;
	this->imaginaryPart = 0;
}

ComplexNumber::ComplexNumber(float realPart, float imaginaryPart) {
	this->realPart = realPart;
	this->imaginaryPart = imaginaryPart;
}

void ComplexNumber::SetImaginaryPart(float imaginaryPart) {
	this->imaginaryPart = imaginaryPart;
}
void ComplexNumber:: SetRealPart(float realPart) {
	this->realPart = realPart;
}

float ComplexNumber::Absolute() {
	return sqrt(powf(realPart, 2) + powf(imaginaryPart, 2));
}

void ComplexNumber::CompositeNumber(ComplexNumber x) {
	this->realPart = x.realPart;
	this->imaginaryPart = -x.imaginaryPart;
}

ComplexNumber ComplexNumber::Sum(ComplexNumber x) {
	ComplexNumber sum;
	
	sum.realPart = this->realPart + x.realPart;
	sum.imaginaryPart = this->imaginaryPart + x.imaginaryPart;
	
	return sum;
}
ComplexNumber operator+(ComplexNumber x, ComplexNumber y) {
	ComplexNumber sum;
	
	sum.realPart = x.realPart + y.realPart;
	sum.imaginaryPart = x.imaginaryPart + y.imaginaryPart;
	return sum;
}

ComplexNumber operator-(ComplexNumber x, ComplexNumber y) {
	ComplexNumber dif;
	
	dif.realPart = x.realPart - y.realPart;
	dif.imaginaryPart = x.imaginaryPart - y.imaginaryPart;
	return dif;
}

ComplexNumber operator*(ComplexNumber x, ComplexNumber y) {
	ComplexNumber mul;
	float a, b, c, d;
	a = x.imaginaryPart * y.imaginaryPart * (-1);
	b =  x.imaginaryPart * y.realPart; 
	c = x.realPart * y.imaginaryPart; 
	d = x.realPart * y.realPart;
	
	mul.realPart = a + d;
	mul.imaginaryPart = b + c;
	
	return mul;
}

ComplexNumber operator/(ComplexNumber x, ComplexNumber y) {
	ComplexNumber div;
	/*
		a + bi
		------
		c + di
	*/
	//	real part = (ac + bd) / (c^2 + d^2)
	div.realPart = (x.realPart * y.realPart + x.imaginaryPart * y.imaginaryPart) / (y.imaginaryPart * y.imaginaryPart + y.realPart * y.realPart);
	
	//	imaginary part = ((bc - ad) / (c^2 + d^2))i
	div.imaginaryPart = (x.imaginaryPart*y.realPart - x.realPart * y.imaginaryPart) / (y.imaginaryPart * y.imaginaryPart + y.realPart * y.realPart);
	
	return div;
}

ostream &operator << (ostream &print, ComplexNumber number) {
	if(number.realPart != 0) {
		print << number.realPart;	
	}
	if(number.imaginaryPart > 0 && number.realPart != 0 && number.imaginaryPart != 0) {
		print << "+";
	}
	if(number.imaginaryPart != 0 && number.imaginaryPart != 1 && number.imaginaryPart != -1) {
		print << number.imaginaryPart << "i";
	}
	if(number.imaginaryPart == 1) {
		print << "i";
	}
	if(number.imaginaryPart == -1){
		print << "-i";
	}
	if(number.imaginaryPart == 0 && number.realPart == 0) {
		print << "0";
	}
	
	return print;
}

istream &operator >> (istream &scan, ComplexNumber &number) {
	cout << "real part: ";
	scan >> number.realPart;
	cout << "imaginary part: ";
	scan >> number.imaginaryPart;
	
	return scan;
}

/*
------------------------------------------------------------------------------
|	Zlomky
------------------------------------------------------------------------------
*/

Zlomek::Zlomek(){
	this->citatel = 0;
	this->jmenovatel = 1;
}

Zlomek::Zlomek(int citatel, int jmenovatel){
	this->citatel = citatel;
	this->jmenovatel = jmenovatel;
}

void Zlomek::SetCitatel(int citatel){
	this->citatel = citatel;
}
void Zlomek::SetJmenovatel(int jmenovatel){
	if(jmenovatel != 0) {
		this->jmenovatel = jmenovatel;
	}
	this->jmenovatel = 1;
}

int Zlomek::Compare(Zlomek zlomek){
	if(((float)this->citatel / (float)this->jmenovatel) > ((float)zlomek.citatel / (float)zlomek.jmenovatel)) {
		return 1;
	}
	if(((float)this->citatel / (float)this->jmenovatel) < ((float)zlomek.citatel / (float)zlomek.jmenovatel)) {
		return -1;
	}
	return 0;
}

void Zlomek::Base() {
	Zlomek temp;
	float citatel, jmenovatel;
	
	temp.citatel = abs(this->citatel);
	temp.jmenovatel = abs(this->jmenovatel);
	
	if(temp.citatel > temp.jmenovatel) {
		for(int i=temp.jmenovatel;i>1;i--){
			if(temp.jmenovatel % i == 0 && temp.citatel % i == 0) {
				citatel = this->citatel / i;
				jmenovatel = this->jmenovatel / i;
				break;
			}
		}
	} else {
		for(int i=temp.citatel;i>1;i--){
			if(temp.jmenovatel % i == 0 && temp.citatel % i == 0) {
				this->citatel = temp.citatel / i;
				this->jmenovatel = temp.jmenovatel / i;
				break;
			}
		}
	}
}

Zlomek operator+(Zlomek c1, Zlomek c2){
	Zlomek vysledek;
	
	if(c1.jmenovatel == c2.jmenovatel) {
		vysledek.citatel = c1.citatel + c2.citatel;
		vysledek.jmenovatel = c1.jmenovatel;
	}
	else {
		vysledek.citatel = c1.citatel * c2.jmenovatel + c2.citatel * c1.jmenovatel;
		vysledek.jmenovatel = c1.jmenovatel * c2.jmenovatel;
	}
	return vysledek; 
}

Zlomek operator-(Zlomek c1, Zlomek c2){
	Zlomek vysledek;
	
	if(c1.jmenovatel == c2.jmenovatel) {
		vysledek.citatel = c1.citatel - c2.citatel;
		vysledek.jmenovatel = c1.jmenovatel;
	}
	else {
		vysledek.citatel = c1.citatel * c2.jmenovatel - c2.citatel * c2.jmenovatel;
		vysledek.jmenovatel = c1.citatel * c2.jmenovatel;
	}
	return vysledek;
}

Zlomek operator*(Zlomek c1, Zlomek c2){
	Zlomek vysledek;
	
	vysledek.citatel = c1.citatel * c2.citatel;
	vysledek.jmenovatel = c1.jmenovatel * c2.jmenovatel;
	return vysledek;
}

Zlomek operator/(Zlomek c1, Zlomek c2){
	Zlomek vysledek;
	
	vysledek.citatel = c1.citatel * c2.jmenovatel;
	vysledek.jmenovatel = c1.jmenovatel * c2.citatel;
	
	return vysledek;
}

ostream &operator << (ostream &print, Zlomek number){
	print << "Zlomek:" << endl << number.citatel << "/" << number.jmenovatel;
	return print;
}
istream &operator >> (istream &scan, Zlomek &number){
	cout << "Zadejte citatel: ";
	scan >> number.citatel;
	do {
		cout << "Zadejte nenulovy jmenovatel: ";
		scan >> number.jmenovatel;
	} while(number.jmenovatel == 0);
	
	return scan;
}

