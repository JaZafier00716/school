#pragma once
#include <iostream>
#include <string>
using std::string;

class Sprite
{
public:
	Sprite(double HP, double baseDmg);
	virtual bool getHit(double dmg);
	void attack(Sprite* targetSprite);
	bool isAlive();
	virtual double calculateDamage(double dmg);
private:
	double HP;
	double baseDamage;
};

