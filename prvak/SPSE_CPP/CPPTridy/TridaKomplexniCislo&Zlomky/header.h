#ifndef HEADER_H
#define HEADER_H
#include <iostream>
#include <cmath>
using namespace std;

/*
------------------------------------------------------------------------------
|	Komplexni cisla
------------------------------------------------------------------------------
*/

class ComplexNumber {
private:
	float imaginaryPart;
	float realPart;
public:
	ComplexNumber();
	ComplexNumber(float realPart, float imaginaryPart);
	float GetRealPart() {return this->realPart;};
	float GetImaginaryPart() {return this->imaginaryPart;};
	void SetImaginaryPart(float imaginaryPart);
	void SetRealPart(float realPart);
	float Absolute();
	void CompositeNumber(ComplexNumber x);
	ComplexNumber Sum(ComplexNumber x);
	friend ComplexNumber operator+(ComplexNumber x, ComplexNumber y);
	friend ComplexNumber operator-(ComplexNumber x, ComplexNumber y);
	friend ComplexNumber operator*(ComplexNumber x, ComplexNumber y);
	friend ComplexNumber operator/(ComplexNumber x, ComplexNumber y);
	friend ostream &operator << (ostream &print, ComplexNumber number);
	friend istream &operator >> (istream &scan, ComplexNumber &number);
};

/*
------------------------------------------------------------------------------
|	Zlomky
------------------------------------------------------------------------------
*/

class Zlomek {
private:
	int citatel;
	int jmenovatel;
public:
	Zlomek();
	Zlomek(int citatel, int jmenovatel);
	int GetCitatel() {return this->citatel;};
	int GetJmenovatel() {return this->jmenovatel != 0 ? this->jmenovatel : 1;};
	void SetCitatel(int citatel);
	void SetJmenovatel(int jmenovatel);
	int Compare(Zlomek zlomek);
	void Base();
	friend Zlomek operator+(Zlomek c1, Zlomek c2);
	friend Zlomek operator-(Zlomek c1, Zlomek c2);
	friend Zlomek operator*(Zlomek c1, Zlomek c2);
	friend Zlomek operator/(Zlomek c1, Zlomek c2);
	friend ostream &operator << (ostream &print, Zlomek number);
	friend istream &operator >> (istream &scan, Zlomek &number);
};
#endif
