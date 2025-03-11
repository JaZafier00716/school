#include "classroom.h"

int main () {
  Classroom class1;

  cin >> class1;
  
  cout << endl;
  class1.print_student_avges();
  cout << endl;
  class1.printStudentsWithScholar();

  return 0;
}
