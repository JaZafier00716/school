#pragma once
#include <string>
#include <iostream>
#include <cmath>
using std::string, std::cin, std::cout, std::endl, std::istream, std::operator>>;

class Grades
{
private:
  string lecture_name;
  int *grades;
  int grade_count;
public:
  Grades(string lecture_name, int *grades, int grade_count);
  Grades();
  ~Grades();

  double get_avg();
  int get_grade();
  int* get_grades();

  void setLecture(string lecture_name, int *grades, int grade_count);
  friend istream &operator>> (istream &scan, Grades &lecture);
};

