#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

bool allocate_int(int N, int **p) {

    *p = (int*)malloc(sizeof(int)*N);

    if (p != NULL) {
        return true;
    } else {
        return false;
    }
}

int main() {
    int *p = NULL;

    if(allocate_int(10, &p)) {
        p[0] = 10;
    } else {
        return 0;
    }


    return 0;
}