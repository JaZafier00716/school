#include "student.h"

Student::Student(string first_name, string last_name, Grades*grades, int lecture_num)
{
  this->first_name = first_name;
  this->last_name = last_name;
  this->grades = grades;
  this->lecture_num = lecture_num;
}

Student::Student() {
  this->first_name = "";
  this->last_name = "";
  this->grades = nullptr;
  this->lecture_num = 0;
}

Student::~Student()
{  
  delete[] this->grades;
}

double Student::get_grade_avg() {
  int sum = 0;
  for (int i = 0; i < this->lecture_num; i++)
  {
    sum += this->grades[i].get_grade();
  }
  return (double)sum / (double)this->lecture_num;
}

int Student::get_max_grade() {
  int max = this->grades[0].get_grade();
  for (int i = 0; i < this->lecture_num; i++)
  {
    int curr = this->grades[i].get_grade();
    if(max < curr) {
      max = curr;
    }
  }
  return max;
}

string Student::get_full_name() {
  return this->first_name + " " + this->last_name;
}


void Student::setStudent(string first_name, string last_name, Grades*grades, int lecture_num) {
  this->first_name = first_name;
  this->last_name = last_name;
  this->grades = grades;
  this->lecture_num = lecture_num;
}

istream &operator>> (istream &scan, Student &s) {
  cout << "Enter first name:\t";
  scan >> s.first_name;
  cout << "Enter last name:\t";
  scan >> s.last_name;
  cout << "Enter number of lectures:\t";
  scan >> s.lecture_num;
  s.grades = new Grades[s.lecture_num];

  for (int i = 0; i < s.lecture_num; i++)
  {
    cout << "Enter lectures:" << endl;
    scan >> s.grades[i];
  }
  return scan;
}