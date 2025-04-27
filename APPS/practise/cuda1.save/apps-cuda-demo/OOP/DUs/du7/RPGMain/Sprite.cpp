#include "Sprite.h"

Sprite::Sprite(double HP, double baseDmg)
{
	this->HP = HP;
	this->baseDamage = baseDmg;
}

bool Sprite::isAlive()
{
	return this->HP > 0;
}

double Sprite::calculateDamage(double dmg) {
	return dmg;
}

bool Sprite::getHit(double attackingdmg)
{
	double finalDmg = calculateDamage(attackingdmg);

	this->HP -= finalDmg;
	std::cout << "Sprite's HP was reduced by " << finalDmg << std::endl;
	
	return isAlive();
}

void Sprite::attack(Sprite* targetSprite)
{
	double finalDmg = calculateDamage(this->baseDamage);

	bool isAlive = targetSprite->getHit(finalDmg);
	
	if (!isAlive)
	{
		std::cout << "Enemy sprite has been killed" << std::endl;
	}
}


