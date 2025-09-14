#include "classroom.h"


Classroom::Classroom(Student *students, int student_count)
{
  this->students = students;
  this->student_count = student_count;
}

Classroom::Classroom() {
  this->students = nullptr;
  this->student_count = 0;
}

Classroom::~Classroom()
{
  delete[] students;
}

void Classroom::printStudentsWithScholar() {
  cout << "Student with scholar: " << endl;
  for (int i = 0; i < student_count; i++)
  {
    if(this->students[i].get_grade_avg() < 1.7 && this->students[i].get_max_grade() <=2) {
      cout << this->students[i].get_full_name() << endl;
    }
  } 
}

void Classroom::print_student_avges() {
  cout << "Average grade: " << endl;
  for (int i = 0; i < this->student_count; i++)
  {
    cout << this->students[i].get_full_name() << ": " << this->students[i].get_grade_avg() << endl;
  }
}

void Classroom::addStudent(Student student) {
  this->students[this->student_count] = student;
  this->student_count++;
}

istream &operator>> (istream &scan, Classroom &c) {
  cout << "Enter number of students:\t";
  cin >> c.student_count;
  cout << "Enter Students:" << endl;
  cout << "-----------------------" << endl;
  c.students = new Student[c.student_count];
  for (int i = 0; i < c.student_count; i++)
  {
    cout << "Enter student " << i << ":" << endl; 
    cin >> c.students[i];
    cout << "---------------------" << endl;
  }

  return scan;
}