// Useful links:
// mad godbolt - compiler explorer
// c++ weekly
// Jason Turner

// g++ main.cpp -std=c++23 -o main && ./main -Wall -Wextra -Wconversion -pedantic (useful but might not be interesting to us) -Werror (not useful during development, because of loose variables, we may have not yet defined function of)
// inline funkce znamena, ze muze byt definovana ve vice souborech, ale linker ji spojí do jedné funkce (nebo ji úplně odstraní, pokud se nevolá)
//


#include <iostream>
#include <print>

using std::cin, std::cout, std::endl, std::println;

int main()
{
  println("VSB-TUO {}", 69);
  cout << typeid(double).name() << endl;

    // What about this?
    // std::cout << fun1(1) << std::endl;

  return 0;
}