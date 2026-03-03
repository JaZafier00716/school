#include "tasks.h"
#include <iostream>

int main() {
    auto parser = create_word_parser("foo");


    auto x = parser("a");    // null
    std::cout << "parser('a'):  \t\"" << (x == nullptr ? "null" : (x[0] == '\0' ? "\"\"" : x)) << "\"" << std::endl;
    x = parser("f");    // null
    std::cout << "parser('f'):  \t\"" << (x == nullptr ? "null" : (x[0] == '\0' ? "\"\"" : x)) << "\"" << std::endl;
    x = parser("fo");   // null
    std::cout << "parser('fo'): \t\"" << (x == nullptr ? "null" : (x[0] == '\0' ? "\"\"" : x)) << "\"" << std::endl;
    x = parser("foox"); // "x"
    std::cout << "parser('foox'): \t\"" << (x == nullptr ? "null" : (x[0] == '\0' ? "\"\"" : x)) << "\"" << std::endl;

    return 0;
}
