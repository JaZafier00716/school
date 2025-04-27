#pragma once
#include "student.h"

class Classroom
{
private:
  Student *students;
  int student_count;
public:
  Classroom(Student *students, int student_count);
  Classroom();
  ~Classroom();
  void print_student_avges();
  void printStudentsWithScholar();
  void addStudent(Student student);
  friend istream &operator>> (istream &scan, Classroom &c);
};

