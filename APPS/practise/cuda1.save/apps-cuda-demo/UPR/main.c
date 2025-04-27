#include <stdio.h>
#include <stdlib.h>

// void allocate_2D(int *** a, int rows, int cols) {
//     *a = (int**) malloc(sizeof(int*) * rows);
//     for (int i = 0; i < rows; i++)
//     {
//         (*a)[i] = (int*)malloc(sizeof(int) * cols); // Chceme dereferencovat 'a' ne hodnotu na pozici i proto (*a)
//     }
// }

// void deallocate_2d(int *** a, int rows) {
//     for (int i = 0; i < rows; i++)
//     {
//         free((*a)[i]);
//     }
//     free(*a);
//     *a = NULL;
// }


int main() {
    // Linearizovane pole
        // malloc (sizeof(int) * rows * cols);
        // pro pristup i * cols + j

    // int a[10] = { 0 };
    // int velikost = sizeof (a) / sizeof(int);

    // int *pole = funkce(NULL);

    // for (int i = 0; i < velikost; i++)
    // {
    //     printf("%d\n", a[i]);
    // }

    // printf("%p\n", a);
    // printf("%p\n", &a);
    // printf("%p\n", &a[0]);

    // Vsechny varianty jsou spravne
    // a[5] = 15;
    // *(a + 5) = 15;
    // 5[a] = 15;

    // int ** arr = NULL;
    // int rows = 5;
    // int cols = 5;

    //     printf("%d\n", a[i]);
    // }

    // printf("%p\n", a);
    // printf("%p\n", &a);
    // printf("%p\n", &a[0]);

    // Vsechny varianty jsou spravne
    // allocate_2D(&arr, rows, cols);
    // arr[0][5] = 5;
    // deallocate_2d(&arr, rows);

    char * str = "NAZDAR!"; // Pointer ukazuje na retezec v kodu v instrukcni sade
    char str2[] = "CAUES!";

    printf("%c", str[0]);
    printf("%c", str2[0]);

    // str[0] = 'Z'; // Nelze menit, jinak by kod mohl menit sam sebe
    str2[0] = 'Z';



    return 0;
}