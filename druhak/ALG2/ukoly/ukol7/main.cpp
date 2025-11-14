#include "class.h"


int main () {
  const auto inputs = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",};
  const auto inputs2 = { "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
  "seventeen", "eighteen", "nineteen", "twenty",
  "twenty-one", "twenty-two", "twenty-three", "twenty-four", "twenty-five",
  "twenty-six", "twenty-seven", "twenty-eight", "twenty-nine",};
  const auto removals = { "twenty-one", "twenty-two", "twenty-three", "twenty-four", "twenty-five",
    "twenty-six", "twenty-seven", "twenty-eight", "twenty-nine",
    "aardvark", "Bene Gesserit", "crane", "dog", "element"};
  CuckooHashTable table;

  for (const auto &input : inputs) {
    table.insert(input);
  }
  table.printTables();
  cout << endl;

  for (const auto &input : inputs2) {
    table.insert(input);
  }
  table.printTables();
  cout <<  endl;

  for (const auto &removal : removals) {
    table.remove(removal);
  }
  table.printTables();
  cout << endl;

  return 0;
}