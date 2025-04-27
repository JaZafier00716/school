#pragma once
#include "Person.h"
#include "Lesson.h"

class Student : Person
{
private:
  unsigned int points;
  unsigned int absence;
public:
  Student() : Person("", "") {
    this->points = 0;
    this->absence = 0;
  }
  Student(string name, string id) : Person(name, id){}

  bool getsThrough(unsigned int min_points, unsigned int max_absence) {
    return this->points >= min_points && this->absence >= max_absence;
  }
};

