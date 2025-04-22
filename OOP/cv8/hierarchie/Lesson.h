#pragma once
#include "Student.h"
#include "Teacher.h"


class Lesson
{
private:
  Student *students;
  int studentCount;
  Teacher teacher;
  string name;
public:
  Lesson(string name, Teacher t) {
    this->teacher = t;
    this->students = nullptr;
    this->studentCount = 0;
    this->name = name;
  }
  Lesson(Student *students, Teacher t, int studentCount, string className) {
    this->students = students;
    this->studentCount = studentCount;
    this->teacher = t;
    this->name = name;
  }

  void addStudent(string name, string id) {
    this->students[studentCount] = Student(name, id);
  }

};



