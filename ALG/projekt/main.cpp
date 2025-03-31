#include "funkce.h"



int main() {

  int list_count;
  cout << "Enter number of sorted lists:\t";
  cin >> list_count;

  ListArray arr(list_count);

  arr.printLists();
  

  List merged = arr.merge_lists();

  merged.print_list();

  // for (int i = 0; i < list_count; i++)
  // {
  //   lists[i].N = enter_list_size(i);
  //   total_size += size;

  //   lists[i].values = new int[lists[i].N];
  //   get_list(&(lists[i]), size);

  //   cout << "You have entered:" << endl;
  //   print_list(lists[i], size);
  // }
  
  // int *merged_list = new int[total_size];

  // merge_lists(lists, list_count, total_size, &merged_list);
  


  return 0;
}

