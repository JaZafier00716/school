#pragma once
#include "grades.h"


class Student
{
private:
  string first_name, last_name;
  Grades *grades;
  int lecture_num;
public:
  Student();
  Student(string first_name, string last_name, Grades*grades, int lecture_num);
  ~Student();

  double get_grade_avg();
  int get_max_grade();
  string get_full_name();

  void setStudent(string first_name, string last_name, Grades*grades, int lecture_num);
  friend istream &operator>> (istream &scan, Student &s);
};
