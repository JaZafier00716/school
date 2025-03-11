#include "grades.h"

Grades::Grades(string lecture_name, int *grades, int grade_count)
{
  this->lecture_name = lecture_name;
  this->grade_count = grade_count;
  this->grades = grades;
}

Grades::Grades()
{
  this->lecture_name = "";
  this->grade_count = 0;
  this->grades = nullptr;
}

Grades::~Grades()
{
  delete[] this->grades;
}

double Grades::get_avg()
{
  int sum = 0;
  for (int i = 0; i < this->grade_count; i++)
  {
    sum += this->grades[i];
  }
  return (double)sum / (double)this->grade_count;
}
int Grades::get_grade()
{
  return round(this->get_avg());
}
int *Grades::get_grades()
{
  return this->grades;
}

void Grades::setLecture(string lecture_name, int *grades, int grade_count)
{
  this->lecture_name = lecture_name;
  this->grade_count = grade_count;
  this->grades = grades;
}

istream &operator>>(istream &scan, Grades &lecture)
{
  cout << "Enter lecture name:\t";
  scan >> lecture.lecture_name;
  cout << "Enter grade count:\t";
  scan >> lecture.grade_count;
  lecture.grades = new int[lecture.grade_count];

  for (int i = 0; i < lecture.grade_count; i++)
  {
    cout << "Enter grade " << i << ":\t";
    scan >> lecture.grades[i];
  }

  return scan;
}