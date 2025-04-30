#pragma once
#include "Person.h"

class Teacher : Person
{
private:
  unsigned int powerLevel;
  unsigned int teachingSkills;
public:
  Teacher() : Person("", "") {
    this->powerLevel = 1;
    this->teachingSkills = 0;
  }
  Teacher(string name, string id) : Person(name, id){
    this->powerLevel = 1;
    this->teachingSkills = 0;
  }
  Teacher(string name, string id, unsigned int powerLevel) : Person(name, id){
    this->powerLevel = powerLevel;
    this->teachingSkills = 0;
  }
  Teacher(string name, string id, unsigned int teachingSkills) : Person(name, id){
    this->powerLevel = 1;
    this->teachingSkills = teachingSkills;
  }
  Teacher(string name, string id, unsigned int powerLevel, unsigned int teachingSkills) : Person(name, id){
    this->powerLevel = powerLevel;
    this->teachingSkills = teachingSkills;
  }
  unsigned int getTeachingSkills() {
    return this->teachingSkills;
  }
  unsigned int getPowerLevel() {
    return this->powerLevel;
  }
};

