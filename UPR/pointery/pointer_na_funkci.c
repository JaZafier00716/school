#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void set (int p) {
    p=50;
    printf("%d", p);
}

int add (int a, int b) {
    return a + b;
}

int subtract (int a, int b) {
    return a -b;
}

int multiply (int a, int b) {
    return a * b;
}

int divide (int a, int b) {
    return a / b;
}

int main() {
    for (int i = 0; i < 20; i++)
    {
        printf("%d\n", rand() % (7 + 1));
    }
    
    // int a = 5;
    // // void(*funciton_pointer)(int) = set;
    // // typ_vystupu (*nazev_pointru_na_funkci)(datove_typy_parametru) = (&funkce)
    // // typ_vystupu (*nazev_pointru_na_pole_funkci[])(datove_typy_parametru) = {&adresa_funkce}

    // int (*funciton_pointer[])(int, int) = {&add, &subtract, &multiply, &divide};

    // srand(time(NULL));
    // int random_op_index = rand () % 4;
    // // (*nazev_pointru_na_pole_funkci[index_funkce])(parametry)
    // int y = (*funciton_pointer[random_op_index])(10, 5);
    // printf("%d", y);
    return 0;
}