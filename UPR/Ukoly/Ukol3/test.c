#include <stdio.h>

int main() {
    int x = 4;
    int *pX = &x;
    int y = *pX;

    printf("x: %d\n", x);
    printf("&x: %p\n", &x);
    printf("pX: %p\n", pX);
    printf("*px: %d\n", *pX);
    printf("y: %d\n", y);
    

    return 0;
}